package com.example.services;

import com.example.DTOs.TravelLogDTO;
import com.example.common.ResponseData;
import com.example.entities.TravelLog;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TravelLogService {

    ResponseEntity<ResponseData<List<TravelLog>>> getAll();
    ResponseEntity<ResponseData<Boolean>> add(TravelLogDTO dto);
    ResponseEntity<ResponseData<TravelLog>> edit(Integer id, TravelLogDTO log);
    ResponseEntity<ResponseData<Boolean>> delete(Integer id);
    void generateExcelReport(HttpServletResponse response) throws IOException;
    void generateFilteredExcelReport(HttpServletResponse response, String dateRange,
                                     String regNumber, String ownerName) throws IOException;
}
