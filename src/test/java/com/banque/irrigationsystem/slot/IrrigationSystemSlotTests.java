package com.banque.irrigationsystem.slot;


import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.modules.land.dto.LandType;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.entity.dao.LandRepository;
import com.banque.irrigationsystem.modules.land.service.LandService;
import com.banque.irrigationsystem.modules.slot.dto.SlotConfiguration;
import com.banque.irrigationsystem.modules.slot.entity.IrrigationSlot;
import com.banque.irrigationsystem.modules.slot.entity.dao.IrrigationSlotRepository;
import com.banque.irrigationsystem.modules.slot.service.IrrigationSlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.banque.irrigationsystem.modules.slot.dto.IrrigationType.SURFACE_IRRIGATION;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class IrrigationSystemSlotTests {

	private static final String LAND_REFERENCE_2 = "REF-002";
	private static final AddLandRequest ADD_LAND_REQUEST = new AddLandRequest("REF-001",
			2,
			"Test purpose",
			LandType.AGRICULTURAL);

	private LandService landService;
	private IrrigationSlotService slotService;
	private LandRepository landRepository;
	private IrrigationSlotRepository slotRepository;

	@Autowired
	public IrrigationSystemSlotTests(LandService landService, IrrigationSlotService slotService, LandRepository landRepository, IrrigationSlotRepository slotRepository) {
		this.landService = landService;
		this.landRepository = landRepository;
		this.slotService = slotService;
		this.slotRepository = slotRepository;
	}

	@BeforeEach
	public void reset(){
		landRepository.deleteAll();
		slotRepository.deleteAll();
	}


	@Test
	public void whenIrrigationSlotsAreFetched(){

		//given
		AddLandRequest request = ADD_LAND_REQUEST;

		//when
		LandDetail detail = landService.addLand(request);
		List<IrrigationSlot> slots = slotService.fetchSlots(PaginationRequest.builder()
				.pageSize(10)
				.pageNumber(10)
				.build());

		//then
		LocalDateTime expectedTriggerTime = LocalDateTime.now().plusSeconds(SURFACE_IRRIGATION.getAfterTimeToTriggerInSeconds());//irrigation type is selected based on lad type

		//only one record should be returned
		assertThat(slots.size()).isEqualTo(1);
		IrrigationSlot slot = slots.get(0);
		//land reference should be safe as value passed for request
		assertThat(slot.getLandReference()).isEqualTo(ADD_LAND_REQUEST.landReference());
		//amount of water predicted should be same as value stored in slot
		assertThat(slot.getAmountOfWaterInGallons()).isEqualTo(SURFACE_IRRIGATION.getAmountOfWater());
		//start time predicted should be same as value stored in slot
		assertThat(slot.getTimeToLaunch().toString().substring(0,18))
				.isEqualTo(expectedTriggerTime.toString().substring(0,18));
	}

	@Test
	public void whenSlotIsReConfigured(){

		AddLandRequest request = ADD_LAND_REQUEST.setLandReference(LAND_REFERENCE_2);

		//given
		landService.addLand(request);
		LocalDateTime newTriggerTime = LocalDateTime.now().plusMinutes(150000l);
		SlotConfiguration slotConfiguration = new SlotConfiguration(newTriggerTime,35l,2);

		//when
		Optional<IrrigationSlot> previousSlot = slotService.fetchSlotByPendingLandRef(LAND_REFERENCE_2);

		slotService.configureSlot(LAND_REFERENCE_2,slotConfiguration);
		Optional<IrrigationSlot> updatedSlot = slotService.fetchSlotByPendingLandRef(LAND_REFERENCE_2);

		//then
		assertThat(updatedSlot.isPresent()).isEqualTo(true);

		//TimeToLaunch should have changed to after date
		assertThat(updatedSlot.get().getTimeToLaunch()).isAfter(previousSlot.get().getTimeToLaunch());
		//amountOfWaterInGallons should be same with latest config request
		assertThat(updatedSlot.get().getAmountOfWaterInGallons()).isEqualTo(slotConfiguration.amountOfWaterInGallons());
	}

}
