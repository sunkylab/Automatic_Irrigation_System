package com.banque.irrigationsystem.modules.slot.entity.dao;

import com.banque.irrigationsystem.modules.slot.dto.IrrigationStatus;
import com.banque.irrigationsystem.modules.slot.entity.IrrigationSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IrrigationSlotRepository extends JpaRepository<IrrigationSlot,Long> {

    IrrigationSlot findByJobId (String jobId);
    Optional<IrrigationSlot> findByStatusAndLandReference (IrrigationStatus status, String reference);

}
