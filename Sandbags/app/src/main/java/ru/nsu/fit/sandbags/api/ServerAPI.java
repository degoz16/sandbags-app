package ru.nsu.fit.sandbags.api;

import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

public class ServerAPI {
    public FutureTask<List<List<Integer>>> getCurrentSandbagsState() {
        Callable<List<List<Integer>>> task = () -> {
            List<List<Integer>> currentState = new ArrayList<>();
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
                        System.out.println(key);
                        if (key.equals("number")) { //floor number
                            jsonReader.skipValue();
                            key = jsonReader.nextName();
                            if (key.equals("places")) {
                                jsonReader.beginArray(); //places
                                currentState.add(parsePlaces(jsonReader));
                                jsonReader.endArray();
                            }
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

        FutureTask<List<List<Integer>>> future = new FutureTask<>(task);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(future);
        return future;
    }

    private List<Integer> parsePlaces(JsonReader jsonReader) {
        List<Integer> floor = new ArrayList<>();
        try {
            while (jsonReader.hasNext()) {
                jsonReader.beginObject();
                String key = jsonReader.nextName();
                if (key.equals("placeNumber")) {
                    jsonReader.skipValue();
                    key = jsonReader.nextName();
                    if (key.equals("sandbags")) {
                        floor.add(jsonReader.nextInt());
                    }
                }
                jsonReader.endObject();
            }
        } catch (IOException ignored) {
        }
        return floor;
    }
}
