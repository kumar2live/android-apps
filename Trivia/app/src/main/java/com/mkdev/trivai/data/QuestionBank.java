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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mkdev.trivai.controller.AppController.TAG;

public class QuestionBank {
    ArrayList<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://opentdb.com/api.php?amount=20&difficulty=easy&type=boolean", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JsonStuff", "onResponse: " + response);
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
                Log.d("JsonStuff", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JsonStuff", "onErrorResponse: " + error);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return null;
    }
}
