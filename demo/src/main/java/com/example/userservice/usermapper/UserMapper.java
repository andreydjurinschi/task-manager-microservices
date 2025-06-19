package com.example.userservice.usermapper;

import com.example.userservice.dtos.UserDTO;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(User user){
        return new UserDTO(user.getUsername(), user.getEmail(), user.getFullName(), user.getRole());
    }

    public User CreateDTOtoEntity(UserDTO userCreateDTO){
        return new User(userCreateDTO.getUsername(),userCreateDTO.getEmail(), userCreateDTO.getFullName(), userCreateDTO.getRole());
    }
}
