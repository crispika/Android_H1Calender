package com.comp90018.H1Calendar.utils;

import com.comp90018.H1Calendar.model.DayItem;

/**
 * Events emitted by the EventBus
 */
public class Events {

    public static class DayClickedEvent {
        private DayItem dayItem;

        public DayClickedEvent(DayItem dayItem) {
            this.dayItem = dayItem;
        }

        public DayItem getDayItem() {
            return dayItem;
        }
    }

    public static class MonthChangeEvent {
        private String monthFullName;

        public MonthChangeEvent(String monthFullName) {
            this.monthFullName = monthFullName;
        }

        public String getMonthFullName() {
            return monthFullName;
        }
    }

    public static class BackToToday {}


}
