package com.dotclimbing.dataanalysis.controller;


import com.dotclimbing.dataanalysis.dto.AccStatusResultDto;
import com.dotclimbing.dataanalysis.service.SparkJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spark")
class SparkJobController {

    private final SparkJobService sparkJobService;

    @GetMapping("/accounting")
    public ResponseEntity<Object> getAccountingReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today) {

        log.info("회계 집계 요청 - {} ~ {}", startDate, today);
        return ResponseEntity.ok(sparkJobService.runAccounting(startDate, today));
    }

    @GetMapping("/result/accounting")
    public ResponseEntity<?> getAccountingResult() {
        AccStatusResultDto result = sparkJobService.getAccountingResult();
        if (result == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attendance")
    public ResponseEntity<Object> getAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today) {

        log.info("출석 집계 요청 - {} ~ {}", startDate, today);
        return ResponseEntity.ok(sparkJobService.runAttendance(startDate, today));
    }
}
