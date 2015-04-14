package ru.com.cardiomagnyl.util;

import java.util.Comparator;

import ru.com.cardiomagnyl.model.timeline.Timeline;

public class TimelineComparator implements Comparator<Timeline> {
    public int compare(Timeline timelineA, Timeline timelineB) {
        Long timeA = Tools.dateFromShortDate(timelineA.getDate()).getTime();
        Long timeB = Tools.dateFromShortDate(timelineB.getDate()).getTime();
        return timeB.compareTo(timeA);
    }
}
