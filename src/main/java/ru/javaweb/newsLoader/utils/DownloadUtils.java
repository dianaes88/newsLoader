package ru.javaweb.newsLoader.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class DownloadUtils {

    public static JsonElement getJsonElementFromRequest(String api_url) throws IOException {
        URL url = new URL(api_url);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        request.disconnect();
        return (JsonElement) jsonElement;
    }
    public static ContentOfApiArticle[] getJsonAsStringFromRequest(String api_url) throws IOException {
        JsonElement jsonElement = getJsonElementFromRequest(api_url);
        String inputString = jsonElement.toString();

        System.out.println(inputString);
        Gson gson = new Gson();
        ContentOfApiArticle[] outputList = gson.fromJson(inputString, ContentOfApiArticle[].class);
        System.out.println(outputList);

        return outputList;
    }

    public static String getTextOnUrl(String url) throws IOException {
        HttpURLConnection request = (HttpURLConnection) (new URL(url)).openConnection();

        StringBuilder respBody = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream()))) {
            reader.lines().forEach(l -> respBody.append(l + "\r\n"));
        }
        return respBody.toString();
    }
}
