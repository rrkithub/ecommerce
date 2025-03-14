package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.UserDTO;
import com.rkit.e.commerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user){
        if ("Registration Success!".equals(authService.registerUser(user))){
            return ResponseEntity.ok("Registration Success!");
        } else{
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }
}
