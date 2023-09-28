package com.technoabinash.quizapplication.room.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.technoabinash.quizapplication.room.Converters;
import com.technoabinash.quizapplication.room.dao.QuizDao;
import com.technoabinash.quizapplication.room.dto.Questions;

@Database(entities = {Questions.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract QuizDao quizDao();




}