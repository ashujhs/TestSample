package com.ash.timesample;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Stack;

public class NonOverlappingRange {


    private static XLogger log = XLoggerFactory.getXLogger(NonOverlappingRange.class);

    public void processRange(String dataSource) throws IOException {
        File file = getResource(dataSource);
        FileReader fileReader = new FileReader(file);
        StopWatch stopWatch = new StopWatch();
        PriorityQueue<DateTimeRecord> pQueue =  createPriorityQueue(10);
        PriorityQueue<DateTimeRecord> resultQueue =  createPriorityQueue(100);
        try(BufferedReader reader = new BufferedReader(fileReader)){
            String line = null;
            stopWatch.start();
            while((line = reader.readLine()) != null){
                DateTimeRecord parsedRecord = parseRecords(line);
                pQueue.add(parsedRecord);

            }
            StopWatch stopWatch1 = new StopWatch();stopWatch1.start();
            PriorityQueue<DateTimeRecord> dateTimeRecordStack = traverseRangeAndUpdate(pQueue);
            Iterator<DateTimeRecord> iterator = dateTimeRecordStack.iterator();
            while(dateTimeRecordStack.size()>0){
                log.info("Rec - {}",dateTimeRecordStack.poll().toString());
            }
            stopWatch1.stop();
            log.info("size of total ranges {} and took - {}",dateTimeRecordStack.size(),stopWatch1.toString() );
        } catch (IOException e) {
            log.error("Error in reading the file {}",e.getLocalizedMessage());
        }



    }

    private PriorityQueue<DateTimeRecord> traverseRangeAndUpdate(PriorityQueue<DateTimeRecord> pQueue) {
        PriorityQueue<DateTimeRecord> resultSet = createPriorityQueue(10);
        while(pQueue.size()>0){
            DateTimeRecord topRecords = pQueue.poll();

            DateTimeRecord secondTopRecord = pQueue.poll();


            calculateNewRanges(topRecords, secondTopRecord, resultSet, pQueue);
        }
        return resultSet;
    }

    private void calculateNewRanges(DateTimeRecord topRecords, DateTimeRecord secondTopRecord, PriorityQueue<DateTimeRecord> resultSet, PriorityQueue<DateTimeRecord> pQueue){
        if(null == secondTopRecord){
            resultSet.add(topRecords);
            return ;
        }
        if(topRecords.getEndDate().isBefore(secondTopRecord.getStartDate())){
            // SKIP any further calculation because top range is not overlapping
            resultSet.add(topRecords);
        }else if(topRecords.getEndDate().isAfter(secondTopRecord.getStartDate())){
            createRanges(pQueue, topRecords.getStartDate(), secondTopRecord.getStartDate(), topRecords.getValue());

            if(topRecords.getEndDate().isBefore(secondTopRecord.getEndDate())){
                    // Create 3 ranges 1: top.start - next.start=tval 2  next.start - top.end=tval + nval 3 top.end - next.end=nval
                    createRanges(pQueue, secondTopRecord.getStartDate(), topRecords.getEndDate(), topRecords.getValue()+secondTopRecord.getValue());
                    createRanges(pQueue, topRecords.getEndDate(), secondTopRecord.getEndDate(), secondTopRecord.getValue());
                }else{
                    //create 1: top.start - next.start= tval 2: next.end - next.end= tval+nval 3: next.end - top.end = tval+nval
                    createRanges(pQueue, secondTopRecord.getStartDate(), secondTopRecord.getEndDate(), topRecords.getValue()+secondTopRecord.getValue());
                    createRanges(pQueue, secondTopRecord.getEndDate(), topRecords.getEndDate(), topRecords.getValue()+secondTopRecord.getValue());
                }
        }
    }

    private void createRanges(PriorityQueue<DateTimeRecord> pQueue, LocalDateTime startDate, LocalDateTime endDate, int value) {
        pQueue.add(new DateTimeRecord(startDate,getPreviousDateTime(endDate),value));

    }


    private Stack<DateTimeRecord> calculateNewRanges(DateTimeRecord topRecords, DateTimeRecord secondTopRecord, Stack<DateTimeRecord> resultSet){

        return resultSet;
    }

    private LocalDateTime getPreviousDateTime(LocalDateTime localDateTime){
        return localDateTime.minus(1,ChronoUnit.SECONDS);
    }
    private LocalDateTime getNextDateTime(LocalDateTime localDateTime){
        return localDateTime.plus(1,ChronoUnit.SECONDS);
    }
    private DateTimeRecord parseRecords (String csvData){
        if(StringUtils.isNotEmpty(csvData)){
            String[] dataStrings = csvData.split(",");
            return new DateTimeRecord(LocalDateTime.parse(dataStrings[0]),LocalDateTime.parse(dataStrings[1]),Integer.valueOf(dataStrings[2]));
        }
        return null;
    }

    private PriorityQueue<DateTimeRecord> createPriorityQueue(int capacity){
        return new PriorityQueue<>(capacity, new Comparator<DateTimeRecord>() {
            @Override
            public int compare(DateTimeRecord o1, DateTimeRecord o2) {

                LocalDateTime startDate1 = o1.getStartDate();
                LocalDateTime startDate2 = o2.getStartDate();
                LocalDateTime endDate1 = o1.getEndDate();
                LocalDateTime endDate2 = o2.getEndDate();
                if(startDate1.compareTo(startDate2) ==0) {
                    return (endDate1.compareTo(endDate2));
                }else{
                    return startDate1.compareTo(startDate2);
                }
            }
        });

    }
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
