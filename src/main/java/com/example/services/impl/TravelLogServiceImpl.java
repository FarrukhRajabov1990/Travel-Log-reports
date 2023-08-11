package com.example.services.impl;

import com.example.DTOs.TravelLogDTO;
import com.example.common.ResponseData;
import com.example.entities.TravelLog;
import com.example.services.TravelLogService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TravelLogServiceImpl implements TravelLogService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TravelLogServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<ResponseData<List<TravelLog>>> getAll() {
        List<TravelLog> logs = jdbcTemplate.query("SELECT * FROM travel_logs",
                new BeanPropertyRowMapper<>(TravelLog.class));
        return ResponseData.success200(logs);
    }

    @Override
    public ResponseEntity<ResponseData<Boolean>> add(TravelLogDTO dto) {
        LocalDate date = toLocalDate(dto.getEntryDate());
        jdbcTemplate.update("INSERT INTO travel_logs(entry_date, vehicle_reg_number, vehicle_owner_name, odom_begin," +
                        "odom_end, route, journey_description) VALUES (?, ?, ?, ?, ?, ?, ?)", date, dto.getVehicleRegNumber(),
                dto.getVehicleOwnerName(), dto.getOdomBegin(), dto.getOdomEnd(), dto.getRoute(), dto.getJourneyDescription());
        return ResponseData.success200(true);
    }

    @Override
    public ResponseEntity<ResponseData<TravelLog>> edit(Integer id, TravelLogDTO log) {
        LocalDate date = toLocalDate(log.getEntryDate());
        jdbcTemplate.update("UPDATE travel_logs SET entry_date=?, vehicle_reg_number=?, vehicle_owner_name=?," +
                "odom_begin=?, odom_end=?, route=?, journey_description=? WHERE id=?", date, log.getVehicleRegNumber(),
                log.getVehicleOwnerName(), log.getOdomBegin(), log.getOdomEnd(), log.getRoute(), log.getJourneyDescription(), id);

        TravelLog updatedLog = jdbcTemplate.query("SELECT * FROM travel_logs WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(TravelLog.class)).stream().findAny().orElse(null);
        return ResponseData.success200(updatedLog);
    }

    @Override
    public ResponseEntity<ResponseData<Boolean>> delete(Integer id) {
        jdbcTemplate.update("DELETE FROM travel_logs WHERE id=?", id);
        return ResponseData.success200(true);
    }

    @Override
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        List<TravelLog> logs = jdbcTemplate.query("SELECT * FROM travel_logs ORDER BY odom_begin",
                new BeanPropertyRowMapper<>(TravelLog.class));

        creatingStream(response, logs);
    }

    @Override
    public void generateFilteredExcelReport(HttpServletResponse response, String dateRange,
                                            String regNumber, String ownerName) throws IOException {
        String[] dates = dateRange.split("-");
        LocalDate date1 = toLocalDate(dates[0]);
        LocalDate date2 = toLocalDate(dates[1]);
        List<TravelLog> logs = jdbcTemplate.query("SELECT * FROM travel_logs WHERE entry_date>=? AND entry_date<=? " +
                        "AND vehicle_reg_number=? AND vehicle_owner_name=? ORDER BY odom_begin",
                new Object[]{date1, date2, regNumber, ownerName}, new BeanPropertyRowMapper<>(TravelLog.class));

        creatingStream(response, logs);
    }

    private void creatingStream(HttpServletResponse response, List<TravelLog> logs) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Travel logs");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Entry date");
        row.createCell(2).setCellValue("Vehicle registration number");
        row.createCell(3).setCellValue("Vehicle owner name");
        row.createCell(4).setCellValue("Odometer at the beginning");
        row.createCell(5).setCellValue("Odometer at the end");
        row.createCell(6).setCellValue("Route");
        row.createCell(7).setCellValue("Journey description");

        int rowIndex = 1;
        int sum = 0;

        for (TravelLog log: logs) {
            HSSFRow dataRow = sheet.createRow(rowIndex);
            String date = new SimpleDateFormat("yyyy.MM.dd").format(log.getEntryDate());
            dataRow.createCell(0).setCellValue(log.getId());
            dataRow.createCell(1).setCellValue(date);
            dataRow.createCell(2).setCellValue(log.getVehicleRegNumber());
            dataRow.createCell(3).setCellValue(log.getVehicleOwnerName());
            dataRow.createCell(4).setCellValue(log.getOdomBegin());
            dataRow.createCell(5).setCellValue(log.getOdomEnd());
            dataRow.createCell(6).setCellValue(log.getRoute());
            dataRow.createCell(7).setCellValue(log.getJourneyDescription());
            rowIndex++;
            sum+= log.getOdomEnd() - log.getOdomBegin();
        }
        HSSFRow dataRow = sheet.createRow(rowIndex);
        dataRow.createCell(0).setCellValue("Total traveled distance: ");
        dataRow.createCell(2).setCellValue(sum);


        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }


    private LocalDate toLocalDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return LocalDate.parse(stringDate, formatter);
    }
}
