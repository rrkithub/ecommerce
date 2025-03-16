package com.rkit.e.commerce.controller;

import com.rkit.e.commerce.dto.UserDTO;
import com.rkit.e.commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserDTO> findUserByName(@RequestParam String userName){
    return ResponseEntity.ok(userService.findUserByUserName(userName));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAllUsers(){
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @PatchMapping("/user/{userName}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable String userName){
        return ResponseEntity.ok(userService.updateUserDetails(userName, userDTO));
    }
}
