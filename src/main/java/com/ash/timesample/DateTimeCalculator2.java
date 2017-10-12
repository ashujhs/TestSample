package com.ash.timesample;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class DateTimeCalculator2 {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static XLogger log = XLoggerFactory.getXLogger(DateTimeCalculator2.class);

    /**
     * Method to read the records from the source and process the data to calculate the date values total.
     * @param inputDateStr
     * @param dataSource
     * @param maxLoadSize
     * @return sumOfOccurrences which is total of all the values for provided date ranges
     * @throws FileNotFoundException
     */
    public long processor(String inputDateStr, String dataSource, long maxLoadSize) throws FileNotFoundException {
        File file = getResource(dataSource);

        // Using LRUCache to hold the records but it can save 25% processing time if we use HASHMap vs LinkedHashMap
        LRUCache<Integer,  DateTimeRecord> dataMap = null;
        long maxDataLoad = maxLoadSize;
        long sumOfOccurrences=0;
        FileReader fileReader = new FileReader(file);
        StopWatch stopWatch = new StopWatch();

        try(BufferedReader reader = new BufferedReader(fileReader)){
            String line = null;
            stopWatch.start();
            while((line = reader.readLine()) != null){
                if(dataMap == null){
                    // Creating LRU Cache +1 size to avoid any heap issue
                    dataMap = new LRUCache<Integer, DateTimeRecord>(maxDataLoad+1);
                }
                DateTimeRecord parsedRecord = parseRecords(line);
                if(null!= parsedRecord){
                    dataMap.put(parsedRecord.hashCode(), parsedRecord);
                }
                // Calculating for each chunk not for each record
                if(dataMap.size()>=maxDataLoad){
                    sumOfOccurrences += dateCalculator(dataMap.values(), inputDateStr);
                    // Save memory
                    dataMap.clear();
                }
            }
            stopWatch.stop();
            log.info("To process {} records, it took {} seconds",maxDataLoad,stopWatch.toString());
            stopWatch.reset();

            // Corner case, when there were EOF before maxDataLoad
            if(!dataMap.isEmpty()){
                sumOfOccurrences += dateCalculator(dataMap.values(), inputDateStr);
            }
        } catch (IOException e) {
            log.error("Error in reading the file {}",e.getLocalizedMessage());
        }
        return sumOfOccurrences;
    }

    /**
     * Data Source record parser into POJO
     * @param csvData
     * @return
     */
    private DateTimeRecord parseRecords (String csvData){
            if(StringUtils.isNotEmpty(csvData)){
                String[] dataStrings = csvData.split(",");
                return new DateTimeRecord(LocalDateTime.parse(dataStrings[0]),LocalDateTime.parse(dataStrings[1]),Integer.valueOf(dataStrings[2]));
            }
            return null;
    }
    public Integer dateCalculator(Collection<DateTimeRecord> dateTimeRecords, String inputDate){
        DateTimeCalculator2 dateTimeCalc = new DateTimeCalculator2();

        LocalDateTime inDate = dateTimeCalc.formatDate(inputDate);

        return dateTimeCalc.periodCalculator(dateTimeRecords,inDate);

    }

    private Integer periodCalculator(Collection<DateTimeRecord> dateTimeRecords, LocalDateTime inDate){
        // Running parallel streams to calculate faster and getting the sum
        return dateTimeRecords.parallelStream().filter(record -> record.inRange(inDate)).mapToInt(rec -> rec.getValue()).sum();
    }

    /**
     * Parsing the date string into DateTime format
     * @param inputDate
     * @return
     */
    private LocalDateTime formatDate(String inputDate)  {
        // Converting the Date into first hour of the day to compare with the DateTime
        ZoneId zoneId = ZoneId.systemDefault();
         LocalDate locDate = LocalDate.parse(inputDate,formatter);
         return locDate.atStartOfDay(zoneId).toLocalDateTime();
    }

    /**
     * Getting the resource data
     *
     * @param resourceLocation
     * @return
     */
    protected File getResource(String resourceLocation) {
        try {
            // Reading the source data from file as input
            return new ClassPathResource(resourceLocation.replaceFirst("classpath:", "")).getFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
