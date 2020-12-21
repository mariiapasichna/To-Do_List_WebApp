package com.mariiapasichna.service;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.Role;
import com.mariiapasichna.repository.RoleRepository;
import com.mariiapasichna.service.implementation.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class RoleServiceTests {
    private RoleService roleService;
    private Role role;

    @MockBean
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
        role = new Role();
        role.setName("TestRole");

        Mockito.when(roleRepository.save(role)).thenReturn(role);
        Mockito.when(roleRepository.save(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(role));
        Mockito.when(roleRepository.findById(100L)).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(roleRepository).delete(role);
        Mockito.when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
    }

    @Test
    public void createRoleTest() {
        Assertions.assertEquals(roleService.create(role), role);
        Assertions.assertThrows(NullEntityReferenceException.class, () -> roleService.create(null));
        Mockito.verify(roleRepository, Mockito.times(1)).save(role);
    }

    @Test
    public void readByIdRoleTest() {
        Assertions.assertEquals(roleService.readById(role.getId()), role);
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.readById(100L));
        Mockito.verify(roleRepository, Mockito.times(1)).findById(role.getId());
    }

    @Test
    public void updateRoleTest() {
        Role newRole = new Role();
        newRole.setId(100L);
        Assertions.assertEquals(roleService.update(role), role);
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.update(newRole));
        Assertions.assertThrows(NullEntityReferenceException.class, () -> roleService.update(null));
        Mockito.verify(roleRepository, Mockito.times(1)).findById(role.getId());
        Mockito.verify(roleRepository, Mockito.times(1)).save(role);
    }

    @Test
    public void deleteRoleTest() {
        roleService.delete(role.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleService.delete(100L));
        Mockito.verify(roleRepository, Mockito.times(1)).findById(role.getId());
        Mockito.verify(roleRepository, Mockito.times(1)).delete(role);
    }

    @Test
    public void getAllRoleTest() {
        Assertions.assertEquals(roleService.getAll(), Arrays.asList(role));
        Mockito.when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(roleService.getAll(), new ArrayList<>());
        Mockito.verify(roleRepository, Mockito.times(2)).findAll();
    }
}