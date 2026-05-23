package com.dotclimbing.dataanalysis.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountingStatusDto {

    @CsvBindByName(column = "일자")
    private String date;              // "2026-01-01 14:55"

    @CsvBindByName(column = "구분")
    private String category;          // "수입" / "지출"

    @CsvBindByName(column = "계정")
    private String account;           // "상품 판매" / "환불"

    @CsvBindByName(column = "세부내역")
    private String detail;            // "일일이용", "자유 이용권 1개월" 등

    @CsvBindByName(column = "수입")
    private String income;            // "20,000" or ""

    @CsvBindByName(column = "지출")
    private String expense;           // "90,000" or ""

    @CsvBindByName(column = "기간잔액")
    private String periodBalance;     // "20,000"

    @CsvBindByName(column = "누적잔액")
    private String totalBalance;      // "1,350,105,075"

    @CsvBindByName(column = "거래처")
    private String trader;            // "판매" / "홍길동" 등

    @CsvBindByName(column = "결제")
    private String payment;           // "○"

    @CsvBindByName(column = "납부방법")
    private String paymentMethod;     // "현금" / "카드" / "무통장" / "기타"

    @CsvBindByName(column = "금융기관")
    private String financialInstitution; // "삼성카드" / "현대카드" / ""

    @CsvBindByName(column = "할부")
    private String installment;       // "일시불" / ""

    @CsvBindByName(column = "현금영수증")
    private String cashReceipt;       // "발행" / "×" / ""

    @CsvBindByName(column = "세금계산서")
    private String taxInvoice;        // "×" / ""

    @CsvBindByName(column = "거래명세서")
    private String tradeStatement;    // "×" / ""

    @CsvBindByName(column = "거래메모")
    private String memo;              // "" / "-암벽화 10회 대여비..."

    private Long incomeAmount;
}