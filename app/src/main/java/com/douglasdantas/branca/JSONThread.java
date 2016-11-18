package com.douglasdantas.branca;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONThread implements Runnable {

    boolean finished;
    String url;
    JSONArray array;

    public JSONThread(String s) {
        url = s;
    }

    public boolean isFinished() { return finished; }

    public JSONArray returnArray() { return array; }
    @Override
    public void run() {
        try {
            array = getJSONObjectFromURL(url);
            finished = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONArray(jsonString);
    }
}