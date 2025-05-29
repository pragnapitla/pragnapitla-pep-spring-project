package com.example.service;


import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Message> createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255
                || !accountRepository.existsById(message.getPostedBy())) {
            return Optional.empty();
        }

        return Optional.of(messageRepository.save(message));
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id) {
        return messageRepository.findById(id);
    }

    public int deleteMessageById(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessageText(int id, String newText) {
        Optional<Message> existingMessage = messageRepository.findById(id);
        if (existingMessage.isPresent()
                && newText != null
                && !newText.isBlank()
                && newText.length() <= 255) {

            Message message = existingMessage.get();
            message.setMessageText(newText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
