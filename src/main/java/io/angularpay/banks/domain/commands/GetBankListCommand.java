package io.angularpay.banks.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.banks.adapters.outbound.MongoAdapter;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.Role;
import io.angularpay.banks.exceptions.ErrorObject;
import io.angularpay.banks.models.GetBankListCommandRequest;
import io.angularpay.banks.validation.DefaultConstraintValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GetBankListCommand extends AbstractCommand<GetBankListCommandRequest, List<Bank>>
        implements LargeDataResponseCommand {

    private final MongoAdapter mongoAdapter;
    private final DefaultConstraintValidator validator;

    public GetBankListCommand(
            ObjectMapper mapper,
            MongoAdapter mongoAdapter,
            DefaultConstraintValidator validator) {
        super("GetBankListCommand", mapper);
        this.mongoAdapter = mongoAdapter;
        this.validator = validator;
    }

    @Override
    protected List<Bank> handle(GetBankListCommandRequest request) {
        Pageable pageable = PageRequest.of(request.getPaging().getIndex(), request.getPaging().getSize());
        return this.mongoAdapter.listBanks(pageable).getContent();
    }

    @Override
    protected List<ErrorObject> validate(GetBankListCommandRequest request) {
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
