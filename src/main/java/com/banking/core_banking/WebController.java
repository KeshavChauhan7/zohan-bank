package com.banking.core_banking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
public class WebController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BankingService bankingService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("transactions", transactionRepository.findAllByOrderByTimestampDesc());
        return "dashboard";
    }

    @PostMapping("/web/create")
    public String createAccount(@RequestParam String ownerName, @RequestParam String accountNumber) {
        accountRepository.save(new Account(accountNumber, ownerName, BigDecimal.ZERO));
        return "redirect:/";
    }

    @PostMapping("/web/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        bankingService.deposit(accountNumber, amount);
        return "redirect:/";
    }

    @PostMapping("/web/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        bankingService.withdraw(accountNumber, amount);
        return "redirect:/";
    }

    @PostMapping("/web/transfer")
    public String transfer(@RequestParam String fromAccount, @RequestParam String toAccount, @RequestParam BigDecimal amount) {
        bankingService.transferMoney(fromAccount, toAccount, amount);
        return "redirect:/";
    }
}