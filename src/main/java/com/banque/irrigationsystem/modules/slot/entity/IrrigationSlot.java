package com.banque.irrigationsystem.modules.slot.entity;

import com.banque.irrigationsystem.core.entity.BaseEntity;
import com.banque.irrigationsystem.modules.slot.dto.IrrigationStatus;
import com.banque.irrigationsystem.modules.slot.dto.IrrigationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Where(clause = "del_flag='N'")
public class IrrigationSlot extends BaseEntity {

    private LocalDateTime timeToLaunch;
    private LocalDateTime timeEnded;
    private Long amountOfWaterInGallons;
    private Integer irrigationIntervalInMinutes;
    @Enumerated(EnumType.STRING)
    private IrrigationStatus status;
    private IrrigationType irrigationType;

    private String jobId;
    private String landReference;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IrrigationSlot that)) return false;
        if (!super.equals(o)) return false;

        if (getTimeToLaunch() != null ? !getTimeToLaunch().equals(that.getTimeToLaunch()) : that.getTimeToLaunch() != null)
            return false;
        if (getTimeEnded() != null ? !getTimeEnded().equals(that.getTimeEnded()) : that.getTimeEnded() != null)
            return false;
        if (getAmountOfWaterInGallons() != null ? !getAmountOfWaterInGallons().equals(that.getAmountOfWaterInGallons()) : that.getAmountOfWaterInGallons() != null)
            return false;
        if (getIrrigationIntervalInMinutes() != null ? !getIrrigationIntervalInMinutes().equals(that.getIrrigationIntervalInMinutes()) : that.getIrrigationIntervalInMinutes() != null)
            return false;
        if (getStatus() != that.getStatus()) return false;
        if (getIrrigationType() != that.getIrrigationType()) return false;
        return getLandReference() != null ? getLandReference().equals(that.getLandReference()) : that.getLandReference() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getTimeToLaunch() != null ? getTimeToLaunch().hashCode() : 0);
        result = 31 * result + (getTimeEnded() != null ? getTimeEnded().hashCode() : 0);
        result = 31 * result + (getAmountOfWaterInGallons() != null ? getAmountOfWaterInGallons().hashCode() : 0);
        result = 31 * result + (getIrrigationIntervalInMinutes() != null ? getIrrigationIntervalInMinutes().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getIrrigationType() != null ? getIrrigationType().hashCode() : 0);
        result = 31 * result + (getLandReference() != null ? getLandReference().hashCode() : 0);
        return result;
    }
}
