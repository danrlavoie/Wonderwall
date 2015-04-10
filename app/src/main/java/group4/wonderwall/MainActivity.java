package group4.wonderwall;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.graphics.Color;


public class MainActivity extends ActionBarActivity {

    private RadioGroup instrumentGroup;
    public static String instrument = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //click the guitar button on app start
        RadioButton rb1 = (RadioButton) findViewById(R.id.radioGuitar);
        rb1.performClick();

    }

    public void playSong(View view) {
        Intent intent = new Intent(this, SongChooserActivity.class);
        //create the intent and start the activity
        startActivity(intent);

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

    //get the instrument selected
    public void onInstrumentSelect(View view){

        instrumentGroup = (RadioGroup) findViewById(R.id.radioInstrument);
        if(instrumentGroup.getCheckedRadioButtonId()!=-1){
            int buttonId= instrumentGroup.getCheckedRadioButtonId();

            System.out.println("button id: "+ buttonId);
            if(buttonId == 2131230785){
                System.out.println("Selecting Guitar.");

                instrument = "Guitar";
            }
            else if(buttonId == 2131230786){
                System.out.println("Selecting Bass.");

                instrument = "Bass";
            }
        }
    }

}
