package com.comp90018.H1Calendar.utils;

import com.comp90018.H1Calendar.model.DayItem;

/**
 * Events emitted by the EventBus
 */
public class Events {

    public static class DayClickedEvent {
        public DayItem dayItem;

        public DayClickedEvent(DayItem dayItem) {
            this.dayItem = dayItem;
        }

        public DayItem getDayItem() {
            return dayItem;
        }
    }


}
