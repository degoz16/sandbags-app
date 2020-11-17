package ru.nsu.fit.sandbags.firebase;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.nsu.fit.sandbags.MainActivity;
import ru.nsu.fit.sandbags.UpdateManager;
import ru.nsu.fit.sandbags.map.PinView;

public class FirebaseManager extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token){

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Message received", Toast.LENGTH_SHORT).show();
                }
            });
            int floor = Integer.parseInt(remoteMessage.getData().get("floor"));
            int point = Integer.parseInt(remoteMessage.getData().get("place"));
            int num = Integer.parseInt(remoteMessage.getData().get("num"));
            MainActivity mainActivity = MainActivity.getMainActivityWeakReference().get();
            PinView map = mainActivity.getMap();
            mainActivity
                .getUpdateManager()
                .getNumbersOfSeats(floor)
                .get(point)
                .setNum(num);
            map.refreshPins();
        }
        System.out.println("Message received");
        System.out.println(remoteMessage.getData());
    }
}
