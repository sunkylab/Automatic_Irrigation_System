package com.banque.irrigationsystem.modules.slot.service.implementation;

import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.modules.slot.dto.IrrigationStatus;
import com.banque.irrigationsystem.modules.slot.dto.IrrigationType;
import com.banque.irrigationsystem.modules.slot.dto.SlotConfiguration;
import com.banque.irrigationsystem.modules.slot.entity.IrrigationSlot;
import com.banque.irrigationsystem.modules.slot.entity.dao.IrrigationSlotRepository;
import com.banque.irrigationsystem.modules.slot.exceptions.IrrigationSlotExceptions;
import com.banque.irrigationsystem.modules.slot.service.IrrigationSlotService;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.service.LandService;
import com.banque.irrigationsystem.shared.integrations.impl.SensorActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class IrrigationSlotServiceImpl implements IrrigationSlotService {

    private LandService landService;
    private Scheduler scheduler;

    private IrrigationSlotRepository slotRepository;

    @Autowired
    public IrrigationSlotServiceImpl(LandService landService, IrrigationSlotRepository slotRepository,Scheduler scheduler ) {
        this.landService = landService;
        this.slotRepository = slotRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void configureSlot(String landReference, SlotConfiguration slotConfiguration) {

        if(! landService.landExists(landReference)) {
            throw new IrrigationSlotExceptions("Land could not be found in irrigation channel");
        }

        IrrigationSlot slot = slotRepository.findByStatusAndLandReference(IrrigationStatus.PENDING,landReference)
                .orElseThrow(()-> new IrrigationSlotExceptions("No Pending Records"));

        slot.setTimeToLaunch(slotConfiguration.timeToLaunch());
        slot.setAmountOfWaterInGallons(slotConfiguration.amountOfWaterInGallons());
        slot.setIrrigationIntervalInMinutes(slotConfiguration.irrigationIntervalInMinutes());
        slotRepository.save(slot);
        log.info("updated slot for land {}",landReference);


        log.info("pushing updated slot to scheduler {}",slot.getJobId());


        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("job_ref",slot.getJobId());

        updateIrrigationSchedule(slot.getTimeToLaunch(),slot.getJobId(), dataMap);
    }

    @Override
    public void createIrrigationSlot(String landReference) {

        LandDetail detail = landService.fetchLandByReference(landReference);

        IrrigationType irrigationToUse = IrrigationType.predictIrrigationSlotData(detail.landType());

        IrrigationSlot irrigationSlot = IrrigationSlot.builder()
                .landReference(landReference)
                .irrigationType(irrigationToUse)
                .amountOfWaterInGallons(irrigationToUse.getAmountOfWater())
                .timeToLaunch(LocalDateTime.now().plusSeconds(irrigationToUse.getAfterTimeToTriggerInSeconds()))
                .status(IrrigationStatus.PENDING)
                .jobId(UUID.randomUUID().toString())
                .build();

        irrigationSlot = slotRepository.save(irrigationSlot);
        log.info("created slot for land {}",landReference);

        log.info("pushing slot to scheduler ID: {} to run at {}",irrigationSlot.getJobId(),irrigationSlot.getTimeToLaunch());
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("job_ref",irrigationSlot.getJobId());

        scheduleIrrigation(irrigationSlot.getTimeToLaunch(),irrigationSlot.getJobId(), dataMap,true);
    }

    @Override
    public Optional<IrrigationSlot> fetchSlotById(Long id) {
        return slotRepository.findById(id);
    }

    @Override
    public Optional<IrrigationSlot> fetchSlotByPendingLandRef(String landReference) {
        return slotRepository.findByStatusAndLandReference(IrrigationStatus.PENDING,landReference);
    }

    @Override
    public List<IrrigationSlot> fetchSlots(PaginationRequest paginationRequest) {

        Pageable pageable = Pageable
                .ofSize(paginationRequest.getPageSize())
                .withPage(paginationRequest.getPageNumber());

        return slotRepository.findAll(pageable)
                .getContent();
    }

    @Override
    public void markSlotAsUtilized(String jobId) {
        LocalDateTime currentDate = LocalDateTime.now();

        IrrigationSlot irrigationSlot = slotRepository.findByJobId(jobId);
        irrigationSlot.setStatus(IrrigationStatus.COMPLETED);
        irrigationSlot.setLastUpdatedOn(currentDate);
        irrigationSlot.setTimeEnded(currentDate);

        slotRepository.save(irrigationSlot);
        log.info("Slot status updated");
    }


    private String scheduleIrrigation(LocalDateTime triggerDateTime, String jobId, Map<String,String> jobData,boolean isNewJob) {

        if(!isNewJob){
            try {
                scheduler.deleteJob(JobKey.jobKey(jobId));
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        //Set Job ID
        JobBuilder builder = JobBuilder.newJob(SensorActivityImpl.class)
                .withIdentity(jobId,"slot_jobs");

        //Set Job Data
        jobData.entrySet().forEach(stringStringEntry -> {
            builder.usingJobData(stringStringEntry.getKey(),stringStringEntry.getValue());
        });

        //Set job trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(Date.from(triggerDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        JobDetail job = builder.build();

        //Schedule job
        Set<Trigger>  triggers = new HashSet<>();
        triggers.add(trigger);
        try {
            scheduler.scheduleJob(job, triggers, true);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        //Return job ID
        return job.getKey().getName();
    }

    private void updateIrrigationSchedule(LocalDateTime triggerDateTime, String jobId, Map<String,String> jobData){
        scheduleIrrigation(triggerDateTime,jobId,jobData,false);
    }
}
