package com.example.controllers;

import com.example.DTOs.TravelLogDTO;
import com.example.common.ResponseData;
import com.example.entities.TravelLog;
import com.example.services.TravelLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel/log")
public class TravelLogController {

    private final TravelLogService service;

    @GetMapping("/get/all")
    public ResponseEntity<ResponseData<List<TravelLog>>> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseData<Boolean>> add(@RequestBody TravelLogDTO dto) {
        return service.add(dto);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseData<TravelLog>> edit(@PathVariable(value = "id") Integer id,
                                                             @RequestBody TravelLogDTO dto) {
        return service.edit(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Boolean>> delete(@PathVariable(value = "id") Integer id) {
        return service.delete(id);
    }

    @GetMapping("/get/excel")
    public void generateExcelReport(HttpServletResponse response)
            throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=travelLog.xls";

        response.setHeader(headerKey, headerValue);

        service.generateExcelReport(response);
    }

    @GetMapping("/get/excel/{dateRange}/{regNumber}/{ownerName}")
    public void generateFilteredExcelReport(HttpServletResponse response,
                                            @PathVariable(value = "dateRange") String dateRange,
                                            @PathVariable(value = "regNumber") String regNumber,
                                            @PathVariable(value = "ownerName") String ownerName)
            throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=travelLog.xls";

        response.setHeader(headerKey, headerValue);

        service.generateFilteredExcelReport(response, dateRange, regNumber, ownerName);
    }
}
