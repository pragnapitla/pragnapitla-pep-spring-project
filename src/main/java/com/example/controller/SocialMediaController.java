package com.example.controller;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // 1: User registration
    @PostMapping("/register")
    public @ResponseBody Account register(@RequestBody Account account) {
        Optional<Account> result = accountService.register(account);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } 
        Account registered = result.get();
        if (registered.getUsername()!=null && registered.getAccountId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return registered;
    }

    // 2: User login
    @PostMapping("/login")
    public @ResponseBody Account login(@RequestBody Account account) {
        return accountService.login(account.getUsername(), account.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    // 3: Create message
    @PostMapping("/messages")
    public @ResponseBody Message createMessage(@RequestBody Message message) {
        return messageService.createMessage(message)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    // 4: Get all messages
    @GetMapping("/messages")
    public @ResponseBody List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // 5: Get message by ID
    @GetMapping("/messages/{id}")
    public @ResponseBody Message getMessageById(@PathVariable int id) {
        return messageService.getMessageById(id).orElse(null);
    }

    // 6: Delete message by ID
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int id) {
    int deletedCount = messageService.deleteMessageById(id);
    
    if (deletedCount == 0) {
        return ResponseEntity.ok().build(); 
    } else {
        return ResponseEntity.ok(deletedCount); 
    }
}

    // 7: Update message text
    @PatchMapping("/messages/{id}")
    public @ResponseBody Integer updateMessageText(@PathVariable int id, @RequestBody Message newMessage) {
        if (newMessage.getMessageText() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        int updated = messageService.updateMessageText(id, newMessage.getMessageText());
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return updated;
    }

    // 8: Get all messages by account ID
    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody List<Message> getMessagesByAccountId(@PathVariable int accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
}
