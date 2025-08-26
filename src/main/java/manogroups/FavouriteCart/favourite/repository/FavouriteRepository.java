package manogroups.FavouriteCart.favourite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manogroups.FavouriteCart.favourite.entity.Favourite;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long>{
    List<Favourite> findByUserEmailAndStoreNameOrderByFavouriteIdAsc(String email, String storeName);
    boolean existsByUserEmailAndStoreNameAndProductCode(String userEmail, String storeName, String productCode);
    void deleteByUserEmailAndStoreNameAndProductCode(String userEmail, String storeName, String productCode);
}
