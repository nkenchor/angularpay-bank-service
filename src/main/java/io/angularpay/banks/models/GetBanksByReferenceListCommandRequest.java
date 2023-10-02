package io.angularpay.banks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GetBanksByReferenceListCommandRequest extends AccessControl {

    @NotEmpty
    private List<String> references;

    GetBanksByReferenceListCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
