package ru.nsu.fit.sandbags;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ru.nsu.fit.sandbags.firebase.FirebaseManager;
import ru.nsu.fit.sandbags.fragments.FloorFragment;
import ru.nsu.fit.sandbags.map.PinStruct;
import ru.nsu.fit.sandbags.map.PinView;

public class MainActivity extends AppCompatActivity {
    final private static String SENDER_ID = "454872156489";
    private PinView map;
    private final List<Button> floors = new ArrayList<>(5);
    private UpdateManager updateManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static WeakReference<MainActivity> mainActivityWeakReference;

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public UpdateManager getUpdateManager() {
        return updateManager;
    }

    public static WeakReference<MainActivity> getMainActivityWeakReference() {
        return mainActivityWeakReference;
    }


    public PinView getMap() {
        return map;
    }

    public void updatePinsOnMap(Map<String, PinStruct> pinStructList) {
        if (map != null) {
            map.setPins(new ArrayList<>(pinStructList.values()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityWeakReference = new WeakReference<>(this);

        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        floors.add(0, findViewById(R.id.floor_1));
        floors.add(1, findViewById(R.id.floor_2));
        floors.add(2, findViewById(R.id.floor_3));
        floors.add(3, findViewById(R.id.floor_4));
        floors.add(4, findViewById(R.id.floor_5));

        FloorFragment floorFragment = new FloorFragment();
        updateManager = new UpdateManager(this);

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            floors.get(i).setOnClickListener(view -> {
                //updateManager.updateFromServer();
                updatePinsOnMap(updateManager.getNumbersOfSeats(finalI));
                updateManager.setFloor(finalI);
            });
        }

        map = findViewById(R.id.mapView);
        map.setImage(ImageSource.asset("map.png"));
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (map.isReady()) {
                    float radius = 100 / map.getScale();
                    PointF point = map.viewToSourceCoord(e.getX(), e.getY());
                    List<PinStruct> pinsList = new ArrayList<>(updateManager.getNumbersOfSeats(updateManager.getFloor()).values());
                    PinStruct pinStruct = pinsList.get(0);
                    assert point != null;
                    float dist = (float) Math.sqrt(
                            Math.pow(pinStruct.getPoint().x - point.x, 2) +
                                    Math.pow(pinStruct.getPoint().y - 128 - point.y, 2)
                    );
                    for (int i = 0; i < pinsList.size(); i++) {
                        float dist1 = (float) Math.sqrt(
                                Math.pow(pinsList.get(i).getPoint().x - point.x, 2) +
                                        Math.pow(pinsList.get(i).getPoint().y - 128 - point.y, 2)
                        );
                        if (dist1 < dist) {
                            dist = dist1;
                            pinStruct = pinsList.get(i);
                        }
                    }
                    if (dist < radius) {
                        pinStruct.setFollow(!pinStruct.isFollow());
                        String topic = "place_" + updateManager.getFloor() + "_" + Math.round(pinStruct.getPoint().x) + "_" + Math.round(pinStruct.getPoint().y);
                        System.out.println(topic);
                        editor.putBoolean(topic, pinStruct.isFollow());
                        editor.apply();
                        if (pinStruct.isFollow()) {
                            fm.subscribeToTopic(topic).addOnCompleteListener(task -> {

                            });
                        } else {
                            fm.unsubscribeFromTopic(topic).addOnCompleteListener(task -> {

                            });
                        }
                        map.refreshPins();
                    }
                }
                return true;
            }
        });

        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });


        //refresh.setOnClickListener(view -> updateManager.updateFromServer());
        updatePinsOnMap(updateManager.getNumbersOfSeats(0));
        //updaterThread.start();

        fm.getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }

                    String token = task.getResult();
                    System.out.println(token);
                    fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com").
                            setMessageId(Hashing
                                    .sha256()
                                    .hashString(token + System.currentTimeMillis(), Charsets.UTF_8)
                                    .toString())
                            .addData("token", token)
                            .build());
                });


        for (int i = 0; i < updateManager.getFloorCnt(); i++) {
            for (PinStruct pinStruct : updateManager.getNumbersOfSeats(i).values()) {
                boolean followed = sharedPreferences.getBoolean(
                        "place_" + i + "_" + (int)pinStruct.getPoint().x + "_" + (int)pinStruct.getPoint().y,
                        false);
                System.out.println(followed);
                pinStruct.setFollow(followed);
            }
        }
        map.refreshPins();

        boolean defaultTopic = sharedPreferences.getBoolean("defaultTopic", false);
        if (!defaultTopic) {
            System.out.println("default topic");
            fm.subscribeToTopic("default").addOnCompleteListener(task -> {

            });
            editor.putBoolean("defaultTopic", true);
            editor.apply();
        }
        updateManager.updateFromServer();
    }
}