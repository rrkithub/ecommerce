package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.UserDTO;
import com.rkit.e.commerce.service.AuthService;
import com.rkit.e.commerce.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.rkit.e.commerce.util.Constants.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO user) {
        if (REGISTRATION_SUCCESS_MESSAGE.equals(authService.registerUser(user))) {
            return ResponseEntity.ok(REGISTRATION_SUCCESS_MESSAGE);
        } else {
            return ResponseEntity.internalServerError().body(Constants.SOMETHING_WENT_WRONG);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        if (authService.loginUser(userDTO)) {
            return ResponseEntity.ok("Login Success..!!!");
        }
        return ResponseEntity.badRequest().body("Invalid Credentials");
    }
}
