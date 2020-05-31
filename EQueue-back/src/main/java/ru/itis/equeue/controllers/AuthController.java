package ru.itis.equeue.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.equeue.dto.LoginDto;
import ru.itis.equeue.dto.TokenDto;
import ru.itis.equeue.dto.UserDto;
import ru.itis.equeue.services.UsersService;

@RestController
public class AuthController {
    @Autowired
    private UsersService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/registration")
    public ResponseEntity<TokenDto> registration(@RequestBody UserDto userDto) {
        try {
            userService.registration(userDto);
            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
