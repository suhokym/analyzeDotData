package com.dotclimbing.dataanalysis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spark")  // yaml에서 spark: 하위 값들을 읽어옴
@Getter @Setter
public class SparkProperties {

    private App app = new App();        // spark.app.*
    private String master;              // spark.master
    private Executor executor = new Executor();  // spark.executor.*
    private Driver driver = new Driver();        // spark.driver.*

    // spark.app.name
    @Getter @Setter
    public static class App {
        private String name;
    }

    // spark.executor.memory / cores
    @Getter @Setter
    public static class Executor {
        private String memory;
        private int cores;
    }

    // spark.driver.memory
    @Getter @Setter
    public static class Driver {
        private String memory;
    }
}
