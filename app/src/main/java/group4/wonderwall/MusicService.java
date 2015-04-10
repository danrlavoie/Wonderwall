package group4.wonderwall;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/*
 */

public class MusicService extends Service implements
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {

	//media player
	private MediaPlayer player;
	//song list
	private ArrayList<Song> songs;
	//binder
	private final IBinder musicBind = new MusicBinder();
	//title of current song
	private String songTitle="";
	//notification id
	private static final int NOTIFY_ID=1;
	//shuffle flag and random
	private boolean shuffle=false;
	private Random rand;

	public void onCreate(){
		//create the service
		super.onCreate();
		//random
		rand=new Random();
		//create player
		player = new MediaPlayer();
		//initialize
		initMusicPlayer();
        playSong();
	}

	public void initMusicPlayer(){
		//set player properties
		player.setWakeMode(getApplicationContext(), 
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//set listeners
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	//pass song list
	public void setList(ArrayList<Song> theSongs){
		songs=theSongs;
	}

	//binder
	public class MusicBinder extends Binder {
		MusicService getService() { 
			return MusicService.this;
		}
	}

	//activity will bind to service
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	//release resources when unbind
	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}

	//play a song
	public void playSong(){
		//play
		player.reset();
		//get song
        double randomSong = rand.nextDouble();
        Uri trackUri;
        if (randomSong > 0.8) {
            trackUri = Uri.parse("android.resource://com.example.musicplayer/raw/wonderwall");
        }
        else {
            trackUri = Uri.parse("android.resource://com.example.musicplayer/raw/stairway_to_heaven");
        }

		//set the data source
		try{ 
			player.setDataSource(getApplicationContext(), trackUri);
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync(); 
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//check if playback has reached the end of a track
		if(player.getCurrentPosition()>0){
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v("MUSIC PLAYER", "Playback Error");
		mp.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		//start playback
		mp.start();
		//notification
		Intent notIntent = new Intent(this, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.play)
		.setTicker(songTitle)
		.setOngoing(true)
		.setContentTitle("Playing")
		.setContentText(songTitle);
		Notification not = builder.build();
		startForeground(NOTIFY_ID, not);
	}

	//playback methods
	public int getPosn(){
		return player.getCurrentPosition();
	}

	public int getDur(){
		return player.getDuration();
	}

	public boolean isPng(){
		return player.isPlaying();
	}

	public void pausePlayer(){
		player.pause();
	}

	public void seek(int posn){
		player.seekTo(posn);
	}

	public void go(){
		player.start();
	}

	//skip to previous track
	public void playPrev(){
        double randomSong = rand.nextDouble();
        int newSong;
        if (randomSong > 0.8) {
            newSong = 5;
        }
        else {
            newSong = 2;
        }
		playSong();
	}

	//skip to next
	public void playNext(){
		double randomSong = rand.nextDouble();
        int newSong;
        if (randomSong > 0.8) {
            newSong = 5;
        }
        else {
            newSong = 2;
        }
		playSong();
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
	}


}
