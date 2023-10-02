package io.angularpay.banks.domain.commands;

public interface BankUpdatePublisherCommand<T extends BankSupplier> {

    BankHashManagerCommand getBankHashManagerCommand();

    default void publishBankUpdate(T commandResponse) {
        BankHashManagerCommand bankHashManagerCommand = this.getBankHashManagerCommand();
        bankHashManagerCommand.updateBankHash(commandResponse.getBank());
    }
}
