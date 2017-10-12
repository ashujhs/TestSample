package com.ash.timesample;

import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.core.runtime.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateTimeCalcTest1 {
    XLogger logger = XLoggerFactory.getXLogger(DateTimeCalcTest1.class);
    List<DateTimeRecord> dataList ;
    @Before
    public void setup(){
    }
    @Test
    public void dateCalculatorHappyPath() throws Exception {
        dataList= seedData(10);

        StopWatch watch = new StopWatch();

        watch.start();
        DateTimeCalculator1 calc = new DateTimeCalculator1();
        String sample1 = "2017-10-08";
        String sample2 = "2017-11-09";
        String sample3 = "2017-01-08";
        String sample4 = "2017-09-01";
        String sample5 = "2017-03-19";
        String sample6 = "2017-02-19";
        String sample7 = "2017-12-09";

        Integer integer1 = calc.dateCalculator(dataList, sample1);
        Assert.isNotNull(integer1);
        logger.info("Sample 1 - {}", integer1);
        Integer integer2 = calc.dateCalculator(dataList, sample2);
        Assert.isNotNull(integer2);
        logger.info("Sample 2 - {}", integer2);
        Integer integer3 = calc.dateCalculator(dataList, sample3);
        Assert.isNotNull(integer3);
        logger.info("Sample 3 - {}", integer3);
        Integer integer4 = calc.dateCalculator(dataList, sample4);
        Assert.isNotNull(integer4);
        logger.info("Sample 4 - {}", integer4);

        Integer integer5 = calc.dateCalculator(dataList, sample5);
        Assert.isNotNull(integer5);
        logger.info("Sample 5 - {}", integer5);

        Integer integer6 = calc.dateCalculator(dataList, sample6);
        Assert.isNotNull(integer6);
        logger.info("Sample 6 - {}", integer6);

        Integer integer7 = calc.dateCalculator(dataList, sample7);
        Assert.isNotNull(integer7);
        logger.info("Sample 7 - {}", integer7);
        watch.stop();

        logger.info("Processed these records in {} time", watch.toString());
    }


    @Test
    public void dateCalculator_performance_test() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        dataList= seedData(10000000);
        logger.info("Seeding took- {}", watch.toString());
        watch.reset();
        watch.start();
        DateTimeCalculator1 calc = new DateTimeCalculator1();
        String sample1 = "2017-10-08";
        String sample2 = "2017-11-09";
        String sample3 = "2017-01-08";
        String sample4 = "2017-09-01";
        String sample5 = "2017-03-19";
        String sample6 = "2017-02-19";
        String sample7 = "2017-12-09";

        Integer integer1 = calc.dateCalculator(dataList, sample1);
        Assert.isNotNull(integer1);
        logger.info("Sample 1 - {}", integer1);
        Integer integer2 = calc.dateCalculator(dataList, sample2);
        Assert.isNotNull(integer2);
        logger.info("Sample 2 - {}", integer2);
        Integer integer3 = calc.dateCalculator(dataList, sample3);
        Assert.isNotNull(integer3);
        logger.info("Sample 3 - {}", integer3);
        Integer integer4 = calc.dateCalculator(dataList, sample4);
        Assert.isNotNull(integer4);
        logger.info("Sample 4 - {}", integer4);

        Integer integer5 = calc.dateCalculator(dataList, sample5);
        Assert.isNotNull(integer5);
        logger.info("Sample 5 - {}", integer5);

        Integer integer6 = calc.dateCalculator(dataList, sample6);
        Assert.isNotNull(integer6);
        logger.info("Sample 6 - {}", integer6);

        Integer integer7 = calc.dateCalculator(dataList, sample7);
        Assert.isNotNull(integer7);
        logger.info("Sample 7 - {}", integer7);
        watch.stop();

        logger.info("Processed these records in {} time", watch.toString());
    }

    private List<DateTimeRecord> seedData(int totalRecords) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        List<DateTimeRecord> dateTimeRecordList = new ArrayList<>();
        for(int i=0; i<totalRecords;i++) {
            LocalDateTime now = LocalDateTime.now();
            SecureRandom random = new SecureRandom();
            random.nextInt(100);
            LocalDateTime startDate = now.minus(random.nextInt(100), ChronoUnit.DAYS).minus(random.nextInt(1000), ChronoUnit.MINUTES);
            LocalDateTime endDate = now.plus(random.nextInt(100), ChronoUnit.DAYS).plus(random.nextInt(1000), ChronoUnit.MINUTES);
            Integer value = random.nextInt(100);
            dateTimeRecordList.add(new DateTimeRecord(startDate.truncatedTo(ChronoUnit.SECONDS), endDate.truncatedTo(ChronoUnit.SECONDS), value));
        }
        return dateTimeRecordList;
    }



    protected File getResource(String resourceLocation) {
        try {
            return new ClassPathResource(resourceLocation.replaceFirst("classpath:", "")).getFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}