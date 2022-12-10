package com.banque.irrigationsystem.core.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JsonIgnore
    @Version
    private int version;
    @JsonIgnore
    private String delFlag = "N";
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime lastUpdatedOn;
    @JsonIgnore
    private LocalDateTime deletedOn;

    public BaseEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return version == that.version && id.equals(that.id) && delFlag.equals(that.delFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, delFlag);
    }
}
