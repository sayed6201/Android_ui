        
================================================================================================================
SplashActivity.java
================================================================================================================    
        View mSplashImage = findViewById(R.id.splash);
        TextView mSplashText = findViewById(R.id.splashText);
        //snimstion
        Animation splashAnimImage = AnimationUtils.loadAnimation(this, R.anim.splash_anim_img);
        Animation splashAnimText = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        //animation set..
        mSplashText.startAnimation(splashAnimText);
        mSplashImage.startAnimation(splashAnimImage);
        
        //snimstion event handler
        splashAnimImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (sessionManager.isLoggedIn()) {
                    {
                        //new Handler().postDelayed(this::gotoMainActivity, 1000);
                        new myAssyctask().execute();
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


================================================================================================================
    splash_anim_img.xml
================================================================================================================
        <?xml version="1.0" encoding="utf-8"?>
        <set xmlns:android="http://schemas.android.com/apk/res/android">
            <translate android:duration="2000" android:fromXDelta="-100%" android:toXDelta="0%" />
            <alpha
                android:interpolator="@android:anim/accelerate_interpolator"
                android:duration="2000" android:fromAlpha="0.0" android:toAlpha="1.0" />
        </set>

================================================================================================================
    splash_anim.xml
================================================================================================================
        <?xml version="1.0" encoding="utf-8"?>
        <set xmlns:android="http://schemas.android.com/apk/res/android">
            <translate
                android:fromXDelta="100%" android:fromYDelta="0%"
                android:toXDelta="0%" android:toYDelta="0%"
                android:duration="2000"/>
            <alpha
                android:interpolator="@android:anim/accelerate_interpolator"
                android:duration="2000" android:fromAlpha="0.0" android:toAlpha="1.0" />
        </set>
