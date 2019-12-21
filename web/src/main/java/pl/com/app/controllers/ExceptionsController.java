package pl.com.app.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.com.app.exceptions.AccessDeniedException;
import pl.com.app.exceptions.MyException;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler({MyException.class})
    public String myExceptionHandler(MyException e, Model model) {
        model.addAttribute("code", e.getExceptionInfo().getExceptionCode());
        model.addAttribute("message", e.getExceptionInfo().getExceptionMessage());
        model.addAttribute("dateTime", e.getExceptionInfo().getExceptionDateTime());
        return "error-page";
    }

    @ExceptionHandler({AccessDeniedException.class})
    public String accessDeniedExceptionHandler(MyException e, Model model) {
        model.addAttribute("code", e.getExceptionInfo().getExceptionCode());
        model.addAttribute("message", e.getExceptionInfo().getExceptionMessage());
        model.addAttribute("dateTime", e.getExceptionInfo().getExceptionDateTime());
        return "error-page";
    }

}
