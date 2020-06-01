package ru.itis.equeue.handlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.equeue.exceptions.FileStorageException;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleException(FileStorageException exception, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", exception.getMsg());
        mav.setViewName("error");
        return mav;

    }
}
