package landister.home.resistanceavalon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import landister.home.resistanceavalontracker.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	private static final String DEFAULT_ANNOUNCER= "TripsOro";
	private static final String CheckBox = null;
	private Button startGameBtn;
	private CheckBox merlinChbx, percivalChbx, morganaChbx, oberonChbx, mordredChbx;
	private MediaPlayer mediaPlayer;
	private List<String> soundList;
	private String announcerFolder;
	private Spinner announcerSpinner;
	private Timer pauseTimer;
	private int longPauseTime, shortPauseTime, pauseTime = 3;
	
	//Denotes if the program is currently running
	private boolean stillRunning;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pauseTimer = new Timer();
        announcerSpinner = (Spinner) this.findViewById(R.id.announcerSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.announcer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        longPauseTime = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("longPauseTimer", "3"));
        shortPauseTime = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shortPauseTimer", "2"));
        // Apply the adapter to the spinner
        announcerSpinner.setAdapter(adapter);
        getWindow().getDecorView().findViewById(android.R.id.content).setKeepScreenOn(true);
        announcerFolder= DEFAULT_ANNOUNCER;
        addListenerOnButton();
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	stillRunning = true;
    	//try {
	    	//AssetFileDescriptor afd;
			//afd = getAssets().openFd(announcerFolder+"/pause.wav");
	    	//mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			//mediaPlayer.prepare();
			//mediaPlayer.start();
		//} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		//}
    }
    
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	if(mediaPlayer != null)
    		mediaPlayer.reset();
    	stillRunning = false;
    	
    	
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	if(mediaPlayer != null)
    		mediaPlayer.release();
    	
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	longPauseTime = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("longPauseTimer", "3"));
        shortPauseTime = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("shortPauseTimer", "1"));
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    
   
    
    
    public void addListenerOnButton() {
    	 
    	startGameBtn = (Button) findViewById(R.id.btnStart);
    	merlinChbx = (CheckBox) findViewById(R.id.chkMerlin);
    	percivalChbx = (CheckBox) findViewById(R.id.chkPercival);
    	morganaChbx = (CheckBox) findViewById(R.id.chkMorgana);
    	oberonChbx = (CheckBox) findViewById(R.id.chkOberon);
    	mordredChbx = (CheckBox) findViewById(R.id.chkMordred);
    	mediaPlayer = new MediaPlayer();

    	
    	soundList = new ArrayList<String>();
    	addOnCompletionListener();
    	startGameBtn.setOnClickListener(new OnClickListener() {
     
              //Run when button is clicked
    	  @Override
    	  public void onClick(View v) {
    		 soundList.clear();
    		soundList.add("closeEyesFistIn.wav");
			soundList.add("sp");
    		if(!oberonChbx.isChecked()){
    			soundList.add("minionOpenEyes.wav");
    		}
    		else{
    			soundList.add("minionOpenEyesExceptOberon.wav");
    		}

			soundList.add("lp");
    		
    		soundList.add("minionsCloseYourEyes.wav");

			soundList.add("sp");
    		
    		if(merlinChbx.isChecked()){
	    		if(!mordredChbx.isChecked()){
	    			soundList.add("merlin.wav");
	    		}
	    		else{
	    			soundList.add("merlinWithMordred.wav");
	    		}

    			soundList.add("lp");
	    		soundList.add("closeYourEyesThumbsDown.wav");
    			soundList.add("sp");
    		}
    		
    		
    		if(percivalChbx.isChecked() && merlinChbx.isChecked()){
    			if(!morganaChbx.isChecked()){
    				soundList.add("percival.wav");
    			}
    			else{
    				soundList.add("percivalWithMorgana.wav");
    			} 
    			soundList.add("lp");
	    		soundList.add("closeYourEyesThumbsDown.wav");
    			soundList.add("sp");
    		}

			soundList.add("openEyes.wav");
    		mediaPlayer.reset();
    		try{
    			AssetFileDescriptor afd = getAssets().openFd(announcerFolder +"/"+soundList.get(0));
                Log.d("TAG", Arrays.toString(getAssets().list(".")));
    			soundList.remove(0);
	    		mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
	    		mediaPlayer.prepare();
	    		mediaPlayer.start();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	  }
    	});
     
    }
    
    public void addOnCompletionListener(){
    	
    	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(!stillRunning)
	        		return;
				mediaPlayer.reset();
				AssetFileDescriptor afd;
				try {
					if(soundList.size() > 0){
						if(!(soundList.get(0).equals("sp") || soundList.get(0).equals("lp"))){
							afd = getAssets().openFd( announcerFolder+"/"+soundList.get(0));
							soundList.remove(0);
							mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							mediaPlayer.prepare();
							mediaPlayer.start();
						}
						else if (soundList.get(0).equals("sp")){
							pauseTimer.schedule(new PauseTask(), shortPauseTime * 1000);
							soundList.remove(0);
						}
						else if (soundList.get(0).equals("lp")){
							pauseTimer.schedule(new PauseTask(), longPauseTime * 1000);
							soundList.remove(0);
						}
					}
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}
		});
    	
    	mediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
			}
		});
    	
    	announcerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    		@Override
    		public void onItemSelected(AdapterView<?> parent, View view, int pos,
    				long id) {
    			announcerFolder = getFolderNameFromSpinner((String)parent.getItemAtPosition(pos));
    		}

    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {
    			// TODO Auto-generated method stub
    			
    		}
      		
      	
      	
      	});
    }


    private String getFolderNameFromSpinner(String announcerName){

        int indexOfFirstSpace = announcerName.indexOf(' ');
        if(indexOfFirstSpace == -1){
            return announcerName;
        }
        return announcerName.substring(0, indexOfFirstSpace);
    }
    
    class PauseTask extends TimerTask {
        public void run() {
        	if(!stillRunning)
        		return;
        	mediaPlayer.reset();
			AssetFileDescriptor afd;
			try {
				if(soundList.size() > 0){
					if(!(soundList.get(0).equals("sp") || soundList.get(0).equals("lp"))){
						afd = getAssets().openFd( announcerFolder+"/"+soundList.get(0));
						soundList.remove(0);
						mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
						mediaPlayer.prepare();
						mediaPlayer.start();
					}
					else if (soundList.get(0).equals("sp")){
						pauseTimer.schedule(new PauseTask(), shortPauseTime * 1000);
						soundList.remove(0);
					}
					else if (soundList.get(0).equals("lp")){
						pauseTimer.schedule(new PauseTask(), longPauseTime * 1000);
						soundList.remove(0);
					}
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	
        }
      }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if(item.getItemId() == R.id.menu_settings){
    		Intent intent = new Intent(this, SettingsActivity.class);
    		startActivity(intent);
    	}
		return true;
		
    }
    
     

    
}
