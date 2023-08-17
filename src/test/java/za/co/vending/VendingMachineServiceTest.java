package za.co.vending;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import za.co.vending.entity.VendingItem;
import za.co.vending.exception.InvalidQuantityException;
import za.co.vending.exception.ItemNotFoundException;
import za.co.vending.repo.VendingItemRepository;
import za.co.vending.service.VendingMachineService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Map;

import static org.mockito.Mockito.*;

public class VendingMachineServiceTest {

    @Mock
    private VendingItemRepository vendingItemRepository;

    @InjectMocks
    private VendingMachineService vendingMachineService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddToCart_ValidItemAndQuantity_ShouldAddToCart() {
        VendingItem item = new VendingItem();
        item.setName("ItemName");
        item.setPrice(2.0);

        when(vendingItemRepository.findByNameIgnoreCase(anyString())).thenReturn(item);

        vendingMachineService.addToCart("ItemName", 3);

        Map<String, Integer> cart = vendingMachineService.getCart();
        assertEquals(3, cart.get("ItemName"));
    }

    @Test
    public void testAddToCart_InvalidQuantity_ShouldThrowException() {
        assertThrows(InvalidQuantityException.class, () -> {
            vendingMachineService.addToCart("ItemName", 0);
        });
    }

    @Test
    public void testAddToCart_ItemNotFound_ShouldThrowException() {
        when(vendingItemRepository.findByNameIgnoreCase(anyString())).thenReturn(null);

        assertThrows(ItemNotFoundException.class, () -> {
            vendingMachineService.addToCart("ItemName", 2);
        });
    }

}


