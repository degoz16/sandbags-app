package ru.nsu.fit.sandbags.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.util.List;

import ru.nsu.fit.sandbags.MainActivity;
import ru.nsu.fit.sandbags.map.PinStruct;
import ru.nsu.fit.sandbags.map.PinView;
import ru.nsu.fit.sandbags.R;
import ru.nsu.fit.sandbags.UpdateManager;

public class FloorFragment extends Fragment {


    private static PinView map;


    public void updatePinsOnMap(List<PinStruct> pinStructList){
        if (map != null) {
            map.setPin(pinStructList);
        }
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
    }
}