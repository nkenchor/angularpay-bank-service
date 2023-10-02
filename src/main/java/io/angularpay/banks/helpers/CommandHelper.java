package io.angularpay.banks.helpers;

import io.angularpay.banks.adapters.outbound.MongoAdapter;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.exceptions.CommandException;
import io.angularpay.banks.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.angularpay.banks.exceptions.ErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommandHelper {

    public static Bank getBankByReferenceOrThrow(MongoAdapter mongoAdapter, String reference) {
        return mongoAdapter.findBankByReference(reference).orElseThrow(
                () -> commandException(HttpStatus.NOT_FOUND, REQUEST_NOT_FOUND)
        );
    }

    private static CommandException commandException(HttpStatus status, ErrorCode errorCode) {
        return CommandException.builder()
                .status(status)
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .build();
    }

}
