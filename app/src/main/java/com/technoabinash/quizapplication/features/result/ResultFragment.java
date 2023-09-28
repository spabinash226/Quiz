package com.technoabinash.quizapplication.features.result;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.technoabinash.quizapplication.R;


public class ResultFragment extends Fragment {
    private String UserScore = "";
    public ResultFragment() {
        // Required empty public constructor
    }
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeApplicationOnBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView score = view.findViewById(R.id.score);

        assert getArguments() != null;
        score.setText(getArguments().getString("score", ""));

        Button quitBtn = view.findViewById(R.id.quitBtn);
        Button restartBtn = view.findViewById(R.id.reStartBtn);
        quitBtn.setOnClickListener(view1 -> {
            showQuitDialog(view);

        });

        restartBtn.setOnClickListener(view1 -> {
            // restart the quiz
            openHomeFragment(view);
        });
    }



    private void openHomeFragment(View view){
        final NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_resultFragment_to_homeFragment2);
        navController.popBackStack();
    }

    private void closeApplication(View view){
        final NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_resultFragment_to_homeFragment2);
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
        navController.navigate(R.id.action_resultFragment_to_homeFragment2,null,navOptions);

    }


    private void showQuitDialog(View view){
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


    private void closeApplicationOnBackPressed(){

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showQuitDialogForBackPressed();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);

    }


    private void showQuitDialogForBackPressed(){
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
}