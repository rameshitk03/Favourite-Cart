package manogroups.FavouriteCart.Cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import manogroups.FavouriteCart.Cart.DTO.CartResponse;
import manogroups.FavouriteCart.Cart.entity.Cart;
import manogroups.FavouriteCart.Cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestHeader("Authorization") String authHeader,@RequestBody Cart cart){
        try{
            String message = cartService.addCart(authHeader.substring(7),cart);
            return ResponseEntity.ok(message);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/carts/{storeName}")
    public ResponseEntity<?> getCarts(@RequestHeader("Authorization") String authHeader,@PathVariable String storeName){
        List<CartResponse> carts = cartService.getCarts(authHeader.substring(7),storeName);
        if(carts.isEmpty()){
            return ResponseEntity.ok("No Products in Your Cart. Please Add a Product in your Cart");
        }
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/cart/{storeName}")
    public ResponseEntity<Boolean> isCart(@RequestHeader("Authorization") String authHeader,@PathVariable String storeName, @RequestParam String productCode){
        boolean cart = cartService.isCart(authHeader.substring(7),storeName,productCode);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<String> updateCart(@RequestHeader("Authorization") String authHeader,@PathVariable Long cartId,@RequestParam int noOfQuantity){
        try{
            String message = cartService.updateCart(authHeader.substring(7),cartId,noOfQuantity);
            return ResponseEntity.ok(message);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{storeName}")
    public ResponseEntity<String> deleteCart(@RequestHeader("Authorization") String authHeader,@PathVariable String storeName,@RequestParam String productCode){
        try{
            String message = cartService.deleteCart(authHeader.substring(7),storeName,productCode);
            return ResponseEntity.ok(message);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
