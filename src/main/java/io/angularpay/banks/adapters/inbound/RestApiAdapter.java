package io.angularpay.banks.adapters.inbound;

import io.angularpay.banks.configurations.AngularPayConfiguration;
import io.angularpay.banks.domain.Bank;
import io.angularpay.banks.domain.commands.*;
import io.angularpay.banks.models.*;
import io.angularpay.banks.ports.inbound.RestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.angularpay.banks.helpers.Helper.fromHeaders;

@RestController
@RequestMapping("/banks/entities")
@RequiredArgsConstructor
@Profile("!test")
public class RestApiAdapter implements RestApiPort {

    private final CreateBankCommand createBankCommand;
    private final UpdateBankCommand updateBankCommand;
    private final EnableBankCommand enableBankCommand;
    private final GetBankByReferenceCommand getBankByReferenceCommand;
    private final FindBankByNameCommand findBankByNameCommand;
    private final FindBankBySwiftCodeCommand findBankBySwiftCodeCommand;
    private final GetBankListCommand getBankListCommand;
    private final GetBanksByReferenceListCommand getBanksByReferenceListCommand;
    private final GetBanksByCountryReferenceCommand getBanksByCountryReferenceCommand;
    private final AngularPayConfiguration configuration;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public GenericReferenceResponse createBank(
            @RequestBody GenericBankApiModel request,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        CreateBankCommandRequest createBankCommandRequest = CreateBankCommandRequest.builder()
                .genericBankApiModel(request)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.createBankCommand.execute(createBankCommandRequest);
    }

    @PutMapping("/{bankReference}")
    @Override
    public void updateBank(
            @PathVariable String bankReference,
            @RequestBody GenericBankApiModel request,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        UpdateBankCommandRequest updateBankCommandRequest = UpdateBankCommandRequest.builder()
                .reference(bankReference)
                .genericBankApiModel(request)
                .authenticatedUser(authenticatedUser)
                .build();
        this.updateBankCommand.execute(updateBankCommandRequest);
    }

    @PutMapping("/{bankReference}/enable/{enabled}")
    @Override
    public void enableBank(
            @PathVariable String bankReference,
            @PathVariable Boolean enabled,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        EnableBankCommandRequest enableBankCommandRequest = EnableBankCommandRequest.builder()
                .reference(bankReference)
                .enabled(enabled)
                .authenticatedUser(authenticatedUser)
                .build();
        this.enableBankCommand.execute(enableBankCommandRequest);
    }

    @GetMapping("/{bankReference}")
    @ResponseBody
    @Override
    public Bank getBankByReference(
            @PathVariable String bankReference,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GenericBankCommandRequest genericBankCommandRequest = GenericBankCommandRequest.builder()
                .reference(bankReference)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.getBankByReferenceCommand.execute(genericBankCommandRequest);
    }

    @GetMapping("/bank-name/{bankName}")
    @ResponseBody
    @Override
    public List<Bank> findBankByName(
            @PathVariable String bankName,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        FindBankByNameCommandRequest findBankByNameCommandRequest = FindBankByNameCommandRequest.builder()
                .name(bankName)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.findBankByNameCommand.execute(findBankByNameCommandRequest);
    }

    @GetMapping("/swift-code/{swiftCode}")
    @ResponseBody
    @Override
    public List<Bank> findBankBySwiftCode(
            @PathVariable String swiftCode,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        FindBankBySwiftCodeCommandRequest findBankBySwiftCodeCommandRequest = FindBankBySwiftCodeCommandRequest.builder()
                .swiftCode(swiftCode)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.findBankBySwiftCodeCommand.execute(findBankBySwiftCodeCommandRequest);
    }

    @GetMapping("/list/page/{page}")
    @ResponseBody
    @Override
    public List<Bank> getBankList(
            @PathVariable int page,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetBankListCommandRequest getBankListCommandRequest = GetBankListCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .paging(Paging.builder().size(this.configuration.getPageSize()).index(page).build())
                .build();
        return this.getBankListCommand.execute(getBankListCommandRequest);
    }

    @GetMapping("/list/{bankReferences}")
    @ResponseBody
    @Override
    public List<Bank> getBanksByReferenceList(
            @PathVariable List<String> bankReferences,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetBanksByReferenceListCommandRequest getBanksByReferenceListCommandRequest = GetBanksByReferenceListCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .references(bankReferences)
                .build();
        return this.getBanksByReferenceListCommand.execute(getBanksByReferenceListCommandRequest);
    }


    @GetMapping("/list/country/{countryReference}")
    @ResponseBody
    @Override
    public List<Bank> getBanksByCountryReference(
            @PathVariable String countryReference,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        GetBanksByCountryReferenceCommandRequest getBanksByCountryReferenceCommandRequest = GetBanksByCountryReferenceCommandRequest.builder()
                .authenticatedUser(authenticatedUser)
                .countryReference(countryReference)
                .build();
        return this.getBanksByCountryReferenceCommand.execute(getBanksByCountryReferenceCommandRequest);
    }

}
