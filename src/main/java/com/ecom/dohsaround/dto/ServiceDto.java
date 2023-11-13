package com.ecom.dohsaround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    private Long id;
    private String serviceName;
    private String description;
    private int serviceFee;
    private boolean active;
}
