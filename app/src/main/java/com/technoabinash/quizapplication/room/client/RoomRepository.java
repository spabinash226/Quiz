//package com.technoabinash.quizapplication.room.client;
//
//import android.content.Context;
//
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Room;
//
//import com.technoabinash.quizapplication.features.dashboard.dto.Quiz;
//import com.technoabinash.quizapplication.features.dashboard.dto.QuizQuestion;
//import com.technoabinash.quizapplication.network.FlowResponse;
//import com.technoabinash.quizapplication.room.dao.QuizDao;
//import com.technoabinash.quizapplication.room.db.AppDatabase;
//import com.technoabinash.quizapplication.room.dto.Questions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import timber.log.Timber;
//
//public class RoomRepository {
//    private final QuizDao dao;
//
//    @Inject
//    public RoomRepository(QuizDao dao) {
//        this.dao = dao;
//    }
//
//
//    public List<Questions> getAllQuestions() {
//        return dao.getAllQuestions();
//    }
//
//
//    public void insertDataIntoQuestionList(Questions questions) {
//        dao.insertDataIntoQuestionList(questions);
//    }
//
//    public void insertAllDataIntoQuestionList(FlowResponse<Quiz> data) {
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
//        for (Questions quest:questions){
//            Questions exist= dao.getQuestion(quest.getQuestion());
//            if (exist!=null){
//                Timber.v("Already Exist");
//            }else {
//                dao.insertDataIntoQuestionList(quest);
//            }
//
//        }
////        dao.insertAllDataIntoQuestionList(questions);
//    }}
//}
