
package io.angularpay.banks.models;

import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.commands.BankSupplier;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class GenericCommandResponse extends GenericReferenceResponse implements BankSupplier {

    private final String requestReference;
    private final String itemReference;
    private final Bank bank;
}
