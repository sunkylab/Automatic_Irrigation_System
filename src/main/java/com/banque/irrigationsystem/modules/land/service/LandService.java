package com.banque.irrigationsystem.modules.land.service;

import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.dto.UpdateLandRequest;

import java.util.List;

public interface LandService {
    LandDetail addLand(AddLandRequest addLandRequest);

    LandDetail updateLand(UpdateLandRequest updateLandRequest);

    List<LandDetail> fetchAllLands(PaginationRequest paginationRequest);

    LandDetail fetchLandById(Long id);
    LandDetail fetchLandByReference(String reference);

    boolean landExists(String reference);

}
