package com.banque.irrigationsystem.modules.land.dto;

import java.time.LocalDateTime;


public record LandDetail(
        String landReference,
        Integer numberOfPlots,
        String purpose,
        LandType landType,
        long id,
        LocalDateTime timeCreated
){

}
