package ru.itis.equeue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.equeue.services.ConfirmService;

@RestController
public class ConfirmController {

    @Autowired
    private ConfirmService confirmService;

    //  fetch(`${API_URL}/confirm/${id}`)
    @GetMapping("/confirm/{confirm-string}")
    @ResponseBody
    public ResponseEntity<String> confirmUser(@PathVariable("confirm-string") String confirmString) {
        try {
            confirmService.confirm(confirmString);
            return ResponseEntity.ok("Successful email confirmation");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Confirmation error");
        }
    }
}