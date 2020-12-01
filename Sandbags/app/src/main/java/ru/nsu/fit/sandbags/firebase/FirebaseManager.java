package ru.nsu.fit.sandbags.firebase;


import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.nsu.fit.sandbags.MainActivity;
import ru.nsu.fit.sandbags.UpdateManager;
import ru.nsu.fit.sandbags.map.PinStruct;
import ru.nsu.fit.sandbags.map.PinView;

public class FirebaseManager extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            //Handler handler = new Handler(Looper.getMainLooper());
            //handler.post(() -> Toast.makeText(getApplicationContext(), "Message received", Toast.LENGTH_SHORT).show());
            try {
                int floor = Integer.parseInt(remoteMessage.getData().get("floor"));
                int x = Integer.parseInt(remoteMessage.getData().get("x"));
                int y = Integer.parseInt(remoteMessage.getData().get("y"));
                int num = Integer.parseInt(remoteMessage.getData().get("num"));
                boolean predict = Boolean.parseBoolean(remoteMessage.getData().get("predict"));
                MainActivity mainActivity = MainActivity.getMainActivityWeakReference().get();
                UpdateManager updateManager = mainActivity.getUpdateManager();
                PinStruct newPinStruct = new PinStruct(num, new PointF(x, y), predict);
                newPinStruct.setFollow(updateManager.getNumbersOfSeats(floor).get(x + "_" + y).isFollow());
                updateManager
                        .getNumbersOfSeats(floor)
                        .put(x + "_" + y, newPinStruct);
                updateManager.updatePinsOnMap();
            } catch (Exception ignored) {

            }
        }

        System.out.println("Message received");
    }
}
