package com.banque.irrigationsystem.modules.land.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Builder
@Getter
@Setter
public class PaginationRequest {

    @Min(value = 1, message = "pageSize cannot be less than 1")
    private int pageSize;
    private String pageToken;
}
