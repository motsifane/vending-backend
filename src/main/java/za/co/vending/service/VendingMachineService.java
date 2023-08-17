package za.co.vending.service;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.vending.entity.VendingItem;
import za.co.vending.exception.InvalidQuantityException;
import za.co.vending.exception.ItemNotFoundException;
import za.co.vending.model.VendingItemDto;
import za.co.vending.model.VendingResponse;
import za.co.vending.repo.VendingItemRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@NoArgsConstructor
public class VendingMachineService {

    private List<VendingItemDto> items;
    private List<VendingItem> itemsDb;
    private Map<String, Integer> itemInventory = new HashMap<>();
    private Map<String, Integer> cart = new HashMap<>();
    private double totalPaid;
    @Autowired
    private VendingItemRepository vendingItemRepository;

    public int getItemInventory(String name) {
        return itemInventory.getOrDefault(name, 0);
    }

    public boolean purchaseItem(String name, int quantity) {
        VendingItemDto item = getItemByName(name);
        if (item != null && item.getQuantity() >= quantity) {
            item.setQuantity(item.getQuantity() - quantity);
            return true;
        }
        return false;
    }

    /**
     * Get the list of available vending items.
     *
     * @return A list of vending items.
     */
    public List<VendingItemDto> getItems() {
        itemsDb = vendingItemRepository.findAll();
        items = mapValue(itemsDb);
        return items;
    }

    private List<VendingItemDto> mapValue(List<VendingItem> itemsDb) {
        List<VendingItemDto> itemDtos = new ArrayList<>();
        for (VendingItem item : itemsDb) {
            VendingItemDto itemDto = new Gson().fromJson(new Gson().toJson(item), VendingItemDto.class);
            itemDtos.add(itemDto);
        }
        return itemDtos;
    }

    // Get a vending item by its name
    public VendingItemDto getItemByName(String name) {
        return items.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void resetTotalPaid() {
        totalPaid = 0;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void addPayment(double amount) {
        totalPaid += amount;
    }

    public double calculateChange(String itemName) {
        VendingItemDto item = getItemByName(itemName);
        return totalPaid - item.getPrice();
    }

    // Get the contents of the shopping cart
    public Map<String, Integer> getCart() {
        return cart;
    }

    public VendingResponse getCartItems() {
        VendingResponse response = new VendingResponse();
        Map<String, Integer> cart = getCart();
        double totalPrice = calculateTotalPrice(cart).getTotalPrice();
        response.setCart(cart);
        response.setTotalPrice(totalPrice);
        return response;
    }

    /**
     * Add items to the shopping cart.
     *
     * @param itemName The name of the item to add.
     * @param quantity The quantity of items to add.
     */
    public VendingResponse addToCart(String itemName, int quantity) {
        VendingResponse response = new VendingResponse();
        log.debug("Adding {} item(s) of {} to the cart.", quantity, itemName);

        if (quantity <= 0) {
            log.warn("Invalid quantity: {}. Quantity must be greater than zero.", quantity);
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }

        VendingItem item = vendingItemRepository.findByNameIgnoreCase(itemName);
        if (item == null) {
            log.warn("Item not found: {}", itemName);
            throw new ItemNotFoundException("Item not found.");
        }

        cart.put(itemName, cart.getOrDefault(itemName, 0) + quantity);
        log.info("{} item(s) of {} added to the cart.", quantity, itemName);
        response.setSuccessful(true);
        response.setMessage(String.format("item [%s] added to cart", itemName));
        return response;
    }
    //    public void addToCart(String itemName, int quantity) {
//        cart.put(itemName, cart.getOrDefault(itemName, 0) + quantity);
//    }

    // Clear the shopping cart
    public void clearCart() {
        cart.clear();
    }

    // Calculate the total price of items in the cart
    public VendingResponse calculateTotalPrice(Map<String, Integer> cart) {
        VendingResponse response = new VendingResponse();
        if (cart == null || cart.isEmpty()) {
            cart = getCart();
        }
        double total = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            VendingItemDto item = getItemByName(entry.getKey());
            total += item.getPrice() * entry.getValue();
        }
        response.setTotalPrice(total);
        return response;
    }

    // Add a new vending machine item OR update an existing item
    public VendingResponse addItem(VendingItemDto vendingItem) {
        VendingResponse response = new VendingResponse();
        VendingItem item = vendingItemRepository.findByNameIgnoreCase(vendingItem.getName());
        if (item == null) {
            log.info("Item found: {}", vendingItem.getName());
            VendingItem itemNew = new Gson().fromJson(new Gson().toJson(vendingItem), VendingItem.class);
            vendingItemRepository.save(itemNew);
            response.setMessage(String.format("vending machine item [%s] added", vendingItem.getName()));
        } else {
            item.setPrice(vendingItem.getPrice());
            item.setQuantity(vendingItem.getQuantity());
            vendingItemRepository.save(item);
            response.setMessage(String.format("vending machine item [%s] updated", vendingItem.getName()));
        }
        response.setSuccessful(true);
        return response;
    }

    // Update the vending machine quantities only if the amount is paid in full
    public void updateCartQuantity(double change) {
        if (change >= 0) {
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                VendingItem item = vendingItemRepository.findByNameIgnoreCase(entry.getKey());
                item.setQuantity(item.getQuantity() - entry.getValue());
                vendingItemRepository.save(item);
            }
        } else {
            log.warn("there is still a due amount for R{}", change);
        }
        clearCart();
    }

    public VendingResponse pay(double amount) {
        VendingResponse response = new VendingResponse();
        double totalPrice = calculateTotalPrice(null).getTotalPrice();
        double change = amount - totalPrice;
        response.setTotalPrice(totalPrice);
        response.setChange(change);
        updateCartQuantity(change);
        return response;
    }
}

