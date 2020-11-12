package ru.nsu.fit.sandbags;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import ru.nsu.fit.sandbags.api.ServerAPI;
import ru.nsu.fit.sandbags.fragments.FloorFragment;
import ru.nsu.fit.sandbags.map.PinStruct;

public class UpdateManager {
    private final ServerAPI serverAPI = new ServerAPI();
    private List<List<PinStruct>> numbersOfSeats = new ArrayList<>();
    private final Object monitor = new Object();
    private int floor = 0;

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public UpdateManager(FloorFragment floorFragment) {
        List<PinStruct> floorSeats = new ArrayList<>();
        floorSeats.add(0, new PinStruct(10, new PointF(320f, 1800f)));
        for (int i = 0; i < 5; i++) {
            numbersOfSeats.add(i, new ArrayList<>());
        }
        numbersOfSeats.set(3, floorSeats);

        Thread updaterThread = new Thread(() -> {
            List<List<PinStruct>> list = null;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("TEST UPDATE LOOP");
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                FutureTask<List<List<PinStruct>>> futureTask = serverAPI.getCurrentSandbagsState();
                try {
                    list = futureTask.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    break;
                }
                if (list != null) {
                    numbersOfSeats = list;
                }
                floorFragment.updatePinsOnMap(numbersOfSeats.get(floor));
            }
        });
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
