package com.dylanlxlx.campuslink.utils;

import org.json.JSONArray;
import org.json.JSONException;

public class JsonDeal {
    public static JSONArray reverseJSONArray(JSONArray jsonArray) {
        JSONArray reversedArray = new JSONArray();
        for (int i = jsonArray.length() - 1; i >= 0; i--) {
            try {
                reversedArray.put(jsonArray.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reversedArray;
    }
}
