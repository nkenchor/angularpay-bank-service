package io.angularpay.banks.adapters.outbound;

import io.angularpay.banks.domain.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends MongoRepository<Bank, String> {

    Optional<Bank> findByReference(String reference);

    @Query(value = "{'name': {$regex : ?0, $options: 'i'}}")
    List<Bank> findByNameContains(String name);

    @Query(value = "{'swiftCode': {$regex : ?0, $options: 'i'}}")
    List<Bank> findBySwiftCodeContains(String swiftCode);

    List<Bank> findByCountryReference(String countryReference);
}
