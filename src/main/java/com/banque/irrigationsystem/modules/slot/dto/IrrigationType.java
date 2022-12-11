package com.banque.irrigationsystem.modules.slot.dto;

import com.banque.irrigationsystem.modules.land.dto.LandType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public enum IrrigationType {

    SURFACE_IRRIGATION("Surface Irrigation",50l,120l),
    SPRINKLER_IRRIGATION("Sprinkler Irrigation",20l,60l),
    DRIP_IRRIGATION("Drip Irrigation",10l,60l);


    public static IrrigationType predictIrrigationSlotData(LandType landType){

        if(landType == landType.AGRICULTURAL){
            return IrrigationType.SURFACE_IRRIGATION;

        }else if(landType == landType.GARDEN){
            return IrrigationType.SPRINKLER_IRRIGATION;

        }else {
            return IrrigationType.DRIP_IRRIGATION;

        }
    }

    IrrigationType(String description, Long amountOfWater, Long afterTimeToTriggerInSeconds) {
        this.description = description;
        this.amountOfWater = amountOfWater;
        this.afterTimeToTriggerInSeconds = afterTimeToTriggerInSeconds;
    }

    private String description;
    private Long amountOfWater;
    private Long afterTimeToTriggerInSeconds;
}
