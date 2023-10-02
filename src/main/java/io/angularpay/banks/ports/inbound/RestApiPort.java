package io.angularpay.banks.ports.inbound;

import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.models.GenericBankApiModel;
import io.angularpay.banks.models.GenericReferenceResponse;

import java.util.List;
import java.util.Map;

public interface RestApiPort {
    GenericReferenceResponse createBank(GenericBankApiModel request, Map<String, String> headers);
    void updateBank(String blogPostReference, GenericBankApiModel request, Map<String, String> headers);
    void enableBank(String bankReference, Boolean enabled, Map<String, String> headers);
    Bank getBankByReference(String bankReference, Map<String, String> headers);
    List<Bank> findBankByName(String name, Map<String, String> headers);
    List<Bank> findBankBySwiftCode(String swiftCode, Map<String, String> headers);
    List<Bank> getBankList(int page, Map<String, String> headers);
    List<Bank> getBanksByReferenceList(List<String> bankReferences, Map<String, String> headers);
    List<Bank> getBanksByCountryReference(String countryReference, Map<String, String> headers);
}
