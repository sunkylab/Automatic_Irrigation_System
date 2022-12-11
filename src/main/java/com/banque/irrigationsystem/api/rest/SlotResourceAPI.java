package com.banque.irrigationsystem.api.rest;

import com.banque.irrigationsystem.api.shared.ApiResponse;
import com.banque.irrigationsystem.modules.slot.dto.SlotConfiguration;
import com.banque.irrigationsystem.modules.slot.service.IrrigationSlotService;
import com.banque.irrigationsystem.shared.dto.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/irrigation/v1/slots")
public class SlotResourceAPI {

    private IrrigationSlotService irrigationSlotService;

    @Autowired
    public SlotResourceAPI(IrrigationSlotService irrigationSlotService) {
        this.irrigationSlotService = irrigationSlotService;
    }

    @PostMapping("/{landRef}/configure")
    public ResponseEntity<ApiResponse> configureLand(@PathVariable("landRef") String landReference,
                                                     @RequestBody SlotConfiguration configuration){

        irrigationSlotService.configureSlot(landReference,configuration);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .build());
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<ApiResponse> fetchSlot(@PathVariable("slotId") Long slotId){

        var record = irrigationSlotService.fetchSlotById(slotId);

        return ResponseEntity.ok(ApiResponse.builder()
                .code(0)
                .message("success")
                .data(record)
                .build());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> fetchSlots(@RequestParam("page_size") int pageSize,
                                                  @RequestParam(name="page_number") int pageNumber){

        var records = irrigationSlotService.fetchSlots(PaginationRequest.builder()
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
