package com.rkit.e.commerce.service;

import com.rkit.e.commerce.dto.UserDTO;
import com.rkit.e.commerce.entity.User;
import com.rkit.e.commerce.repository.UserRepository;
import com.rkit.e.commerce.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserDTO user) {
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new RuntimeException("user already exists..!!");
        }
        User userEntity = new User();
        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(user.getRole());
        userRepository.save(userEntity);
        return Constants.REGISTRATION_SUCCESS_MESSAGE;
    }

    public boolean loginUser(UserDTO userDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword()));
        return authentication.isAuthenticated();
    }
}
