package za.co.vending.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import za.co.vending.model.VendingItemDto;
import za.co.vending.model.VendingResponse;
import za.co.vending.service.VendingMachineService;

import java.util.List;

@Slf4j
@Controller
@Api(tags = "Vending Machine API")
public class VendingMachineController {

    @Autowired
    private VendingMachineService vendingMachineService;

    @GetMapping("/")
    @ApiOperation("Gets the list of available vending items")
    public @ResponseBody ResponseEntity<VendingResponse> getAllItems() {
        VendingResponse response = null;
        try {
            response = new VendingResponse();
            List<VendingItemDto> items = vendingMachineService.getItems();
            response.setItems(items);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/cart")
    @ApiOperation("Gets the contents of the shopping cart and total price")
    public  @ResponseBody ResponseEntity<VendingResponse>  getCart() {
        VendingResponse response = null;
        try {
            response = vendingMachineService.getCartItems();
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/add-item")
    @ApiOperation("Adds the new item to the vending machine OR updates the existing item in the vending machine")
    public @ResponseBody ResponseEntity<VendingResponse> saveItem(@RequestBody VendingItemDto vendingItem) {
        log.info("post add item");
        VendingResponse response = null;
        try {
            response = vendingMachineService.addItem(vendingItem);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/add-to-cart")
    @ApiOperation("Add selected item to the shopping cart")
    public @ResponseBody ResponseEntity<VendingResponse> addToCart(@RequestBody VendingItemDto itemDto) {
        VendingResponse response = null;
        try {
            response = vendingMachineService.addToCart(itemDto.getName(), itemDto.getQuantity());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/checkout")
    @ApiOperation("Calculate and display the total price before checkout")
    public @ResponseBody ResponseEntity<VendingResponse> checkout() {
        VendingResponse response = null;
        try {
            response = vendingMachineService.calculateTotalPrice(null);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/pay")
    @ApiOperation("Accept payment, calculate change or amount due, and show result")
    public @ResponseBody ResponseEntity<VendingResponse> pay(@RequestBody VendingItemDto itemDto) {
        VendingResponse response = null;
        try {
            response = vendingMachineService.pay(itemDto.getAmount());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

}

