package com.rkit.e.commerce.service;

import com.rkit.e.commerce.dto.UserDTO;
import com.rkit.e.commerce.entity.User;
import com.rkit.e.commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO findUserByUserName(String userName){
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return getUserDTO(user);
        } else {
            throw new RuntimeException("user not found");
        }

    }

    private UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<UserDTO> fetchAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(this::getUserDTO).collect(Collectors.toList());
    }

    public UserDTO updateUserDetails(String userName, UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails currentUser) {
            if(userName.equals(currentUser.getUsername()) || currentUser.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority())))
            {
                Optional<User> userOptional = userRepository.findByUserName(userName);
                if(userOptional.isPresent()){
                    User user = userOptional.get();
                    if(Objects.nonNull(userDTO.getUserName()) && !Objects.equals(user.getUserName(),userDTO.getUserName())){
                        user.setUserName(userDTO.getUserName());
                    }
                    if(Objects.nonNull(userDTO.getRole()) && !Objects.equals(user.getRole(), userDTO.getRole())){
                        user.setRole(userDTO.getRole());
                    }
                    if(Objects.nonNull(userDTO.getPassword()) && !Objects.equals(user.getPassword(),passwordEncoder.encode(userDTO.getPassword()))){
                        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }
                    userRepository.save(user);
                    return getUserDTO(user);
                }
            }


        }
    return null;


    }
}
