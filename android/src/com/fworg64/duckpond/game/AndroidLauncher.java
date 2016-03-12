package com.fworg64.duckpond.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.fworg64.duckpond.game.DuckPondGame;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {

    private AdView adView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new DuckPondGame(), config);
        layout.addView(gameView);

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(adView, adParams);

        adView.loadAd(adRequestBuilder.build());

        setContentView(layout);

        adView.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                Log.i("Ads", "onAdLoaded");
                adView.bringToFront();
            }
        });


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
