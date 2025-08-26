package manogroups.FavouriteCart.favourite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import manogroups.FavouriteCart.favourite.DTO.FavouriteResponse;
import manogroups.FavouriteCart.favourite.entity.Favourite;
import manogroups.FavouriteCart.favourite.service.FavouriteService;


@RestController
@RequestMapping("api/favourite")
public class FavouriteController {

    @Autowired
    FavouriteService favouriteService;

    @PostMapping("/add")
    public ResponseEntity<String> addFavourite(@RequestHeader("Authorization") String authHeader,@RequestBody Favourite favourite){
        try{
            String message = favouriteService.addFavourite(authHeader.substring(7),favourite);
            return ResponseEntity.ok(message);
        }catch( RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/favourites/{storeName}")
    public ResponseEntity<?> getFavourites(@RequestHeader("Authorization") String authHeader,@PathVariable String storeName){
        List<FavouriteResponse> favourites = favouriteService.getFavourites(authHeader.substring(7),storeName);
        if(favourites.isEmpty()){
            return ResponseEntity.badRequest().body("No Product Available in Your Favourite. Please Add a Product in your Favourite");
        }
        return ResponseEntity.ok(favourites);
    }

    @GetMapping("/favourite/{storeName}")
    public ResponseEntity<Boolean> isFavourite(@RequestHeader("Authorization") String authHeader,@PathVariable String storeName,@RequestParam String productCode){
        boolean favourite = favouriteService.isFavourite(authHeader.substring(7),storeName,productCode);
        return ResponseEntity.ok(favourite);
    }

    @DeleteMapping("delete/{favouriteId}")
    public ResponseEntity<String> deleteFavourite(@RequestHeader("Authorization") String authHeader,@PathVariable Long favouriteId){
        try{
            String message = favouriteService.deleteFavourite(authHeader.substring(7), favouriteId);
            return ResponseEntity.ok(message);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok("Checking");
    }
}

