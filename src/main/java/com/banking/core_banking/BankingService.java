package com.banking.core_banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BankingService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account from = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account to = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds!");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);

        // Record the transaction log
        transactionRepository.save(new Transaction(fromAccountNumber, toAccountNumber, amount, "TRANSFER", LocalDateTime.now()));
    }

    @Transactional
    public void deposit(String accountNumber, BigDecimal amount) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setBalance(acc.getBalance().add(amount));
        accountRepository.save(acc);

        // Record the transaction log
        transactionRepository.save(new Transaction("SYSTEM", accountNumber, amount, "DEPOSIT", LocalDateTime.now()));
    }
    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account acc = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Critical Check: Does the customer have enough money?
        if (acc.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds! Transaction declined.");
        }

        acc.setBalance(acc.getBalance().subtract(amount));
        accountRepository.save(acc);

        // Record the transaction log as a WITHDRAWAL
        transactionRepository.save(new Transaction("SELF", accountNumber, amount, "WITHDRAWAL", LocalDateTime.now()));
    }
}