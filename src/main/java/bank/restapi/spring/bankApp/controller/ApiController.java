package bank.restapi.spring.bankApp.controller;

import bank.restapi.spring.bankApp.model.Account;
import bank.restapi.spring.bankApp.model.User;
import bank.restapi.spring.bankApp.service.AccountService;
import bank.restapi.spring.bankApp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class ApiController {

    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.register(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User loggedInUser = authService.login(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(loggedInUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(@RequestParam String username) {
        List<Account> accounts = accountService.getAccountsByUsername(username);
        return ResponseEntity.ok(accounts);
    }



    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account acc) {
        return accountService.createAccount(acc);
    }

    @PutMapping("/accounts/deposit")
    public Account deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        return accountService.deposit(accountNumber, amount);
    }

    @PutMapping("/accounts/withdraw")
    public Account withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    // ✅ ADD THIS METHOD FOR DELETION:
    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber) {
        boolean deleted = accountService.deleteAccount(accountNumber);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
