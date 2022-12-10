package com.banque.irrigationsystem.modules.land.entity;

import com.banque.irrigationsystem.core.entity.BaseEntity;
import com.banque.irrigationsystem.modules.land.dto.LandType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Where(clause = "del_flag='N'")
public class LandEntity extends BaseEntity {

    private String landReference;
    private int numberOfPlots;
    private String description;
    private LandType landType;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LandEntity that)) return false;
        if (!super.equals(o)) return false;

        if (getNumberOfPlots() != that.getNumberOfPlots()) return false;
        if (getLandReference() != null ? !getLandReference().equals(that.getLandReference()) : that.getLandReference() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        return getLandType() == that.getLandType();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getLandReference() != null ? getLandReference().hashCode() : 0);
        result = 31 * result + getNumberOfPlots();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getLandType() != null ? getLandType().hashCode() : 0);
        return result;
    }
}
