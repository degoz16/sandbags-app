package ru.nsu.fit.sandbags;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.nsu.fit.sandbags.map.PinStruct;

public class UpdateManager {
    private final List<List<PinStruct>> numbersOfSeats = new ArrayList<>();

    public UpdateManager() {
        for (int i = 0; i < 5; i++) {
            //Временный код, потом каждый этаж будет настроен по-разному
            List<PinStruct> floorSeats = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Random random = new Random(j);
                floorSeats.add(j, new PinStruct(i, new PointF(random.nextInt(10) * 200, random.nextInt(10) * 200)));
            }
            numbersOfSeats.add(i, floorSeats);
        }
    }

    public List<PinStruct> getNumbersOfSeats(int i) {
        return numbersOfSeats.get(i);
    }
}
