package ru.nsu.fit.sandbags.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.nsu.fit.sandbags.map.PinStruct;
import ru.nsu.fit.sandbags.map.PinView;
import ru.nsu.fit.sandbags.R;
import ru.nsu.fit.sandbags.UpdateManager;

public class FloorFragment extends Fragment {

    private Button test;
    private PinView map = null;
    private UpdateManager updateManager;
    private PointF center;
    private float scale;

    public void setUpdateManager(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    public void updatePinsOnMap(){
        Random random = new Random();
        PinStruct pinStruct = new PinStruct(0, new PointF(random.nextInt(1000), random.nextInt(1000)));
        List<PinStruct> pinStructs = new ArrayList<>();
        pinStructs.add(pinStruct);
        pinStruct = new PinStruct(10, new PointF(random.nextInt(1000), random.nextInt(1000)));
        pinStructs.add(pinStruct);
        map.setPin(pinStructs);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floor, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map = view.findViewById(R.id.mapView);
        map.setImage(ImageSource.asset("map.png"));
        test = view.findViewById(R.id.test);
        test.setOnClickListener(view1 -> {
            updatePinsOnMap();
        });
    }

}