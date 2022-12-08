/*
 * Copyright (c) 2022. Nomba Financial Services
 *
 * team: Merchant Acquiring
 * author: Timothy Owolabi
 * email: timothy.owolabi@nomba.com
 */
package com.banque.irrigationsystem.domain.model.irrigator;

import java.time.LocalDateTime;

public class IrrigationSlot {

    private String slotId;
    private String sprinklerType;
    private String sprinklerStatus;
    private LocalDateTime timeToLaunch;
    private LocalDateTime timeEnded;



}
