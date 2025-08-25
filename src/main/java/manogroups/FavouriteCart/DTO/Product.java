package manogroups.FavouriteCart.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long productId;
    private String productCode;
    private String productName;
    private Double productSellingPrice;
    private List<String> productImageId;
}
