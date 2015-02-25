package group4.wonderwall;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private boolean strumming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    
    //Call this each time the song checks for user activity
    public void beat(){
        if(stumming){
            strumming = false;
            progress();
        }else{
            pause();
        }
    }
    //Called by strum action listener
    public void strum(){
        strumming = true;
    }
    //Song continues playing, progress advances
    public void progress(){
        //TODO implement song progression
    }
    //Song is paused, halt progress
    public void pause(){
        //TODO implement song pausing
    }
}
