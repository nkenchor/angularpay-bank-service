package io.angularpay.banks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateBankCommandRequest extends AccessControl {

    @NotNull
    @Valid
    private GenericBankApiModel genericBankApiModel;

    CreateBankCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
