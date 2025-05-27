package bank.restapi.spring.bankApp.service;

import bank.restapi.spring.bankApp.model.Account;
import java.util.List;


import bank.restapi.spring.bankApp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account deposit(String accountNumber, Double amount) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        acc.setBalance(acc.getBalance() + amount);
        return accountRepository.save(acc);
    }

    public Account withdraw(String accountNumber, Double amount) {
        Account acc = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        if (acc.getBalance() < amount) throw new RuntimeException("Insufficient funds");
        acc.setBalance(acc.getBalance() - amount);
        return accountRepository.save(acc);
    }

    public boolean deleteAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    accountRepository.delete(account);
                    return true;
                })
                .orElse(false);
    }

    public List<Account> getAccountsByUsername(String username) {
        return accountRepository.findByUsername(username);
    }





}
