package io.angularpay.banks.domain.commands;

import io.angularpay.banks.domain.Bank;

public interface BankSupplier {
    Bank getBank();
}
