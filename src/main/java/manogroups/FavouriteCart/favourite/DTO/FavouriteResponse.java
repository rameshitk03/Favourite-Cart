package manogroups.FavouriteCart.favourite.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteResponse {

    private Long favouriteId;
    private String productCode;
    
    private Long productId;
    private int productQuantity;
    private String productName;
    private Double productPrice;
    private List<String> productImageId;
}

