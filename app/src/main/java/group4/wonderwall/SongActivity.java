package group4.wonderwall;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.os.Build;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SongActivity class contains the functionality to play song.
 * It detects user swipes on the screen and plays song.
 */
public class SongActivity extends ActionBarActivity { //implements View.OnClickListener{
    private boolean strumming;
    float x1,x2;
    Integer score = 0;
    int period = 1000; // repeat every 10 sec.
    Timer timer = new Timer();

    /**
     * Initialize the SongActivity, loads views, data binding.
     * @param savedInstanceState (If app is re-initialized after shut-down, Bundle contains most recent saved state data)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song); //places the UI for this activity here
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                beat();
            }
        }, 0,period);
    }

    /**
     * Detects a touch screen swipe. Logs a left or a right swipe.
     * @param touchevent (the touch screen event being processed)
     * @return boolean (if screen was touched or not)
     */
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                //if left to right sweep event on screen
                if (x1 < x2)
                {
                    System.out.println("Left to Right Swipe");
                }
                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    System.out.println("Right to Left Swipe");
                }
            }
        }
        return false;
    }

    /**
     * Increments the user score
     * @return boolean
     */
    public boolean incrementScore(){
        TextView updateThis = (TextView)findViewById(R.id.score);
        score++;
        updateThis.setText(score.toString());

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Call this each time the song checks for user activity
     */
    public void beat(){
        if(strumming){
            strumming = false;
            progress();
        }else{
            pause();
        }
    }

    /**
     * Called by strum action listener
     */
    public void strum(){

        strumming = true;
    }

    /**
     * Song continues playing, progress advances
     */
    public void progress(){
        //TODO implement song progression
        System.out.println("Song playing");
        incrementScore();
    }

    /**
     * Song is paused, halt progress
     */
    public void pause(){
        //TODO implement song pausing
        System.out.println("Song stopped");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * Public constructor
         */
        public PlaceholderFragment() {
        }

        /**
         * Initializes the fragment view
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_song, container, false);
            return rootView;
        }
    }

    @Override
    public void onPause(){
        super.onPause(); //Always call the super
        //pause the timer and song
        this.timer.cancel();
        this.timer = null;
        System.out.println("I have paused and you should not see the timer incrementing");

        //also need to stop the song
    }
    @Override
    public void onResume(){
        super.onResume(); //Always call the superclass first
        System.out.println("I have resumed and you should now see this counting again");
        //start the timer again since we are resuming the task
        if (this.timer == null) {
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    beat();
                }
            }, 0, period);
        }//if
    }
}
