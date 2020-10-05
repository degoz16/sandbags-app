package ru.nsu.fit.sandbags;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction fragmentTransaction;
    private Button[] floors = new Button[5];
    private Fragment[] floorFragments = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floors[0] = findViewById(R.id.floor_1);
        floors[1] = findViewById(R.id.floor_2);
        floors[2] = findViewById(R.id.floor_3);
        floors[3] = findViewById(R.id.floor_4);
        floors[4] = findViewById(R.id.floor_5);

        floorFragments[0] = new FloorFragment1();
        floorFragments[1] = new FloorFragment2();
        floorFragments[2] = new FloorFragment3();
        floorFragments[3] = new FloorFragment4();
        floorFragments[4] = new FloorFragment5();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, floorFragments[0]);
        fragmentTransaction.commit();

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            floors[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, floorFragments[finalI]);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}