package com.mkdev.trivai.data;

import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mkdev.trivai.controller.AppController;
import com.mkdev.trivai.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

import static com.mkdev.trivai.controller.AppController.TAG;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions() {
        Log.d(TAG, "getQuestions: Inside");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=20&difficulty=easy&type=boolean", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("JsonStuff", "onResponse: " + response);
                try {
//                    Log.d("JsonStuff", " and " + response.getJSONArray("results"));

                    for (int i = 0; i < response.getJSONArray("results").length(); i++) {

                        Log.d("JsonStuff", "onResponse: " + response.getJSONArray("results").get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JsonStuff", "onErrorResponse: " + error);
            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Log.d("JsonStuff", "onResponse: " + response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Question question = new Question();
                        question.setQuestion(response.getJSONArray(i).get(0).toString());
                        question.setAnswer(response.getJSONArray(i).getBoolean(1));

                        questionArrayList.add(question);

//                        Log.d("Json", "onResponse: " + response.getJSONArray(i).get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JsonStuff", "onErrorResponse: " + error);
            }
        });

//        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}
