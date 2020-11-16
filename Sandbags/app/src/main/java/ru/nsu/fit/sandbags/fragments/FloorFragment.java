package ru.nsu.fit.sandbags.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.util.List;

import ru.nsu.fit.sandbags.map.PinStruct;
import ru.nsu.fit.sandbags.map.PinView;
import ru.nsu.fit.sandbags.R;

public class FloorFragment extends Fragment {

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

    }
}