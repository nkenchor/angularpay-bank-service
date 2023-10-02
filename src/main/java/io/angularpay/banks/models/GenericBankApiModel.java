package io.angularpay.banks.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GenericBankApiModel {

    @NotEmpty
    private String name;
    @NotEmpty
    private String city;

    private String branch;

    private String address;

    @NotEmpty
    @JsonProperty("country_reference")
    private String countryReference;

    @NotEmpty
    @JsonProperty("swift_code")
    private String swiftCode;

    private boolean enabled;
}
