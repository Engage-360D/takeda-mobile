package ru.com.cardiomagnyl.util;

import java.util.Comparator;

import ru.com.cardiomagnyl.model.timeline.Timeline;

public class TimelineComparator implements Comparator<Timeline> {
    public int compare(Timeline timelineA, Timeline timelineB) {
        long timeA = Tools.dateFromShortDate(timelineA.getDate()).getTime();
        long timeB = Tools.dateFromShortDate(timelineB.getDate()).getTime();
        return (int) (timeB - timeA);
    }
}
