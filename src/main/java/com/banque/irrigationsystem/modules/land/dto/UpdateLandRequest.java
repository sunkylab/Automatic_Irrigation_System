package com.banque.irrigationsystem.modules.land.dto;


import com.banque.irrigationsystem.shared.validations.EnumNamePattern;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record UpdateLandRequest(
        @NotBlank(message = "landReference cannot be blank") String landReference,
        @Min( value=1, message= "numberOfPlots cannot be less than 1") Integer numberOfPlots,
        String purpose,
        @EnumNamePattern(regexp = "AGRICULTURAL|GARDEN|OTHERS") LandType landType,
        @Min( value=1, message= "Id cannot be less than 1") Long id
){

    public UpdateLandRequest setId(Long id){
        return new UpdateLandRequest(landReference,numberOfPlots,purpose,landType,id);
    }
}
