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
public class GetBankListCommandRequest extends AccessControl {

    @NotNull
    @Valid
    private Paging paging;

    GetBankListCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
