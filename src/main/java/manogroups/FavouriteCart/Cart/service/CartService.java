package manogroups.FavouriteCart.Cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        if(cartRepository.existsByStoreNameAndProductId(cart.getStoreName(),cart.getProductId())){
            return "Product is Already in the Cart";
        }
        cart.setUserEmail(jwtUtil.extractEmail(authHeader));
        cartRepository.save(cart);
        return "Product Added to Cart Successfully";
    }

    public List<CartResponse> getCarts(String authHeader, String storeName) {
        List<Cart> carts = cartRepository.findByUserEmailAndStoreName(jwtUtil.extractEmail(authHeader),storeName);
        List<CartResponse> response= new ArrayList<>();
        for(Cart cart: carts){
            try{
                Product product = restTemplate.getForObject(productGet+"?productId="+cart.getProductId(), Product.class);
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

    public boolean isCart(String authHeader, String storeName, Long productId) {
        return cartRepository.existsByUserEmailAndStoreNameAndProductId(jwtUtil.extractEmail(authHeader),storeName,productId);
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

    public String deleteCart(String authHeader,Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart !=null && jwtUtil.extractEmail(authHeader).equals(cart.getUserEmail()) ){
            cartRepository.deleteById(cartId);
            return "Cart item Removed Successfully.";
        }
        return "Failed to Remove Cart Item.";
    }

}
