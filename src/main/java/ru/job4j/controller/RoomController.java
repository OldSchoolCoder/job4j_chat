package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;
import ru.job4j.dto.RoleDTO;
import ru.job4j.dto.RoomDTO;
import ru.job4j.mappers.RoleMapper;
import ru.job4j.mappers.RoomMapper;
import ru.job4j.operation.Operation;
import ru.job4j.repository.RoomRepository;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    private void exceptionGuard(@RequestBody Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Error! Name of room is null!");
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Room>> findAll() {
        return new ResponseEntity<>((List<Room>) roomRepository.findAll(),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable int id) {
        var room = roomRepository.findById(id);
        return new ResponseEntity<Room>(room.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wrong id! Room not found!")), HttpStatus.OK);
    }

    @PatchMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<RoomDTO> mapping(@Valid @RequestBody Room room) {
        exceptionGuard(room);
        RoomDTO roomDTO = RoomMapper.INSTANCE.toDTO(room);
        return new ResponseEntity<>(roomDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Room> create(@Valid @RequestBody Room room) {
        exceptionGuard(room);
        var savedRoom = roomRepository.save(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Room> update(@Valid @RequestBody Room room) {
        exceptionGuard(room);
        var savedRoom = roomRepository.save(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Room room = new Room();
        room.setId(id);
        roomRepository.delete(room);
        return ResponseEntity.ok().build();
    }
}
