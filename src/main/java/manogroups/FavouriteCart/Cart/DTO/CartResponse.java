package manogroups.FavouriteCart.Cart.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long cartId;
    private String productCode;
    private int quantity;

    private String staffName;
    private Long productId;
    private String productName;
    private int productQuantity;
    private Double productPrice;
    private List<String> productImageId;
}
