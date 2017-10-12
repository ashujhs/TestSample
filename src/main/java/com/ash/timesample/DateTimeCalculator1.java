package com.ash.timesample;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Approach #1 - Tested with huge data set and getting the response in less than 1s but it depends upon each object size too
public class DateTimeCalculator1 {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Integer dateCalculator(List<DateTimeRecord> dateTimeRecords, String inputDate){
        DateTimeCalculator1 dateTimeCalc = new DateTimeCalculator1();

        LocalDateTime inDate = dateTimeCalc.formatDate(inputDate);

        return dateTimeCalc.periodCalculator(dateTimeRecords,inDate);

    }

    private Integer periodCalculator(List<DateTimeRecord> dateTimeRecords, LocalDateTime inDate){
        return dateTimeRecords.parallelStream().filter(record -> record.inRange(inDate)).mapToInt(rec -> rec.getValue()).sum();
    }


    private LocalDateTime formatDate(String inputDate)  {
        ZoneId zoneId = ZoneId.systemDefault();
         LocalDate locDate = LocalDate.parse(inputDate,formatter);
         return locDate.atStartOfDay(zoneId).toLocalDateTime();
    }
}
