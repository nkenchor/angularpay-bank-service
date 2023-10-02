package io.angularpay.banks.adapters.outbound;

import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.ports.outbound.PersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongoAdapter implements PersistencePort {

    private final BankRepository bankRepository;

    @Override
    public Bank createBank(Bank bank) {
        bank.setCreatedOn(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        bank.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return bankRepository.save(bank);
    }

    @Override
    public Bank updateBank(Bank bank) {
        bank.setLastModified(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return bankRepository.save(bank);
    }

    @Override
    public Optional<Bank> findBankByReference(String reference) {
        return bankRepository.findByReference(reference);
    }

    @Override
    public List<Bank> findBankByName(String name) {
        return bankRepository.findByNameContains(name);
    }

    @Override
    public List<Bank> findBankBySwiftCode(String swiftCode) {
        return bankRepository.findBySwiftCodeContains(swiftCode);
    }

    @Override
    public Page<Bank> listBanks(Pageable pageable) {
        return this.bankRepository.findAll(pageable);
    }

    @Override
    public List<Bank> listBanks() {
        return this.bankRepository.findAll();
    }

    @Override
    public List<Bank> findBanksByCountryReference(String countryReference) {
        return this.bankRepository.findByCountryReference(countryReference);
    }

}
