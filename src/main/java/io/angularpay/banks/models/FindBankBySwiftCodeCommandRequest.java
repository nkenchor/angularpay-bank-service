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
public class FindBankBySwiftCodeCommandRequest extends AccessControl {

    @NotEmpty
    private String swiftCode;

    FindBankBySwiftCodeCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
