package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class MultipleChoiceQuestionActivity extends Activity /*AppCompatActivity*/ {

    private static final String TAG = "MCQ Activity";

    // Hold spørgsmål
    MultipleChoiceQuestion question;

    // Views
    TextView textViewQuestion;
    TextView textViewQuestion2;
    TextView textViewNumber;
    TextView textViewTotal;
    Button buttonValidate;
    Button buttonHelp;
    LinearLayout linearLayoutAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_question);

        // Sæt views
        textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        textViewQuestion2 = (TextView) findViewById(R.id.textViewQuestion2);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        buttonValidate = (Button) findViewById(R.id.buttonValidate);
        buttonHelp = (Button) findViewById(R.id.buttonHelp);
        linearLayoutAnswers = (LinearLayout) findViewById(R.id.linearLayoutAnswers);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Hent et spørgsmål fra databasen
        DBHandler dbHandler = new DBHandler(MultipleChoiceQuestionActivity.this);
        //DBHandler dbHandler = new DBHandler(MultipleChoiceQuestionActivity.this, null, null, 1);
        final QuestionList questionList = new QuestionList();
        if (questionList.getQuestionsCount() > 0) {
            question = dbHandler.getMultipleChoiceQuestion(questionList.getNextQuestion().getIndex());
        }

        // Hvis spørgsmålet ikke fandtes i databasen lav et standard et
        if (question == null)
        {
            question = new MultipleChoiceQuestion();
            question.setQuestion("Fejl, spørgsmål ikke fundet. :(", "", "Luk appen og start den igen");
            question.addAnswer("Rigtigt svar", true);
            question.addAnswer("Forkert svar", false);
        }

        // Sæt spørgsmålstekst
        textViewQuestion.setText(question.getQuestion());
        textViewQuestion2.setText(question.getQuestion2());

        // Tilføj svar knapper på layoutet
        for (int i = 0; i < question.getAnswerCount(); i++)
        {
            final ToggleButton button = new ToggleButton(MultipleChoiceQuestionActivity.this);
            if (questionList.getNextQuestionType() == Global.TYPE_MAQ) button.setBackgroundResource(R.drawable.selector_togglebutton2);
            button.setText(question.getAnswer(i).getAnswer());
            button.setTextOff(question.getAnswer(i).getAnswer());
            button.setTextOn(question.getAnswer(i).getAnswer());
            button.setTag(question.getAnswer(i).getRightAnswer()); //button.setTag(i); // Birk 24-09-2015
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button.isChecked()) {

                        // Afmarker andre knapper hvis typen er MCQ eller FTQ
                        switch (questionList.getNextQuestionType())
                        {
                            case Global.TYPE_MCQ:
                            case Global.TYPE_TFQ:
                                // Index for knap
                                int buttonIndex = linearLayoutAnswers.indexOfChild(button);

                                // Fjern checked fra andre knapper
                                for (int i = 0; i < linearLayoutAnswers.getChildCount(); i++)
                                {
                                    if (i != buttonIndex) {
                                        ToggleButton toggleButton = (ToggleButton) linearLayoutAnswers.getChildAt(i);
                                        toggleButton.setChecked(false);
                                    }
                                }
                                break;
                        }

                    }
                }
            });

            // Tilføj knap til layout på tilfældig placering
            Random random = new Random();
            float randomFloat = random.nextFloat();
            int layoutChildren = linearLayoutAnswers.getChildCount();
            int index = Math.round(randomFloat * (float) layoutChildren);
            /*
            Log.d("Debug - Random Float", String.valueOf(randomFloat));
            Log.d("Debug - Layout Children", String.valueOf(layoutChildren));
            Log.d("Debug - Layout Index", String.valueOf(index));
            */
            linearLayoutAnswers.addView(button, index);

            // Sær margins på knap
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)button.getLayoutParams();
            params.setMargins(0, 10, 0, 10); //substitute parameters for left, top, right, bottom
            button.setLayoutParams(params);
        }

        // Når man klikker på valideringsknappen
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MultipleChoiceQuestionActivity.this, ValidateAnswerActivity.class);

                // Vedhæft om svaret var rigtigt eller forkert
                Boolean isRight = true;
                int countPoints = 0;
                for (int i = 0; i < linearLayoutAnswers.getChildCount(); i++) {
                    // Få togglebutton
                    ToggleButton toggleButton = (ToggleButton) linearLayoutAnswers.getChildAt(i);

                    // Få point for rigtige svar
                    if (toggleButton.isChecked()) {
                        // Tjek om tag er true
                        if ((boolean) toggleButton.getTag()) {
                            // Tilføj point til puljen for hver rigtig markeret
                            countPoints++;
                        } else {
                            // Ved forkert markeret er svaret forkert
                            isRight = false;

                            // Træk point fra..
                            countPoints--;
                        }
                    } else {
                        // Tjek om tag er true
                        if ((boolean) toggleButton.getTag()) {
                            // Ved rigtigt svar IKKE markeret er svaret forkert
                            isRight = false;
                        }
                    }
                }
                // Man skal ikke kunne få minus point...
                if (countPoints < 0) countPoints = 0;
                // Tjek om svaret er rigtigt
                if (isRight) {
                    // Hvis svaret er rigtigt
                    intent.putExtra(Global.PUT_RIGHTANSWER, true);

                    // Tilføj et rigtigt spørgsmål til listen
                    Global global = new Global();
                    global.addToNumberOfQuestionsCorrect();
                } else {
                    // Hvis svaret er forkert
                    intent.putExtra(Global.PUT_RIGHTANSWER, false);
                }

                // Points
                Global global = new Global();
                // Giv point for rigtigt svar
                global.addToNumberOfPoints(countPoints);
                // Tilføj point til points mulige
                int pointsPossible = 0;
                for (int i = 0; i < linearLayoutAnswers.getChildCount(); i++) {
                    // Tjek om tag er true
                    ToggleButton toggleButton = (ToggleButton) linearLayoutAnswers.getChildAt(i);
                    if ((boolean) toggleButton.getTag()) {
                        pointsPossible++;
                    }
                }
                global.addToNumberOfPointsTotal(pointsPossible);

                // Put info omkring point
                intent.putExtra(Global.PUT_POINTS, countPoints);
                intent.putExtra(Global.PUT_POINTSTOTAL, pointsPossible);

                // Vedhæft info
                intent.putExtra(Global.PUT_INFO, question.getInfo());

                // Slet spørgsmålet fra listen
                QuestionList questionList = new QuestionList();
                questionList.removeNextQuestion();

                // Finish activity
                finish();

                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Sæt hjælp knappen
        if (questionList.getNextQuestionType() == Global.TYPE_MAQ) buttonHelp.setBackgroundResource(R.drawable.selector_button2);

        // Hjælp knappen
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lav dialogboks og vis den
                AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(MultipleChoiceQuestionActivity.this);

                LayoutInflater inflater = MultipleChoiceQuestionActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_questionhelp, null);
                dialogAlert.setView(dialogView);

                TextView textViewTitle = (TextView) dialogView.findViewById(R.id.textViewTitle);
                TextView textViewText = (TextView) dialogView.findViewById(R.id.textViewText);

                switch (questionList.getNextQuestionType())
                {
                    case Global.TYPE_MCQ:
                        textViewTitle.setText(R.string.question_helpmcq_title);
                        textViewText.setText(R.string.question_helpmcq_text);
                        break;

                    case Global.TYPE_TFQ:
                        textViewTitle.setText(R.string.question_helptfq_title);
                        textViewText.setText(R.string.question_helptfq_text);
                        break;

                    case Global.TYPE_MAQ:
                        textViewTitle.setText(R.string.question_helpmaq_title);
                        textViewText.setText(R.string.question_helpmaq_text);
                        break;
                }

                dialogAlert.create().show();
            }
        });

        // Sæt spørgsmål nummer
        Global global = new Global();
        textViewTotal.setText(String.valueOf(global.getNumberOfQuestions()));
        textViewNumber.setText(String.valueOf(global.getNumberOfQuestions() - (questionList.getQuestionsCount() - 1)));

        // Se om man skal vise hjælpe teksten
        int PREFERENCE_MODE_PRIVATE = 0;
        SharedPreferences preferenceSettings = getSharedPreferences(Global.PREFERENCE_FILE, Global.PREFERENCE_MODE_PRIVATE);
        switch (questionList.getNextQuestionType())
        {
            case Global.TYPE_MCQ:
                if (preferenceSettings.getBoolean(Global.PRE_SHOWMCQ, false))
                {
                    // Vis hjælp
                    buttonHelp.performClick();

                    // Gem at man har set hjælpen
                    SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
                    preferenceEditor.putBoolean(Global.PRE_SHOWMCQ, false);
                    preferenceEditor.apply();
                }
                break;

            case Global.TYPE_TFQ:
                if (preferenceSettings.getBoolean(Global.PRE_SHOWTFQ, false))
                {
                    // Vis hjælp
                    buttonHelp.performClick();

                    // Gem at man har set hjælpen
                    SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
                    preferenceEditor.putBoolean(Global.PRE_SHOWTFQ, false);
                    preferenceEditor.apply();
                }
                break;

            case Global.TYPE_MAQ:
                if (preferenceSettings.getBoolean(Global.PRE_SHOWMAQ, false))
                {
                    // Vis hjælp
                    buttonHelp.performClick();

                    // Gem at man har set hjælpen
                    SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
                    preferenceEditor.putBoolean(Global.PRE_SHOWMAQ, false);
                    preferenceEditor.apply();
                }
                break;
        }
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
                global.setNumberOfQuestions(global.getNumberOfQuestions() - (questionList.getQuestionsCount() - 1));

                // Tilføj point til points mulige
                global.addToNumberOfPointsTotal(1);

                // Slet spørgsmålet fra listen
                questionList.removeNextQuestion();

                // Finish activity
                finish();

                // Gå til resultatet
                Intent intent = new Intent(MultipleChoiceQuestionActivity.this, ResultActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_multiple_choice_question, menu);
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
