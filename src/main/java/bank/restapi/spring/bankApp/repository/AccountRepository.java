package bank.restapi.spring.bankApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import bank.restapi.spring.bankApp.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);


    List<Account> findByUsername(String username);
}
