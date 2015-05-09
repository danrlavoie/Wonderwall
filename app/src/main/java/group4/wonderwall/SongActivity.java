package group4.wonderwall;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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

import android.widget.RelativeLayout;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import group4.wonderwall.MainActivity;
import group4.wonderwall.MusicController;
import group4.wonderwall.MusicService;
import group4.wonderwall.R;

/**
 * SongActivity class contains the functionality to play song.
 * It detects user swipes on the screen and plays song.
 */
public class SongActivity extends ActionBarActivity { //implements View.OnClickListener{
    private boolean strumming;
    float x1,x2;
    Integer score = 0;
    int combo = 0;
    int period = 1000; // repeat every 10 sec.
    Timer timer = new Timer();
    private boolean mIsBound = false;
    private boolean mIsPlaying = true;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            System.out.println("SERVICE CONNECTED");
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;

    public final static String SCORE = "edu.rit.Wonderwall.SCORE";
    /**
     * Initialize the SongActivity, loads views, data binding.
     * @param savedInstanceState (If app is re-initialized after shut-down, Bundle contains most recent saved state data)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song); //places the UI for this activity here
        doBindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        startService(music);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
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
                    incrementScore();
                    System.out.println("Left to Right Swipe");
                    strum();
                }
                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    incrementScore();
                    System.out.println("Right to Left Swipe");
                    strum();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView updateThis = (TextView)findViewById(R.id.score);
                score+=1+(combo/5);
                updateThis.setText(score.toString());
                updateCombo();
            }
        });
        return true;
    }

    private void updateCombo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView combotxt = (TextView) findViewById(R.id.combotxt);
                if (combo < 5) {
                    combotxt.setText("");
                } else {
                    combotxt.setText("x" + ((combo / 5) + 1));
                }
            }
        });
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
        incrementScore();
    }

    /**
     * Song continues playing, progress advances
     */
    public void progress(){
        //TODO implement song progression
        combo++;
        System.out.println("Song playing");
        incrementScore();
        if (mServ != null) {
            System.out.print("mServ exists! ");
            if (!mIsPlaying) {
                System.out.println("...wasn't playing, resuming music");
                mServ.resumeMusic();
                mIsPlaying = true;
            }
        }
        System.out.println("Combo: "+combo/5);
    }

    /**
     * Song is paused, halt progress
     */
    public void pause(){
        System.out.println("Song stopped");
        if (mServ != null) {
            System.out.print("mServ exists! ");
            if (mIsPlaying) {
                System.out.println("...was playing, pausing music");
                mServ.pauseMusic();
                mIsPlaying = false;
            }
        }
        combo=0;
        System.out.println("Combo Broken");
        updateCombo();
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

            //code that gets the instrument selected and changes the background image
            String inst = MainActivity.instrument;
            if(inst.equals("Guitar")){
                Resources res = getResources();
                Drawable drawableG = res.getDrawable(R.drawable.guitar);
                rootView.setBackground(drawableG);
                System.out.println("Guitar selected");
                return rootView;
            }
            else if(inst.equals("Bass")){
                Resources res = getResources();
                Drawable drawableB = res.getDrawable(R.drawable.bass);
                rootView.setBackground(drawableB);
                System.out.println("Bass selected");
                return rootView;
            }
            return null;

        }
    }
    public void quit(View view){
        finish();
    }

    @Override
    public void onPause(){
        super.onPause(); //Always call the super
        //pause the timer and song
        this.timer.cancel();
        this.timer = null;
        System.out.println("I have paused and you should not see the timer incrementing");
        if (mServ != null) {
            if (mIsPlaying) {
                mServ.pauseMusic();
                mIsPlaying = false;
            }
        }
        finish();
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
        }
        if (mServ != null) {
            if (!mIsPlaying) {
                mServ.resumeMusic();
                mIsPlaying = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    public void songCompleted(View view) {
        Intent intent = new Intent(this, SongCompleted.class);
        //create the intent and start the activity
        String message = score.toString();
        intent.putExtra(SCORE, message);
        startActivity(intent);
    }


}
