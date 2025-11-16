package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.UserJdbcRepo;
import org.example.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BalanceCheckService {

    private final UserJdbcRepo repo;

    public BalanceCheckService(UserJdbcRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public boolean deductBalance(Long userId, Double amount) {

        User user = repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));
        if (user.getBalance() < amount) {
            log.warn("Insufficient balance");
            return false;
        }
        user.setBalance(user.getBalance() - amount);
        repo.save(user);
        log.info("Saving updated balance after deduction");
        return true;
    }
}
