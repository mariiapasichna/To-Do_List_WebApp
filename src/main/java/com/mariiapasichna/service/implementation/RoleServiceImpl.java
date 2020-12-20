package com.mariiapasichna.service.implementation;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.Role;
import com.mariiapasichna.repository.RoleRepository;
import com.mariiapasichna.service.RoleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        try {
            return roleRepository.save(role);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Role can't be 'null'");
        }
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    @Override
    public Role update(Role role) {
        if (role == null) {
            throw new NullEntityReferenceException("Role can't be null");
        }
        readById(role.getId());
        return roleRepository.save(role);
    }

    @Override
    public void delete(long id) {
        Role role = readById(id);
        roleRepository.delete(role);
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}