package kr.clude.yearnning.smartdorm;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements MainNavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private MainNavigationDrawerFragment mMainNavigationDrawerFragment;
    public View mActionbarView;

    /**
     *
     */
    private enum ActionBarState {
        SHOWEN, HIDDEN, SHOWING, HIDING
    }

    private ActionBarState mActionBarState = ActionBarState.SHOWEN;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    //private CharSequence mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /**
         *
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionbarView = findViewById(R.id.toolbar_container);

        /**
         *
         */
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setLogo(R.drawable.ic_launcher);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_HOME_AS_UP);

        /**
         *
         */
        mMainNavigationDrawerFragment = (MainNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // mTitle = getTitle();

        // Set up the drawer.
        mMainNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {

        /*
        if (number != 0)
            Toast.makeText(MainActivity.this, "준비중인 기능입니다.", Toast.LENGTH_SHORT).show();
*/

        /*
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }*/
    }

    public void restoreActionBar() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mMainNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    /**
     *
     */
    public void hideActionbar() {
        if (mActionBarState == ActionBarState.SHOWEN) {
            mActionBarState = ActionBarState.HIDING;
            final Animation mActionbarHideAnimation = AnimationUtils.loadAnimation(this, R.anim.base_actionbar_hide);
            mActionbarView.startAnimation(mActionbarHideAnimation);
            mActionBarState = ActionBarState.HIDDEN;
        }
    }


    /**
     *
     */
    public void showActionbar() {
        if (mActionBarState == ActionBarState.HIDDEN) {
            mActionBarState = ActionBarState.SHOWING;
            final Animation mActionbarShowAnimation = AnimationUtils.loadAnimation(this, R.anim.base_actionbar_show);
            mActionbarView.startAnimation(mActionbarShowAnimation);
            mActionBarState = ActionBarState.SHOWEN;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mMainNavigationDrawerFragment.toggleDrawer();
        }
        return super.onKeyDown(keyCode, event);
    }


    private boolean doubleBackToExitPressedOnce = false;

    /**
     * 뒤로가기 두번
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르시면, \"돔\"에서 빠져나갑니다.", Toast.LENGTH_SHORT)
                .show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);
    }


}
