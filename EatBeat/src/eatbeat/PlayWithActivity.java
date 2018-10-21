package eatbeat;
import java.util.Locale;

import com.example.eatbeat.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

public class PlayWithActivity extends Activity implements View.OnClickListener {
	Button ptp,ptc,ptrp,bkbn;
	public static String Mode;
	public static Activity Context;
    TextToSpeech speaker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playwith_layout);
		ptp=(Button)findViewById(R.id.p2p_btn);
		ptc=(Button)findViewById(R.id.p2c_btn);
		ptrp=(Button)findViewById(R.id.p2rp_btn);
		bkbn=(Button)findViewById(R.id.back_btn);
		ptp.setOnClickListener(this);
		ptc.setOnClickListener(this);
		ptrp.setOnClickListener(this);
		bkbn.setOnClickListener(this);
		Context = this;
		speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					speaker.setLanguage(Locale.US);
				}
			}});
		 
		
	}

	 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.p2p_btn :
			Mode = "p2p";
			speaker.speak("You Choosed Person to Person ", TextToSpeech.QUEUE_FLUSH, null);
			Intent i = new Intent(this,LevelChooseActivity.class);
			startActivity(i);
			break;
			
			
		case R.id.p2c_btn:
			Mode = "p2c";
			speaker.speak("You Choosed Person to Computer ", TextToSpeech.QUEUE_FLUSH, null);
			Intent j= new Intent(this, LevelChooseActivity.class); 
			startActivity(j);
			break;
		
		case R.id.back_btn:
			
			this.finish();
			break;
			
		case R.id.p2rp_btn:
			 Mode = "p2rp";
			 speaker.speak("You Choosed Person to Remote Person ", TextToSpeech.QUEUE_FLUSH, null);
		     startActivity(new Intent(v.getContext(),RemotePlayActivity.class));
		     break;
		}
	}

}
