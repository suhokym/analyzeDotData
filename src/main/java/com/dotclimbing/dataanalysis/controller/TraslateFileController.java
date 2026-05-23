package com.dotclimbing.dataanalysis.controller;

import com.dotclimbing.dataanalysis.dto.AccountingStatusDto;
import com.dotclimbing.dataanalysis.dto.DailyAttendanceDto;
import com.dotclimbing.dataanalysis.service.CsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/csv")
public class TraslateFileController {


        private final CsvService csvService;

        @PostMapping("/accounting")
        public ResponseEntity<?> uploadAccounting(
                @RequestParam("files") List<MultipartFile> files) throws IOException {


            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                log.info("회계현황 파싱 중 - 파일명: {}", file.getOriginalFilename());
               csvService.parseAccountingStatusCsv(file);
            }


            return ResponseEntity.ok("저장완료");
        }

        @PostMapping("/attendance")
        public ResponseEntity<?> uploadAttendance(
                @RequestParam("files") List<MultipartFile> files) throws IOException {


            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                log.info("출석현황 파싱 중 - 파일명: {}", file.getOriginalFilename());
                 csvService.parseDailyAttendanceCsv(file);

            }

            return ResponseEntity.ok("저장완료");
        }
    }

