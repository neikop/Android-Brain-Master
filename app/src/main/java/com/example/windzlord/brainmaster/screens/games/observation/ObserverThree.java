package com.example.windzlord.brainmaster.screens.games.observation;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windzlord.brainmaster.R;
import com.example.windzlord.brainmaster.adapters.AnimationAdapter;
import com.example.windzlord.brainmaster.adapters.CountDownTimerAdapter;
import com.example.windzlord.brainmaster.managers.ManagerBrain;
import com.example.windzlord.brainmaster.screens.games.GameDaddy;

import java.util.Random;

import at.markushi.ui.CircleButton;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObserverThree extends GameDaddy {

    @BindView(R.id.layout_answer)
    ViewGroup layoutAnswer;

    @BindView(R.id.layout_a)
    ViewGroup layoutA;

    @BindView(R.id.button_a)
    CircleButton buttonA;

    @BindView(R.id.textView_answer_a)
    TextView answerA;

    @BindView(R.id.layout_b)
    ViewGroup layoutB;

    @BindView(R.id.button_b)
    CircleButton buttonB;

    @BindView(R.id.textView_answer_b)
    TextView answerB;

    @BindView(R.id.layout_c)
    ViewGroup layoutC;

    @BindView(R.id.button_c)
    CircleButton buttonC;

    @BindView(R.id.textView_answer_c)
    TextView answerC;

    @BindView(R.id.layout_d)
    ViewGroup layoutD;

    @BindView(R.id.button_d)
    CircleButton buttonD;

    @BindView(R.id.textView_answer_d)
    TextView answerD;

    private CircleButton[] buttons;
    private ViewGroup[] layouts;

    private int srcHided = R.drawable.ic_type_concentration;
    private int srcMarked = R.drawable.game_iz_resource_0z;
    private int srcNormal = R.color.colorFaded;
    private int bgrCorrect = R.drawable.custom_oval_background_outline_correct;
    private int bgrWrong = R.drawable.custom_oval_background_outline_wrong;
    private int bgrNormal = R.drawable.custom_oval_background_outline;

    private boolean[] coreArray;
    private int answer;
    private int[] coreArrayAnswer;

    public ObserverThree() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return createView(inflater.inflate(R.layout.game_observer_three, container, false));
    }

    @Override
    protected void startGame() {
        super.startGame();
        buttons = new CircleButton[]{buttonA, buttonB, buttonC, buttonD};
        layouts = new ViewGroup[]{layoutA, layoutB, layoutC, layoutD};
        layoutAnswer.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        for (int i = 0; i < 4; i++) {
            int clicked = i;
            buttons[i].setOnClickListener(v -> goNext(clicked));
        }
    }

    @Override
    protected void goPrepare() {
        ScaleAnimation scaleOne = new ScaleAnimation(1, 0, 1, 0, 1, 0.5f, 1, 0.5f);
        scaleOne.setDuration(250);
        ScaleAnimation scaleTwo = new ScaleAnimation(0, 1, 0, 1, 1, 0.5f, 1, 0.5f);
        scaleTwo.setDuration(250);
        scaleOne.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                for (View view : goChildGroup(layoutGame)) {
                    view.setVisibility(View.VISIBLE);
                    ((ImageView) view).setImageResource(srcHided);
                }
                for (View view : goChildGroup(layoutGame))
                    view.startAnimation(scaleTwo);
            }
        });
        scaleTwo.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                prepareQuiz();
            }
        });
        for (View view : goChildGroup(layoutGame)) view.startAnimation(scaleOne);
    }

    @Override
    protected void prepareQuiz() {
        new CountDownTimerAdapter(500) {
            public void onFinish() {
                goShow();
            }
        }.start();
    }

    @Override
    protected void goShow() {
        ScaleAnimation scaleOne = new ScaleAnimation(1, 0, 1, 0, 1, 0.5f, 1, 0.5f);
        scaleOne.setDuration(250);
        ScaleAnimation scaleTwo = new ScaleAnimation(0, 1, 0, 1, 1, 0.5f, 1, 0.5f);
        scaleTwo.setDuration(250);
        scaleOne.setAnimationListener(new AnimationAdapter() {
            public void onAnimationStart(Animation animation) {
                onShowing = true;
            }

            public void onAnimationEnd(Animation animation) {
                layoutAnswer.setVisibility(View.VISIBLE);
                for (ViewGroup group : layouts) group.startAnimation(scaleTwo);

                showQuiz();
                for (View view : goChildGroup(layoutGame)) view.startAnimation(scaleTwo);
            }
        });
        scaleTwo.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                onShowing = false;
                clickable = true;
                counter = new CountDownTimer(TIME, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        remain = millisUntilFinished;
                        gameStatusLayout.setTimeProgress((int) millisUntilFinished);
                        gameStatusLayout.setTimeCount((int) (millisUntilFinished / 50));
                    }

                    @Override
                    public void onFinish() {
                        goNext(4);
                    }
                }.start();
                going++;
                gameStatusLayout.setGoingCount(going);
                gameStatusLayout.setGoingProgress(going);
            }
        });
        if (going >= QUIZ) goEndGame(ManagerBrain.OBSERVATION, 3);
        else for (View view : goChildGroup(layoutGame)) view.startAnimation(scaleOne);
    }

    @Override
    protected void showQuiz() {
        coreArray = getCoreArray();
        answer = countTrue(coreArray);

        coreArrayAnswer = getAnswerArray(answer);
        answerA.setText("" + coreArrayAnswer[0]);
        answerB.setText("" + coreArrayAnswer[1]);
        answerC.setText("" + coreArrayAnswer[2]);
        answerD.setText("" + coreArrayAnswer[3]);

        for (int i = 0; i < 16; i++)
            ((ImageView) layoutGame.getChildAt(i)).setImageResource(
                    coreArray[i] ? srcMarked : srcNormal);
    }

    private void goNext(int clicked) {
        if (!clickable) return;
        clickable = false;
        boolean completed = answer == coreArrayAnswer[clicked];
        if (clicked < 4) buttons[clicked].setBackgroundResource(bgrWrong); // When Time Out
        buttons[coreArrayAnswer[4]].setBackgroundResource(bgrCorrect);
        new CountDownTimerAdapter(1000) {
            public void onFinish() {
                ScaleAnimation scale = new ScaleAnimation(1, 0, 1, 0, 1, 0.5f, 1, 0.5f);
                scale.setDuration(250);
                scale.setAnimationListener(new AnimationAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        layoutAnswer.setVisibility(View.INVISIBLE);
                    }
                });
                for (int i = 0; i < 4; i++) {
                    buttons[i].setBackgroundResource(bgrNormal);
                    layouts[i].startAnimation(scale);
                }
            }
        }.start();
        goNext(completed);
    }

    @Override
    protected void goPause() {
        super.goPause();
        layoutAnswer.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onButtonResume() {
        super.onButtonResume();
        new CountDownTimerAdapter(450) {
            public void onFinish() {
                layoutAnswer.setVisibility(View.VISIBLE);
                AlphaAnimation goFadeShow = new AlphaAnimation(0, 1);
                goFadeShow.setDuration(300);
                layoutAnswer.startAnimation(goFadeShow);
            }
        }.start();
    }

    private int countTrue(boolean[] array) {
        int ret = 0;
        for (boolean bool : array) if (bool) ret++;
        return ret;
    }

    private boolean[] getCoreArray() {
        boolean[] ret = new boolean[16];
        int count = 0;
        for (int i = 0; i < 16; i++) if (ret[i] = new Random().nextBoolean()) count++;
        if (count < 6 | count > 10) return getCoreArray();
        return ret;
    }

    private int[] getAnswerArray(int x) {
        switch (new Random().nextInt(4)) {
            case 1:
                return new int[]{x, x + 1, x + 2, x + 3, 0};
            case 2:
                return new int[]{x - 1, x, x + 1, x + 2, 1};
            case 3:
                return new int[]{x - 2, x - 1, x, x + 1, 2};
            default:
                return new int[]{x - 3, x - 2, x - 1, x, 3};
        }
    }
}
