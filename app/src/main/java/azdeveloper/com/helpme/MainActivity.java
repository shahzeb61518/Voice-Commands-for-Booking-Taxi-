package azdeveloper.com.helpme;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView txtTranslated;
    EditText edtTxtSearch;
    ImageView searchImg;
    private TextView showVoicText;
    TextToSpeech t1,t2;
    RelativeLayout relativeLayout;
    private BroadcastReceiver mReceiver;
    int level = 0;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_CALL = 101;

   public void  welcome(){

        t2 = new TextToSpeech(getApplication(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.ENGLISH);
                    String b = "Hello, Try any command on the screen.";
                    t2.speak(b, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),"Try any command on the screen",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        txtTranslated = findViewById(R.id.txtTranslated);



        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        welcome();
//        t1 = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    t1.setLanguage(Locale.ENGLISH);
//                    String b = "Hi, how can i help you.";
//                    t1.speak(b, TextToSpeech.QUEUE_FLUSH, null);
//                }
//            }
//        });
        micOpening();


        startService(new Intent(getApplicationContext(),Hotel.class));

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE ,  Manifest.permission.SEND_SMS , Manifest.permission.RECEIVE_SMS , Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_CONTACTS );
            }
        }catch (Exception ex){
            Toast.makeText(this, ex+"", Toast.LENGTH_SHORT).show();
        }




         edtTxtSearch = findViewById(R.id.edTxtSearch);
        final String sedtxt = edtTxtSearch.getText().toString().trim();
        showVoicText = (TextView) findViewById(R.id.showVoicTxt);
        RelativeLayout relativeLayoutTaping = findViewById(R.id.relativeLayoutTaping);
        searchImg = findViewById(R.id.searchIcon);
        //For Hotel
        final String scommandhotelChi = "最近的酒店在哪里";
        final String scommandhotelChi0 = "哪里是最近的酒店";
        final String scommandhotelChi1 = "最近的酒店";
        final String scommandhotelChi2 = "最近的酒店";
        final String scommandhotelEng = "旅馆";
        final String scommandhotelEng1 = "where is the nearest hotel";
        //For Police Station
        final String scommandpoliceChi = "最近的警察局";
        final String scommandpoliceChi0 = "最近的警察局";
        final String scommandpoliceChi1 = "最近的警察局在哪里";
        final String scommandpolicChi2 = "哪个是最近的警察局";
        final String scommandpolice4 = "नज़दीकी पुलिस स्टेशन कहां है";
        final String scommandpolice5 = "where is the nearest police station";
        final String scommandpolice6 = "where is the nearest police station";

        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         if (edtTxtSearch.getText().toString().trim().equals(scommandhotelChi)||
                             edtTxtSearch.getText().toString().trim().equals(scommandhotelChi0)||
                             edtTxtSearch.getText().toString().trim().equals(scommandhotelChi1)||
                             edtTxtSearch.getText().toString().trim().equals(scommandhotelChi2)||
                             edtTxtSearch.getText().toString().trim().equals(scommandhotelEng)||
                             edtTxtSearch.getText().toString().trim().equals(scommandhotelEng1)){
                    startActivity(new Intent(getApplicationContext(), Hotel.class));
                    txtTranslated.setText("Where is the nearest Hotel");
                    t1.speak("im taking you to the nearest hotel. select one choice and say book me after fifteen seconds",TextToSpeech.QUEUE_FLUSH,null);

                }else if(edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi0)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi1)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpolicChi2)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpolice4)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpolice5)||
                         edtTxtSearch.getText().toString().trim().equals(scommandpolice6)){
                    txtTranslated.setText("Where is the nearest Police Station");
                    startActivity(new Intent(getApplicationContext(), Police_Station.class));
                    t1.speak("im taking you to the nearest police station. select one choice and say book me after fifteen seconds",TextToSpeech.QUEUE_FLUSH,null);

                }else{
                    Toast.makeText(getApplicationContext(),"Enter any command given below",Toast.LENGTH_SHORT).show();
          t1.speak("Enter any command given below",TextToSpeech.QUEUE_FLUSH,null);

                         }
            }
        });

        relativeLayoutTaping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.SIMPLIFIED_CHINESE);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, how can i help you");
                t1.speak("Hi, how can i help you", TextToSpeech.QUEUE_FLUSH, null);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 12);
                } else {
                    Toast.makeText(MainActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void micOpening(){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.CHINESE);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi, how can i help you");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 12);
        } else {

            Toast.makeText(MainActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //    writeCalendarEvent();

                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    showVoicText.setText(result.get(0));

                }
                break;


            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // txvResult.setText(result.get(0));
                    if (result.get(0).contains("yes")) {
                        String a = "ok exiting the application";
                        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
                        t1.speak(a, TextToSpeech.QUEUE_FLUSH, null);
                        finish();
                    } else if (result.get(0).contains("no")) {
                        String a = "ok continue";
                        Toast.makeText(MainActivity.this, a, Toast.LENGTH_SHORT).show();
                        t1.speak(a, TextToSpeech.QUEUE_FLUSH, null);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;


            case 12:


                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtTxtSearch.setText(result.get(0));

                    //For Hotel
                    final String scommandhotelChi = "最近的酒店在哪里";
                    final String scommandhotelChi0 = "哪里是最近的酒店";
                    final String scommandhotelChi1 = "最近的酒店";
                    final String scommandhotelChi2 = "最近的酒店";
                    final String scommandhotelEng = "旅馆";
                    final String scommandhotelEng1 = "where is the nearest hotel";
                    //For Police Station
                    final String scommandpoliceChi = "最近的警察局";
                    final String scommandpoliceChi0 = "最近的警察局";
                    final String scommandpoliceChi1 = "最近的警察局在哪里";
                    final String scommandpolicChi2 = "哪个是最近的警察局";
                    final String scommandpolice4 = "where is nearest police station";
                    final String scommandpolice5 = "where is the nearest police station";
                    final String scommandpolice6 = "where is the nearest police station";

                    if (edtTxtSearch.getText().toString().trim().equals(scommandhotelChi)||
                            edtTxtSearch.getText().toString().trim().equals(scommandhotelChi0)||
                            edtTxtSearch.getText().toString().trim().equals(scommandhotelChi1)||
                            edtTxtSearch.getText().toString().trim().equals(scommandhotelChi2)||
                            edtTxtSearch.getText().toString().trim().equals(scommandhotelEng)||
                            edtTxtSearch.getText().toString().trim().equals(scommandhotelEng1)){
                        txtTranslated.setText("Where is the nearest Hotel");
                        t1.speak("im taking you to the nearest hotel. select one of your choic then say book me",TextToSpeech.QUEUE_FLUSH,null);
                        startActivity(new Intent(this, Hotel.class));

                    }else if (edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi0)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi1)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpolicChi2)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpoliceChi)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpolice4)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpolice5)||
                            edtTxtSearch.getText().toString().trim().equals(scommandpolice6)){
                        txtTranslated.setText("Where is the nearest Police Station");
                        t1.speak("im taking you to the nearest police station. select one of your choice and say book me",TextToSpeech.QUEUE_FLUSH,null);
                        startActivity(new Intent(this, Police_Station.class));

                    }else if (result.get(0).contains("your name")) {
                        t1.speak("Hi! My name is Kate. Tap screen to enter command.",TextToSpeech.QUEUE_FLUSH,null);

                    }
                    else if (result.get(0).contains("exit")) {
                        Toast.makeText(this,"ok exiting the application" , Toast.LENGTH_SHORT).show();
                        t1.speak("ok exiting the application", TextToSpeech.QUEUE_FLUSH, null);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }else {

                        t1.speak("sorry, I didn't get that. Tap screen again ", TextToSpeech.QUEUE_FLUSH, null);
                        txtTranslated.setText("sorry, I didn't get that. Tap screen again");
                    }
                }


                if (resultCode != RESULT_OK) {

                    String a = "i didn't catch that. tap screen again";
                    t1.speak(a, TextToSpeech.QUEUE_FLUSH, null);

                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        t1.speak("Do you really wanna exit?", TextToSpeech.QUEUE_FLUSH, null);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 11);
        } else {
            Toast.makeText(MainActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        t1=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String b="Hi! My name is Kate. Tap screen to enter command.";
                }
            }
        });
    }

    public void uberClick(View view) {


    }

    public void Onclick(View view) {

    }
}
