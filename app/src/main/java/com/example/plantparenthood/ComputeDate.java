package com.example.plantparenthood;

import android.os.Build;

import java.time.LocalDateTime;

/**
 * Due to Date class being deprecated this is required
 */
public class ComputeDate
{
    static final int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};
    static final String[] strMonths = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    /**
     * Private constructor, do not make an object of this,
     * just access Static methods
     */
    private ComputeDate(){};
    public enum Month
    {
        January,
        February,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December;

        public static ComputeDate.Month getValue(int month)
        {
            ComputeDate.Month[] months = ComputeDate.Month.values();
            ComputeDate.Month currentMonth = null;
            for (int i = 0; i < months.length; i++)
            {
                if(months[i].ordinal() == month)
                {
                    currentMonth = months[i];
                    break;
                }
            }
            return currentMonth;
        }
    }

    public static int computeDayOfYear(boolean isLeapYear, Month month, int day)
    {
        int dayOfYear = day;

        //december not included as its one month less
        switch (month) {
            case January:
                dayOfYear+=31;
                break;
            case February:
                dayOfYear+=59;
                break;
            case March:
                dayOfYear+=90;
                break;
            case April:
                dayOfYear+=120;
                break;
            case May:
                dayOfYear+=151;
                break;
            case June:
                dayOfYear+=181;
                break;
            case July:
                dayOfYear+=212;
                break;
            case August:
                dayOfYear+=243;
                break;
            case September:
                dayOfYear+=273;
                break;
            case October:
                dayOfYear+=304;
                break;
            case November:
                dayOfYear+=334;
                break;
            default:
                break;
        }

        if(isLeapYear && month.ordinal()-1 > Month.February.ordinal())
            dayOfYear++;

        return dayOfYear;
    }

    public static String computeDayMonth(boolean isLeapYear, int dayOfYear)
    {
        String formattedDate = "";

        int counter = 0;
        for (int i = 0; i < months.length; i++)
        {
            counter += months[i];
            if(i == 1 && isLeapYear)//leap year
                counter++;

            if(dayOfYear < counter)
            {
                formattedDate = strMonths[i] + " " + (dayOfYear - (counter-months[i]));
                break;
            }
        }

        return formattedDate;
    }

    public static int getDayOfTheYear()
    {
        int currentDay = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            currentDay = LocalDateTime.now().getDayOfYear();
        }

        return  currentDay;
    }
}
