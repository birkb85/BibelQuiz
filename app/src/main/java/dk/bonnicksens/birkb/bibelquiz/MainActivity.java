package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity /*AppCompatActivity*/ {

    private static final String TAG = "MainActivity";

    // Views
    Button buttonStart;
    Button buttonInfo;
    Button buttonContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sæt views
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonInfo = (Button) findViewById(R.id.buttonInfo);
        buttonContact = (Button) findViewById(R.id.buttonContact);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Transition
        //getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hvis jeg får brug for at skjule software keyboardet når en aktivitet startes
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Gem antal multiple choice questions
        //DBHandler dbHandler = new DBHandler(MainActivity.this, null, null, 1);
        //int count = dbHandler.getMultipleChoiceQuestionCount();
        //Log.d("Antal MCQ", String.valueOf(count));
        //Global.setMultipleChoiceQuestionCount(count);

        // Test modtag spørgsmål blandet
        //DBHandler dbHandler = new DBHandler(MainActivity.this, null, null, 1);
        //dbHandler.randomIdTest();

        // Button start
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish activity
                finish();

                // Gå til forberedelser
                Intent intent = new Intent(MainActivity.this, PreparingActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Button Information
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish activity
                finish();

                // Gå til forberedelser
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Button Contact
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish activity
                finish();

                // Gå til forberedelser
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Initialise shared preferences
        // Shared preferences
        SharedPreferences preferenceSettings = getSharedPreferences(Global.PREFERENCE_FILE, Global.PREFERENCE_MODE_PRIVATE);
        if (!preferenceSettings.getBoolean("INIT", false))
        {
            SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
            preferenceEditor.putBoolean("INIT", true);
            preferenceEditor.putBoolean(Global.PRE_SHOWMCQ, true);
            preferenceEditor.putBoolean(Global.PRE_SHOWTFQ, true);
            preferenceEditor.putBoolean(Global.PRE_SHOWMAQ, true);
            preferenceEditor.putBoolean(Global.PRE_SHOWSQ, true);
            preferenceEditor.apply();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // Man klikker på tilbage knappen

                // Finish activity
                finish();

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
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
    */
}
