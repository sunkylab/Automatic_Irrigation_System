package com.banque.irrigationsystem.modules.land.entity.dao;

import com.banque.irrigationsystem.modules.land.entity.LandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandRepository extends JpaRepository<LandEntity,Long> {
    Optional<LandEntity> findById(Long id);

    Optional<LandEntity> findByLandReference(String reference);

}
