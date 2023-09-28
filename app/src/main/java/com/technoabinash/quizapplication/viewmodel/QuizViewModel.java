package com.technoabinash.quizapplication.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.technoabinash.quizapplication.application.MainApplication;
import com.technoabinash.quizapplication.features.dashboard.dto.Quiz;
import com.technoabinash.quizapplication.network.FlowResponse;
import com.technoabinash.quizapplication.repository.QuizRepo;
import com.technoabinash.quizapplication.room.dto.Questions;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import timber.log.Timber;

@HiltViewModel
public class QuizViewModel extends ViewModel {
    private final QuizRepo quizRepo;
    private final Context context;

    @Inject
    public QuizViewModel(QuizRepo quizRepo, @ApplicationContext Context context) {
        this.quizRepo = quizRepo;
        this.context = context;
    }

//    public MutableLiveData<FlowResponse<Quiz>> sendQuizRequest() {
//        MutableLiveData<FlowResponse<Quiz>> mutableLiveData = new MutableLiveData<>();
//        quizRepo.sendQuizListRequest(mutableLiveData);
//        return mutableLiveData;
//    }


    public MutableLiveData<FlowResponse<List<Questions>>> sendQuizRequestLocal() {
        MutableLiveData<FlowResponse<List<Questions>>> mutableLiveData = new MutableLiveData<>();
        FlowResponse<List<Questions>> flowResponse = new FlowResponse<>();
        if (!MainApplication.hasNetwork()) {
            List<Questions> questions = quizRepo.getAllQuestions();
            if (questions != null && questions.size()>0) {
                flowResponse.setLdData(questions);
                mutableLiveData.postValue(flowResponse);
                return mutableLiveData;
            }else {
                Toast.makeText(context, "You are playing this quiz offline. Go online for new questions.", Toast.LENGTH_SHORT).show();
                flowResponse.setInternetAvailable(true);
                mutableLiveData.postValue(flowResponse);

            }

            return mutableLiveData;
        }
        List<Questions> questions = quizRepo.getAllQuestions();

        if (questions != null&&questions.size()>0) {
            flowResponse.setLdData(questions);
            mutableLiveData.postValue(flowResponse);

            MutableLiveData<FlowResponse<Quiz>> liveData = new MutableLiveData<>();
            quizRepo.sendQuizListRequest(liveData);

        }else {
            flowResponse.setLoading(true);
            mutableLiveData.postValue(flowResponse);
            MutableLiveData<FlowResponse<Quiz>> liveData = new MutableLiveData<>();
            quizRepo.sendQuizListRequest(liveData);
            liveData.observeForever(data->{
                if (data.getLoading()) {
                    Timber.v("HomeFragment loading.");
                }
                if (data.getInternetAvailable()) {
                    Timber.v("HomeFragment no internet.");
                    Toast.makeText(context, "You are playing this quiz offline. Go online for new questions.", Toast.LENGTH_SHORT).show();
                }

                if (data.getTokenExpired()) {
                    Timber.v("HomeFragment token is expired");
                }

                if (data.getError() != null && !data.getError().isEmpty()) {
                    Timber.v("HomeFragment error" + data.getError());
                }

                if (data.getMessage() != null && !data.getMessage().isEmpty()) {
                    Timber.v("HomeFragment message" + data.getMessage());
                }


                if (data.getLdData() != null) {                    ;
                    flowResponse.setLdData(quizRepo.getAllQuestions());
                    mutableLiveData.postValue(flowResponse);
                    Timber.v("HomeFragment mutableData" + data);
                    Timber.v("HomeFragment mutableData" + data.getLdData().results.size());
                }
            });
        }

        return mutableLiveData;
    }
//    public MutableLiveData<List<Questions>> sendQuizRequestLocal() {
//        MutableLiveData<List<Questions>> mutableLiveData = new MutableLiveData<>();
//        List<Questions> questions = roomRepository.getAllQuestions();
//        if (questions != null) {
//            mutableLiveData.postValue(roomRepository.getAllQuestions());
//        }
//        return mutableLiveData;
//    }

//    public void saveToLocalDB(FlowResponse<Quiz> data) {
//        List<Questions> questions = new ArrayList<>();
//        if (data.getLdData() != null) {
//            ArrayList<QuizQuestion> results = data.getLdData().results;
//            for (QuizQuestion q : results) {
//                Questions question = new Questions();
//                question.setCategory(q.getCategory());
//                question.setQuestion(q.getQuestion());
//                question.setDifficulty(q.getDifficulty());
//                question.setType(q.getType());
//                question.setCategory(q.getCategory());
//                question.setCorrect_answer(q.getCorrect_answer());
//                question.setIncorrect_answers(q.getIncorrect_answers());
//                questions.add(question);
//            }
//
//            roomRepository.insertAllDataIntoQuestionList(questions);
//
//            Timber.v(" mutableData%s", data);
//            Timber.v(" mutableData%s", data.getLdData().results.size());
//        }
//
//    }

}
