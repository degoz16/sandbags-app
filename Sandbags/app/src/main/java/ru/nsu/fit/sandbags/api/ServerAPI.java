package ru.nsu.fit.sandbags.api;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

import ru.nsu.fit.sandbags.map.PinStruct;

public class ServerAPI {
    private final SharedPreferences sharedPreferences;

    public ServerAPI(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public FutureTask<List<Map<String, PinStruct>>> getCurrentSandbagsState() {
        Callable<List<Map<String, PinStruct>>> task = () -> {
            List<Map<String, PinStruct>> currentState = new ArrayList<>();
            try {
                URL serverEndpoint = new URL("https://sandbags-project.herokuapp.com/api/v1/state/");
                HttpsURLConnection myConnection =
                        (HttpsURLConnection) serverEndpoint.openConnection();
                myConnection.setRequestProperty("Accept", "application/json");
                if (myConnection.getResponseCode() == 200) {
                    Reader responseBodyReader =
                            new InputStreamReader(myConnection.getInputStream(), "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray(); //
                    int i = 0;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject(); // floor
                        String key = jsonReader.nextName();
                        if (key.equals("places")) {
                            jsonReader.beginArray(); //places
                            currentState.add(parsePlaces(jsonReader, i));
                            //System.out.println(currentState.size());
                            jsonReader.endArray();
                            i++;
                        }
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.close();
                    myConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return currentState;
        };

        FutureTask<List<Map<String, PinStruct>>> future = new FutureTask<>(task);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(future);
        return future;
    }

    private Map<String, PinStruct> parsePlaces(JsonReader jsonReader, int i) {
        Map<String, PinStruct> floor = new TreeMap<>();
        try {
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();

                float xCoord = -1;
                float yCoord = -1;
                int empty = -1;
                int predict = -1;
                String key = jsonReader.nextName();
                if (key.equals("x_coord")) {
                    xCoord = jsonReader.nextInt();
                }
                key = jsonReader.nextName();
                if (key.equals("y_coord")) {
                    yCoord = jsonReader.nextInt();
                }
                key = jsonReader.nextName();
                if (key.equals("sandbags")) {
                    empty = jsonReader.nextInt();
                }
                key = jsonReader.nextName();
                if (key.equals("predict")) {
                    predict = jsonReader.nextInt();
                }
                PinStruct struct = new PinStruct(empty, new PointF(xCoord, yCoord), predict);
                boolean followed = sharedPreferences.getBoolean(
                        "place_" + i + "_" + (int)struct.getPoint().x + "_" + (int)struct.getPoint().y,
                        false);
                System.out.println(followed);
                struct.setFollow(followed);
                floor.put((int)xCoord + "_" + (int)yCoord, struct);
                jsonReader.endObject();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floor;
    }
}
