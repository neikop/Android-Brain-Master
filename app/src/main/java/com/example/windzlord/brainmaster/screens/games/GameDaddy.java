package com.example.windzlord.brainmaster.screens.games;


import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.windzlord.brainmaster.R;
import com.example.windzlord.brainmaster.adapters.AnimationAdapter;
import com.example.windzlord.brainmaster.adapters.CountDownTimerAdapter;
import com.example.windzlord.brainmaster.layout.GameStatusLayout;
import com.example.windzlord.brainmaster.managers.ManagerBrain;
import com.example.windzlord.brainmaster.managers.ManagerNetwork;
import com.example.windzlord.brainmaster.managers.ManagerPreference;
import com.example.windzlord.brainmaster.managers.ManagerServer;
import com.example.windzlord.brainmaster.managers.ManagerUserData;


import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class GameDaddy extends Fragment {

    @BindView(R.id.layout_welcome)
    protected ViewGroup layoutWelcome;

    @BindView(R.id.textView_welcome)
    protected TextView textViewWelcome;

    @BindView(R.id.view_welcome_circle)
    protected View viewWelcomeCircle;

    @BindView(R.id.view_welcome_left)
    protected View viewWelcomeLeft;

    @BindView(R.id.view_welcome_right)
    protected View viewWelcomeRight;

    @BindView(R.id.layout_next)
    protected ViewGroup layoutNext;

    @BindView(R.id.imageView_next)
    protected ImageView imageViewNext;

    @BindView(R.id.layout_pause)
    protected ViewGroup layoutPause;

    @BindView(R.id.view_pause_right)
    protected View viewPauseRight;

    @BindView(R.id.view_pause_left)
    protected View viewPauseLeft;

    @BindView(R.id.textView_pause)
    protected TextView textViewPause;

    @BindView(R.id.button_back)
    protected CircleButton buttonBack;

    @BindView(R.id.button_resume)
    protected CircleButton buttonResume;

    @BindView(R.id.button_exit)
    protected CircleButton buttonExit;

    @BindView(R.id.layout_game_status)
    protected GameStatusLayout gameStatusLayout;

    @BindView(R.id.layout_score)
    protected ViewGroup layoutScore;

    @BindView(R.id.imageView_score)
    protected ImageView imageViewScore;

    @BindView(R.id.textView_score)
    protected TextView textViewScore;

    @BindView(R.id.textView_bonus)
    protected TextView textViewBonus;

    @BindView(R.id.layout_game)
    protected ViewGroup layoutGame;

    protected boolean onShowing = false;
    protected boolean isPauseOnShowing;

    protected CountDownTimer counter;
    protected long remain;
    protected boolean canPause = false;
    protected boolean canResume = false;
    protected boolean clickable = false;
    protected int going;
    protected int score;

    protected int RATE = 1;
    protected int TIME = ManagerBrain.TIME * RATE;
    protected int QUIZ = ManagerBrain.QUIZ;

    protected View createView(View view) {
        ButterKnife.bind(this, view);
        startGame();
        addListeners();

        return view;
    }

    protected void startGame() {
        gameStatusLayout.updateValues(100, TIME, TIME, 0, QUIZ, 0);
        going = score = 0;

        goStartAnimation();
    }

    protected void addListeners() {
        gameStatusLayout.setPauseListener(v -> goPause());
    }

    protected void goStartAnimation() {
        ScaleAnimation countOne = new ScaleAnimation(1, 1.3f, 1, 1.3f, 1, 0.5f, 1, 0.5f);
        ScaleAnimation countTwo = new ScaleAnimation(1, 1.3f, 1, 1.3f, 1, 0.5f, 1, 0.5f);
        ScaleAnimation countThree = new ScaleAnimation(1, 1.3f, 1, 1.3f, 1, 0.5f, 1, 0.5f);
        countThree.setDuration(200);
        countThree.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                textViewWelcome.setText("1");
                new CountDownTimerAdapter(800) {
                    public void onFinish() {
                        ScaleAnimation scale = new ScaleAnimation(1, 0, 1, 0, 1, 0.5f, 1, 0.5f);
                        scale.setDuration(400);
                        scale.setAnimationListener(new AnimationAdapter() {
                            public void onAnimationEnd(Animation animation) {
                                layoutWelcome.setVisibility(View.INVISIBLE);
                                canPause = true;
                                goPrepare();
                            }
                        });
                        viewWelcomeCircle.startAnimation(scale);

                        TranslateAnimation goRight = new TranslateAnimation(1, 0, 1, 1, 1, 0, 1, 0);
                        goRight.setDuration(250);
                        goRight.setFillAfter(true);
                        TranslateAnimation goLeft = new TranslateAnimation(1, 0, 1, -1, 1, 0, 1, 0);
                        goLeft.setDuration(250);
                        goLeft.setFillAfter(true);
                        viewWelcomeLeft.startAnimation(goLeft);
                        viewWelcomeRight.startAnimation(goRight);
                    }
                }.start();
            }
        });
        countTwo.setDuration(200);
        countTwo.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                textViewWelcome.setText("2");
                new CountDownTimerAdapter(800) {
                    public void onFinish() {
                        textViewWelcome.startAnimation(countThree);
                    }
                }.start();
            }
        });
        countOne.setDuration(200);
        countOne.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                textViewWelcome.setText("3");
                new CountDownTimerAdapter(800) {
                    public void onFinish() {
                        textViewWelcome.startAnimation(countTwo);
                    }
                }.start();
            }
        });
        textViewWelcome.setText("  ");
        new CountDownTimerAdapter(300) {
            public void onFinish() {
                ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, 1, 0.5f, 1, 0.5f);
                scale.setDuration(400);
                scale.setAnimationListener(new AnimationAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        textViewWelcome.startAnimation(countOne);
                        new CountDownTimerAdapter(100) {
                            public void onFinish() {
                                textViewWelcome.setText("3");
                            }
                        }.start();
                    }
                });
                layoutWelcome.setVisibility(View.VISIBLE);
                goVisibility(View.INVISIBLE, viewWelcomeLeft, viewWelcomeRight);
                viewWelcomeCircle.startAnimation(scale);

                TranslateAnimation goRight = new TranslateAnimation(1, -1, 1, 0, 1, 0, 1, 0);
                goRight.setDuration(250);
                TranslateAnimation goLeft = new TranslateAnimation(1, 1, 1, 0, 1, 0, 1, 0);
                goLeft.setDuration(250);
                new CountDownTimerAdapter(150) {
                    public void onFinish() {
                        goVisibility(View.VISIBLE, viewWelcomeLeft, viewWelcomeRight);
                        viewWelcomeLeft.startAnimation(goRight);
                        viewWelcomeRight.startAnimation(goLeft);
                    }
                }.start();
            }
        }.start();
    }

    protected abstract void goPrepare();

    protected abstract void prepareQuiz();

    protected abstract void goShow();

    protected abstract void showQuiz();

    protected void goNext(boolean completed) {
        clickable = false;
        counter.cancel();
        new CountDownTimerAdapter(300) {
            public void onFinish() {
                imageViewNext.setImageResource(completed ?
                        R.drawable.game_ic_color_correct : R.drawable.game_ic_color_cross);
                layoutNext.setVisibility(View.VISIBLE);

                ScaleAnimation scaleOne = new ScaleAnimation(0, 1, 0, 1, 1, 0.5f, 1, 0.5f);
                scaleOne.setDuration(250);
                layoutNext.startAnimation(scaleOne);

                if (ManagerPreference.getInstance().getSound())
                    if (completed)
                        ManagerBrain.goMusic(getActivity(), ManagerBrain.SOUND_CORRECT, false);
                    else ManagerBrain.goMusic(getActivity(), ManagerBrain.SOUND_WRONG, false);
            }
        }.start();
        new CountDownTimerAdapter(1000) {
            public void onFinish() {
                ScaleAnimation scaleTwo = new ScaleAnimation(1, 0, 1, 0, 1, 0.5f, 1, 0.5f);
                scaleTwo.setDuration(250);
                scaleTwo.setAnimationListener(new AnimationAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        layoutNext.setVisibility(View.INVISIBLE);
                    }
                });
                layoutNext.startAnimation(scaleTwo);
            }
        }.start();
        nextQuiz(completed);
    }

    protected void nextQuiz(boolean completed) {
        new CountDownTimerAdapter(1000) {
            public void onFinish() {
                int bonus = completed ? (gameStatusLayout.getTimeCount() + 9) / 10 : 0;
                score += bonus;
                textViewBonus.setText("+" + bonus);
                textViewBonus.setVisibility(View.VISIBLE);
                TranslateAnimation goUp = new TranslateAnimation(
                        1, 0, 1, 0, 1, 0, 1, -1);
                goUp.setDuration(500);
                goUp.setAnimationListener(new AnimationAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        textViewBonus.setVisibility(View.INVISIBLE);
                        textViewScore.setText("" + score);
                    }
                });
                textViewBonus.startAnimation(goUp);
                goPrepare();
            }
        }.start();
    }

    protected void goPause() {
        if (!canPause) return;
        boolean isPausing = layoutPause.getVisibility() == View.VISIBLE;
        if (isPausing) return;
        canResume = true;
        isPauseOnShowing = onShowing;
        layoutGame.setVisibility(View.INVISIBLE);
        layoutPause.setVisibility(View.VISIBLE);
        if (counter != null) counter.cancel();
        goPauseAnimation();
    }

    protected void goPauseAnimation() {
        TranslateAnimation goRight = new TranslateAnimation(1, -1, 1, 0, 1, 0, 1, 0);
        goRight.setDuration(150);
        TranslateAnimation goLeft = new TranslateAnimation(1, 1, 1, 0, 1, 0, 1, 0);
        goLeft.setDuration(150);
        AlphaAnimation goFadeShow = new AlphaAnimation(0, 1);
        goFadeShow.setDuration(300);
        goRight.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                goVisibility(View.VISIBLE, textViewPause, buttonBack, buttonResume);
                goStartAnimation(goFadeShow, textViewPause, buttonBack, buttonResume);
            }
        });
        goVisibility(View.INVISIBLE, textViewPause, buttonBack, buttonResume, buttonExit);
        viewPauseLeft.startAnimation(goRight);
        viewPauseRight.startAnimation(goLeft);
    }

    @OnClick(R.id.button_back)
    protected void onButtonBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.button_resume)
    protected void onButtonResume() {
        if (!canResume) return;
        canResume = false;
        TranslateAnimation goLeft = new TranslateAnimation(1, 0, 1, -1, 1, 0, 1, 0);
        goLeft.setDuration(150);
        TranslateAnimation goRight = new TranslateAnimation(1, 0, 1, 1, 1, 0, 1, 0);
        goRight.setDuration(150);
        AlphaAnimation goFadeHide = new AlphaAnimation(1, 0);
        goFadeHide.setDuration(300);
        goFadeHide.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                goVisibility(View.INVISIBLE, textViewPause, buttonBack, buttonResume);
                viewPauseLeft.startAnimation(goLeft);
                viewPauseRight.startAnimation(goRight);
            }
        });
        goRight.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                layoutPause.setVisibility(View.INVISIBLE);
                layoutGame.setVisibility(View.VISIBLE);
                AlphaAnimation goFadeShow = new AlphaAnimation(0, 1);
                goFadeShow.setDuration(300);
                goFadeShow.setAnimationListener(new AnimationAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        if (clickable & !isPauseOnShowing) goResume();
                    }
                });
                layoutGame.startAnimation(goFadeShow);
            }
        });
        goStartAnimation(goFadeHide, textViewPause, buttonBack, buttonResume);
    }

    protected void goResume() {
        counter = new CountDownTimer(remain, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                remain = millisUntilFinished;
                gameStatusLayout.setTimeProgress((int) millisUntilFinished);
                gameStatusLayout.setTimeCount((int) (millisUntilFinished / (50 * RATE)));
            }

            @Override
            public void onFinish() {
                clickable = false;
                new CountDownTimerAdapter(1000) {
                    public void onFinish() {
                        textViewBonus.setText("+0");
                        textViewBonus.setVisibility(View.VISIBLE);
                        TranslateAnimation goUp = new TranslateAnimation(
                                1, 0, 1, 0, 1, 0, 1, -1);
                        goUp.setDuration(500);
                        goUp.setAnimationListener(new AnimationAdapter() {
                            public void onAnimationEnd(Animation animation) {
                                textViewBonus.setVisibility(View.INVISIBLE);
                            }
                        });
                        textViewBonus.startAnimation(goUp);
                        goPrepare();
                    }
                }.start();
            }
        }.start();
    }

    protected void goEndGame(String name, int index) {
        canPause = false;
        layoutGame.setVisibility(View.INVISIBLE);
        layoutPause.setVisibility(View.VISIBLE);
        updateInfo(name, index);
    }

    protected void updateInfo(String name, int position) {
        goEndAnimation(score > ManagerPreference.getInstance().getScore(name, position));

        int level = ManagerPreference.getInstance().getLevel(name, position);
        int expNext = ManagerPreference.getInstance().getExpNext(name, position);
        int expCurrent = ManagerPreference.getInstance().getExpCurrent(name, position);
        String userID = ManagerPreference.getInstance().getUserID();
        String userImage = ManagerPreference.getInstance().getUserImage();

        expCurrent += score;
        boolean levelUp = expCurrent >= expNext;
        expCurrent = expCurrent - (levelUp ? expNext : 0);
        level += levelUp ? 1 : 0;

        // Update Preference
        ManagerPreference.getInstance().putLevel(name, position, level);
        ManagerPreference.getInstance().putExpCurrent(name, position, expCurrent);
        ManagerPreference.getInstance().putScore(name, position,
                Math.max(score, ManagerPreference.getInstance().getScore(name, position)));

        // Update Database local
        ManagerUserData.getInstance().updateScore(
                userID, userImage, name, position, level, expCurrent,
                ManagerPreference.getInstance().getScore(name, position));

        // Upload to server
        if (ManagerNetwork.getInstance().isConnectedToInternet())
            if (!userID.isEmpty()) ManagerServer.getInstance().uploadSingleScore(
                    ManagerUserData.getInstance().getScoreByInfo(userID, name, position));
    }

    protected void goEndAnimation(boolean getHigh) {
        TranslateAnimation goRight = new TranslateAnimation(1, -1, 1, 0, 1, 0, 1, 0);
        goRight.setDuration(150);
        TranslateAnimation goLeft = new TranslateAnimation(1, 1, 1, 0, 1, 0, 1, 0);
        goLeft.setDuration(150);
        AlphaAnimation goFadeShow = new AlphaAnimation(0, 1);
        goFadeShow.setDuration(300);
        goRight.setAnimationListener(new AnimationAdapter() {
            public void onAnimationEnd(Animation animation) {
                goVisibility(View.VISIBLE, textViewPause, buttonExit);
                goStartAnimation(goFadeShow, textViewPause, buttonExit);
                if (getHigh) {
                    ScaleAnimation scale = new ScaleAnimation(1, 1.3f, 1, 1.3f, 1, 0.5f, 1, 0.5f);
                    scale.setDuration(300);
                    scale.setFillAfter(true);
                    layoutScore.startAnimation(scale);
                    goHighScoreColor();
                }
            }
        });
        textViewPause.setText("GAME  END");
        goVisibility(View.INVISIBLE, textViewPause, buttonExit, buttonBack, buttonResume);
        viewPauseLeft.startAnimation(goRight);
        viewPauseRight.startAnimation(goLeft);
    }

    protected void goHighScoreColor() {
        imageViewScore.setImageResource(R.color.colorOrangeLight);
        textViewScore.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @OnClick(R.id.button_exit)
    protected void onButtonExit() {
        getActivity().onBackPressed();
    }

    protected void goStartAnimation(Animation animation, View... views) {
        for (View view : views) view.startAnimation(animation);
    }

    protected void goStartAnimation(Animation animation, List<View> views) {
        for (View view : views) view.startAnimation(animation);
    }

    protected void goVisibility(int visibility, View... views) {
        for (View view : views) view.setVisibility(visibility);
    }

    protected List<View> goChildGroup(ViewGroup group) {
        List<View> ret = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) ret.add(group.getChildAt(i));
        return ret;
    }

    protected void goImageResource(View view, @DrawableRes int res) {
        ((ImageView) view).setImageResource(res);
    }
}
