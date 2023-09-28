package com.technoabinash.quizapplication.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.technoabinash.quizapplication.application.MainApplication;
import com.technoabinash.quizapplication.features.dashboard.dto.Quiz;
import com.technoabinash.quizapplication.features.dashboard.dto.QuizQuestion;
import com.technoabinash.quizapplication.network.FlowResponse;
import com.technoabinash.quizapplication.network.RetroCallback;
import com.technoabinash.quizapplication.network.RetroHelper;
import com.technoabinash.quizapplication.network.RetrofitService;
import com.technoabinash.quizapplication.room.dao.QuizDao;
import com.technoabinash.quizapplication.room.dto.Questions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import timber.log.Timber;

public class QuizRepo {
    private final RetrofitService retrofitService;
    private final Context context;
    private final QuizDao dao;

    @Inject
    public QuizRepo(RetrofitService retrofitService, @ApplicationContext Context context, QuizDao dao) {
        this.retrofitService = retrofitService;
        this.context = context;
        this.dao = dao;
    }


    public void sendQuizListRequest(MutableLiveData<FlowResponse<Quiz>> liveData) {
        RetroHelper<Quiz> retroHelper = new RetroHelper<>();
        Call<Quiz> call = retrofitService.sendQuizListRequest(10);
        FlowResponse<Quiz> flowResponse = new FlowResponse<>();

        if (!MainApplication.hasNetwork()) {
            flowResponse.setInternetAvailable(true);
            liveData.postValue(flowResponse);
        }

        retroHelper.enqueue(call, new RetroCallback() {
            @Override
            public void onLoading() {
                flowResponse.setLoading(true);
                liveData.postValue(flowResponse);

            }

            @Override
            public void onFinished() {
                flowResponse.setLoading(false);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onSuccess(int code, Object response) {
                flowResponse.setLdData((Quiz) response);
                liveData.postValue(flowResponse);
                insertAllDataIntoQuestionList(flowResponse);


            }

            @Override
            public void onError(int code, String message) {
                flowResponse.setError(message);
                liveData.postValue(flowResponse);

            }

            @Override
            public void onHttpException(String error, String errorBody) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onSocketTimeoutException(String error) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void onIOException(String error) {
                flowResponse.setError(error);
                liveData.postValue(flowResponse);
            }

            @Override
            public void OnTokenExpired(String error) {
                flowResponse.setTokenExpired(true);
                flowResponse.setMessage(error);
                liveData.postValue(flowResponse);
            }
        });

    }

    public List<Questions> getAllQuestions() {
        return dao.getAllQuestions();
    }

    public void insertAllDataIntoQuestionList(FlowResponse<Quiz> data) {
        List<Questions> questions = new ArrayList<>();
        if (data.getLdData() != null) {
            ArrayList<QuizQuestion> results = data.getLdData().results;
            for (QuizQuestion q : results) {
                Questions question = new Questions();
                question.setCategory(q.getCategory());
                question.setQuestion(q.getQuestion());
                question.setDifficulty(q.getDifficulty());
                question.setType(q.getType());
                question.setCategory(q.getCategory());
                question.setCorrect_answer(q.getCorrect_answer());
                question.setIncorrect_answers(q.getIncorrect_answers());
                questions.add(question);
            }
            for (Questions quest : questions) {
                Questions exist = dao.getQuestion(quest.getQuestion());
                if (exist != null) {
                    Timber.v("Already Exist");
                } else {
                    dao.insertDataIntoQuestionList(quest);
                }

            }
//        dao.insertAllDataIntoQuestionList(questions);
        }
    }


}
