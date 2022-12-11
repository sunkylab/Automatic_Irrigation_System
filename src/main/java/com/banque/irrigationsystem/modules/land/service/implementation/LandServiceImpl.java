package com.banque.irrigationsystem.modules.land.service.implementation;

import com.banque.irrigationsystem.core.exceptions.AppBaseException;
import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.dto.UpdateLandRequest;
import com.banque.irrigationsystem.modules.land.entity.LandEntity;
import com.banque.irrigationsystem.modules.land.entity.dao.LandRepository;
import com.banque.irrigationsystem.modules.land.service.LandService;
import com.banque.irrigationsystem.shared.messaging.IrrigationEvent;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LandServiceImpl implements LandService {

    private LandRepository landRepository;

    private ApplicationEventPublisher publisher;

    @Autowired
    public LandServiceImpl(LandRepository landRepository, ApplicationEventPublisher publisher) {
        this.landRepository = landRepository;
        this.publisher = publisher;
    }

    @Override
    public LandDetail addLand(AddLandRequest addLandRequest) {

        validateRequest(addLandRequest);

        LandEntity landEntity = LandEntity.builder()
                .landReference(addLandRequest.landReference())
                .numberOfPlots(addLandRequest.numberOfPlots())
                .description(addLandRequest.purpose())
                .landType(addLandRequest.landType())
                .build();

        var recordId  = landRepository.save(landEntity).getId();

        // push event to irrigation service to automatically create slot for land
        publisher.publishEvent(new IrrigationEvent(landEntity.getLandType().name(),landEntity.getLandReference()));

        return this.fetchLandById(recordId);
    }

    @Override
    public LandDetail updateLand(UpdateLandRequest updateLandRequest) {

        LandEntity landEntity = landRepository.findById(updateLandRequest.id())
                .orElseThrow(()->new AppBaseException("Record not Found"));

        Optional.ofNullable(updateLandRequest.numberOfPlots()).ifPresent(landEntity :: setNumberOfPlots);
        Optional.ofNullable(updateLandRequest.landType()).ifPresent(landEntity :: setLandType);
        Optional.ofNullable(updateLandRequest.purpose()).ifPresent(landEntity :: setDescription);

        landRepository.save(landEntity);

        return this.fetchLandById(landEntity.getId());

    }

    @Override
    public List<LandDetail> fetchAllLands(PaginationRequest paginationRequest) {

        Pageable pageable = Pageable.
                ofSize(paginationRequest.getPageSize())
                .withPage(paginationRequest.getPageNumber());

        return landRepository.findAll(pageable).stream()
                .map(landEntity -> convertToLandDTO(landEntity))
                .collect(Collectors.toList());
    }

    @Override
    public LandDetail fetchLandById(Long id) {
        LandEntity landEntity = landRepository.findById(id).orElseThrow(()->new AppBaseException("Record not Found"));

        return convertToLandDTO(landEntity);
    }

    @Override
    public LandDetail fetchLandByReference(String reference) {
        LandEntity landEntity =  landRepository.findByLandReference(reference).orElse(null);

        return convertToLandDTO(landEntity);
    }

    @Override
    public boolean landExists(String reference) {

        LandEntity landEntity = landRepository.findByLandReference(reference).orElse(null);

        if(Objects.isNull(landEntity)){
            return false;
        }

        return true;
    }


    private LandDetail convertToLandDTO(LandEntity landEntity){

        return new LandDetail(landEntity.getLandReference(),
                landEntity.getNumberOfPlots(),
                landEntity.getDescription(),
                landEntity.getLandType(),
                landEntity.getId(),
                landEntity.getCreatedDate()
                );
    }


    private void validateRequest(AddLandRequest addLandRequest){

        if(addLandRequest.numberOfPlots() <= 0 ){
            throw new AppBaseException("Invalid number of plots");
        }

        if(StringUtils.isBlank(addLandRequest.landReference())){
            throw new AppBaseException("Land reference cannot be empty");
        }

        if(landExists(addLandRequest.landReference())){
            throw new AppBaseException("Land already exists");
        }

    }
}
