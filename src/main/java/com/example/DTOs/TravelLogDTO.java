package com.example.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelLogDTO {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String entryDate;
    private String vehicleRegNumber;
    private String vehicleOwnerName;
    private Integer odomBegin;
    private Integer odomEnd;
    private String route;
    private String journeyDescription;
}
