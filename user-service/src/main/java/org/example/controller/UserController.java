package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.UserJdbcRepo;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserJdbcRepo jdbcRepo;

    @GetMapping("/dummy")
    public String dummyUserMessage() {
        log.info("dummy message from user..");
        return "dummy message from user..";
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User save = jdbcRepo.save(user);
        log.info("User added : {}", save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @PostMapping("/addMultipleUsers")
    public ResponseEntity<List<User>> addMultipleUsers(@RequestBody List<User> users) {
        List<User> save = jdbcRepo.saveAll(users);
        log.info("Multiple Users added : {}", save);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = jdbcRepo.findById(id);
        if (user.isPresent()) {
            log.info("User found : {}", user.get());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            log.info("User not found with id : {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findAllUsers")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> all = jdbcRepo.findAll();
        log.info("All users:");
        all.forEach(
                System.out::println
        );
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
