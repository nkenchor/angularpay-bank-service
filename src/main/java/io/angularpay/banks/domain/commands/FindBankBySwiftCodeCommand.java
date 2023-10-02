package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.adapters.outbound.MongoAdapter;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.Role;
import io.angularpay.banks.exceptions.ErrorObject;
import io.angularpay.banks.models.FindBankBySwiftCodeCommandRequest;
import io.angularpay.banks.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FindBankBySwiftCodeCommand extends AbstractCommand<FindBankBySwiftCodeCommandRequest, List<Bank>>
        implements LargeDataResponseCommand {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;

    public FindBankBySwiftCodeCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter) {
        super("FindBankBySwiftCodeCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected List<Bank> handle(FindBankBySwiftCodeCommandRequest request) {
        return this.mongoAdapter.findBankBySwiftCode(request.getSwiftCode());
    }

    @Override
    protected List<ErrorObject> validate(FindBankBySwiftCodeCommandRequest request) {
        return this.validator.validate(request);
    }

    @Override
    protected List<Role> permittedRoles() {
        return Arrays.asList(
                Role.ROLE_UNVERIFIED_USER,
                Role.ROLE_VERIFIED_USER,
                Role.ROLE_BANK_ADMIN,
                Role.ROLE_PLATFORM_ADMIN
        );
    }

}
