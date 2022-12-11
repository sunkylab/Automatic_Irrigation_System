package com.banque.irrigationsystem.api.rest;

import com.banque.irrigationsystem.api.shared.ApiResponse;
import com.banque.irrigationsystem.modules.land.dto.AddLandRequest;
import com.banque.irrigationsystem.modules.land.dto.LandDetail;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import com.banque.irrigationsystem.modules.land.dto.UpdateLandRequest;
import com.banque.irrigationsystem.modules.land.service.LandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/irrigation/v1/lands")
public class LandResourceAPI {

    private LandService landService;

    @Autowired
    public LandResourceAPI(LandService landService) {
        this.landService = landService;
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> addLand(@RequestBody @Valid AddLandRequest addLandRequest){

        LandDetail landDetail = landService.addLand(addLandRequest);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .data(landDetail)
                .build());
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> updateLand(@RequestBody @Valid UpdateLandRequest updateLandRequest){

        var updatedData = landService.updateLand(updateLandRequest);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .data(updatedData)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> fetchLand(@PathVariable( "id") Long id){

        var fetchedData = landService.fetchLandById(id);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .data(fetchedData)
                .build());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> fetchLands(@RequestParam("page_size") int pageSize,
                                                  @RequestParam(name="page_number",required = false) int pageNumber){

        var records = landService.fetchAllLands(PaginationRequest.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .build());

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .data(records)
                .build());
    }

}
