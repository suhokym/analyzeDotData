package com.dotclimbing.dataanalysis.service;

import com.dotclimbing.dataanalysis.dto.AccStatusResultDto;
import com.dotclimbing.dataanalysis.entity.AccAnalysisResult;
import com.dotclimbing.dataanalysis.entity.AccDailySalesResult;
import com.dotclimbing.dataanalysis.entity.AccMonthlySalesResult;
import com.dotclimbing.dataanalysis.entity.AccWeeklySalesResult;
import com.dotclimbing.dataanalysis.job.SparkJob;
import com.dotclimbing.dataanalysis.repository.AccAnalysisResultRepository;
import com.dotclimbing.dataanalysis.repository.AccDailySalesResultRepository;
import com.dotclimbing.dataanalysis.repository.AccMonthlySalesResultRepository;
import com.dotclimbing.dataanalysis.repository.AccWeeklySalesResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SparkJobService {

    private final SparkJob sparkJob;
    private final AccAnalysisResultRepository accAnalysisResultRepository;
    private final AccMonthlySalesResultRepository accMonthlySalesResultRepository;
    private final AccDailySalesResultRepository accDailySalesResultRepository;
    private final AccWeeklySalesResultRepository accWeeklySalesResultRepository;

    @Transactional
    public AccStatusResultDto runAccounting(LocalDate startDate, LocalDate today) {
        log.info("회계 집계 시작 - {} ~ {}", startDate, today);
        AccStatusResultDto result = (AccStatusResultDto) sparkJob.run(startDate, today, "accounting_status");
        saveAccResult(result);
        return result;
    }

    private void saveAccResult(AccStatusResultDto dto) {
        accAnalysisResultRepository.deleteAll();
        accMonthlySalesResultRepository.deleteAll();
        accDailySalesResultRepository.deleteAll();
        accWeeklySalesResultRepository.deleteAll();

        accAnalysisResultRepository.save(
                new AccAnalysisResult(null, dto.getTotalSales(), LocalDateTime.now())
        );

        List<AccMonthlySalesResult> monthly = dto.getMonthlySales().stream()
                .map(m -> new AccMonthlySalesResult(null, m.getMonth(), m.getTotalAmount(), m.getCount()))
                .collect(Collectors.toList());
        accMonthlySalesResultRepository.saveAll(monthly);

        List<AccDailySalesResult> daily = dto.getDailySales().stream()
                .map(d -> new AccDailySalesResult(null, d.getDate(), d.getTotalAmount(), d.getCount()))
                .collect(Collectors.toList());
        accDailySalesResultRepository.saveAll(daily);

        List<AccWeeklySalesResult> weekly = dto.getWeeklySales().stream()
                .map(w -> new AccWeeklySalesResult(null, w.getDayOfWeek(), w.getTotalAmount(), w.getCount()))
                .collect(Collectors.toList());
        accWeeklySalesResultRepository.saveAll(weekly);

        log.info("회계 집계 결과 저장 완료");
    }

    @Transactional(readOnly = true)
    public AccStatusResultDto getAccountingResult() {
        List<AccAnalysisResult> summaries = accAnalysisResultRepository.findAll();
        if (summaries.isEmpty()) return null;

        AccAnalysisResult summary = summaries.get(0);

        List<AccStatusResultDto.MonthlySales> monthly = accMonthlySalesResultRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AccMonthlySalesResult::getMonth))
                .map(m -> AccStatusResultDto.MonthlySales.builder()
                        .month(m.getMonth())
                        .totalAmount(m.getTotalAmount())
                        .count(m.getCount())
                        .build())
                .collect(Collectors.toList());

        List<AccStatusResultDto.DailySales> daily = accDailySalesResultRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AccDailySalesResult::getDate))
                .map(d -> AccStatusResultDto.DailySales.builder()
                        .date(d.getDate())
                        .totalAmount(d.getTotalAmount())
                        .count(d.getCount())
                        .build())
                .collect(Collectors.toList());

        Map<String, Integer> dayOrder = Map.of(
                "Sun", 1, "Mon", 2, "Tue", 3, "Wed", 4, "Thu", 5, "Fri", 6, "Sat", 7
        );
        List<AccStatusResultDto.WeeklySales> weekly = accWeeklySalesResultRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(w -> dayOrder.getOrDefault(w.getDayOfWeek(), 8)))
                .map(w -> AccStatusResultDto.WeeklySales.builder()
                        .dayOfWeek(w.getDayOfWeek())
                        .totalAmount(w.getTotalAmount())
                        .count(w.getCount())
                        .build())
                .collect(Collectors.toList());

        return AccStatusResultDto.builder()
                .totalSales(summary.getTotalSales())
                .calculatedAt(summary.getCalculatedAt().toString())
                .monthlySales(monthly)
                .dailySales(daily)
                .weeklySales(weekly)
                .build();
    }

    public Object runAttendance(LocalDate startDate, LocalDate today) {
        log.info("출석 집계 시작 - {} ~ {}", startDate, today);
        return sparkJob.run(startDate, today, "daily_attendance");
    }
}