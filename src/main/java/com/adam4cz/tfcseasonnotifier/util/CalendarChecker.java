package com.adam4cz.tfcseasonnotifier.util;

import com.adam4cz.tfcseasonnotifier.TFCSeasonNotifier;

import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import net.minecraft.server.level.ServerLevel;

public class CalendarChecker {

    public static Month getCurrentMonth(ServerLevel serverLevel) {
        ICalendar calendar = Calendars.get(serverLevel);

        TFCSeasonNotifier.debugLog("calendarTicks: " + calendar.getCalendarTicks());
        TFCSeasonNotifier.debugLog("calendarMonth: " + calendar.getCalendarMonthOfYear());

        return calendar.getCalendarMonthOfYear();
    }

}
