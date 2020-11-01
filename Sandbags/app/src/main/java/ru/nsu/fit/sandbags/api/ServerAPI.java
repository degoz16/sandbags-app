package ru.nsu.fit.sandbags.api;

import android.util.JsonReader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ServerAPI {
    public Future<List<List<Integer>>> getCurrentSandbagsState() {
        Callable task = () -> {
            List<List<Integer>> currentState = new ArrayList<>();
            try {
                URL serverEndpoint = new URL("http://127.0.0.1:8000/api/v1/state/");
                HttpURLConnection myConnection =
                        (HttpURLConnection) serverEndpoint.openConnection();
                myConnection.setRequestProperty("Accept", "application/json");
                if (myConnection.getResponseCode() == 200) {
                    Reader responseBodyReader =
                            new InputStreamReader(myConnection.getInputStream(),
                                    "UTF-8");
                    Scanner scanner = new Scanner(responseBodyReader);
                    while (scanner.hasNext())
                        System.out.println(scanner.next());
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginArray(); //floors
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject(); // floor
                        String key = jsonReader.nextName();
                        if (key.equals("number")) { //floor number
                            jsonReader.skipValue();
                            key = jsonReader.nextName();
                            if (key.equals("places")) {
                                jsonReader.beginArray(); //places
                                currentState.add(parsePlaces(jsonReader));
                                System.out.println(currentState.size());
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
        Thread thread = new Thread(future);
        thread.start();
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
