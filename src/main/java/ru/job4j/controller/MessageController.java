package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository MessageRepository) {
        this.messageRepository = MessageRepository;
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

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Error! " +
                    "Description of message is null!");
        }
        var savedMessage = messageRepository.save(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getDescription() == null) {
            throw new NullPointerException("Error! " +
                    "Description of message is null!");
        }
        messageRepository.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        messageRepository.delete(message);
        return ResponseEntity.ok().build();
    }
}
