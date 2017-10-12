package com.ash.timesample;

import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class NonOverlappingRangeTest {

    XLogger logger = XLoggerFactory.getXLogger(NonOverlappingRange.class);
    /**
     * Initial test to check the values
     * @throws Exception
     */
    @Test
    public void processTestHappyPathWithDataSource() throws  Exception{
        NonOverlappingRange calc = new NonOverlappingRange();
        String sample1 = "2017-10-09";
        calc.processRange("data.txt");
    }
    @Test
    public void processTestHappyPERFPathWithDataSource() throws  Exception{
        NonOverlappingRange calc = new NonOverlappingRange();
        String sample1 = "2017-10-09";
        calc.processRange("data1.txt");
    }

}