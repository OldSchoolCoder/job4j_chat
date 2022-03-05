package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.dto.PersonDTO;
import ru.job4j.exception_handling.WrongIdException;
import ru.job4j.mappers.PersonMapper;
import ru.job4j.operation.Operation;
import ru.job4j.repository.PersonRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    private void exceptionGuard(@RequestBody Person person) {
        var username = person.getName();
        var password = person.getPassword();
        if (username == null || password == null) {
            throw new NullPointerException("Username and password" +
                    " mustn't be empty");
        }
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return (List<Person>) personRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personRepository.findById(id);
        return new ResponseEntity<Person>(person.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wrong id! Person not found!")), HttpStatus.OK);
    }

    @PatchMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<PersonDTO>
    mapping(@Valid @RequestBody Person person) {
        exceptionGuard(person);
        PersonDTO personDTO = PersonMapper.INSTANCE.toDTO(person);
        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person>
    create(@Valid @RequestBody Person person) {
        exceptionGuard(person);
        var savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Person>
    update(@Valid @RequestBody Person person) {
        exceptionGuard(person);
        var savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id < 0) {
            throw new WrongIdException("Error! ID = " + id + " is wrong!");
        }
        Person person = new Person();
        person.setId(id);
        personRepository.delete(person);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> signUp(@Valid @RequestBody Person person) {
        exceptionGuard(person);
        person.setPassword(encoder.encode(person.getPassword()));
        var savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @ExceptionHandler(value = {WrongIdException.class})
    public void exceptionHandler(Exception e,
                                 HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper
                .writeValueAsString(new HashMap<>() {
                    {
                        put("message", e.getMessage());
                        put("type", e.getClass());
                    }
                }));
    }
}
