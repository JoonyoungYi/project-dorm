package kr.clude.yearnning.smartdorm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends Activity {
    private static final String TAG = "Splash Activity";

    /*

     */
    int screen_width = -1;
    int screen_height = -1;

    /**
     *
     */
    LoginApiTask mLoginApiTask = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        /*
        final LinearLayout mWholeLayout = (LinearLayout) findViewById(R.id.whole_layout);
        ViewTreeObserver vto = mWholeLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //remove listener to ensure only one call is made.
                mWholeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int h = mWholeLayout.getHeight();
                int w = mWholeLayout.getWidth();

                //make use of height and width
                Log.d(TAG, "h -> " + h);
                Log.d(TAG, "w -> " + w);
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginApiTask = new LoginApiTask();
        mLoginApiTask.execute();
    }

    /**
     *
     */
    public class LoginApiTask extends AsyncTask<Void, Void, Void> {

        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {


            try {
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);

            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoginApiTask = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }

        super.onDestroy();
    }

}
