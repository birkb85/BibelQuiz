package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class PreparingActivity extends Activity /*AppCompatActivity*/ {

    private static final String TAG = "PreparingActivity";

    // Views
    Button buttonStart;
    Spinner spinnerNumberOfQuestions;
    ToggleButton toggleButtonHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);

        // Sæt views
        buttonStart = (Button) findViewById(R.id.buttonStart);
        spinnerNumberOfQuestions = (Spinner) findViewById(R.id.spinnerNumberOfQuestions);
        toggleButtonHelp = (ToggleButton) findViewById(R.id.toggleButtonHelp);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sæt number of questions spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.preparing_numberOfQuestions, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerNumberOfQuestions.setAdapter(adapter);

        // Sæt Toggle button help
        toggleButtonHelp.setBackgroundResource(R.drawable.selector_button);

        // Toggle button help
        // Tjek først om den skal være on eller off
        SharedPreferences preferenceSettings = getSharedPreferences(Global.PREFERENCE_FILE, Global.PREFERENCE_MODE_PRIVATE);
        if (preferenceSettings.getBoolean(Global.PRE_SHOWMCQ, false) ||
                preferenceSettings.getBoolean(Global.PRE_SHOWTFQ, false) ||
                preferenceSettings.getBoolean(Global.PRE_SHOWMAQ, false) ||
                preferenceSettings.getBoolean(Global.PRE_SHOWSQ, false))
        {
            toggleButtonHelp.setChecked(true);
        }
        else
        {
            toggleButtonHelp.setChecked(false);
        }
        // Sæt on click
        toggleButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferenceSettings = getSharedPreferences(Global.PREFERENCE_FILE, Global.PREFERENCE_MODE_PRIVATE);
                SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
                if (((ToggleButton) v).isChecked())
                {
                    preferenceEditor.putBoolean(Global.PRE_SHOWMCQ, true);
                    preferenceEditor.putBoolean(Global.PRE_SHOWTFQ, true);
                    preferenceEditor.putBoolean(Global.PRE_SHOWMAQ, true);
                    preferenceEditor.putBoolean(Global.PRE_SHOWSQ, true);
                }
                else
                {
                    preferenceEditor.putBoolean(Global.PRE_SHOWMCQ, false);
                    preferenceEditor.putBoolean(Global.PRE_SHOWTFQ, false);
                    preferenceEditor.putBoolean(Global.PRE_SHOWMAQ, false);
                    preferenceEditor.putBoolean(Global.PRE_SHOWSQ, false);
                }
                preferenceEditor.apply();
            }
        });

        // Button start
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Få antal spørgsmål
                int numberOfQuestions = 0;
                Boolean allQuestions = false;
                switch (spinnerNumberOfQuestions.getSelectedItemPosition())
                {
                    case 0:
                        numberOfQuestions = 10;
                        break;
                    case 1:
                        numberOfQuestions = 20;
                        break;
                    case 2:
                        numberOfQuestions = 30;
                        break;
                    case 3:
                        numberOfQuestions = 40;
                        break;
                    case 4:
                        numberOfQuestions = 60000;
                        allQuestions = true;
                        break;
                }

                // Tilføj spørgsmål til liste
                DBHandler dbHandler = new DBHandler(PreparingActivity.this);
                //DBHandler dbHandler = new DBHandler(MainActivity.this, null, null, 1);
                QuestionList questionList = new QuestionList();
                questionList.clearList();
                ArrayList<Integer> questionIdsMCQ;
                ArrayList<Integer> questionIdsTFQ;
                ArrayList<Integer> questionIdsMAQ;
                ArrayList<Integer> questionIdsSQ;
                int numberOfMCQ;
                int numberOfTFQ;
                int numberOfMAQ;
                int numberOfSQ;

                // Få nogle Multiple Choice Questions
                questionIdsMCQ = dbHandler.getRandomQuestion(numberOfQuestions, Global.TYPE_MCQ);
                // Få nogle True False Questions
                questionIdsTFQ = dbHandler.getRandomQuestion(numberOfQuestions, Global.TYPE_TFQ);
                // Få nogle Multiple Answer Questions
                questionIdsMAQ = dbHandler.getRandomQuestion(numberOfQuestions, Global.TYPE_MAQ);
                // Få nogle Sequence Questions
                questionIdsSQ = dbHandler.getRandomQuestion(numberOfQuestions, Global.TYPE_SQ);

                // Sæt procent efter hvor mange spørgsmål man vil svare på
                if (allQuestions)
                {
                    numberOfMCQ = questionIdsMCQ.size(); //Math.floor(numberOfQuestions * 0.75)
                    numberOfTFQ = questionIdsTFQ.size();
                    numberOfMAQ = questionIdsMAQ.size();
                    numberOfSQ = questionIdsSQ.size();
                }
                else
                {
                    numberOfMCQ = (int) Math.floor(numberOfQuestions * 0.35);
                    numberOfTFQ = (int) Math.floor(numberOfQuestions * 0.35);
                    numberOfMAQ = (int) Math.floor(numberOfQuestions * 0.15);
                    numberOfSQ = (int) Math.floor(numberOfQuestions * 0.15);
                }

                // Indsæt angivne procentdel af hver spørgsmåltype
                for (int i = 0; i < numberOfMCQ; i++)
                {
                    if (questionIdsMCQ.size() > 0)
                    {
                        questionList.addQuestionToList(Global.TYPE_MCQ, questionIdsMCQ.get(0));
                        questionIdsMCQ.remove(0);
                    }
                }
                for (int i = 0; i < numberOfTFQ; i++)
                {
                    if (questionIdsTFQ.size() > 0)
                    {
                        questionList.addQuestionToList(Global.TYPE_TFQ, questionIdsTFQ.get(0));
                        questionIdsTFQ.remove(0);
                    }
                }
                for (int i = 0; i < numberOfMAQ; i++)
                {
                    if (questionIdsMAQ.size() > 0)
                    {
                        questionList.addQuestionToList(Global.TYPE_MAQ, questionIdsMAQ.get(0));
                        questionIdsMAQ.remove(0);
                    }
                }
                for (int i = 0; i < numberOfSQ; i++)
                {
                    if (questionIdsSQ.size() > 0)
                    {
                        questionList.addQuestionToList(Global.TYPE_SQ, questionIdsSQ.get(i));
                        questionIdsSQ.remove(0);
                    }
                }

                // Hvis der ikke er indsat nok spørgsmål indsat så lidt flere
                if (!allQuestions) {
                    Boolean searchForQuestions = true;
                    while (questionList.getQuestionsCount() < numberOfQuestions && searchForQuestions) {

                        // Indsæt MCQ hvis flere tilbage
                        Boolean mcqLeft = true;
                        if (questionIdsMCQ.size() > 0) {
                            questionList.addQuestionToList(Global.TYPE_MCQ, questionIdsMCQ.get(0));
                            questionIdsMCQ.remove(0);
                        } else {
                            mcqLeft = false;
                        }

                        // Indsæt TFQ hvis flere tilbage
                        Boolean tfqLeft = true;
                        if (questionIdsTFQ.size() > 0) {
                            questionList.addQuestionToList(Global.TYPE_TFQ, questionIdsTFQ.get(0));
                            questionIdsTFQ.remove(0);
                        } else {
                            tfqLeft = false;
                        }

                        // Indsæt MAQ hvis flere tilbage
                        Boolean maqLeft = true;
                        if (questionIdsMAQ.size() > 0) {
                            questionList.addQuestionToList(Global.TYPE_MAQ, questionIdsMAQ.get(0));
                            questionIdsMAQ.remove(0);
                        } else {
                            maqLeft = false;
                        }

                        // Indsæt SQ hvis flere tilbage
                        Boolean sqLeft = true;
                        if (questionIdsSQ.size() > 0) {
                            questionList.addQuestionToList(Global.TYPE_SQ, questionIdsSQ.get(0));
                            questionIdsSQ.remove(0);
                        } else {
                            sqLeft = false;
                        }

                        // Hvis der ikke er flere spørgsmål tilbage, søg da ikke efter flere.
                        if (!mcqLeft && !tfqLeft && !maqLeft && !sqLeft) {
                            searchForQuestions = false;
                        }
                    }
                }

                // Bland spørgsmålene på listen
                questionList.mixQuestions();

                // Sæt antallet af spørgsmål jeg skal til at besvare og reset correct
                Global global = new Global();
                global.setNumberOfQuestions(questionList.getQuestionsCount());
                global.resetNumberOfQuestionsCorrect();
                global.resetPoints();

                // Sæt starttid
                global.setStartTime();

                // Gå til mit spørgsmål
                Intent intent;
                if (questionList.getQuestionsCount() > 0) {
                    switch (questionList.getNextQuestionType()) {
                        case Global.TYPE_MCQ:
                        case Global.TYPE_TFQ:
                        case Global.TYPE_MAQ:
                            // Gå til Multiple Choice Question
                            intent = new Intent(PreparingActivity.this, MultipleChoiceQuestionActivity.class);
                            break;

                        case Global.TYPE_SQ:
                            // Gå til Sequence Question
                            intent = new Intent(PreparingActivity.this, SequenceQuestionActivity.class);
                            break;

                        default:
                            // Gå til Hovedmenu
                            Log.d(TAG, "Fejl i type på spørgsmål.");
                            intent = new Intent(PreparingActivity.this, MainActivity.class);
                            break;
                    }
                }
                else
                {
                    // Gå til Hovedmenu
                    Log.d(TAG, "Fejl: ikke nogle spørgsmål på liste.");
                    intent = new Intent(PreparingActivity.this, MainActivity.class);
                }

                // Finish activity
                finish();

                // Gå til næste aktivitet
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
                Intent intent = new Intent(PreparingActivity.this, MainActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_preparing, menu);
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
