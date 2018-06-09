package com.example.windzlord.brainmaster.screens.types;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.windzlord.brainmaster.R;
import com.example.windzlord.brainmaster.adapters.CountDownTimerAdapter;
import com.example.windzlord.brainmaster.layout.GameCoverLayout;
import com.example.windzlord.brainmaster.managers.ManagerBrain;
import com.example.windzlord.brainmaster.managers.ManagerPreference;
import com.example.windzlord.brainmaster.objects.FragmentChanger;
import com.example.windzlord.brainmaster.screens.games.memory.MemoryThree;
import com.example.windzlord.brainmaster.screens.games.memory.MemoryOne;
import com.example.windzlord.brainmaster.screens.games.memory.MemoryTwo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMemory extends Fragment {

    @BindView(R.id.game_memory_one)
    GameCoverLayout gameMemoryOne;

    @BindView(R.id.game_memory_two)
    GameCoverLayout gameMemoryTwo;

    @BindView(R.id.game_memory_three)
    GameCoverLayout gameMemoryThree;


    public FragmentMemory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.game_zipe_memory, container, false);
        settingThingsUp(view);

        return view;
    }

    private void settingThingsUp(View view) {
        ButterKnife.bind(this, view);

        new CountDownTimerAdapter(10) {
            public void onFinish() {
                getInfo();
            }
        }.start();
    }

    private void getInfo() {
        GameCoverLayout[] games = {gameMemoryOne, gameMemoryTwo, gameMemoryThree};
        for (int i = 0; i < games.length; i++) {
            games[i].setScore(ManagerPreference.getInstance().getScore(ManagerBrain.MEMORY, i + 1));
            games[i].setLevel(ManagerPreference.getInstance().getLevel(ManagerBrain.MEMORY, i + 1));
            games[i].setExpNextLvl(ManagerPreference.getInstance().getExpNext(ManagerBrain.MEMORY, i + 1));
            games[i].setExpCurrent(ManagerPreference.getInstance().getExpCurrent(ManagerBrain.MEMORY, i + 1));
            games[i].setUnlocked(ManagerPreference.getInstance().isUnlocked(ManagerBrain.MEMORY, i + 1));
        }
    }

    @OnClick(R.id.button_back)
    public void onBackPressed() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.game_memory_one)
    public void goGameMemoryOne() {
        if (gameMemoryOne.isUnlocked()) {
            EventBus.getDefault().post(new FragmentChanger(new MemoryOne(), true));
        }
    }

    @OnClick(R.id.game_memory_two)
    public void goGameMemoryTwo() {
        if (gameMemoryTwo.isUnlocked())
            EventBus.getDefault().post(new FragmentChanger(new MemoryTwo(), true));
    }

    @OnClick(R.id.game_memory_three)
    public void goGameMemoryThree() {
        if (gameMemoryThree.isUnlocked())
            EventBus.getDefault().post(new FragmentChanger(new MemoryThree(), true));
    }

}
