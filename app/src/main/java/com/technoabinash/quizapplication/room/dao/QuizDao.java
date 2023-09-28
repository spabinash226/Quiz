package com.technoabinash.quizapplication.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.technoabinash.quizapplication.room.dto.Questions;

import java.util.List;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM Questions ORDER BY RANDOM() limit 10")
    List<Questions> getAllQuestions();

    @Query("SELECT * FROM Questions where question=:quest")
    Questions getQuestion(String quest);

    @Insert
    void insertDataIntoQuestionList(Questions questions);
    @Insert
    void insertAllDataIntoQuestionList(List<Questions> questions);


}
