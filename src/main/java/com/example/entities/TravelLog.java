package com.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelLog {

    private Integer id;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date entryDate;
    private String vehicleRegNumber;
    private String vehicleOwnerName;
    private Integer odomBegin;
    private Integer odomEnd;
    private String route;
    private String journeyDescription;
}
