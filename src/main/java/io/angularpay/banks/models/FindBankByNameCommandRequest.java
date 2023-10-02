package io.angularpay.banks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FindBankByNameCommandRequest extends AccessControl {

    @NotEmpty
    private String name;

    FindBankByNameCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
