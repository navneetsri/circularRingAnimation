package com.example.navneetsrivastava.sampleholocircle;

import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.navneetsrivastava.sampleholocircle.HoloCircularProgressBar;

/**
 * The Class CircularProgressBarSample.
 *
 * @author Pascal Welsch
 * @since 05.03.2013
 */
public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();

    /**
     * The Switch button.
     */
    private Button mColorSwitchButton;

    private HoloCircularProgressBar mHoloCircularProgressBar;
    private ObjectAnimator mProgressBarAnimator;
    protected boolean mAnimationHasEnded = false;
    private Button mZero;
    private Button mOne;
    private Switch mAutoAnimateSwitch;
    private EditText edtValueToCalculate;
    private TextView txtCounter;
    private String value="";
    private Integer i = 0;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getIntent() != null) {
            final Bundle extras = getIntent().getExtras();
            if (extras != null) {
                final int theme = extras.getInt("theme");
                if (theme != 0) {
                    setTheme(theme);
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mHoloCircularProgressBar = (HoloCircularProgressBar) findViewById(R.id.holoCircularProgressBar1);

        mColorSwitchButton = (Button) findViewById(R.id.random_color);
        mColorSwitchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                switchColor();
            }
        });

        mZero = (Button) findViewById(R.id.zero);
        mZero.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mProgressBarAnimator != null) {
                    mProgressBarAnimator.cancel();
                }
                animate(mHoloCircularProgressBar, null, 0f, 1000);
                mHoloCircularProgressBar.setMarkerProgress(0f);

            }
        });

        mOne = (Button) findViewById(R.id.one);
        edtValueToCalculate = (EditText)findViewById(R.id.edt_value);
        txtCounter = (TextView)findViewById(R.id.txtview1);

        mOne.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mProgressBarAnimator != null) {
                    mProgressBarAnimator.cancel();
                }
                value = edtValueToCalculate.getText().toString().trim();
                Float floatValue = Float.parseFloat(value)/100;
                if(Float.parseFloat(value)==0)
                {
                    showAlertMassageOnBack(R.string.warning,R.string.alert_message_empty_edtxt);
                }
                else if(Float.parseFloat(value)>100)
                {
                    showAlertMassageOnBack(R.string.warning,
                            R.string.alert_message);
                }
                else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(i=1; i < Integer.parseInt(value)+1; i++)
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        txtCounter.setText(String.valueOf(i)+"%");
                                    }
                                });
                                try {
                                    int threadTime = 3000/(Integer.parseInt(value));
                                    Thread.sleep(threadTime);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }}
                    }).start();



                    animate(mHoloCircularProgressBar, null, floatValue, 3000); //1507 to change string into float
                        // animate(mHoloCircularProgressBar, null, .5f, 3000); //1507 1f,1000
                        mHoloCircularProgressBar.setMarkerProgress(floatValue); //1507 1f
                        mHoloCircularProgressBar.setProgressColor(getResources().getColor(R.color.pGreen));

                }

            }
        });

        mAutoAnimateSwitch = (Switch) findViewById(R.id.auto_animate_switch);
        mAutoAnimateSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mOne.setEnabled(false);
                    mZero.setEnabled(false);

                    animate(mHoloCircularProgressBar, new AnimatorListener() {

                        @Override
                        public void onAnimationCancel(final Animator animation) {
                            animation.end();
                        }

                        @Override
                        public void onAnimationEnd(final Animator animation) {
                            if (!mAnimationHasEnded) {
                                animate(mHoloCircularProgressBar, this);
                            } else {
                                mAnimationHasEnded = false;
                            }
                        }

                        @Override
                        public void onAnimationRepeat(final Animator animation) {
                        }

                        @Override
                        public void onAnimationStart(final Animator animation) {
                        }
                    });
                } else {
                    mAnimationHasEnded = true;
                    mProgressBarAnimator.cancel();

                    mOne.setEnabled(true);
                    mZero.setEnabled(true);
                }

            }
        });

    }

    /**
     * generates random colors for the ProgressBar
     */
    protected void switchColor() {
        Random r = new Random();
        int randomColor = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        mHoloCircularProgressBar.setProgressColor(randomColor);

        randomColor = Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        mHoloCircularProgressBar.setProgressBackgroundColor(randomColor);
    }

    /**
     * Animate.
     *
     * @param progressBar
     *            the progress bar
     * @param listener
     *            the listener
     */
    private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener) {
        final float progress = (float) (Math.random() * 2);
        int duration = 3000;
        animate(progressBar, listener, progress, duration);
    }

    private void animate(final HoloCircularProgressBar progressBar, final AnimatorListener listener,
                         final float progress, final int duration) {

        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);

        mProgressBarAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {
            }
        });
        if (listener != null) {
            mProgressBarAnimator.addListener(listener);
        }
        mProgressBarAnimator.reverse();
        mProgressBarAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(progress);
        mProgressBarAnimator.start();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.circular_progress_bar_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_switch_theme:
                switchTheme(); //1607 blocked the theme switching from one to another.
                break;

            default:
                Log.w(TAG, "couldn't map a click action for " + item);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Switch theme.
     */
    public void switchTheme() {

        final Intent intent = getIntent();
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final int theme = extras.getInt("theme", -1);
            if (theme == R.style.AppThemeLight) {
                getIntent().removeExtra("theme");
            } else {
                intent.putExtra("theme", R.style.AppThemeLight);
            }
        } else {
            intent.putExtra("theme", R.style.AppThemeLight);
        }
        finish();
        startActivity(intent);
    }

    public void showAlertMassageOnBack(int titleHeader, int message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyActivity.this);

        // set title
        alertDialogBuilder.setTitle(titleHeader);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                dialog.cancel();

                            }
                        });



        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}