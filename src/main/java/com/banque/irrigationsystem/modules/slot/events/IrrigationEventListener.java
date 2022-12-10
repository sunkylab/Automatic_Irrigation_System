package com.banque.irrigationsystem.modules.slot.events;

import com.banque.irrigationsystem.modules.slot.service.IrrigationSlotService;
import com.banque.irrigationsystem.shared.messaging.IrrigationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IrrigationEventListener {

    private IrrigationSlotService irrigationSlotService;

    @Autowired
    public IrrigationEventListener(IrrigationSlotService irrigationSlotService) {
        this.irrigationSlotService = irrigationSlotService;
    }

    @EventListener
    void processEvent(IrrigationEvent event) {

        irrigationSlotService.createIrrigationSlot(event.getLandReference());

    }
}
