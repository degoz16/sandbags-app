package ru.nsu.fit.sandbags;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.sandbags.fragments.FloorFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction fragmentTransaction;
    private final List<Button> floors = new ArrayList<>(5);
    private FloorFragment floorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floors.add(0, findViewById(R.id.floor_1));
        floors.add(1, findViewById(R.id.floor_2));
        floors.add(2, findViewById(R.id.floor_3));
        floors.add(3, findViewById(R.id.floor_4));
        floors.add(4, findViewById(R.id.floor_5));

        floorFragment = new FloorFragment();
        floorFragment.setUpdateManager(new UpdateManager());

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            floors.get(i).setOnClickListener(view -> {

            });
        }
    }
}