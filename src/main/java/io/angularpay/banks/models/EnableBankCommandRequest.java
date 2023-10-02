package io.angularpay.banks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EnableBankCommandRequest extends AccessControl {

    @NotEmpty
    private String reference;

    @NotNull
    private Boolean enabled;

    EnableBankCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
