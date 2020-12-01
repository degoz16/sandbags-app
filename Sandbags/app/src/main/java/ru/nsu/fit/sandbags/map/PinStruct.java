package ru.nsu.fit.sandbags.map;

import android.graphics.PointF;

public class PinStruct {
    private final int num;
    private final PointF point;
    private boolean follow = false;
    private boolean prediction = false;

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public PinStruct(int num, PointF point, boolean prediction) {
        this.num = num > 0 ? num % 11 : 0;
        this.point = point;
        this.prediction = prediction;
    }

    public boolean isPrediction() {
        return prediction;
    }

    public int getNum() {
        return num;
    }

    public PointF getPoint() {
        return point;
    }
}
