/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.module_base.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.example.module_base.base.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SPUtil {

    private static SPUtil prefsUtil;
    public Context context;
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public synchronized static SPUtil getInstance() {
        return prefsUtil;
    }

    public static void init(Context context) {
        prefsUtil = new SPUtil();
        prefsUtil.context = context;
        prefsUtil.prefs = prefsUtil.context.getSharedPreferences(BaseApplication.Companion.getMContext().getPackageName(), Context.MODE_PRIVATE);
        prefsUtil.editor = prefsUtil.prefs.edit();
    }

    private SPUtil() {
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return this.prefs.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return this.prefs.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return this.prefs.getString(key, defaultVal);
    }

    public String getString(String key) {
        return this.prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return this.prefs.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return this.prefs.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return this.prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return this.prefs.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return this.prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return this.prefs.getStringSet(key, defaultVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key) {
        return this.prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return this.prefs.getAll();
    }

    public boolean exists(String key) {
        return prefs.contains(key);
    }


    public SPUtil putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putBoolean(String key, String value) {
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    public void commit() {
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SPUtil putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
        return this;
    }

    public SPUtil putMap(String key, HashMap<String, Integer> map) {
        JSONObject object = new JSONObject(map);
        String value = object.toString();
        editor.putString(key, value);
        editor.commit();
        return this;

    }

    public HashMap<String, Integer> getMap(String key) {
        HashMap<String, Integer> map = new HashMap<>();
        String value = prefs.getString(key, "");
        if (!TextUtils.isEmpty(value)) {
            try {
                JSONObject jsonObject = new JSONObject(value);
                Iterator<String> stringIterator = jsonObject.keys();
                while (stringIterator.hasNext()) {
                    String mapKey = stringIterator.next();
                    map.put(mapKey, jsonObject.getInt(mapKey));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;

    }

    public void putObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(key, objectVal);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (prefs.contains(key)) {
            String objectVal = prefs.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public  <T> boolean putListData(String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        SharedPreferences.Editor editor = prefs.edit();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }
    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public  <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = prefs.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            try {
                Object object = new JSONTokener(json).nextValue();
                if (object instanceof JSONArray) {//为了兼容旧版本，旧版本如果有播放记录开始取出来的还是上一个版本的Object类型的对象 而不是list 因为这个版本做了改动变成list
                    JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                    for (JsonElement elem : array) {
                        list.add(gson.fromJson(elem, cls));
                    }
                }
            }
            catch (Exception e){

                e.printStackTrace();
            }

        }
        return list;
    }



    public SPUtil remove(String key) {
        editor.remove(key);
        editor.commit();
        return this;
    }

    public SPUtil removeAll() {
        editor.clear();
        editor.commit();
        return this;
    }

    public static final String cityAddress = "cityAddress";

    public SPUtil setAddress(String address) {
        editor.putString(cityAddress, address);
        editor.commit();
        return this;
    }

    public String getAddress() {
        return this.prefs.getString(cityAddress, "");
    }

    public static final String category = "category";

    public SPUtil setCategory(boolean added) {
        editor.putBoolean(category, added);
        editor.commit();
        return this;
    }

    public boolean saveCategory() {
        return this.prefs.getBoolean(category, false);
    }


    public static final String playRecord = "playRecord";

    //设置播放记录，false上次播放小说，true上次播放Radio
    public SPUtil setPlayRecord(boolean b) {
        editor.putBoolean(playRecord, b);
        editor.commit();
        return this;
    }

    public boolean getPlayRecord() {
        return this.prefs.getBoolean(playRecord, true);
    }
}
