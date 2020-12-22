package ru.nsu.fit.sandbags.firebase;

import android.graphics.PointF;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.nsu.fit.sandbags.MainActivity;
import ru.nsu.fit.sandbags.UpdateManager;
import ru.nsu.fit.sandbags.map.PinStruct;

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
                int predict = Integer.parseInt(remoteMessage.getData().get("predict"));
                MainActivity mainActivity = MainActivity.getMainActivityWeakReference().get();
                UpdateManager updateManager = mainActivity.getUpdateManager();
                System.out.println(x + "_" + y);
                PinStruct newPinStruct = new PinStruct(num, new PointF(x, y), predict);
                newPinStruct.setFollow(updateManager.getNumbersOfSeats(floor).get(x + "_" + y).isFollow());
                updateManager
                        .getNumbersOfSeats(floor)
                        .put(x + "_" + y, newPinStruct);
                updateManager.updatePinsOnMap();
                System.out.println("Message received");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
