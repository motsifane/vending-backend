package za.co.vending.exception;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}