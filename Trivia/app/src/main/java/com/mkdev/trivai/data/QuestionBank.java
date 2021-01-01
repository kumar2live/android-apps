package com.mkdev.trivai.data;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.LongDef;

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

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
//        final String openDBUrl = "https://opentdb.com/api.php?amount=10&difficulty=easy&type=boolean&encode=base64";
      final String openDBUrl = "https://opentdb.com/api.php?amount=20&difficulty=easy&type=boolean";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, openDBUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("JsonStuff", "onResponse: " + response);
                try {
//                    Log.d("JsonStuff", " and " + response.getJSONArray("results"));
                    for (int i = 0; i < response.getJSONArray("results").length(); i++) {
//                        Log.d("JsonStuff", "onResponse: " + response.getJSONArray("results").get(i));

                        JSONObject jsonObject =  new JSONObject((response.getJSONArray("results").get(i)).toString());
//                        Log.d("Json", "onResponse: " + jsonObject.getString("question"));

//                        Log.d("TrueOrFalse", "onResponse: " + jsonObject.getString("question"));
//                        Log.d("TrueOrFalse", "onResponse: " + jsonObject.getString("correct_answer"));
//                        Log.d("TrueOrFalse", String.valueOf("onResponse: " + jsonObject.getString("correct_answer").equals("True")));

                        Question question = new Question();
                        question.setQuestion(jsonObject.getString("question"));
                        question.setAnswer(jsonObject.getString("correct_answer").equals("True"));


                        questionArrayList.add(question);
//                        Log.d("JsonResults", "getQuestions: " + questionArrayList.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(null != callBack) {
                    callBack.processFinished(questionArrayList);
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

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
//        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
//        Log.d("JsonResults", "getQuestions: " + questionArrayList.size());
        return questionArrayList;
    }
}
