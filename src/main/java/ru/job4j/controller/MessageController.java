package ru.job4j.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.dto.MessageDTO;
import ru.job4j.mappers.MessageMapper;
import ru.job4j.operation.Operation;
import ru.job4j.repository.MessageRepository;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {

    private final MessageRepository messageRepository;

    private void exceptionGuard(Message message) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Error! " +
                    "Description of message is null!");
        }
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return (List<Message>) messageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        var message = messageRepository.findById(id);
        return new ResponseEntity<Message>(message.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Wrong id! Message not found!")), HttpStatus.OK);
    }

    @PatchMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<MessageDTO>
    mapping(@Valid @RequestBody Message message) {
        exceptionGuard(message);
        MessageDTO messageDTO = MessageMapper.INSTANCE.toDTO(message);
        return new ResponseEntity<>(messageDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message>
    create(@Valid @RequestBody Message message) {
        exceptionGuard(message);
        var savedMessage = messageRepository.save(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Message>
    update(@Valid @RequestBody Message message) {
        exceptionGuard(message);
        var savedMessage = messageRepository.save(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }
}
