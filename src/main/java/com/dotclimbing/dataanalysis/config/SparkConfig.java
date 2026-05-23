package com.dotclimbing.dataanalysis.config;

import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SparkConfig {

    private final SparkProperties sparkProperties;

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(sparkProperties.getApp().getName())
                .master(sparkProperties.getMaster())
                .config("spark.excutor.memory", sparkProperties.getExecutor().getMemory())
                .config("spark.excutor.cores", String.valueOf(sparkProperties.getExecutor().getCores()))
                .config("spark.driver.memory", sparkProperties.getDriver().getMemory())
                .config("spark.sql.shuffle.partitions", "200")
                .config("spark.ui.enabled", "false")
                .getOrCreate();
    }
}
