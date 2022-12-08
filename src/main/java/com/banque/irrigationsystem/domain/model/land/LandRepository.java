/*
 * Copyright (c) 2022. Nomba Financial Services
 *
 * team: Merchant Acquiring
 * author: Timothy Owolabi
 * email: timothy.owolabi@nomba.com
 */
package com.banque.irrigationsystem.domain.model.land;

import java.util.List;

public interface LandRepository {

    /**
     * Finds a land using given id.
     *
     * @param landId Id
     * @return Land if found, else {@code null}
     */
    Land find(Long landId);

    /**
     * Finds all land.
     *
     * @return All cargo.
     */
    List<Land> findAll();

    /**
     * Saves given land.
     *
     * @param land land to save
     */
    Land save(Land land);

}
