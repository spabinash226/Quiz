package com.technoabinash.quizapplication.features.dashboard.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


import com.technoabinash.quizapplication.R;
import com.technoabinash.quizapplication.room.dto.Questions;
import com.technoabinash.quizapplication.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    private QuizViewModel quixVm = null;

    List<Questions> questions;
    //    ArrayList<QuizQuestion> questions;
    Questions question;
    //    QuizQuestion question;
    String correctAnswer;


    private TextView option1, option2, option3, option4, questionCounter, tv_question, tv_timer;
    private LinearLayout progressbar;
    private Button nxtBtn;
    int index = 0;
    CountDownTimer timer;
    int totalCorrectAnswers = 0;


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeApplicationOnBackPressed();
    }


    private void closeApplicationOnBackPressed() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showQuitDialogForBackPressed();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        quixVm = new ViewModelProvider(this).get(QuizViewModel.class);

        option1 = view.findViewById(R.id.option_1);
        option2 = view.findViewById(R.id.option_2);
        option3 = view.findViewById(R.id.option_3);
        option4 = view.findViewById(R.id.option_4);
        progressbar = view.findViewById(R.id.progressbar);

        nxtBtn = view.findViewById(R.id.nextBtn);

        option1.setOnClickListener(view15 -> makeSelected(view15));

        option2.setOnClickListener(view14 -> makeSelected(view14));


        option3.setOnClickListener(view13 -> makeSelected(view13));

        option4.setOnClickListener(view12 -> makeSelected(view12));


        nxtBtn.setOnClickListener(view1 -> {
            if (questions != null && !questions.isEmpty()) {
                reset();
                if (index < questions.size() - 1) {
                    index++;
//                showAllOptionTextView();
                    setNextQuestion();
                } else {
                    openResultFragment(view);
                }
            } else {
                Toast.makeText(requireContext(), "Questions are not fetched yet", Toast.LENGTH_SHORT).show();
            }


        });

        questionCounter = view.findViewById(R.id.questionCounter);
        tv_question = view.findViewById(R.id.question);
        tv_timer = view.findViewById(R.id.timer);

        ObserverLocalData(view);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button quizButton = view.findViewById(R.id.quizBtn);
        quizButton.setOnClickListener(view1 -> {
            showQuitDialog(view);
        });
    }

    private void makeSelected(View view) {
        if (timer != null) {
            timer.cancel();
            TextView selected = (TextView) view;
            checkAnswer(selected);
        }

    }

    void showAnswer() {
        if (correctAnswer.equals(option1.getText().toString()))
            option1.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_right)));
        else if (correctAnswer.equals(option2.getText().toString()))
            option2.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_right)));
        else if (correctAnswer.equals(option3.getText().toString()))
            option3.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_right)));
        else if (correctAnswer.equals(option4.getText().toString()))
            option4.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_right)));
    }

    void reset() {
        showAllOptionTextView();
        option1.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_unselected)));
        option2.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_unselected)));
        option3.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_unselected)));
        option4.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_unselected)));

    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(correctAnswer)) {
            totalCorrectAnswers++;
            textView.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_right)));
        } else {
            showAnswer();
            textView.setBackground(ContextCompat.getDrawable(requireContext(), (R.drawable.option_wrong)));
        }
    }

    void setNextQuestion() {
        if (timer != null)
            timer.cancel();

        assert timer != null;
        timer.start();
        if (index < questions.size()) {
            questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);
            tv_question.setText(question.getQuestion());
            correctAnswer = question.getCorrect_answer().trim();
            List<String> options = new ArrayList<>(question.getIncorrect_answers());
            options.add(question.getCorrect_answer().trim());

            Collections.shuffle(options);

            if (options.size() > 0) {
                if (options.get(0) != null) {
                    setOptionTextView(option1, options.get(0));
                }
            } else {
                hideVisibilityOfTextView(option1);
            }

            if (options.size() > 1) {
                if (options.get(1) != null) {
                    setOptionTextView(option2, options.get(1));
                }
            } else {
                hideVisibilityOfTextView(option2);
            }

            if (options.size() > 2) {
                if (options.get(2) != null) {
                    setOptionTextView(option3, options.get(2));
                }
            } else {
                hideVisibilityOfTextView(option3);
            }

            if (options.size() > 3) {
                if (options.get(3) != null) {
                    setOptionTextView(option4, options.get(3));
                }
            } else {
                hideVisibilityOfTextView(option4);
            }
        }
    }

    private void setOptionTextView(TextView tv, String text) {
        tv.setText(text);
    }

    private void hideVisibilityOfTextView(TextView tv) {
        tv.setVisibility(View.GONE);
    }

    private void showOptionTextView(TextView tv) {
        tv.setVisibility(View.VISIBLE);
    }

    private void showAllOptionTextView() {
        showOptionTextView(option1);
        showOptionTextView(option2);
        showOptionTextView(option3);
        showOptionTextView(option4);
    }

    private void resetTimer(View view) {
        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(String.valueOf(millisUntilFinished / 1000));
                if (millisUntilFinished <= 20) {
                    tv_timer.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPurple));
                }
            }

            @Override
            public void onFinish() {
                openResultFragment(view);

            }
        };
    }
//

//    private void ObserverData(View view) {
//
//        quixVm.sendQuizRequest().observe(getViewLifecycleOwner(), data -> {
//            if (data.getLoading()) {
//                Timber.v("HomeFragment loading.");
//            }
//            if (data.getInternetAvailable()) {
//                ObserverLocalData(view);
//                Timber.v("HomeFragment no internet.");
//                Toast.makeText(requireContext(), "You are playing this quiz offline. Go online for new questions.", Toast.LENGTH_SHORT).show();
//            }
//
//            if (data.getTokenExpired()) {
//                Timber.v("HomeFragment token is expired");
//            }
//
//            if (data.getError() != null && !data.getError().isEmpty()) {
//                Timber.v("HomeFragment error" + data.getError());
//            }
//
//            if (data.getMessage() != null && !data.getMessage().isEmpty()) {
//                Timber.v("HomeFragment message" + data.getMessage());
//            }
//
//
//            if (data.getLdData() != null) {
////                quixVm.saveToLocalDB(data);
//                ObserverLocalData(view);
//                Timber.v("HomeFragment mutableData" + data);
//                Timber.v("HomeFragment mutableData" + data.getLdData().results.size());
//            }
//        });
//
//    }

    private void ObserverLocalData(View view) {
        progressbar.setVisibility(View.VISIBLE);
//        ProgressDialog progress=new ProgressDialog(requireContext());
        quixVm.sendQuizRequestLocal().observe(getViewLifecycleOwner(), data -> {
            if (data.getLdData() != null && data.getLdData().size() > 0) {
//                progress.dismiss();
                progressbar.setVisibility(View.GONE);
                Timber.v("HomeFragment loading.");
                questions = data.getLdData();
                resetTimer(view);
                setNextQuestion();

            }
            if (data.getInternetAvailable()) {
                progressbar.setVisibility(View.GONE);
                showNoInternetDialogue(view);
            }

        });
    }


    private void openResultFragment(View view) {
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        String totalAnswers = String.format("%d/%d", (totalCorrectAnswers), questions.size());
        bundle.putString("score", totalAnswers);
        navController.navigate(R.id.action_homeFragment_to_resultFragment2, bundle);
        index = 0;
        timer = null;
    }

    private void closeApplication(View view) {
        final NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_resultFragment_to_homeFragment2);
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build();
        navController.navigate(R.id.action_resultFragment_to_homeFragment2, null, navOptions);


    }

    private void showQuitDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.quit_quiz).
                setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        closeApplication(view);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    private void showQuitDialogForBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.close_app).
                setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        getActivity().finishAndRemoveTask();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
    private void showNoInternetDialogue(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.no_internet).
                setCancelable(false)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ObserverLocalData(view);
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        showQuitDialogForBackPressed();
//                    }
//                });

        builder.show();
    }

}