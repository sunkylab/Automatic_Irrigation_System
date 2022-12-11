package com.banque.irrigationsystem.modules.land.dto;


import com.banque.irrigationsystem.shared.validations.EnumNamePattern;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record UpdateLandRequest(
        String landReference,
        @Min( value=1, message= "numberOfPlots cannot be less than 1") Integer numberOfPlots,
        String purpose,
        @EnumNamePattern(regexp = "AGRICULTURAL|GARDEN|OTHERS") LandType landType,
        @NotNull(message = "Id cannot be null") @Min( value=1, message= "Id cannot be less than 1") Long id
){

    public UpdateLandRequest setId(Long id){
        return new UpdateLandRequest(landReference,numberOfPlots,purpose,landType,id);
    }
}
