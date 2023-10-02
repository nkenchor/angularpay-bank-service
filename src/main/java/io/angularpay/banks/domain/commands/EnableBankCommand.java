package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.adapters.outbound.MongoAdapter;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.Role;
import io.angularpay.banks.exceptions.ErrorObject;
import io.angularpay.banks.models.EnableBankCommandRequest;
import io.angularpay.banks.models.GenericCommandResponse;
import io.angularpay.banks.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static io.angularpay.banks.helpers.CommandHelper.getBankByReferenceOrThrow;

@Slf4j
@Service
@Profile("!test")
public class EnableBankCommand extends AbstractCommand<EnableBankCommandRequest, GenericCommandResponse>
        implements BankUpdatePublisherCommand<GenericCommandResponse> {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;
    private final BankHashManagerCommand bankHashManagerCommand;

    public EnableBankCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter,
            BankHashManagerCommand bankHashManagerCommand) {
        super("EnableBankCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
        this.bankHashManagerCommand = bankHashManagerCommand;
    }

    @Override
    protected GenericCommandResponse handle(EnableBankCommandRequest request) {
        Bank bank = getBankByReferenceOrThrow(this.mongoAdapter, request.getReference()).toBuilder()
                .enabled(request.getEnabled())
                .build();

        Bank response = this.mongoAdapter.updateBank(bank);
        return GenericCommandResponse.builder()
                .requestReference(response.getReference())
                .bank(response)
                .build();
    }

    @Override
    protected List<ErrorObject> validate(EnableBankCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(Role.ROLE_BANK_ADMIN, Role.ROLE_PLATFORM_ADMIN);
    }

    @Override
    public BankHashManagerCommand getBankHashManagerCommand() {
        return this.bankHashManagerCommand;
    }
}
