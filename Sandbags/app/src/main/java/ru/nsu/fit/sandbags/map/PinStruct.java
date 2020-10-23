package ru.nsu.fit.sandbags.map;

import android.graphics.PointF;

public class PinStruct {
    private final int num;
    private final PointF point;

    public PinStruct(int num, PointF point) {
        this.num = num > 0 ? num % 11 : 0;
        this.point = point;
    }

    public int getNum() {
        return num;
    }

    public PointF getPoint() {
        return point;
    }
}
