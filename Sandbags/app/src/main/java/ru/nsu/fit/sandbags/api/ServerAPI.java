package ru.nsu.fit.sandbags.api;

import android.graphics.PointF;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

import ru.nsu.fit.sandbags.map.PinStruct;

public class ServerAPI {
    public FutureTask<List<List<PinStruct>>> getCurrentSandbagsState() {
        Callable<List<List<PinStruct>>> task = () -> {
            List<List<PinStruct>> currentState = new ArrayList<>();
            try {
                URL serverEndpoint = new URL("https://sandbags-project.herokuapp.com/api/v1/state/");
                HttpsURLConnection myConnection =
                        (HttpsURLConnection) serverEndpoint.openConnection();
                myConnection.setRequestProperty("Accept", "application/json");
                if (myConnection.getResponseCode() == 200) {
                    Reader responseBodyReader =
                            new InputStreamReader(myConnection.getInputStream(), "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray(); //floors
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject(); // floor
                        String key = jsonReader.nextName();
                        if (key.equals("places")) {
                            jsonReader.beginArray(); //places
                            currentState.add(parsePlaces(jsonReader));
                            System.out.println(currentState.size());
                            jsonReader.endArray();
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

        FutureTask<List<List<PinStruct>>> future = new FutureTask<>(task);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(future);
        return future;
    }

    private List<PinStruct> parsePlaces(JsonReader jsonReader) {
        List<PinStruct> floor = new ArrayList<>();
        try {
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();

                float xCoord = -1;
                float yCoord = -1;
                int empty = -1;

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
                PinStruct struct = new PinStruct(empty, new PointF(xCoord, yCoord));
                floor.add(struct);
                jsonReader.endObject();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floor;
    }
}
