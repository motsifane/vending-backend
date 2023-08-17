package za.co.vending.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidQuantityException.class)
    public ModelAndView handleInvalidQuantityException(InvalidQuantityException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ModelAndView handleItemNotFoundException(ItemNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ItemAlreadyExistException.class)
    public ModelAndView handleItemAlreadyExistException(ItemAlreadyExistException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}

