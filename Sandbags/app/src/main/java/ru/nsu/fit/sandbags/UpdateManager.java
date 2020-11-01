package ru.nsu.fit.sandbags;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.sandbags.map.PinStruct;

public class UpdateManager {
    private final List<List<PinStruct>> numbersOfSeats = new ArrayList<>();
    private final Object monitor = new Object();
    private final Thread updaterThread = new Thread(()->{
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            //Updater code
        }
    });

    public UpdateManager() {
        List<PinStruct> floorSeats = new ArrayList<>();
        floorSeats.add(0, new PinStruct(10, new PointF(320f, 1800f)));
        for (int i = 0; i < 5; i++) {
            numbersOfSeats.add(i, new ArrayList<>());
        }
        numbersOfSeats.set(3, floorSeats);

        updaterThread.start();
    }

    public void updateFromServer() {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    public List<PinStruct> getNumbersOfSeats(int i) {
        return numbersOfSeats.get(i);
    }
}
