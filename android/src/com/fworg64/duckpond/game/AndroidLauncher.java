package com.fworg64.duckpond.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {

    private AdView adView;
    DuckPondGame game;
    DuckPondGame.DuckPondGameAdStateListener adStateListener;

    boolean showAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        game = new DuckPondGame();

		RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(game, config);
        layout.addView(gameView);

        showAd = true;
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(adView, adParams);

        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
                if (showAd)
                {
                    adView.bringToFront();
                }
                else Log.i("Ads", "Loaded but not shown");

            }
        });

        setContentView(layout);

        adStateListener = new DuckPondGame.DuckPondGameAdStateListener() {
            private boolean adIsShown = false;
            //AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addTestDevice("5CDB4729D058AB52762E6860D99F5C8E"); //poor turbo
            AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addTestDevice("2B6AEF8CC87F54F168BF638279B53CFD");

            @Override
            public void ShowBannerAd() {
                showAd = true;
                if (!adIsShown && showAd) {
                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            adView.loadAd(adRequestBuilder.build());
                        }
                    });

                }
                adIsShown = true;
            }

            @Override
            public void HideBannerAd() {
                if (adIsShown) {
                    runOnUiThread(new Runnable() //run on ui thread
                    {
                        public void run() {
                            adView.destroy();
                        }
                    });
                    adIsShown = false;
                }
                showAd = false; //prevent ad from showing on load
            }
        };

        game.setAdListener(adStateListener);


		//initialize(new DuckPondGame(), config);
	}


    @Override
    public void onResume() {
        super.onResume();

        // Resume the AdView.
        adView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        adView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        adView.destroy();

        super.onDestroy();
    }
}
