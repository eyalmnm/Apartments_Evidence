package md.starlab.apartmentsevidenceapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by Sergey Ostrovsky
 * on 7/16/18
 */
public class JSONUtils {

    private static final String TAG = "JSONUtils";

    public static JSONObject getJSONObjectValue(JSONObject jsonObject, String key) {
        JSONObject value = null;
        try {
            value = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            Log.e(TAG, "getJSONObjectValue", e);
        }
        return value;
    }

    public static String getStringValue(JSONObject jsonObject, String key) {
        String value = null;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getString(key);
            } catch (JSONException e) {
                Log.e(TAG, "getStringValue", e);
            }
        }
        return value;
    }

    public static String[] getStringArrayValue(JSONObject jsonObject, String key) {
        String[] value = null;
        Vector<String> valuesVec = new Vector<String>();
        if (jsonObject.has(key)) {
            try {
                String valueStr = jsonObject.getString(key);
                valueStr = valueStr.replace("[", "");
                valueStr = valueStr.replace("]", "");
                StringTokenizer stringTokenizer = new StringTokenizer(valueStr, ",");
                while (stringTokenizer.hasMoreElements()) {
                    valuesVec.add((String) stringTokenizer.nextElement());
                }
            } catch (JSONException e) {
                Log.e(TAG, "getStringValue", e);
            }
        }
        value = new String[valuesVec.size()];
        valuesVec.copyInto(value);
        return value;
    }

    public static long[] getLongArrayValue(JSONObject jsonObject, String key) {
        long[] value = null;
        Vector<Long> valuesVec = new Vector<Long>();
        if (jsonObject.has(key)) {
            try {
                String valueStr = jsonObject.getString(key);
                valueStr = valueStr.replace("[", "");
                valueStr = valueStr.replace("]", "");
                StringTokenizer stringTokenizer = new StringTokenizer(valueStr, ",");
                while (stringTokenizer.hasMoreElements()) {
                    valuesVec.add(Long.valueOf((String) stringTokenizer.nextElement()));
                }
            } catch (JSONException e) {
                Log.e(TAG, "getStringValue", e);
            }
        }
        value = new long[valuesVec.size()];
        for (int i = 0; i < valuesVec.size(); i++) {
            value[i] = valuesVec.get(i).longValue();
        }
        return value;
    }

    public static float[] getFloatArrayValue(JSONObject jsonObject, String key) {
        float[] value = null;
        Vector<Float> valuesVec = new Vector<Float>();
        if (jsonObject.has(key)) {
            try {
                String valueStr = jsonObject.getString(key);
                valueStr = valueStr.replace("[", "");
                valueStr = valueStr.replace("]", "");
                StringTokenizer stringTokenizer = new StringTokenizer(valueStr, ",");
                while (stringTokenizer.hasMoreElements()) {
                    valuesVec.add(Float.valueOf((String) stringTokenizer.nextElement()));
                }
            } catch (JSONException e) {
                Log.e(TAG, "getStringValue", e);
            }
        }
        value = new float[valuesVec.size()];
        for (int i = 0; i < valuesVec.size(); i++) {
            value[i] = valuesVec.get(i).floatValue();
        }
        return value;
    }

    public static int getIntValue(JSONObject jsonObject, String key) {
        int value = 0;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getInt(key);
            } catch (JSONException e) {
                Log.e(TAG, "getIntValue", e);
            }
        }
        return value;
    }

    public static long getLongValue(JSONObject jsonObject, String key) {
        long value = 0;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getLong(key);
            } catch (JSONException e) {
                Log.e(TAG, "getIntValue", e);
            }
        }
        return value;
    }

    public static float getFloatValue(JSONObject jsonObject, String key) {
        float value = 0;
        if (jsonObject.has(key)) {
            try {
                value = (float) jsonObject.getDouble(key);
            } catch (JSONException e) {
                Log.e(TAG, "getIntValue", e);
            }
        }
        return value;
    }

    public static double getDoubleValue(JSONObject jsonObject, String key) {
        double value = 0;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getDouble(key);
            } catch (JSONException e) {
                Log.e(TAG, "getDoubleValue", e);
            }
        }
        return value;
    }

    public static boolean getBooleanValue(JSONObject jsonObject, String key) {
        boolean value = false;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getBoolean(key);
            } catch (JSONException e) {
                Log.e(TAG, "getBooleanValue", e);
            }
        }
        return value;
    }

    public static JSONArray getJsonArray(JSONObject jsonObject, String key) {
        JSONArray array = new JSONArray();
        if (jsonObject.has(key)) {
            try {
                array = jsonObject.getJSONArray(key);
            } catch (JSONException e) {
                Log.e(TAG, "getJsonArray", e);
            }
        }
        return array;
    }

    public static JSONArray convertToJsonArray(float[] floatArray) throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < floatArray.length; i++) {
            array.put(floatArray[i]);
        }
        return array;
    }

    public static Bundle convertJsonObject2Bundle(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        if (false == keys.hasNext()) return null;
        Bundle bundle = new Bundle();
        String key;
        while (keys.hasNext()) {
            key = keys.next();
            Object value = getStringValue(jsonObject, key);
            bundle.putString(key, String.valueOf(value));
        }
        return bundle;
    }

    public static String getJSONData(Context context, int jsonResourceId) {
        InputStream inputStream = context.getResources().openRawResource(jsonResourceId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonSB = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                jsonSB.append(line); //.append('\n');
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            Log.e("LOG",
                    "QuestionnaireActivity: getJSONData(): error_1: " + exc.getMessage());
        } finally {
            try {
                inputStream.close();
                bufferedReader.close();
            } catch (IOException exc) {
                Log.e("LOG",
                        "QuestionnaireActivity: getJSONData(): error_2: " + exc.getMessage());
            }
        }
        return jsonSB.toString();
    }

}