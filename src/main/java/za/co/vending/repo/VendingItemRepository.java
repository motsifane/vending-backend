package za.co.vending.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.vending.entity.VendingItem;

public interface VendingItemRepository extends JpaRepository<VendingItem, Long> {
    VendingItem findByNameIgnoreCase(String name);
}
