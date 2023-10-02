package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.exceptions.CommandException;
import io.angularpay.banks.ports.outbound.OutboundMessagingPort;
import io.angularpay.banks.ports.outbound.PersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.angularpay.banks.exceptions.ErrorCode.INVALID_MESSAGE_ERROR;

@Slf4j
@Service
@Profile("!test")
public class BankHashManagerCommand {

    private final ObjectMapper mapper;
    private final OutboundMessagingPort outboundMessagingPort;

    public BankHashManagerCommand(
            ObjectMapper mapper,
            OutboundMessagingPort outboundMessagingPort,
            PersistencePort persistencePort) {
        this.mapper = mapper;
        this.outboundMessagingPort = outboundMessagingPort;
        this.loadBanksHash(persistencePort);
    }

    public void loadBanksHash(PersistencePort persistencePort) {
        try {
            log.info("reading banks from database...");
            List<Bank> banks = persistencePort.listBanks();

            if (CollectionUtils.isEmpty(banks)) {
                log.info("banks collection is empty");
                return;
            }

            log.info("read {} banks from database", banks.size());

            Map<String, String> bankMap = banks.stream()
                    .collect(Collectors.toMap(Bank::getId, this::bankToStringOrElseEmptyString));
            log.info("updating banks hash");
            this.outboundMessagingPort.publishBanks(bankMap);
        } catch (Exception exception) {
            log.error("An error occurred while publishing banks hash");
            throw new RuntimeException(CommandException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorCode(INVALID_MESSAGE_ERROR)
                    .message(exception.getMessage())
                    .cause(exception)
                    .build());
        }
    }

    public void updateBankHash(Bank bank) {
        try {
            String message = bankToStringOrElseEmptyString(bank);
            log.info("updating banks hash with {}", message);
            this.outboundMessagingPort.updateBankHash(bank.getReference(), message);
        } catch (Exception exception) {
            String message = bankToStringOrElseEmptyString(bank);
            log.error("An error occurred while updating bank hash with value {}", message, exception);
            throw new RuntimeException(CommandException.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorCode(INVALID_MESSAGE_ERROR)
                    .message(exception.getMessage())
                    .cause(exception)
                    .build());
        }
    }

    private String bankToStringOrElseEmptyString(Bank bank) {
        try {
            return mapper.writeValueAsString(bank);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }
}
