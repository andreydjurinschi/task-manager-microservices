package com.example.userservice.service;

import com.example.userservice.dao.UserDAOImpl;
import com.example.userservice.dtos.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exceptions.CreateOrUpdateEntityException;
import com.example.userservice.exceptions.EntityNotFoundException;
import com.example.userservice.usermapper.UserMapper;
import com.example.userservice.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDAOImpl userDao;
    private final UserMapper userMapper;
    private  final UserValidator validator;

    @Autowired
    public UserService(UserDAOImpl userDao, UserMapper userMapper, UserValidator validator) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.validator = validator;
    }

    /**
     * Retrieves all users and returns them as DTOs
     * @return a list of {@link UserDTO}
     */
    public List<UserDTO> getUsers() {
        List<User> users = userDao.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(userMapper.toDto(user));
        }
        return userDTOs;
    }

    public UserDTO getUserByUsername(String username){
        User user = userDao.findByUsername(username);
        if(user == null){
            throw  new EntityNotFoundException("user not found");
        }
        return userMapper.toDto(user);
    }

    /**
     * Retrieves an user by id and returns him as DTO
     * @param id
     * @return
     */
    public UserDTO getUser(Long id) {
        User user = userDao.findById(id);
        if(user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        return userMapper.toDto(user);
    }

    public void createUser(UserDTO userCreateDTO) throws CreateOrUpdateEntityException {

        List<String> errors = new ArrayList<>();
        if (!validator.checkIfNotEmpty(userCreateDTO.getUsername()) ||
                !validator.checkLength(userCreateDTO.getUsername(), 1, 25)) {
            errors.add("Username must be between 1 and 25 characters");
        }

        if (!validator.checkIfNotEmpty(userCreateDTO.getEmail()) ||
                !validator.checkLength(userCreateDTO.getEmail(), 1, 50)) {
            errors.add("Email must be between 1 and 50 characters");
        }

        if (!validator.checkIfNotEmpty(userCreateDTO.getFullName()) ||
                !validator.checkLength(userCreateDTO.getFullName(), 1, 50)) {
            errors.add("Full name must be between 1 and 50 characters");
        }

        if (userCreateDTO.getRole() == null) {
            errors.add("Role must be set");
        }
        if(!errors.isEmpty()) {
            throw new CreateOrUpdateEntityException(errors);
        }
        User user = userMapper.CreateDTOtoEntity(userCreateDTO);
        userDao.createOrUpdate(user);
    }


/*    public void updateUser(UserDTO dto) throws CreateOrUpdateEntityException {
        List<String> errors = new ArrayList<>();

        User user = userDao.findById(dto.getId());
        if (user == null) {
            throw new EntityNotFoundException("User with id " + dto.getId() + " not found");
        }

        if (dto.getUsername() != null) {
            if (!validator.checkLength(dto.getUsername(), 1, 25)) {
                errors.add("Username must be between 1 and 25 characters");
            } else {
                user.setUsername(dto.getUsername());
            }
        }

        if (dto.getEmail() != null) {
            if (!validator.checkLength(dto.getEmail(), 1, 50)) {
                errors.add("Email must be between 1 and 50 characters");
            } else {
                user.setEmail(dto.getEmail());
            }
        }

        if (dto.getFullName() != null) {
            if (!validator.checkLength(dto.getFullName(), 1, 50)) {
                errors.add("Full name must be between 1 and 50 characters");
            } else {
                user.setFullName(dto.getFullName());
            }
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (!errors.isEmpty()) {
            throw new CreateOrUpdateEntityException(errors);
        }
        userDao.createOrUpdate(user);
    }*/

    public void deleteUser(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userDao.delete(id);
    }
}
