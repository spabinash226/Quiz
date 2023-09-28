package com.technoabinash.quizapplication.features.dashboard.dto;

import java.util.ArrayList;



public class Quiz{
    public int response_code;
    public ArrayList<QuizQuestion> results;

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public ArrayList<QuizQuestion> getResults() {
        return results;
    }

    public void setResults(ArrayList<QuizQuestion> results) {
        this.results = results;
    }
}

