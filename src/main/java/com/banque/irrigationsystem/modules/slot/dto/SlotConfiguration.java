package com.banque.irrigationsystem.modules.slot.dto;

import java.time.LocalDateTime;

public record SlotConfiguration (
        LocalDateTime timeToLaunch,
        Long amountOfWaterInGallons,
        Integer irrigationIntervalInMinutes
){

}
