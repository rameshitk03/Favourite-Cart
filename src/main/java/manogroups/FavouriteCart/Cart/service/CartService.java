package manogroups.FavouriteCart.Cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import manogroups.FavouriteCart.Cart.DTO.CartResponse;
import manogroups.FavouriteCart.Cart.entity.Cart;
import manogroups.FavouriteCart.Cart.repository.CartRepository;
import manogroups.FavouriteCart.DTO.Product;
import manogroups.FavouriteCart.Jwt.JwtUtil;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${productGet}")
    private String productGet;

    public String addCart(String authHeader, Cart cart) {
        System.out.println(jwtUtil.extractEmail(authHeader));
        if(cartRepository.existsByUserEmailAndStoreNameAndProductCode(jwtUtil.extractEmail(authHeader),cart.getStoreName(),cart.getProductCode())){
            return "Product is Already in the Cart";
        }
        cart.setUserEmail(jwtUtil.extractEmail(authHeader));
        cartRepository.save(cart);
        return "Product Added to Cart Successfully";
    }

    public List<CartResponse> getCarts(String authHeader, String storeName) {
        List<Cart> carts = cartRepository.findByUserEmailAndStoreNameOrderByCartIdAsc(jwtUtil.extractEmail(authHeader),storeName);
        List<CartResponse> response= new ArrayList<>();
        for(Cart cart: carts){
            try{
                Product product = restTemplate.getForObject(productGet+cart.getProductId(), Product.class);
                if(product!=null){
                    CartResponse merge = new CartResponse(
                        cart.getCartId(),
                        product.getProductCode(),
                        cart.getNoOfQuantity(),
                        cart.getProductId(),
                        product.getProductName(),
                        product.getProductSellingPrice(),
                        product.getProductImageId()
                    );
                    response.add(merge);
                }
            }catch(RestClientException e){
                System.out.println("Error in Fetching Product Details"+cart.getProductId());
            }
        }
        return response;
    }

    public boolean isCart(String authHeader, String storeName, String productCode) {
        System.out.println(productCode);
        System.out.println(storeName);
        System.out.println(jwtUtil.extractEmail(authHeader));
        return cartRepository.existsByUserEmailAndStoreNameAndProductCode(jwtUtil.extractEmail(authHeader),storeName,productCode);
    }

    public String updateCart(String authHeader,Long cartId, int noOfQuantity){
        Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new RuntimeException("Cart Item Not Found"));

        if(jwtUtil.extractEmail(authHeader).equals(cart.getUserEmail())){
            cart.setNoOfQuantity(noOfQuantity);
            cartRepository.save(cart);
            return "Cart Updated Successfully";
        }
        return "Failed to Update Cart";
    }

    @Transactional
    public String deleteCart(String authHeader,String storeName, String productCode) {
        String userEmail = jwtUtil.extractEmail(authHeader);
        System.out.println(userEmail);
        System.out.println(storeName);
        System.out.println(productCode);
        if(cartRepository.existsByUserEmailAndStoreNameAndProductCode(userEmail, storeName, productCode)){
            cartRepository.deleteByUserEmailAndStoreNameAndProductCode(userEmail,storeName,productCode);
            return "Cart Item was Removed";
        }
        return "Cart Not Found";
    }

}
