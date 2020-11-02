package ru.nsu.fit.sandbags;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.nsu.fit.sandbags.fragments.FloorFragment;

public class MainActivity extends AppCompatActivity {
    private final List<Button> floors = new ArrayList<>(5);
    private ImageButton refresh;
    private FloorFragment floorFragment;
    private static UpdateManager updateManager;
    private final Thread updaterThread = new Thread(()->{
        while (!Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.MINUTES.sleep(5);
            } catch (InterruptedException e) {
                break;
            }
            updateManager.updateFromServer();
        }
    });

    public static UpdateManager getUpdateManager() {
        return updateManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floors.add(0, findViewById(R.id.floor_1));
        floors.add(1, findViewById(R.id.floor_2));
        floors.add(2, findViewById(R.id.floor_3));
        floors.add(3, findViewById(R.id.floor_4));
        floors.add(4, findViewById(R.id.floor_5));

        refresh = findViewById(R.id.refresh);

        floorFragment = new FloorFragment();
        updateManager = new UpdateManager(floorFragment);

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            floors.get(i).setOnClickListener(view -> {
                //updateManager.updateFromServer();
                floorFragment.updatePinsOnMap(updateManager.getNumbersOfSeats(finalI));
                updateManager.setFloor(finalI);
            });
        }
        refresh.setOnClickListener(view -> updateManager.updateFromServer());
        floorFragment.updatePinsOnMap(updateManager.getNumbersOfSeats(0));
        updaterThread.start();
    }
}