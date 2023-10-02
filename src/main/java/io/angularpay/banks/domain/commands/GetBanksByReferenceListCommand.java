package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.adapters.outbound.MongoAdapter;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.Role;
import io.angularpay.banks.exceptions.ErrorObject;
import io.angularpay.banks.models.GetBanksByReferenceListCommandRequest;
import io.angularpay.banks.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetBanksByReferenceListCommand extends AbstractCommand<GetBanksByReferenceListCommandRequest, List<Bank>>
        implements LargeDataResponseCommand {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;

    public GetBanksByReferenceListCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter) {
        super("GetBanksByReferenceListCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    protected List<Bank> handle(GetBanksByReferenceListCommandRequest request) {
        return request.getReferences().stream().parallel()
                .map(reference -> mongoAdapter.findBankByReference(reference).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    protected List<ErrorObject> validate(GetBanksByReferenceListCommandRequest request) {
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
