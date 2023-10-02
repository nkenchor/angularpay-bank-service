package io.angularpay.banks.domain.commands;

public interface ResourceReferenceCommand<T, R> {

    R map(T referenceResponse);
}
