package io.angularpay.banks.domain.commands;

public interface SensitiveDataCommand<T> {
    T mask(T raw);
}
