package ru.nsu.fit.sandbags;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import ru.nsu.fit.sandbags.api.ServerAPI;
import ru.nsu.fit.sandbags.map.PinStruct;

public class UpdateManager {
    private final ServerAPI serverAPI = new ServerAPI();
    private final List<List<PinStruct>> numbersOfSeats = new ArrayList<>();
    private final Object monitor = new Object();

    public UpdateManager() {
        List<PinStruct> floorSeats = new ArrayList<>();
        floorSeats.add(0, new PinStruct(10, new PointF(320f, 1800f)));
        for (int i = 0; i < 5; i++) {
            numbersOfSeats.add(i, new ArrayList<>());
        }
        numbersOfSeats.set(3, floorSeats);

        Thread updaterThread = new Thread(() -> {
            List<List<Integer>> list = null;
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                        FutureTask<List<List<Integer>>> futureTask = serverAPI.getCurrentSandbagsState();
                        list = futureTask.get();
                    } catch (InterruptedException e) {
                        break;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println(list.size());
                if (list != null) {
                    for (int i = 0; i < numbersOfSeats.size(); i++) {
                        System.out.println(list.get(i).size());
                        System.out.println(numbersOfSeats.get(i).size());
                        for (int j = 0; j < numbersOfSeats.get(i).size(); j++) {
                            numbersOfSeats.get(i).get(j).setNum(list.get(i).get(j));
                        }
                    }
                }
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
