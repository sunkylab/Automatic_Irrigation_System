/*
 * Copyright (c) 2022. Nomba Financial Services
 *
 * team: Merchant Acquiring
 * author: Timothy Owolabi
 * email: timothy.owolabi@nomba.com
 */
package com.banque.irrigationsystem.infrastructure.persistence.spring_jpa;

import com.banque.irrigationsystem.domain.model.land.Land;
import com.banque.irrigationsystem.domain.model.land.LandRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class LandRepositoryJPA implements JpaRepository<Land,Long>, LandRepository {

    @Override
    public Land find(Long landId) {
        return null;
    }

    @Override
    public Land save(Land land) {

        return null;
    }
}
