package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
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
        return new ResponseEntity<Message>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message Message) {
        var savedMessage = messageRepository.save(Message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message Message) {
        messageRepository.save(Message);
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
