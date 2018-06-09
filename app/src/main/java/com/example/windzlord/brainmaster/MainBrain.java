package com.example.windzlord.brainmaster;

import android.app.Application;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.windzlord.brainmaster.managers.ManagerBrain;
import com.example.windzlord.brainmaster.managers.ManagerFile;
import com.example.windzlord.brainmaster.managers.ManagerGameData;
import com.example.windzlord.brainmaster.managers.ManagerNetwork;
import com.example.windzlord.brainmaster.managers.ManagerPreference;
import com.example.windzlord.brainmaster.managers.ManagerServer;
import com.example.windzlord.brainmaster.managers.ManagerUserData;
import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by WindzLord on 12/27/2016.
 */

public class MainBrain extends Application {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        settingThingsUp();
    }

    private void settingThingsUp() {
        ManagerPreference.init(this);
        ManagerGameData.init(this);
        ManagerNetwork.init(this);
        ManagerServer.init(this);
        ManagerUserData.init(this);
        ManagerFile.init(this);
        FacebookSdk.sdkInitialize(this);
        initImageLoader();

        String userID = ManagerPreference.getInstance().getUserID();
        ManagerServer.getInstance().uploadLocalToServer(userID);

        goLoopSync(ManagerBrain.GAME_LOOPER_SYNC);
    }

    private void goLoopSync(boolean loop) {
        new CountDownTimer(60200, 1000) {
            @Override
            public void onTick(long l) {
                Log.d(TAG, l / 1000 + "");
            }

            @Override
            public void onFinish() {
                String userID = ManagerPreference.getInstance().getUserID();
                ManagerServer.getInstance().uploadLocalToServer(userID);
                if (loop) goLoopSync(loop);
            }
        }.start();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
