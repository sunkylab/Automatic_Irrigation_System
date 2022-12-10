package com.banque.irrigationsystem.land;

import com.banque.irrigationsystem.core.exceptions.AppBaseException;
import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.modules.land.dto.LandType;
import com.banque.irrigationsystem.modules.land.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.dto.UpdateLandRequest;
import com.banque.irrigationsystem.modules.land.entity.dao.LandRepository;
import com.banque.irrigationsystem.modules.land.service.LandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;


@SpringBootTest
class IrrigationSystemLandTests {

	private static final String LAND_REFERENCE_1 = "REF-001";
	private static final String LAND_REFERENCE_2 = "REF-002";
	private static final AddLandRequest ADD_LAND_REQUEST = new AddLandRequest(LAND_REFERENCE_1,
			2,
			"Test purpose",
			LandType.AGRICULTURAL);

	private LandService landService;
	private LandRepository landRepository;

	@Autowired
	public IrrigationSystemLandTests(LandService landService, LandRepository landRepository) {
		this.landService = landService;
		this.landRepository = landRepository;
	}

	@BeforeEach
	public void resetData(){
		this.landRepository.deleteAll();
	}


	@Test
	public void whenLandIsCreatedSuccessfully(){

		//given
		AddLandRequest request = ADD_LAND_REQUEST;

		//when
		LandDetail detail = landService.addLand(request);
		boolean exists = landService.landExists(LAND_REFERENCE_1);

		//then
		AddLandRequest expected = new AddLandRequest(detail.landReference(),
				detail.numberOfPlots(),
				detail.purpose(),
				detail.landType());

		assertThat(exists).isTrue();
		assertThat(detail.id()).isNotNull();
		assertThat(request).isEqualTo(expected);
	}

	@Test
	public void whenLandCreationDetailIsInvalid(){

		//given
		AddLandRequest invalidNumOfPlotsRequest = ADD_LAND_REQUEST.setNumberOfPlots(0);
		AddLandRequest invalidLandReferenceRequest = ADD_LAND_REQUEST.setLandReference("");

		//when
		AppBaseException exception1 = catchThrowableOfType(() -> landService.addLand(invalidNumOfPlotsRequest),
				AppBaseException.class);
		AppBaseException exception2 = catchThrowableOfType(() -> landService.addLand(invalidLandReferenceRequest),
				AppBaseException.class);

		//then
		assertThat(exception1.getMessage()).isEqualTo("Invalid number of plots");
		assertThat(exception2.getMessage()).isEqualTo("Land reference cannot be empty");

	}

	@Test
	public void whenLandAlreadyExists(){
		//given
		AddLandRequest duplicateLandRequest = ADD_LAND_REQUEST.setNumberOfPlots(20);

		//when
		landService.addLand(ADD_LAND_REQUEST);
		AppBaseException exception = catchThrowableOfType(() -> landService.addLand(duplicateLandRequest),
				AppBaseException.class);

		//then
		assertThat(exception.getMessage()).isEqualTo("Land already exists");
	}

	@Test
	public void whenLandIsUpdatedSuccessfully(){
		//given
		UpdateLandRequest updatedLandRequest = new UpdateLandRequest(
				ADD_LAND_REQUEST.landReference(),
				30,
				ADD_LAND_REQUEST.purpose(),
				LandType.GARDEN,
				0l
				);

		//when
		LandDetail createdLand = landService.addLand(ADD_LAND_REQUEST);
		LandDetail updatedLand = landService.updateLand(updatedLandRequest.setId(createdLand.id()));

		//then
		assertThat(createdLand.id()).isEqualTo(updatedLand.id());
		assertThat(updatedLand.landType()).isEqualTo(LandType.GARDEN);
		assertThat(updatedLand.numberOfPlots()).isEqualTo(30);
	}

	@Test
	public void whenLandDoesNotExistForUpdate(){
		//given
		UpdateLandRequest updatedLandRequest = new UpdateLandRequest(
				ADD_LAND_REQUEST.landReference(),
				ADD_LAND_REQUEST.numberOfPlots(),
				ADD_LAND_REQUEST.purpose(),
				ADD_LAND_REQUEST.landType(),
				1l
		);

		//when
		AppBaseException exception = catchThrowableOfType(() -> landService.updateLand(updatedLandRequest),
				AppBaseException.class);

		//then
		assertThat(exception.getMessage()).isEqualTo("Record not Found");
	}

	@Test
	public void whenLandRecordsAreFetched(){

		//given
		AddLandRequest secondRequest = ADD_LAND_REQUEST
				.setLandReference(LAND_REFERENCE_2)
				.setNumberOfPlots(20);

		//when
		LandDetail landDetail = landService.addLand(ADD_LAND_REQUEST);
		landService.addLand(secondRequest);

		LandDetail fetchedRecord = landService.fetchLandById(landDetail.id());
		List<LandDetail> allLands = landService.fetchAllLands(PaginationRequest.builder()
				.pageSize(3)
				.build());

		//then
		assertThat(landDetail).isEqualTo(fetchedRecord);
		assertThat(allLands.size()).isEqualTo(2);
	}
}
