package com.versionupdate.demo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kangdongpu on 2016/12/28.
 */
public class JsonUtils {

    public static void put(JSONObject json, String key, String value) {

        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void put(JSONObject json, String key, int value) {

        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void put(JSONObject json, String key, boolean value) {

        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void put(JSONObject json, String key, JSONObject value) {

        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String getString(JSONObject json, String key) {

        String s = "";
        if (json.has(key)) {
            try {

                if (json.getString(key) != null)
                    s = json.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return s;
    }


    public static JSONObject getJSONObject(JSONObject json, String key) {

        JSONObject json2 = new JSONObject();

        if (json.has(key)) {
            try {
                json2 = json.getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json2;

    }


    public static JSONObject toJSONObject(String json) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }

        return jsonObject;
    }

}
