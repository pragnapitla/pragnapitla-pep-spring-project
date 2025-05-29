package com.example.service;


import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty();  
        }

        try {
            Account saved = accountRepository.save(account);
        return Optional.of(saved);
    } catch (DataIntegrityViolationException e) {
        Account conflict = new Account();
        conflict.setUsername(account.getUsername());
        return Optional.of(conflict);
    }
    }


    public Optional<Account> login(String username, String password) {
        return accountRepository.findByUsername(username)
                .filter(a -> a.getPassword().equals(password));
    }
}
