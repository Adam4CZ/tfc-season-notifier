package com.adam4cz.tfcseasonnotifier.util;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;

import net.dries007.tfc.util.calendar.Calendar;
import net.dries007.tfc.util.calendar.CalendarWorldData;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.server.level.ServerLevel;

public class CalendarChecker {
    
    // Constants
    public static final int TICKS_IN_HOUR = ICalendar.TICKS_IN_HOUR;
    public static final int HOURS_IN_DAY = ICalendar.HOURS_IN_DAY;
    public static final int TICKS_IN_DAY = TICKS_IN_HOUR * HOURS_IN_DAY;
    public static final int MONTHS_IN_YEAR = ICalendar.MONTHS_IN_YEAR;

    // This needs to be a float, otherwise there are ~62 minutes per hour
    public static final float TICKS_IN_MINUTE = TICKS_IN_HOUR / 60f;

    public static Month getCurrentMonth(ServerLevel serverLevel) {
        long currentDayTime = serverLevel.getDayTime();
        CalendarWorldData calendarWorldData = CalendarWorldData.get(serverLevel);
        Calendar calendar = calendarWorldData.getCalendar();
        long calendarTicks = calendar.getCalendarTicks();
        long daysInMonth = calendar.getCalendarDaysInMonth();
        // To display current month because TFC starts world on 1st June instead on 1st January
        long ticksInMonth = TICKS_IN_DAY * daysInMonth;
        long ticksStartYearShift = 5 * ticksInMonth;

        TFCSeasonNotifier.debugLog("currentDayTime: " + currentDayTime);
        TFCSeasonNotifier.debugLog("calendarTicks: " + calendarTicks);
        TFCSeasonNotifier.debugLog("daysInMonth: " + daysInMonth);

        return ICalendar.getMonthOfYear(ticksStartYearShift + currentDayTime, daysInMonth);
    }

}
