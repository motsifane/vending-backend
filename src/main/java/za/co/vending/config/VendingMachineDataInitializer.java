package za.co.vending.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import za.co.vending.entity.VendingItem;
import za.co.vending.repo.VendingItemRepository;

import java.util.Arrays;

@Component
public class VendingMachineDataInitializer implements CommandLineRunner {

    @Autowired
    private VendingItemRepository vendingItemRepository;

    @Override
    public void run(String... args) throws Exception {
        VendingItem soda = VendingItem.builder().name("Soft Drink").price(5.00).quantity(10).build();
        VendingItem chips = VendingItem.builder().name("Chips").price(10.00).quantity(15).build();
        VendingItem candy = VendingItem.builder().name("Chocolate Bar").price(15.00).quantity(20).build();

        vendingItemRepository.saveAll(Arrays.asList(soda, chips, candy));
    }
}

