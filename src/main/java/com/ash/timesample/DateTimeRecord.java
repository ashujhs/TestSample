package com.ash.timesample;


import java.time.LocalDateTime;

public class DateTimeRecord {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int value;

    /**
     * Constuctor to create the data source row into POJO
     * @param startDate
     * @param endDate
     * @param value
     */
    public DateTimeRecord(LocalDateTime startDate, LocalDateTime endDate, int value){
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
    }

    /**
     * Date comparator to check whether the provided date is in range dates or not.
     * @param inDate
     * @return
     */
    //Find whether the provided date is in between both the date (Inclusive)
    public boolean inRange(LocalDateTime inDate){
        return this.startDate.compareTo(inDate)<=0 && inDate.compareTo(this.endDate)<=0;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String toString(){
        return String.join(",",this.startDate.toString(), this.endDate.toString(), String.valueOf(this.value));
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateTimeRecord that = (DateTimeRecord) o;

        if (value != that.value) return false;
        if (!startDate.equals(that.startDate)) return false;
        return endDate.compareTo(that.endDate)==0;
    }

    /**
     * HashCode for the record and including the 'value' as part hash code because there could be duplicate date ranges with different values
     * @return
     */
    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + value;
        return result;
    }
}
