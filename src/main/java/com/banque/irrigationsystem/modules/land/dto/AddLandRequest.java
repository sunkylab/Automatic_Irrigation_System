package com.banque.irrigationsystem.modules.land.dto;

import com.banque.irrigationsystem.shared.validations.EnumNamePattern;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record AddLandRequest(
        @NotBlank(message = "landReference cannot be blank") String landReference,
        @Min( value=1, message= "numberOfPlots cannot be less than 1") Integer numberOfPlots,
        String purpose,
        @EnumNamePattern(regexp = "AGRICULTURAL|GARDEN|OTHERS") LandType landType
) {

    public AddLandRequest setLandReference(String landReference) {
        return new AddLandRequest(landReference,numberOfPlots,purpose,landType);
    }

    public AddLandRequest setNumberOfPlots(Integer numberOfPlots) {
        return new AddLandRequest(landReference,numberOfPlots,purpose,landType);
    }

    public AddLandRequest setLandType(LandType landType) {
        return new AddLandRequest(landReference,numberOfPlots,purpose,landType);
    }
}
