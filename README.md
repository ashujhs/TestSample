# Problem 
### DataSet
(1/1/15 - 2/1/15) Value : 5
(2/15/15 - 2/22/15) Value : 9
(1/15/15 - 1/20/15) Value : 2
(2/1/15 - 2/10/15) Value: 1
(1/16/15 - 1/22/15) Value : 3
 
 
### Part 1: 
Have values for date-time ranges (inclusive range). Given any date, calculate total value. Assume the date-times are at second precision. It is also possible that some dates have no values and ranges can overlap. Same date-time ranges with different values may also exist. Also, assume you have a very large set of data.
Define classes and implement. Try to create an efficient implementation. Analyze efficiency of your implementation.
 
### Things to consider for API:
* What if I have to find values for a large set of dates from a large set of date ranges frequently?  
* What if new data is frequently added?
 
** Example :**
For 1/16/15: Total Value = 5 + 2 + 3 = 10
For 1/20/15: Total Value = 5 + 2 + 3 = 10
 
### Part 2: 
From the above create a new set of non-overlapping date-time range objects that have the same value for all dates in the range.
##### Example:
(1/1/15 - 1/14/15) Value : 5
(1/15/15 - 1/15/15) Value : 5 + 2 = 7
(1/16/15 - 1/20/15) Value : 5 + 2 + 3 = 10
 
 
 ## Solutions
  
 ##### Part 1:
There are two approaches for applied to solve the problem #1. in first approach, it loaded all the records in memory and finished 1M record processing in less than 1sec.
In second approach, data loaded in constant chunks which takes care of below items
* Memory size is constant so it addressed the memory complexity
* Time complexity is O(n /log m) where n is numbers of records and m is number of batches.

There are two test cases for each approach - one for happy path and other one is for performance check.

#### Part 2 :
Added 2 test cases as earlier. 