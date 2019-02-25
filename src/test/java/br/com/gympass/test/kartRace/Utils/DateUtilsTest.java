package br.com.gympass.test.kartRace.Utils;

import net.bytebuddy.asm.Advice;
import org.joda.time.LocalTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void sumTimes() {
        List<LocalTime> localTimeList = new ArrayList<>();
        localTimeList.add(new LocalTime(1,2,25,123));
        localTimeList.add(new LocalTime(1,3,17,130));
        LocalTime sumExpected = new LocalTime(2,5,42,253);
        LocalTime dateUtilsSumTime = DateUtils.sumTimes(localTimeList);

        assertTrue(dateUtilsSumTime.isEqual(sumExpected));

    }

    @Test
    public void differenceBetweenTimes() {
        LocalTime localTime1 = new LocalTime(1,2,0,0);
        LocalTime localTime2 = new LocalTime(1,3,0,0);
        LocalTime differenceExpected = new LocalTime(0,1,0,0);

        LocalTime dateUtilsLocalTime = DateUtils.differenceBetweenTimes(localTime1, localTime2);

        assertTrue(dateUtilsLocalTime.isEqual(differenceExpected));
    }
}