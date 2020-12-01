package ru.nsu.fit.sandbags;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import ru.nsu.fit.sandbags.api.ServerAPI;
import ru.nsu.fit.sandbags.fragments.FloorFragment;
import ru.nsu.fit.sandbags.map.PinStruct;

public class UpdateManager {
    private final ServerAPI serverAPI = new ServerAPI();
    private List<Map<String, PinStruct>> numbersOfSeats = new ArrayList<>();
    private final Object monitor = new Object();
    private int floor = 0;
    private MainActivity mainActivity;

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getFloor() {
        return floor;
    }

    public UpdateManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        Map<String, PinStruct> floorSeats = new TreeMap<>();
        floorSeats.put("320_1800", new PinStruct(10, new PointF(320f, 1800f), true));
        for (int i = 0; i < 5; i++) {
            numbersOfSeats.add(i, new TreeMap<>());
        }
        numbersOfSeats.add(3, floorSeats);
    }

    public void updateFromServer() {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    public void updatePinsOnMap() {
        mainActivity.updatePinsOnMap(numbersOfSeats.get(floor));
    }

    public Map<String, PinStruct> getNumbersOfSeats(int i) {
        return numbersOfSeats.get(i);
    }
    public int getFloorCnt() {
        return numbersOfSeats.size();
    }
}
