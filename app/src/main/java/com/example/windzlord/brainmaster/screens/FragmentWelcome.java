package com.example.windzlord.brainmaster.screens;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.windzlord.brainmaster.R;
import com.example.windzlord.brainmaster.managers.ManagerBrain;
import com.example.windzlord.brainmaster.managers.ManagerPreference;
import com.example.windzlord.brainmaster.objects.FragmentChanger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWelcome extends Fragment {

    @BindView(R.id.progressBar_welcome)
    SeekBar progressBarWelcome;

    @BindView(R.id.imageView_welcome)
    ImageView imageViewWelcome;

    public FragmentWelcome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        settingThingsUp(view);

        return view;
    }

    private void settingThingsUp(View view) {
        ButterKnife.bind(this, view);

        getContent();
    }

    private void getContent() {
        if (ManagerPreference.getInstance().getMusic())
            ManagerBrain.goMusic(getActivity(), ManagerBrain.SOUND_WELCOME, false);
        progressBarWelcome.setMax(3000);
        progressBarWelcome.setMax(2800);
        progressBarWelcome.setProgress(0);
        new CountDownTimer(3200, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                progressBarWelcome.setProgress((int) (3000 - millisUntilFinished));
            }

            @Override
            public void onFinish() {
                imageViewWelcome.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new FragmentChanger(new FragmentMain(), false));
            }
        }.start();
    }
}
