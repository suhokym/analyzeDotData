package com.dotclimbing.dataanalysis.job;

import com.dotclimbing.dataanalysis.dto.AccStatusResultDto;
import com.dotclimbing.dataanalysis.dto.DailyAttendanceResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SparkJob {

    private final SparkSession sparkSession;

    private static final String JDBC_URL = "jdbc:h2:file:./data/testdb;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";
    private static final String JDBC_DRIVER = "org.h2.Driver";

    public Object run(LocalDate startDate, LocalDate today, String tableName) {

        if (tableName.equals("accounting_status")) {
            return runAccountingJob();
        } else {
            return runAttendanceJob();
        }
    }

    private AccStatusResultDto runAccountingJob() {
        Dataset<Row> accounting = readFromH2("accounting_status");

        Dataset<Row> sales = accounting
                .filter(col("category").equalTo("수입"))
                .withColumn("amount",
                        regexp_replace(col("income"), ",", "").cast("long"))
                .withColumn("parsed_dt",
                        to_timestamp(col("date"), "yyyy-MM-dd HH:mm"))
                .withColumn("month",
                        date_format(col("parsed_dt"), "yyyy-MM"))
                .withColumn("day",
                        date_format(col("parsed_dt"), "yyyy-MM-dd"))
                .withColumn("day_of_week",
                        date_format(col("parsed_dt"), "E"))
                .withColumn("dow_num",
                        dayofweek(col("parsed_dt")));

        Long totalSales = sales
                .agg(sum("amount"))
                .first()
                .getLong(0);

        List<AccStatusResultDto.MonthlySales> monthlySales = sales
                .groupBy("month")
                .agg(
                        sum("amount").alias("total_amount"),
                        count("*").alias("count")
                )
                .orderBy("month")
                .collectAsList()
                .stream()
                .map(row -> AccStatusResultDto.MonthlySales.builder()
                        .month(row.getString(row.fieldIndex("month")))
                        .totalAmount(row.getLong(row.fieldIndex("total_amount")))
                        .count(row.getLong(row.fieldIndex("count")))
                        .build())
                .collect(Collectors.toList());

        List<AccStatusResultDto.DailySales> dailySales = sales
                .groupBy("day")
                .agg(
                        sum("amount").alias("total_amount"),
                        count("*").alias("count")
                )
                .orderBy("day")
                .collectAsList()
                .stream()
                .map(row -> AccStatusResultDto.DailySales.builder()
                        .date(row.getString(row.fieldIndex("day")))
                        .totalAmount(row.getLong(row.fieldIndex("total_amount")))
                        .count(row.getLong(row.fieldIndex("count")))
                        .build())
                .collect(Collectors.toList());

        List<AccStatusResultDto.WeeklySales> weeklySales = sales
                .groupBy("day_of_week", "dow_num")
                .agg(
                        sum("amount").alias("total_amount"),
                        count("*").alias("count")
                )
                .orderBy("dow_num")
                .collectAsList()
                .stream()
                .map(row -> AccStatusResultDto.WeeklySales.builder()
                        .dayOfWeek(row.getString(row.fieldIndex("day_of_week")))
                        .totalAmount(row.getLong(row.fieldIndex("total_amount")))
                        .count(row.getLong(row.fieldIndex("count")))
                        .build())
                .collect(Collectors.toList());

        return AccStatusResultDto.builder()
                .totalSales(totalSales)
                .monthlySales(monthlySales)
                .dailySales(dailySales)
                .weeklySales(weeklySales)
                .build();
    }

    private List<DailyAttendanceResultDto> runAttendanceJob() {
        Dataset<Row> attendance = readFromH2("daily_attendance");

        return attendance
                .withColumn("entry_hour",
                        hour(to_timestamp(col("entry_time"), "HH:mm")))
                .filter(
                        col("entry_hour").geq(12)
                                .and(col("entry_hour").lt(18))
                )
                .select("name", "group", "entry_time")
                .orderBy("entry_time")
                .collectAsList()
                .stream()
                .map(row -> DailyAttendanceResultDto.builder()
                        .name(row.getString(row.fieldIndex("name")))
                        .group(row.getString(row.fieldIndex("group")))
                        .entryTime(row.getString(row.fieldIndex("entry_time")))
                        .build())
                .collect(Collectors.toList());
    }

    private Dataset<Row> readFromH2(String tableName) {
        return sparkSession.read()
                .format("jdbc")
                .option("url", JDBC_URL)
                .option("dbtable", tableName)
                .option("user", JDBC_USER)
                .option("password", JDBC_PASSWORD)
                .option("driver", JDBC_DRIVER)
                .load();
    }
}


