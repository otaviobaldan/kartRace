package br.com.gympass.test.kartRace.Utils;

import org.joda.time.LocalTime;

import java.util.List;

public class DateUtils {
    public static LocalTime sumTimes(List<LocalTime> timeList){
        int millis = 0;
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        for ( LocalTime localTime : timeList){
            millis = millis + localTime.getMillisOfSecond();
            seconds = seconds + localTime.getSecondOfMinute();
            minutes = minutes + localTime.getMinuteOfHour();
            hours = hours + localTime.getHourOfDay();
        }

        // if the millis are greater than 999 I need to convert to one more second
        if (millis > 999) {
            seconds = seconds + (millis/999);
            millis = millis % 999;
        }

        // if the seconds are greater than 59 I need to convert to one more minute
        if (seconds > 59) {
            minutes = minutes + (seconds / 60);
            seconds = seconds % 60;
        }

        // if the minutes are greater than 59 I need to convert to one more hour
        if (minutes > 59) {
            hours = hours + (minutes/60);
            minutes = minutes % 60;
        }

        // return a new time with all laps summed
        return new LocalTime(hours, minutes, seconds, millis);

    }

    public static LocalTime differenceBetweenTimes(LocalTime firstTime, LocalTime secondTime){
        // desconstruct the second time to have all in one variable and sum
        int hoursSecTime = secondTime.getHourOfDay();
        int minutesSecTime = secondTime.getMinuteOfHour();
        int secondsSecTime = secondTime.getSecondOfMinute();
        int milliSecTime = secondTime.getMillisOfSecond();

        // desconstruct the first time to have all in one variable and sum
        int hoursFirstTime = firstTime.getHourOfDay();
        int minutesFirstTime = firstTime.getMinuteOfHour();
        int secondsFirstTime= firstTime.getSecondOfMinute();
        int milliFirstTime = firstTime.getMillisOfSecond();

        // build the new time, verifying the subtract is less than zero, in this case I change the variable order
        int newHours = (hoursSecTime - hoursFirstTime < 0 ) ? (hoursFirstTime - hoursSecTime) : (hoursSecTime - hoursFirstTime);
        int newMinutes =  (minutesSecTime - minutesFirstTime < 0) ? (minutesFirstTime - minutesSecTime) : (minutesSecTime - minutesFirstTime);
        int newSeconds = (secondsSecTime - secondsFirstTime < 0) ? (secondsFirstTime - secondsSecTime) : (secondsSecTime - secondsFirstTime);
        int newMilli = (milliSecTime - milliFirstTime < 0) ? (milliFirstTime - milliSecTime) : (milliSecTime - milliFirstTime);

        return new LocalTime(newHours, newMinutes, newSeconds, newMilli);
    }
}
