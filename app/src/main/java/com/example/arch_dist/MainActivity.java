package com.example.arch_dist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText etText;
    ImageView ivMic,ivCopy, btnPlay, btnPause, btnNext, btnBack;
    Spinner spLangs;
    String lcode = "en-US";

    // Languages included
    String[] languages = {"English","French"};

    // Language codes
    String[] lCodes = {"en-US","fr-FR"};

    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.hotel_california);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.pablo);

        // initialize views
        etText = findViewById(R.id.etSpeech);
        ivMic = findViewById(R.id.ivSpeak);
        ivCopy = findViewById(R.id.ivCopy);
        spLangs = findViewById(R.id.spLang);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);



        // set onSelectedItemListener for the spinner
        spLangs.setOnItemSelectedListener(this);

        // setting array adapter for spinner
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLangs.setAdapter(adapter);

        // on click listener for mic icon
        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etText.setText("");
                // creating intent using RecognizerIntent to convert speech to text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,lcode);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now!");
                // starting intent for result
                activityResultLauncher.launch(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                mediaPlayer.start();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
                mediaPlayer.pause();
            }
        });
    }

    // activity result launcher to start intent
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is not empty
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
                        // get data and append it to editText
                        ArrayList<String> d=result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        etText.setText(etText.getText()+" "+d.get(0));
                    }
                    if(etText.getText().toString().matches(".*\\bplay Hotel California\\b.*")) {
                        // Le texte contient "play"
                        // Ajouter le code pour gérer le cas où "play" est trouvé
                        if (mediaPlayer2.isPlaying()){
                            mediaPlayer2.pause();
                        }
                        Toast.makeText(MainActivity.this, "Playing music", Toast.LENGTH_SHORT).show();
                        mediaPlayer.start();
                        btnPlay.setVisibility(View.INVISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                    }
                    if(etText.getText().toString().matches(".*\\bplay Pablo\\b.*")) {
                        // Le texte contient "play"
                        // Ajouter le code pour gérer le cas où "play" est trouvé
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        Toast.makeText(MainActivity.this, "Playing music", Toast.LENGTH_SHORT).show();
                        mediaPlayer2.start();
                        btnPlay.setVisibility(View.INVISIBLE);
                        btnPause.setVisibility(View.VISIBLE);
                    }

                    if(etText.getText().toString().matches(".*\\bstop\\b.*")) {
                        // Le texte contient "pause"
                        // Ajouter le code pour gérer le cas où "pause" est trouvé
                        Toast.makeText(MainActivity.this, "Pausing music", Toast.LENGTH_SHORT).show();
                        if (mediaPlayer.isPlaying() || mediaPlayer2.isPlaying()) {
                            mediaPlayer.pause();
                            mediaPlayer2.pause();
                        }
                        btnPlay.setVisibility(View.VISIBLE);
                        btnPause.setVisibility(View.INVISIBLE);
                    }
                }
            });

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // setting lcode corresponding
        // to the language selected
        lcode = lCodes[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // automatically generated method
        // for implementing onItemSelectedListener
    }
}
