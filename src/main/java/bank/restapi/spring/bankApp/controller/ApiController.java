package bank.restapi.spring.bankApp.controller;

import bank.restapi.spring.bankApp.model.Account;
import bank.restapi.spring.bankApp.model.User;
import bank.restapi.spring.bankApp.service.AccountService;
import bank.restapi.spring.bankApp.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ApiController {

    private final AuthService authService;
    private final AccountService accountService;

    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User  registered = authService.register(user.getUsername(), user.getPassword());



            return new ResponseEntity<>(registered, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthService.LoginResponse response = authService.login(request.getUsername(), request.getPassword());
            if(response == null) {
                throw new RuntimeException("Invalid username and password");
            }
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(Authentication authentication) {
        String username = authentication.getName();
        List<Account> accounts = accountService.getAccountsByUsername(username);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account acc, Authentication authentication) {
        acc.setUsername(authentication.getName());
        Account created = accountService.createAccount(acc);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/accounts/deposit")
    public Account deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        return accountService.deposit(accountNumber, amount);
    }

    @PutMapping("/accounts/withdraw")
    public Account withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> deleteAccount(@PathVariable String accountNumber) {
        boolean deleted = accountService.deleteAccount(accountNumber);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
