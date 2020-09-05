package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity /*AppCompatActivity*/ {

    // Views
    TextView textViewQuestionsCorrect;
    TextView textViewQuestionsTotal;
    TextView textViewQuestionsPercentage;
    TextView textViewPointsCorrect;
    TextView textViewPointsTotal;
    TextView textViewPointsPercentage;
    TextView textViewTotalPercentage;
    TextView textViewTime;
    Button buttonMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Sæt views
        textViewQuestionsCorrect = (TextView) findViewById(R.id.textViewQuestionsCorrect);
        textViewQuestionsTotal = (TextView) findViewById(R.id.textViewQuestionsTotal);
        textViewQuestionsPercentage = (TextView) findViewById(R.id.textViewQuestionsPercentage);
        textViewPointsCorrect = (TextView) findViewById(R.id.textViewPointsCorrect);
        textViewPointsTotal = (TextView) findViewById(R.id.textViewPointsTotal);
        textViewPointsPercentage = (TextView) findViewById(R.id.textViewPointsPercentage);
        textViewTotalPercentage = (TextView) findViewById(R.id.textViewTotalPercentage);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        buttonMain = (Button) findViewById(R.id.buttonMain);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sæt tekstfelter
        // Sprøgsmål
        Global global = new Global();
        int questionsCorrect = global.getNumberOfQuestionsCorrectCount();
        int questionsTotal = global.getNumberOfQuestions();
        int questionsPercentage = Math.round(((float)questionsCorrect / (float)questionsTotal) * (float)100);
        textViewQuestionsCorrect.setText(String.valueOf(questionsCorrect));
        textViewQuestionsTotal.setText(String.valueOf(questionsTotal));
        textViewQuestionsPercentage.setText(String.valueOf(questionsPercentage));

        // Point
        int points = global.getNumberOfPoints();
        int pointsTotal = global.getNumberOfPointsTotal();
        int pointsPercentage = Math.round(((float) points / (float) pointsTotal) * (float) 100);
        textViewPointsCorrect.setText(String.valueOf(points));
        textViewPointsTotal.setText(String.valueOf(pointsTotal));
        textViewPointsPercentage.setText(String.valueOf(pointsPercentage));

        // Total procent
        int totalPercentage = Math.round((((float) questionsPercentage + (float) pointsPercentage) / (float) 200) * (float) 100);
        textViewTotalPercentage.setText(String.valueOf(totalPercentage));

        // Sæt slut tid
        global.setEndTime();
        textViewTime.setText(global.getTimeElapsed());

        // Sæt knap
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish activity
                finish();

                // gå til hovedmenu
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // Man klikker på tilbage knappen

                // Finish activity
                finish();

                // Gå til hovedmenu
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
