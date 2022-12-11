package com.banque.irrigationsystem.shared.integrations.impl;

import com.banque.irrigationsystem.modules.slot.service.IrrigationSlotService;
import com.banque.irrigationsystem.shared.integrations.SensorActivity;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SensorActivityImpl implements Job, SensorActivity {

    private IrrigationSlotService slotService;

    @Autowired
    public SensorActivityImpl(IrrigationSlotService slotService) {
        this.slotService = slotService;
    }

    /**
     * This method represents the sensor activity
     * which is executed by the job at the scheduled time
     * **/
    @Override
    public void execute(JobExecutionContext context) {

        String jobRef = context.getJobDetail().getJobDataMap().get("job_ref").toString();
        /* Thread sleep to simulate work done by the sensor */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Irrigation completed");

        slotService.markSlotAsUtilized(jobRef);
    }


    @Override
    public boolean isSensorActive() {
        return true;
    }
}
