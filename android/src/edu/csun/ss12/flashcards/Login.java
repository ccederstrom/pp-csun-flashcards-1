package edu.csun.ss12.flashcards;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnInitListener{
   
	public static String userLoginId;
	private Button btnExit;
	private Button btnLogin;
	private Button btnRegister;
	private EditText mUserName;
	private EditText mPassword;
	private CheckBox mSaveInfo;
	final static String PREFERENCES = "FlashcardPreferences";
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech tts;
    SharedPreferences preferences; 
    SharedPreferences.Editor editor;
    private GestureOverlayView gestures;
	private GestureLibrary mLibrary;
	int counter=0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        
        //Text-to-Speech
        Intent checkIntent = new Intent();
    	checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    	startActivityForResult(checkIntent, 0);
    	tts = new TextToSpeech(this, this);
    	
        // initalize layout
        mUserName = (EditText)this.findViewById(R.id.Login_EditTextUserName);
        mPassword = (EditText)this.findViewById(R.id.Login_EditTextPassword);
        mSaveInfo = (CheckBox)this.findViewById(R.id.Login_CheckBoxSaveAccountInfo);
        btnExit = (Button)this.findViewById(R.id.Login_ButtonExit);
        btnLogin = (Button)this.findViewById(R.id.Login_ButtonLogin);
        btnRegister = (Button)this.findViewById(R.id.Login_ButtonRegister);
        
        //Select username textbox
        mUserName.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				// TODO Auto-generated method stub
				if(gainFocus){
					String speech1 = "Username";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
      //Select password textbox
        mPassword.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				// TODO Auto-generated method stub
				if(gainFocus){
					String speech1 = "Password";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
        //Select Info checkbox
        mSaveInfo.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				// TODO Auto-generated method stub
				if(gainFocus){
					String speech1 = "Remember Account Info";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
      //Select Sign In
        btnLogin.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				if(gainFocus){
					String speech1 = "Sign in";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
      //Select Register
        btnRegister.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				// TODO Auto-generated method stub
				if(gainFocus){
					String speech1 = "Register";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
      //Select Exit
        btnExit.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean gainFocus) {
				// TODO Auto-generated method stub
				if(gainFocus){
					String speech1 = "Exit";
			    	tts.setLanguage(Locale.US);
			    	tts.speak(speech1, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
        	
        });
        // Exit button listener
        btnExit.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		if(mSaveInfo.isChecked()){
                	editor.putBoolean("saveAccountInfo", true);
        			Editable accountInfo = mUserName.getText();
        			editor.putString("userName", accountInfo.toString());
        			accountInfo = mPassword.getText();
        			editor.putString("password", accountInfo.toString());
        			editor.commit();
        		}
        		finish();
        	}
        });
        
        // Login button listener
        btnLogin.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if(mSaveInfo.isChecked()){
                	editor.putBoolean("saveAccountInfo", true);
        			Editable accountInfo = mUserName.getText();
        			editor.putString("userName", accountInfo.toString());
        			accountInfo = mPassword.getText();
        			editor.putString("password", accountInfo.toString());
        			editor.commit();
        		}
    			MySQL_Connection mysql = new MySQL_Connection();
    			String[] login_info = mysql.getLogin( mUserName.getText().toString());
    			
    			if(login_info[0]!=null){
    			// store user id
    	        //preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
    	        //editor = preferences.edit();
    			editor.putInt("userId", Integer.parseInt(login_info[0]));
    			editor.commit();
    			}
    			else{
    				Toast toast = Toast.makeText(getBaseContext(), "Cannot connect server. Please check internet connection", 5);
					toast.show();
					finish();
    			}
    			//Log.d("hung", login_info[0]);  
    			//login_info = new String[]{"1","hung","123"};
    			boolean loginSuccess = false;
    			try {
					String password_MD5 = functions.MD5(mPassword.getText().toString());
					if(password_MD5.compareTo(login_info[2])==0) loginSuccess = true;
					Log.d("A",password_MD5);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				if( login_info[2]!=null)
					Log.d("A",login_info[2]);
				
				Log.d("A",Boolean.toString(loginSuccess));
				if(loginSuccess){
					Log.d("A","Success");
					Log.d("A",login_info[0]);
					startMenu();
				}
				else{
					Toast toast = Toast.makeText(getBaseContext(), "Wrong user name or password.", 10);
					toast.show();
				}       		
        	}
        });  
        
        
        // Login button listener
        btnRegister.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startRegister();
        	}
        });
        
        // Check for saved user information
        if( preferences.getBoolean("saveAccountInfo", false) == true) { 
        	mUserName.setText(preferences.getString("userName", ""));
        	mPassword.setText(preferences.getString("password", ""));
        	mSaveInfo.setChecked(true);
        	editor.putBoolean("saveAccountInfo", true);
        	editor.commit();
        } else {
        	mSaveInfo.setChecked(false);
        	editor.putBoolean("saveAccountInfo", false);
        	editor.commit();
        }
        
        //Gesture   
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.Login_gestures);
        gestures.addOnGesturePerformedListener(new OnGesturePerformedListener(){
 			@Override
 			public void onGesturePerformed(GestureOverlayView overlay,Gesture gesture) {
 				ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
 			    if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
 			        String action = predictions.get(0).name;
 			        if ("left_right".equals(action)) {
 			        	if(counter%6==0||counter%6==1){
 			        		functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
 			        	}
 			        	else
 			        	{
	 			        	for(int i = 0; i<((counter % 6)+2);i++){
	 				            functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
	 			        	}
 			        	}
 			        	System.out.println(counter);
 			        	counter++; 			        	
 			        } else if ("right_left".equals(action)) {
 			        	if(counter>0){
 			        		counter--;
 			        	}
 			        	else{
 			        		counter=5;
 			        	}
 			        	for(int i = 0; i<((counter %6)+1);i++){
 				            functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
 			        	}
 			        } 
 			        else if ("click".equals(action)) {
 			        	if(counter % 6==0){
 			        		for(int i = 0; i<6;i++){
 					            functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
 				        	}
 			        	}
 			        	else{
 				        	for(int i = 0; i<((counter % 6)+1);i++){
 					            functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_DOWN);
 				        	}
 			        	}
 			        	functions.InjectKeys(android.view.KeyEvent.KEYCODE_DPAD_CENTER);
 			        }
 			    }
 			}        	
        });
      //End Gesture
        
    }
    

    public void startMenu(){
    	this.startActivity(new Intent(getBaseContext(), Menu.class));
    }
    
    public void startRegister(){
    	this.startActivity(new Intent(getBaseContext(), Register.class));
    }
    
    @Override
    public void onInit(int arg0) {
    	// TODO Auto-generated method stub
    	
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // success, create the TTS instance
                tts = new TextToSpeech(this, this);
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    
}