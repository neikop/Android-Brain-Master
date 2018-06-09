package com.example.windzlord.brainmaster.screens.games.concentration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.windzlord.brainmaster.R;
import com.example.windzlord.brainmaster.screens.games.GameDaddy;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConcenThree extends GameDaddy {


    public ConcenThree() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.game_concen_three, container, false);
        ButterKnife.bind(this, view);
        addListeners();
        canPause = true;
        return view;
    }

    @Override
    protected void goPrepare() {

    }

    @Override
    protected void prepareQuiz() {

    }

    @Override
    protected void goShow() {

    }

    @Override
    protected void showQuiz() {

    }
}
