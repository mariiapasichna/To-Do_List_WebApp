package com.mariiapasichna.service;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.User;
import com.mariiapasichna.repository.UserRepository;
import com.mariiapasichna.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    private UserService userService;
    private User user;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        user = new User();
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setEmail("test@test.com");
        user.setPassword("Password-1");

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.save(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findById(100L)).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(userRepository).delete(user);
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findByEmail("exception@test.com")).thenThrow(UsernameNotFoundException.class);
    }

    @Test
    public void createUserTest() {
        Assertions.assertEquals(userService.create(user), user);
        Assertions.assertThrows(NullEntityReferenceException.class, () -> userService.create(null));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void readByIdUserTest() {
        Assertions.assertEquals(userService.readById(user.getId()), user);
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.readById(100L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
    }

    @Test
    public void updateUserTest() {
        User newUser = new User();
        newUser.setId(100);
        Assertions.assertEquals(userService.update(user), user);
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.update(newUser));
        Assertions.assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void deleteUserTest() {
        userService.delete(user.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.delete(100L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllUserTest() {
        Assertions.assertEquals(userService.getAll(), Arrays.asList(user));
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(userService.getAll(), new ArrayList<>());
        Mockito.verify(userRepository, Mockito.times(2)).findAll();
    }

    @Test
    public void loadUserByUsernameTest() {
        Assertions.assertEquals(userService.loadUserByUsername(user.getEmail()), user);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("exception@test.com"));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("exception@test.com");
    }
}