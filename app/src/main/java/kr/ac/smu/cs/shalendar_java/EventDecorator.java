package kr.ac.smu.cs.shalendar_java;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private int pivotNum;
    private int color;
    private HashSet<CalendarDay> dates;
    public static int[] eventCount = new int[3];


    public EventDecorator(int color, Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.more);
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    public EventDecorator(int pivotNum, int color, Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.more);
        this.pivotNum = pivotNum;
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setSelectionDrawable(drawable);      // 초록색 사각형
//        view.addSpan(new DotSpan(5, color)); // 날자밑에 점
//        view.addSpan(new CustomEventDecorator(pivotNum,6, color));
//        view.addSpan(new CustomEventDecorator(pivotNum,5, color));

        CustomEventDecorator eventDecorator = new CustomEventDecorator(pivotNum, 6, color);
        view.addSpan(eventDecorator);
//        this.eventCount = eventDecorator.getEventCount();
    }


    public HashSet<CalendarDay> getDates() {
        return this.dates;
    }
}

