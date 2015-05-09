package group4.wonderwall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SongChooserActivity extends Activity{

	//song list variables
	private ArrayList<Song> songList;
	private ListView songView;
    public static String instrument = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);
        this.instrument = MainActivity.instrument;
        System.out.println("SONG CHOOSER'S INSTRUMENT IS: " +this.instrument);
		//retrieve list view
		songView = (ListView)findViewById(R.id.song_list);
		//instantiate list
		songList = new ArrayList<Song>();
		//get songs from device
		getSongList();
		//sort alphabetically by title
		Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
		//create and set adapter
		SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);

	}


	//user song select
	public void songPicked(View view){
        Intent intent = new Intent(this, SongActivity.class);
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
		//menu item selected
		//switch (item.getItemId()) {
		//case R.id.action_end:
		//	stopService(playIntent);
		//	musicSrv=null;
		//	System.exit(0);
		//	break;
		//}
		return super.onOptionsItemSelected(item);
	}

	//method to retrieve song info from device
	public void getSongList(){
        songList.add(new Song(1,"Welcome to the Jungle","Guns and Roses"));
        songList.add(new Song(2,"Wonderwall","Oasis"));
        songList.add(new Song(3,"YYZ","Rush"));
        songList.add(new Song(4,"Bulls on Parade","Rage Against the Machine"));
        songList.add(new Song(5,"Even Flow","Pearl Jam"));
        songList.add(new Song(6,"Stairway to Heaven","Led Zeppelin"));
        songList.add(new Song(7,"Paranoid","Black Sabbath"));
        songList.add(new Song(8,"Knights of Cydonia","Muse"));
        songList.add(new Song(9,"Anarchy in the UK","Sex Pistols"));
        songList.add(new Song(10,"Cult of Personality","Living Colour"));

	}

}
