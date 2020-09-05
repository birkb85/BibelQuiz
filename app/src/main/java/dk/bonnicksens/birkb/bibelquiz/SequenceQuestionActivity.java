package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class SequenceQuestionActivity extends Activity /*AppCompatActivity*/ {

    private static final String TAG = "SeqQuesActivity";

    // Hold spørgsmål
    SequenceQuestion question;

    // Gesturedetector for at kikke efter longpresses
    GestureDetector gestureDetector;
    View viewToMove;

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
        setContentView(R.layout.activity_sequence_question);

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

        // Detecter om man longpresser på en knap man vil flytte, se mere i touchlisteneren
        gestureDetector = new GestureDetector(SequenceQuestionActivity.this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

                // Hvis det view man vil flytte er defineret så start flytningen
                if (viewToMove != null)
                {
                    // Vis en skygge og returner true for at vise at man flytter den
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(viewToMove);
                    viewToMove.startDrag(data, shadowBuilder, viewToMove, 0);
                }

                //Log.d(TAG, "Long press detected");
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }
        });

        // Hent et spørgsmål fra databasen
        DBHandler dbHandler = new DBHandler(SequenceQuestionActivity.this);
        //DBHandler dbHandler = new DBHandler(MultipleChoiceQuestionActivity.this, null, null, 1);
        final QuestionList questionList = new QuestionList();
        if (questionList.getQuestionsCount() > 0) {
            question = dbHandler.getSequenceQuestion(questionList.getNextQuestion().getIndex());
        }

        // Hvis spørgsmålet ikke fandtes i databasen lav et standard et
        if (question == null)
        {
            question = new SequenceQuestion();
            question.setQuestion("Fejl, spørgsmål ikke fundet. :(", "", "Luk appen og start den igen");
            question.addAnswer("Nr. 1", 0);
            question.addAnswer("Nr. 2", 1);
        }

        // Sæt spørgsmålstekst
        textViewQuestion.setText(question.getQuestion());
        textViewQuestion2.setText(question.getQuestion2());

        for (int i = 0; i < question.getAnswerCount(); i++)
        {
            final Button button = new Button(SequenceQuestionActivity.this);
            button.setBackgroundResource(R.drawable.shape_sequencebutton_up);
            button.setText(question.getAnswer(i).getAnswer());
            button.setTag(question.getAnswer(i).getSequenceNumber()); // Sætter tag til svarets sequrence nummer, så kan jeg tjekke rækkefølgen
            button.setOnTouchListener(new ButtonTouchListener()); // Når man vil flytte en knap
            button.setOnDragListener(new ButtonDragListener()); // Tjekker om knappen man flytter kan flyttes her til

            // Tilføj knap til layout på tilfældig placering
            Random random = new Random();
            float randomFloat = random.nextFloat();
            int layoutChildren = linearLayoutAnswers.getChildCount();
            int index = Math.round(randomFloat * (float) layoutChildren);
            linearLayoutAnswers.addView(button, index);

            // Sær margins på knap
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)button.getLayoutParams();
            params.setMargins(80, 10, 80, 10); //substitute parameters for left, top, right, bottom
            button.setLayoutParams(params);
        }

        // Når man klikker på valideringsknappen
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SequenceQuestionActivity.this, ValidateAnswerActivity.class);

                // Vedhæft om svaret var rigtigt eller forkert
                Boolean isRight = true;
                int countPoints = 0;
                for (int i = 0; i < linearLayoutAnswers.getChildCount(); i++) {
                    if (Integer.parseInt(linearLayoutAnswers.getChildAt(i).getTag().toString()) == i) {
                        // tilføj point til puljen for hver rigtig placeret
                        countPoints++;
                    } else {
                        // Hvis der er en forkert placeret er svaret forkert
                        isRight = false;
                    }
                }
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
                global.addToNumberOfPointsTotal(question.getAnswerCount());

                // Put info omkring point
                intent.putExtra(Global.PUT_POINTS, countPoints);
                intent.putExtra(Global.PUT_POINTSTOTAL, question.getAnswerCount());

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
        buttonHelp.setBackgroundResource(R.drawable.selector_sequencebutton);

        // Hjælp knappen
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lav dialogboks og vis den
                AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(SequenceQuestionActivity.this);

                LayoutInflater inflater = SequenceQuestionActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_questionhelp, null);
                dialogAlert.setView(dialogView);

                TextView textViewTitle = (TextView) dialogView.findViewById(R.id.textViewTitle);
                TextView textViewText = (TextView) dialogView.findViewById(R.id.textViewText);

                switch (questionList.getNextQuestionType())
                {
                    case Global.TYPE_SQ:
                        textViewTitle.setText(R.string.question_helpsq_title);
                        textViewText.setText(R.string.question_helpsq_text);
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
        if (preferenceSettings.getBoolean(Global.PRE_SHOWSQ, false))
        {
            // Vis hjælp
            buttonHelp.performClick();

            // Gem at man har set hjælpen
            SharedPreferences.Editor preferenceEditor = preferenceSettings.edit();
            preferenceEditor.putBoolean(Global.PRE_SHOWSQ, false);
            preferenceEditor.apply();
        }
    }

    // Listeneren der tjekker om man rører en knap man vil flytte
    private final class ButtonTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {

            /*
            // Sæt det view man vil til at flytte
            viewToMove = view;

            // Brug gesturedetectoren til at detecte hvornår man må starte flytningen af viewet
            return gestureDetector.onTouchEvent(motionEvent);
            */


            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Vis en skygge og returner true for at vise at man flytter den
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }

        }
    }

    // Listeneren der tjekker om man kan droppe viewet
    private class ButtonDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            //int action = event.getAction();
            View sourceView = (View) event.getLocalState(); // view being dragged. v is view dragging to.

            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    //Toast.makeText(SequenceQuestionActivity.this, "Drag started", Toast.LENGTH_SHORT).show();
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundResource(R.drawable.shape_sequencebutton_down);
                    //Toast.makeText(SequenceQuestionActivity.this, "Drag entered", Toast.LENGTH_SHORT).show();
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundResource(R.drawable.shape_sequencebutton_up);
                    //Toast.makeText(SequenceQuestionActivity.this, "Drag exited", Toast.LENGTH_SHORT).show();
                    break;

                case DragEvent.ACTION_DROP:
                    // definer hvor man vil flytte til
                    int destinationIndex = linearLayoutAnswers.indexOfChild(v);

                    // Slet view og sæt det ind på ny plads
                    linearLayoutAnswers.removeView(sourceView);
                    linearLayoutAnswers.addView(sourceView, destinationIndex);

                    v.setBackgroundResource(R.drawable.shape_sequencebutton_up);

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //Toast.makeText(SequenceQuestionActivity.this, "Drag ended", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

            // Returner sandt for at fortælle at man gerne må flytte her til
            return true;
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
                global.addToNumberOfPointsTotal(question.getAnswerCount());

                // Slet spørgsmålet fra listen
                questionList.removeNextQuestion();

                // Finish activity
                finish();

                // Gå til resultatet
                Intent intent = new Intent(SequenceQuestionActivity.this, ResultActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_sequence_question, menu);
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
