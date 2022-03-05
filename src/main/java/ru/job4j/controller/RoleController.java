package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.dto.PersonDTO;
import ru.job4j.dto.RoleDTO;
import ru.job4j.mappers.PersonMapper;
import ru.job4j.mappers.RoleMapper;
import ru.job4j.operation.Operation;
import ru.job4j.repository.RoleRepository;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private void exceptionGuard(Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("Error! Name of role is null!");
        }
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        var role = roleRepository.findById(id);
        return new ResponseEntity<Role>(role.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wrong id! Role not found!")), HttpStatus.OK);
    }

    @PatchMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<RoleDTO> mapping(@Valid @RequestBody Role role) {
        exceptionGuard(role);
        RoleDTO roleDTO = RoleMapper.INSTANCE.toDTO(role);
        return new ResponseEntity<>(roleDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        exceptionGuard(role);
        var savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) {
        exceptionGuard(role);
        var savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Role role = new Role();
        role.setId(id);
        roleRepository.delete(role);
        return ResponseEntity.ok().build();
    }
}
