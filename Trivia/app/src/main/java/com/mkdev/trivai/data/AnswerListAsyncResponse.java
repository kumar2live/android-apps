package com.mkdev.trivai.data;

import com.mkdev.trivai.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
