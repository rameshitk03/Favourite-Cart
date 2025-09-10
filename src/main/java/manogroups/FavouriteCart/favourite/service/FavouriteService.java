package manogroups.FavouriteCart.favourite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import jakarta.transaction.Transactional;
import manogroups.FavouriteCart.DTO.Product;
import manogroups.FavouriteCart.Jwt.JwtUtil;
import manogroups.FavouriteCart.favourite.DTO.FavouriteResponse;
import manogroups.FavouriteCart.favourite.entity.Favourite;
import manogroups.FavouriteCart.favourite.repository.FavouriteRepository;

@Service
public class FavouriteService {

    @Autowired
    private FavouriteRepository favouriteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${productGet}")
    private String productGet;

    public String addFavourite(String authHeader, Favourite favourite) {
        String userEmail = jwtUtil.extractEmail(authHeader);
        if(favouriteRepository.existsByUserEmailAndStoreNameAndProductCode(userEmail,favourite.getStoreName(),favourite.getProductCode())){
             return "Product is Already in Favourite";
        }
        favourite.setUserEmail(userEmail);
        favouriteRepository.save(favourite);
        return "Product Added in Favourites";
    }

    public List<FavouriteResponse> getFavourites(String authHeader, String storeName) {
        List<Favourite> favourites =  favouriteRepository.findByUserEmailAndStoreNameOrderByFavouriteIdAsc(jwtUtil.extractEmail(authHeader),storeName);
        List<FavouriteResponse> response = new ArrayList<>();
        for(Favourite favourite :favourites){
            try{
                Product product = restTemplate.getForObject(productGet+favourite.getProductId(), Product.class);
                if(product!=null){
                    FavouriteResponse merge = new FavouriteResponse(
                        favourite.getFavouriteId(),
                        product.getProductCode(),
                        favourite.getProductId(),
                        product.getProductQuantity(),
                        product.getProductName(),
                        product.getProductSellingPrice(),
                        product.getProductImageId()
                    );
                    response.add(merge);
                }
            }catch(RestClientException e){
                System.out.println("Error in Fetching Product Details "+favourite.getProductId());
            }
        }
        return response;
    }

    public boolean isFavourite(String authHeader, String storeName, String productCode) {
       return favouriteRepository.existsByUserEmailAndStoreNameAndProductCode(jwtUtil.extractEmail(authHeader),storeName,productCode);
    }

    @Transactional
    public String deleteFavourite(String authHeader,String storeName, String productCode) {
        String userEmail = jwtUtil.extractEmail(authHeader);
        System.out.println(userEmail);
        System.out.println(storeName);
        System.out.println(productCode);
        if(favouriteRepository.existsByUserEmailAndStoreNameAndProductCode(userEmail, storeName, productCode)){
            favouriteRepository.deleteByUserEmailAndStoreNameAndProductCode(userEmail,storeName,productCode);
            return "Favourite Item was removed";
        }
        return  "Favourite Item not Found";
    }
}
