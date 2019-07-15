package kr.ac.smu.cs.shalendar_java;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

/*
  EventDecorator에서 부른다.
  drawBackground에서 점의 위치, 색깔 지정 가능
 */

public class CustomEventDecorator implements LineBackgroundSpan {

    private int pivotNum;
    private int radius;
    private int color;
    //점의 개수 count max = 3;
//    public static int[] eventCount = new int[3];



    public CustomEventDecorator(int radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    public CustomEventDecorator(int pivotNum, int radius, int color) {
        this(radius, color);
        this.pivotNum = pivotNum;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline,
                               int bottom, CharSequence text, int start, int end, int lnum) {

//        Log.d("left", Integer.toString(left));
//        Log.d("right", Integer.toString(right));
//        Log.d("top", Integer.toString(top));
//        Log.d("bottom", Integer.toString(bottom));

        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        /*
        canvas.drawCircle((left + right) / 2 - 20, bottom + radius, radius, paint);
        paint.setColor(oldColor);
        */
        Log.d("들어있는 값", Integer.toString(pivotNum));
        switch(pivotNum) {
            case 1:
                canvas.drawCircle((left + right) / 2, (bottom + top) / 2 + 30, radius, paint);
                paint.setColor(oldColor);
                EventDecorator.eventCount[0]++;
                break;
            case 2:
                canvas.drawCircle((left + right) / 2 , (bottom + top) / 2 + 50, radius, paint);
                paint.setColor(oldColor);
                EventDecorator.eventCount[1]++;
                break;
            case 3:
                canvas.drawCircle((left + right) / 2 , (bottom + top) / 2 + 70, radius, paint);
                paint.setColor(oldColor);
                EventDecorator.eventCount[2]++;
                break;
            default:
                break;
        }
//        eventCount++;
        Log.d("탈출 값", Integer.toString(pivotNum));
    }
}
