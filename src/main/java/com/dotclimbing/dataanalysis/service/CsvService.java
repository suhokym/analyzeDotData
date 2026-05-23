package com.dotclimbing.dataanalysis.service;

import com.dotclimbing.dataanalysis.dto.AccountingStatusDto;
import com.dotclimbing.dataanalysis.dto.DailyAttendanceDto;
import com.dotclimbing.dataanalysis.entity.AccountingStatus;
import com.dotclimbing.dataanalysis.entity.DailyAttendance;
import com.dotclimbing.dataanalysis.repository.AccountingStatusRepository;
import com.dotclimbing.dataanalysis.repository.DailyAttendanceRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final AccountingStatusRepository accountingStatusRepository;
    private final DailyAttendanceRepository dailyAttendanceRepository;

    public void parseAccountingStatusCsv(MultipartFile file) throws IOException {
        BufferedReader reader = toBomRemovedReader(file);

        List<AccountingStatusDto> result = new CsvToBeanBuilder<AccountingStatusDto>(reader)
                .withType(AccountingStatusDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build().parse();

        result.forEach(dto -> dto.setIncomeAmount(toAmount(dto.getIncome())));

        List<AccountingStatus> collect = result.stream().map(this::toAccountingEntity).collect(Collectors.toList());
        accountingStatusRepository.saveAll(collect);
    }

    public void parseDailyAttendanceCsv(MultipartFile file) throws IOException {
        BufferedReader reader = toBomRemovedReader(file);

        List<DailyAttendanceDto> parse = new CsvToBeanBuilder<DailyAttendanceDto>(reader)
                .withType(DailyAttendanceDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build().parse();
        List<DailyAttendance> collect = parse.stream().map(this::toDailyAttendanceEntity).collect(Collectors.toList());
        dailyAttendanceRepository.saveAll(collect);
    }

    private AccountingStatus toAccountingEntity(AccountingStatusDto dto) {
        AccountingStatus entity = new AccountingStatus();
        entity.setDate(dto.getDate());
        entity.setCategory(dto.getCategory());
        entity.setAccount(dto.getAccount());
        entity.setDetail(dto.getDetail());
        entity.setIncome(dto.getIncome());
        entity.setExpense(dto.getExpense());
        entity.setIncomeAmount(dto.getIncomeAmount());
        entity.setPeriodBalance(dto.getPeriodBalance());
        entity.setTotalBalance(dto.getTotalBalance());
        entity.setTrader(dto.getTrader());
        entity.setPayment(dto.getPayment());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setFinancialInstitution(dto.getFinancialInstitution());
        entity.setInstallment(dto.getInstallment());
        entity.setCashReceipt(dto.getCashReceipt());
        entity.setTaxInvoice(dto.getTaxInvoice());
        entity.setTradeStatement(dto.getTradeStatement());
        entity.setMemo(dto.getMemo());
        return entity;
    }

    private DailyAttendance toDailyAttendanceEntity(DailyAttendanceDto dto) {
        DailyAttendance entity = new DailyAttendance();
        entity.setMemberId(dto.getMemberId());
        entity.setName(dto.getName());
        entity.setTitle(dto.getTitle());
        entity.setNickname(dto.getNickname());
        entity.setGroup(dto.getGroup());
        entity.setGender(dto.getGender());
        entity.setAgeGroup(dto.getAgeGroup());
        entity.setPhone(dto.getPhone());
        entity.setEntryTime(dto.getEntryTime());
        entity.setExitTime(dto.getExitTime());
        entity.setStayDuration(dto.getStayDuration());
        entity.setManager(dto.getManager());
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setBirthday(dto.getBirthday());
        entity.setEtc1(dto.getEtc1());
        entity.setEtc2(dto.getEtc2());
        entity.setPoint(dto.getPoint());
        entity.setRegisteredDate(dto.getRegisteredDate());
        return entity;
    }

    private BufferedReader toBomRemovedReader(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8));
        bufferedReader.mark(1);
        if (bufferedReader.read() != 0xFEFF) {
            bufferedReader.reset();
        }
        return bufferedReader;
    }

    private Long toAmount(String value) {
        if (value == null || value.isBlank()) return 0L;
        String cleaned = value.replaceAll("[=\"]", "").replace(",", "").trim();
        if (cleaned.isBlank()) return 0L;
        return Long.parseLong(cleaned);
    }
}
