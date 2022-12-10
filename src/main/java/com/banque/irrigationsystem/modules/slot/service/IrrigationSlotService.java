package com.banque.irrigationsystem.modules.slot.service;

import com.banque.irrigationsystem.modules.slot.dto.SlotConfiguration;
import com.banque.irrigationsystem.modules.slot.entity.IrrigationSlot;
import com.banque.irrigationsystem.modules.land.dto.PaginationRequest;

import java.util.List;
import java.util.Optional;

public interface IrrigationSlotService {

    void configureSlot(String landReference, SlotConfiguration slotConfiguration);

    void createIrrigationSlot(String landReference);

    Optional<IrrigationSlot> fetchSlotById(Long id);

    Optional<IrrigationSlot> fetchSlotByPendingLandRef(String landReference);

    List<IrrigationSlot> fetchSlots(PaginationRequest paginationRequest);

    void markSlotAsUtilized(String jobId);

}
