package com.dotclimbing.dataanalysis.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounting_status")
public class AccountingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String category;
    private String account;
    private String detail;
    private String income;
    private String expense;
    private Long incomeAmount;
    private String periodBalance;
    private String totalBalance;
    private String trader;
    private String payment;
    private String paymentMethod;
    private String financialInstitution;
    private String installment;
    private String cashReceipt;
    private String taxInvoice;
    private String tradeStatement;

    @Column(length = 500)
    private String memo;
}