package manogroups.FavouriteCart.Cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manogroups.FavouriteCart.Cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository <Cart,Long>{
    List<Cart> findByUserEmailAndStoreName(String email, String storeName);
    boolean existsByStoreNameAndProductId(String storeName, Long productId);
    boolean existsByUserEmailAndStoreNameAndProductId(String email, String storeName, Long productId);
}
