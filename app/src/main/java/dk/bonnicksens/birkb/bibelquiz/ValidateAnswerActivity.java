package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class ValidateAnswerActivity extends Activity /*AppCompatActivity*/ {

    private static final String TAG = "ValidateAnswerActivity";

    // Views
    TextView textViewValidation;
    TextView textViewInfo;
    TextView textViewPointsCorrect;
    TextView textViewPointsTotal;

    Button buttonNext;
    //Button buttonEnd;

    // values to save in saved index state
    int answerText = -1;
    String infoText = "";
    int pointsCorrect = 0;
    int pointsTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_answer);

        // Sæt views
        textViewValidation = (TextView) findViewById(R.id.textViewValidation);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        textViewPointsCorrect = (TextView) findViewById(R.id.textViewPointsCorrect);
        textViewPointsTotal = (TextView) findViewById(R.id.textViewPointsTotal);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        //buttonEnd = (Button) findViewById(R.id.buttonEnd);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Få info om
        Boolean rightAnswer;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                rightAnswer = null;
            } else {
                rightAnswer = extras.getBoolean(Global.PUT_RIGHTANSWER);
                infoText = extras.getString(Global.PUT_INFO);
                pointsCorrect = extras.getInt(Global.PUT_POINTS);
                pointsTotal = extras.getInt(Global.PUT_POINTSTOTAL);

            }
        } else {
            // Hent værdier fra saved instance state
            rightAnswer = null;
            answerText = (int) savedInstanceState.getSerializable("ANSWERTEXT");
            infoText = (String) savedInstanceState.getSerializable("INFOTEXT");
            pointsCorrect = (int) savedInstanceState.getSerializable("POINTS");
            pointsTotal = (int) savedInstanceState.getSerializable("POINTSTOTAL");
        }

        if (rightAnswer != null) {
            if (rightAnswer) {
                Random random = new Random();
                float randomFloat = random.nextFloat();
                int strings = 4;
                int index = Math.round(randomFloat * (float) strings);
                switch (index) {
                    case 0:
                        answerText =  R.string.answer_text_right1;
                        break;
                    case 1:
                        answerText =  R.string.answer_text_right2;
                        break;
                    case 2:
                        answerText =  R.string.answer_text_right3;
                        break;
                    case 3:
                        answerText =  R.string.answer_text_right4;
                        break;
                    case 4:
                        answerText =  R.string.answer_text_right5;
                        break;
                }
            } else {
                Random random = new Random();
                float randomFloat = random.nextFloat();
                int strings = 4;
                int index = Math.round(randomFloat * (float) strings);
                switch (index) {
                    case 0:
                        answerText =  R.string.answer_text_wrong1;
                        break;
                    case 1:
                        answerText =  R.string.answer_text_wrong2;
                        break;
                    case 2:
                        answerText =  R.string.answer_text_wrong3;
                        break;
                    case 3:
                        answerText =  R.string.answer_text_wrong4;
                        break;
                    case 4:
                        answerText =  R.string.answer_text_wrong5;
                        break;
                }
            }
        }

        // Sæt teksterne
        if (answerText >= 0) textViewValidation.setText(answerText);
        textViewInfo.setText(infoText);
        textViewPointsCorrect.setText(String.valueOf(pointsCorrect));
        textViewPointsTotal.setText(String.valueOf(pointsTotal));

        // Sæt next knap
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QuestionList questionList = new QuestionList();
                if (questionList.getQuestionsCount() > 0) {
                    Intent intent;
                    switch (questionList.getNextQuestionType())
                    {
                        case Global.TYPE_MCQ:
                        case Global.TYPE_TFQ:
                        case Global.TYPE_MAQ:
                            // Gå til Multiple Choice Question
                            intent = new Intent(ValidateAnswerActivity.this, MultipleChoiceQuestionActivity.class);
                            break;

                        case Global.TYPE_SQ:
                            // Gå til Sequence Question
                            intent = new Intent(ValidateAnswerActivity.this, SequenceQuestionActivity.class);
                            break;

                        default:
                            // Hvis fejl gå til resultat
                            Log.e(TAG, "Fejl i spørgsmålstype, går til resultat");
                            intent = new Intent(ValidateAnswerActivity.this, ResultActivity.class);
                            break;
                    }

                    // Finish activity
                    finish();

                    // Gå til næste activity
                    startActivity(intent);

                    // Fin transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else
                {
                    // Finish activity
                    finish();

                    // Gå til resultat skærm
                    Intent intent = new Intent(ValidateAnswerActivity.this, ResultActivity.class);
                    startActivity(intent);

                    // Fin transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

        /*
        // Sæt end knap
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Sæt antal spørgsmål man har været igennem
                Global global = new Global();
                QuestionList questionList = new QuestionList();
                global.setNumberOfQuestions(global.getNumberOfQuestions() - questionList.getQuestionsCount());

                // Finish activity
                finish();

                // Gå til resultatet
                Intent intent = new Intent(ValidateAnswerActivity.this, ResultActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        */
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // Man klikker på tilbage knappen

                // Gå til resultat skærmen
                // Sæt antal spørgsmål man har været igennem
                Global global = new Global();
                QuestionList questionList = new QuestionList();
                global.setNumberOfQuestions(global.getNumberOfQuestions() - questionList.getQuestionsCount());

                // Finish activity
                finish();

                // Gå til resultatet
                Intent intent = new Intent(ValidateAnswerActivity.this, ResultActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Værdier der skal gemmes i saved instance state
        outState.putInt("ANSWERTEXT", answerText);
        outState.putString("INFOTEXT", infoText);
        outState.putInt("POINTS", pointsCorrect);
        outState.putInt("POINTSTOTAL", pointsTotal);

        super.onSaveInstanceState(outState);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validate_answer, menu);
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
