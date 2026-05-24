package com.dotclimbing.dataanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccStatusResultDto {

    // 전체 총매출
    private Long totalSales;

    // 집계 일시
    private String calculatedAt;

    // 월별 매출
    private List<MonthlySales> monthlySales;

    // 일별 매출
    private List<DailySales> dailySales;

    // 요일별 매출
    private List<WeeklySales> weeklySales;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlySales {
        private String month;       // "2026-01"
        private Long totalAmount;   // 월 총매출
        private Long count;         // 거래 건수
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailySales {
        private String date;        // "2026-01-01"
        private Long totalAmount;   // 일 총매출
        private Long count;         // 거래 건수
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeeklySales {
        private String dayOfWeek;   // "Mon", "Tue", ...
        private Long totalAmount;   // 요일 총매출
        private Long count;         // 거래 건수
    }
}
