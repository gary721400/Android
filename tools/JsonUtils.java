package com.example.jsondemo;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created  :   GaryLiu
 * Email    :   gary@Test.com
 * Date     :   2018/4/8
 * Desc     :
 */

public class JsonUtils {
    private static final String TAG = "JsonUtils";
    private void parseFastGsonTest(){
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + "test.json"));
        BufferedReader bfr = new BufferedReader(isr);
        String line;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder textShow = new StringBuilder("This is parsed by FSON" + "\n");
        GsonTest fson = JSON.parseObject(stringBuilder.toString(),GsonTest.class);

//        GsonBean json = gson.getName();
        textShow.append("star cat is :" + fson.getCat() + "\n");
        for (GsonTest.StudentBean fans:fson.getStudent()) {
            textShow.append("id is :" + fans.getId());
            textShow.append(" name is :" + fans.getName());
            textShow.append(" sex is :" + fans.getSex());
            textShow.append(" age is :" + fans.getAge());
            textShow.append(" heigh is :" + fans.getHeight() + "\n");
//            textShow.append("fans name is :" + fans.getName() + "\n");
//            textShow.append("fans age is :" + fans.getAge() + "\n");
        }
//        mTextView.setText(textShow.toString());

    }

    private void parseFastGson(){
        LogUtil.E(TAG,OTHERTOOLS.getCurrentMethod());
        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + "complex.json"));
        BufferedReader bfr = new BufferedReader(isr);
        String line;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder textShow = new StringBuilder("This is parsed by FSON" + "\n");
        GsonBean fson = JSON.parseObject(stringBuilder.toString(),GsonBean.class);


        textShow.append("star name is :" + fson.getName() + "\n");
        for (GsonBean.FansBean fans:fson.getFans()) {
            textShow.append("fans name is :" + fans.getName() + "\n");
            textShow.append("fans age is :" + fans.getAge() + "\n");
        }
//        mTextView.setText(textShow.toString());

    }

    private void parseGson() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
                .getResourceAsStream("assets/" + "complex.json"));
        BufferedReader bfr = new BufferedReader(isr);

        StringBuilder textShow = new StringBuilder("This is parsed by GSON" + "\n");
        Gson gson = new Gson();
        GsonBean json = gson.fromJson(bfr, GsonBean.class);
        textShow.append("star name is :" + json.getName() + "\n");
        for (GsonBean.FansBean fans:json.getFans()) {
            textShow.append("fans name is :" + fans.getName() + "\n");
            textShow.append("fans age is :" + fans.getAge() + "\n");
        }
//        mTextView.setText(textShow.toString());
    }


    public void parseJson() {
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        InputStreamReader isr = new InputStreamReader(this.getClass().getClassLoader()
                        .getResourceAsStream("assets/" + "complex.json"));
//        String str = getPackageName().toString() + "/assets/" + "complex.json";
//        LogUtil.E(TAG, "str = " + str);
//        InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("/com/example/jsondemo/assets/complex.json"));
        BufferedReader bfr = new BufferedReader(isr);
        String line;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.E(TAG, "sbd = " + stringBuilder.toString());
        StringBuilder textShow = new StringBuilder("This is parsed by org.json" + "\n");
        try {
            JSONObject rootObject = new JSONObject(stringBuilder.toString());
            textShow.append("star name is : " + rootObject.get("name") + "\n");
            JSONArray jsonArray = rootObject.getJSONArray("fans");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                textShow.append("fans name is + " + jsonObject.getString("name") + "\n");
                textShow.append("fans age is + " + jsonObject.getString("age") + "\n");
            }
            LogUtil.E(TAG, "json = " + textShow.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
