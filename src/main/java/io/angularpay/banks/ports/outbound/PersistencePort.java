package io.angularpay.banks.ports.outbound;

import io.angularpay.banks.domain.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PersistencePort {
    Bank createBank(Bank bank);
    Bank updateBank(Bank bank);
    Optional<Bank> findBankByReference(String reference);
    List<Bank> findBankByName(String name);
    List<Bank> findBankBySwiftCode(String swiftCode);
    Page<Bank> listBanks(Pageable pageable);
    List<Bank> listBanks();
    List<Bank> findBanksByCountryReference(String countryReference);
}
