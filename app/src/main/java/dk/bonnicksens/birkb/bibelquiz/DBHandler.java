package dk.bonnicksens.birkb.bibelquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by BirkB on 01-09-2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";

    private static final int DATABASE_VERSION = 1; // Læg en til database version ved hver udgivelse, så databasen opdateres!!!
    private static final String DATABASE_NAME = "QuizDB.db";

    // Question table
    private static final String TABLE_QUESTION = "Table_Question";
    public static final String Q_COLUMN_ID = "Id"; // Id på spørgsmål
    public static final String Q_COLUMN_DATABASEID = "DatabaseId"; // Id hvis jeg udvider med at man kan opdatere fra online database
    public static final String Q_COLUMN_TYPE = "Type"; // Type på spørgsmål
    public static final String Q_COLUMN_DIFFICULTY = "Difficulty"; // Sværhedsgrad på spørgsmål
    public static final String Q_COLUMN_QUESTION = "Question"; // Spørgsmålsteksten
    public static final String Q_COLUMN_QUESTION2 = "Question2"; // Spørgsmålsteksten 2
    public static final String Q_COLUMN_INFO = "Info"; // Info omkring spørgsmål

    // Answer table
    private static final String TABLE_ANSWER = "Table_Answer";
    public static final String A_COLUMN_ID = "Id"; // Id på svar
    public static final String A_COLUMN_QUESTIONID = "QuestionId"; // Id på spørgsmål svaret hører til
    public static final String A_COLUMN_ANSWER = "Answer"; // Svarteksten
    public static final String A_COLUMN_VALUE = "Value"; // Værdi for svaret

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /*
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    */

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Sker første gang databasen oprettes
        // Tables laves
        // Question
        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + "(" +
                Q_COLUMN_ID + " INTEGER PRIMARY KEY," +
                Q_COLUMN_DATABASEID + " INTEGER," +
                Q_COLUMN_TYPE + " TEXT," +
                Q_COLUMN_DIFFICULTY + " INTEGER," +
                Q_COLUMN_QUESTION + " TEXT," +
                Q_COLUMN_QUESTION2 + " TEXT," +
                Q_COLUMN_INFO + " TEXT" + ")";
        db.execSQL(CREATE_QUESTION_TABLE);

        // Answer
        String CREATE_ANSWER_TABLE = "CREATE TABLE " + TABLE_ANSWER + "(" +
                A_COLUMN_ID + " INTEGER PRIMARY KEY," +
                A_COLUMN_QUESTIONID + " INTEGER," +
                A_COLUMN_ANSWER + " TEXT," +
                A_COLUMN_VALUE + " INTEGER" + ")";
        db.execSQL(CREATE_ANSWER_TABLE);

        // Tilføj multiple choice questions to database
        addMultipleChoiceQuestionsToDatabase(db);

        // Tilføj true false questions to database
        addTrueFalseQuestionsToDatabase(db);

        // Tilføj multiple answer questions to database
        addMultipleAnswerQuestionsToDatabase(db);

        // Tilføj sequence questions to database
        addSequenceQuestionsToDatabase(db);

        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Sker ved opdatering af database
        // Tables droppes og oprettes igen
        // Question
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER);

        onCreate(db);
    }

    private void addQuestionToDatabase(MultipleChoiceQuestion question, String type, SQLiteDatabase db) {

        // MULTIPLE CHOICE QUESTION
        // Tilføj spørgsmål og svar til database
        // Tilføj spørgsmål
        ContentValues qValues = new ContentValues();
        qValues.put(Q_COLUMN_DATABASEID, -1);
        qValues.put(Q_COLUMN_TYPE, type);
        qValues.put(Q_COLUMN_DIFFICULTY, 0); // Ikke helt implementeret endnu
        qValues.put(Q_COLUMN_QUESTION, question.getQuestion());
        qValues.put(Q_COLUMN_QUESTION2, question.getQuestion2());
        qValues.put(Q_COLUMN_INFO, question.getInfo());
        long questionId = db.insert(TABLE_QUESTION, null, qValues);
        //Log.d(TAG, "Id of question added to database: " + String.valueOf(questionId));

        // Tilføj svar til spørgsmål
        for (int i = 0; i < question.getAnswerCount(); i++)
        {
            ContentValues aValues = new ContentValues();
            aValues.put(A_COLUMN_QUESTIONID, questionId);
            aValues.put(A_COLUMN_ANSWER, question.getAnswer(i).getAnswer());
            aValues.put(A_COLUMN_VALUE, question.getAnswer(i).getRightAnswer());
            db.insert(TABLE_ANSWER, null, aValues);
        }
    }

    private void addQuestionToDatabase(SequenceQuestion question, String type, SQLiteDatabase db) {

        // SEQUENCE QUESTION
        // Tilføj spørgsmål og svar til database
        // Tilføj spørgsmål
        ContentValues qValues = new ContentValues();
        qValues.put(Q_COLUMN_DATABASEID, -1);
        qValues.put(Q_COLUMN_TYPE, type);
        qValues.put(Q_COLUMN_DIFFICULTY, 0); // Ikke helt implementeret endnu
        qValues.put(Q_COLUMN_QUESTION, question.getQuestion());
        qValues.put(Q_COLUMN_QUESTION2, question.getQuestion2());
        qValues.put(Q_COLUMN_INFO, question.getInfo());
        long questionId = db.insert(TABLE_QUESTION, null, qValues);
        //Log.d(TAG, "Id of question added to database: " + String.valueOf(questionId));

        // Tilføj svar til spørgsmål
        for (int i = 0; i < question.getAnswerCount(); i++)
        {
            ContentValues aValues = new ContentValues();
            aValues.put(A_COLUMN_QUESTIONID, questionId);
            aValues.put(A_COLUMN_ANSWER, question.getAnswer(i).getAnswer());
            aValues.put(A_COLUMN_VALUE, question.getAnswer(i).getSequenceNumber());
            db.insert(TABLE_ANSWER, null, aValues);
        }
    }

    public ArrayList<Integer> getRandomQuestion(int number, String type)
    {
        // Få id'erne få et tilfældigt antal spørgsmål
        ArrayList<Integer> questionIds = new ArrayList<>();

        String query = "SELECT " + Q_COLUMN_ID + " FROM " + TABLE_QUESTION + " WHERE " + Q_COLUMN_TYPE + " = \"" + type + "\" ORDER BY RANDOM() LIMIT " + number;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                questionIds.add(Integer.parseInt(cursor.getString(0)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        db.close();

        return questionIds;
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestion(int id) {

        // Multiple choice question
        // Find et spørgsmål med givent id.
        String qQuery = "SELECT " + Q_COLUMN_QUESTION + "," + Q_COLUMN_QUESTION2 + "," + Q_COLUMN_INFO + " FROM " + TABLE_QUESTION + " WHERE " + Q_COLUMN_ID + " = \"" + id + "\"";
        String aQuery = "SELECT " + A_COLUMN_ANSWER + "," + A_COLUMN_VALUE + " FROM " + TABLE_ANSWER + " WHERE " + A_COLUMN_QUESTIONID + " = \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor qCursor = db.rawQuery(qQuery, null);
        Cursor aCursor = db.rawQuery(aQuery, null);

        MultipleChoiceQuestion question = new MultipleChoiceQuestion();

        if (qCursor.moveToFirst())
        {
            qCursor.moveToFirst();
            String qQuestion = qCursor.getString(0);
            String qQuestion2 = qCursor.getString(1);
            String qInfo = qCursor.getString(2);
            question.setQuestion(qQuestion, qQuestion2, qInfo);
            qCursor.close();

            if (aCursor.moveToFirst())
            {
                aCursor.moveToFirst();
                while (!aCursor.isAfterLast())
                {
                    String aAnswer = aCursor.getString(0);
                    Boolean aRightAnswer = aCursor.getInt(1) == 1; //aCursor.getInt(3) == 1 ? true : false;
                    question.addAnswer(aAnswer, aRightAnswer);
                    aCursor.moveToNext();
                }
                aCursor.close();
            }
        }
        else
        {
            question = null;
        }

        db.close();

        return question;
    }

    public SequenceQuestion getSequenceQuestion(int id) {

        // Sequence question
        // Find et spørgsmål med givent id.
        String qQuery = "SELECT " + Q_COLUMN_QUESTION + "," + Q_COLUMN_QUESTION2 + "," + Q_COLUMN_INFO + " FROM " + TABLE_QUESTION + " WHERE " + Q_COLUMN_ID + " = \"" + id + "\"";
        String aQuery = "SELECT " + A_COLUMN_ANSWER + "," + A_COLUMN_VALUE + " FROM " + TABLE_ANSWER + " WHERE " + A_COLUMN_QUESTIONID + " = \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor qCursor = db.rawQuery(qQuery, null);
        Cursor aCursor = db.rawQuery(aQuery, null);

        SequenceQuestion question = new SequenceQuestion();

        if (qCursor.moveToFirst())
        {
            qCursor.moveToFirst();
            String qQuestion = qCursor.getString(0);
            String qQuestion2 = qCursor.getString(1);
            String qInfo = qCursor.getString(2);
            question.setQuestion(qQuestion, qQuestion2, qInfo);
            qCursor.close();

            if (aCursor.moveToFirst())
            {
                aCursor.moveToFirst();
                while (!aCursor.isAfterLast())
                {
                    String aAnswer = aCursor.getString(0);
                    int aSequenceNumber = aCursor.getInt(1);
                    question.addAnswer(aAnswer, aSequenceNumber);
                    aCursor.moveToNext();
                }
                aCursor.close();
            }
        }
        else
        {
            question = null;
        }

        db.close();

        return question;
    }

    private void addMultipleChoiceQuestionsToDatabase(SQLiteDatabase db)
    {
        // Multiple choice questions
        MultipleChoiceQuestion mcq;

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor mange hustruer havde Abraham?",
                "",
                "Abraham havde 3 hustruer:\nSara, Hagar og Ketura."
        );
        mcq.addAnswer("1", false);
        mcq.addAnswer("2", false);
        mcq.addAnswer("3", true);
        mcq.addAnswer("4", false);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvilken findes ikke i blandt de stammer der fik en arvelod i Israel?",
                "",
                "Israels tolv stammer der fik en arvelod var:\nRuben, Simeon, Juda, Zebulon, Issakar, Dan, Gad, Aser, Naftali, Efraim, Manasse og Benjamin."
        );
        mcq.addAnswer("Aser", false);
        mcq.addAnswer("Issakar", false);
        mcq.addAnswer("Zebulon", false);
        mcq.addAnswer("Hebron", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvilket navn bruges ikke om Jesus?",
                "",
                "Gabriel og Mikael er de to eneste engle der i bibelen nævnes ved navn.\nGabriel er nært knyttet til den himmelske trone, men er ikke et navn for Jesus."
        );
        mcq.addAnswer("Mikael", false);
        mcq.addAnswer("Immanuel", false);
        mcq.addAnswer("Gabriel", true);
        mcq.addAnswer("Ordet", false);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor gammel var Noa da vandfloden begyndte?",
                "",
                "Noa var 600 år da vandfloden begyndte. Jævnfør 1 Mosebog 7:6."
        );
        mcq.addAnswer("200 år", false);
        mcq.addAnswer("400 år", false);
        mcq.addAnswer("450 år", false);
        mcq.addAnswer("600 år", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvilken fugl sendte Noa ikke ud af arken?",
                "",
                "Noa sendte først en Ravn ud, derefter sendte han en due. Jævnfør 1 Mosebog 8:7-12."
        );
        mcq.addAnswer("Ravn", false);
        mcq.addAnswer("Due", false);
        mcq.addAnswer("Krage", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor længe var Noa i arken?",
                "",
                "Noa var i arken 1 år og 10 dage, hvor hver måned er 30 dage.\nJævnfør Indsigt bind 2 s. 1099."
        );
        mcq.addAnswer("40 dage og nætter", false);
        mcq.addAnswer("150 dage", false);
        mcq.addAnswer("1 år og 10 dage", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Noa var far til Kam, hvem blev Kam far til?",
                "",
                "Kam blev far til Kanaan.\nHan blev også far til Kusj som var far til Nimrod.\nAltså nåede Noa at opleve Nimrod i opposition til Jehova."
        );
        mcq.addAnswer("Nimrod", false);
        mcq.addAnswer("Magog", false);
        mcq.addAnswer("Kanaan", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvilken by grundlagde Nimrod ikke?",
                "",
                "Denne onde mand grundlagde flere byer i opposition til Jehova.\nMen Gomorra var ikke en af dem."
        );
        mcq.addAnswer("Babel", false);
        mcq.addAnswer("Nineve", false);
        mcq.addAnswer("Kala", false);
        mcq.addAnswer("Gomorra", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvem var Noa's oldefar?",
                "",
                "Enok var Noa's oldefar."
        );
        mcq.addAnswer("Metusalem", false);
        mcq.addAnswer("Set", false);
        mcq.addAnswer("Enok", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvem begravede Isak?",
                "",
                "Selvom der var fjendskab imellem brødrene i en sådan grad at Jakob frygtede for sit liv flere gange, fortæller bibelen at Jakob og Esau begravede deres far sammen.\nJævnfør 1 Mosebog 35:29."
        );
        mcq.addAnswer("Rebekka", false);
        mcq.addAnswer("Jakob", false);
        mcq.addAnswer("Jakob og Esau", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor gammel var Isak da han giftede sig med Rebekka?",
                "",
                "Isak var villig til at vente på en kvinde der tjente Jehova og som havde de rigtige egenskaber.\nHan var 40 år da de giftede sig.\nJævnfør 1 Mosebog 25:20."
        );
        mcq.addAnswer("20 år", false);
        mcq.addAnswer("32 år", false);
        mcq.addAnswer("43 år", false);
        mcq.addAnswer("40 år", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvem skrev Job's bog?",
                "",
                "Moses skrev Job's bog."
        );
        mcq.addAnswer("Job", false);
        mcq.addAnswer("Josua", false);
        mcq.addAnswer("Moses", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor skrev Johannes Åbenbaringsbogen?",
                "",
                "Johannes var fængslet på øen Patmos da han skrev Åbenbaringsbogen."
        );
        mcq.addAnswer("Rom", false);
        mcq.addAnswer("Jerusalem", false);
        mcq.addAnswer("Cypern", false);
        mcq.addAnswer("Patmos", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvem sagde Jehova følgende til?",
                "\"Har jeg ikke overdraget dig hvervet? Vær modig og stærk. Bliv ikke opskræmt og vær ikke skrækslagen, for Jehova din Gud er med dig overalt hvor du færdes.\"",
                "Josua fik denne opmuntring da han lige havde fået opgaven med lede Israel overdraget efter Moses død.\nJævnfør Josua 1:9."
        );
        mcq.addAnswer("Jakob", false);
        mcq.addAnswer("Nehemias", false);
        mcq.addAnswer("Gideon", false);
        mcq.addAnswer("Josua", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Under hvilken konge blev Israel delt i 2 riger?",
                "",
                "Det var i Rehabeam's regeringstid.\nJeroboam fik 10-stammeriget af Jehova pga. Salomons afgudsdyrkelse.\nJævnfør 1 Kongebog 11:10-13.\nFra da af var der 10-stammeriget Israel og 2-stammeriget Juda."
        );
        mcq.addAnswer("Ahazja", false);
        mcq.addAnswer("Jojada", false);
        mcq.addAnswer("Jojakin", false);
        mcq.addAnswer("Rehabeam", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor lang tid gik der fra Jehova bekendgjorde, at han ville udslette de onde mennesker, til vandfloden kom?",
                "",
                "Der gik 120 år.\nJævnfør 1 Mosebog 6:3."
        );
        mcq.addAnswer("40-50 år", false);
        mcq.addAnswer("50-60 år", false);
        mcq.addAnswer("Ved vi ikke", false);
        mcq.addAnswer("120 år", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvorfor krævede det mod af israelitterne at smøre blod fra vædderlammet på dørstolperne i Ægypten?",
                "",
                "Guden Amon-Re var afbilledet med en vædder og ansås for at være den øverste guddom og farao ansås som værende inkarnation af Amon-Re.\nPræsteskabet afsagde dom og hvis man forsætligt havde dræbt et helligt dyr skulle man dø.\nDesuden blev strafffen ifølge Herodot eksekveret af en vred pøbel.\nJævnfør Indsigt bind 2 s. 1188-1189 og Indsigt bind 1 s. 772."
        );
        mcq.addAnswer("Ægypterne gik meget op i at holde alt rent", false);
        mcq.addAnswer("Ægypterne kunne se de tilbad Jehova", false);
        mcq.addAnswer("Ægypterne anså dyr for hellige, især vædderen", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvorfor var det et krav at farao lod israelitterne drage ud af Ægypten for at tilbede og ofre til Jehova?",
                "",
                "Ægypterne anså især væddere og tyre som hellige dyr.\nPræsterne afsagde dødsdommen og ifølge Herodot blev straffen eksekveret af en vred pøbel.\nMåske håbede farao at det ville skræmme israelitterne nok til at opgive.\nJævnfør Indsigt bind 2 s. 1188-1189 og Indsigt bind 1 s. 772."
        );
        mcq.addAnswer("Farao skulle ydmyges", false);
        mcq.addAnswer("Jøderne ville ikke have noget med ægypterne at gøre", false);
        mcq.addAnswer("Der var dødsstraf for at slagte bla. væddere og tyre", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvem bliver øjensynligt beskrevet i Ezekiel 28:11-19 som den skærmende kerub?",
                "",
                "Klagesangen i Ezekiels bog kapitel 28 er henvendt til Tyrus konge, men der er flere lighedspunkter til Satan.\nHan var hovmodig, gjorde sig selv til gud, var i \"Eden Guds have\", \"På Guds hellige bjerg var du\", var uangribelig i sin adfærd \"til den dag der fandtes uretfærdighed\" hos ham og han blev berøvet sin skønhed og gjort til \"aske på jorden\"."
        );
        mcq.addAnswer("Salomon", false);
        mcq.addAnswer("Jesus", false);
        mcq.addAnswer("Satan", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvad betyder \"malakh\" (hebraisk) og \"aggelos\" (græsk), der normalt oversættes med ordet engel?",
                "",
                "Undertiden bruges disse titler også om mennesker og er da oversat \"sendebud\", men når det drejer sig om åndeskabninger oversættes det med ordet engel.\nJævnfør Indsigt bind 1 s. 488."
        );
        mcq.addAnswer("Tjener", false);
        mcq.addAnswer("Åndeskabning", false);
        mcq.addAnswer("Sendebud", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "I Samuel 4:4 omtales Jehova som \"han som troner på ......\"?",
                "",
                "På låget af pagtens ark var afbilledet 2 keruber og imellem disse ville Jehova tale til Moses.\nJævnfør Indsigt bind 1 s. 1255."
        );
        mcq.addAnswer("Stjerner", false);
        mcq.addAnswer("Himle", false);
        mcq.addAnswer("Keruber", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor mange gange viste engelen Gabriel sig for Jehovas tjenere?",
                "",
                "Engelen Gabriel viste sig 2 gange for Daniel i synet om gedebukken og vædderen og de 70 uger.\nHan viste sig for præsten Zakarias og fortalte, at hans hustru ville føde Johannes Døberen og for Maria for at fortælle hende, at hun skulle føde Jesus."
        );
        mcq.addAnswer("6", false);
        mcq.addAnswer("2", false);
        mcq.addAnswer("1", false);
        mcq.addAnswer("4", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvad betyder navnet Gabriel?",
                "",
                "Bortset fra Mikael er han den eneste engel vi kender ved navn.\nGabriel er en højtstående engel, der siges om ham, at han \"står lige foran Gud\", at han blev\"udsendt fra Gud\"."
        );
        mcq.addAnswer("Jehova udfrier", false);
        mcq.addAnswer("Jehova er frelse", false);
        mcq.addAnswer("Sendebud", false);
        mcq.addAnswer("En Guds mand", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvorfor måtte Israels konge ikke anskaffe sig mange heste?",
                "",
                "Heste og stridsvogne var datidens ypperste krigsvåben og ved at anskaffe sig disse risikerede man, at glemme at det var Jehova der vandt Israels krige.\nDe skulle stole helt på ham.\nJævnfør Indsigt bind 1 s. 905."
        );
        mcq.addAnswer("Kongen skulle undgå materialisme", false);
        mcq.addAnswer("Heste ville koste for meget at holde", false);
        mcq.addAnswer("Heste kunne kun købes hos hedenske nationer", false);
        mcq.addAnswer("Heste gav militær slagkraft", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor meget småkvæg havde Noa med sig i arken, af hver art?",
                "",
                "Af hver af de rene arter tog han 7.\nIfølge hebrarisk gramatik betyder en gentagelse af en mængde ikke nødvendigvis at mængden skal fordobles, men derimod at mængden fordeles. Jævnfør 1 Mosebog 7:2."
        );
        mcq.addAnswer("11", false);
        mcq.addAnswer("14", false);
        mcq.addAnswer("2", false);
        mcq.addAnswer("7", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor strandede Noa's ark?",
                "",
                "Noa's ark strandede på Ararats bjerge. Jævnfør 1 Mosebog 8:4."
        );
        mcq.addAnswer("Horebs bjerg", false);
        mcq.addAnswer("Morijas bjerg", false);
        mcq.addAnswer("Hermons bjerg", false);
        mcq.addAnswer("Ararats bjerge", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvilket dyr skulle Noa kun have 2 af med i arken?",
                "",
                "Noa havde kun 2 krokodiller med. Han tog kun 2 med af hver uren art, 7 af hver ren og 7 af hver af de flyvende skabninger. Jævnfør 1 Mosebog 7:2,3."
        );
        mcq.addAnswer("Gæs", false);
        mcq.addAnswer("Kvæg", false);
        mcq.addAnswer("Hajer", false);
        mcq.addAnswer("Krokodiller", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvordan døde ypperstepræsten Eli?",
                "",
                "Eli faldt bagover fra sin stol og brækkede halsen, han døde i en ulykke. Jævnfør 1 Samuel 4:18."
        );
        mcq.addAnswer("Ved sværdet?", false);
        mcq.addAnswer("På grund af sygdom?", false);
        mcq.addAnswer("Ved en ulykke?", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvad blev de engle der syndede og blev fædre til Nefilim kastet i?",
                "",
                "De blev kastet i Tartaros. Jævnfør 2 Peter 2:4."
        );
        mcq.addAnswer("Gehenna", false);
        mcq.addAnswer("Afgrunden", false);
        mcq.addAnswer("Tartaros", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor finder vi skriftstedet?",
                "\"Ydmyg jer derfor under Guds vældige hånd, så han til sin tid kan ophøje jer; kast samtidig al jeres bekymring på ham, for han tager sig af jer.\"",
                "Svaret er 1 Peter 5:6-7.\nSalme 55:22 siger også: \"Kast din byrde på Jehova, og han vil sørge for dig. Han vil aldrig tillade at den retfærdige vakler.\"\nJehova ønsker virkelig, at vi skal vide, at han er dybt interesseret i hver eneste af os. Han ønsker at vi har det godt og han vil gerne hjælpe os så længe vi er trofaste."
        );
        mcq.addAnswer("2 Timotius 1:5", false);
        mcq.addAnswer("1 Johannes 2:4", false);
        mcq.addAnswer("1 Peter 5:6-7", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor finder vi skriftstedet?",
                "\"Hans usynlige [egenskaber] ses nemlig klart fra verdens skabelse af, både hans evige kraft og hans guddommelighed, idet de fornemmes i de ting der er frembragt, så de er uden undskyldning;\"",
                "Svaret er Romerne 1:20. Ja, Jehovas kærlighed til os mennesker fornemmes klart af hans skaberværk og i at han har skabt os til at nyde det.\nDesuden er det også i hans skaberværk at man fornemmer hvem han er."
        );
        mcq.addAnswer("Job 24:3", false);
        mcq.addAnswer("Daniel 2:4", false);
        mcq.addAnswer("Romerne 1:20", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "Hvor finder vi skriftstedet?",
                "\"For, hvad Jehova angår, hans øjne skuer ud over hele jorden for, at han kan vise sig stærk sammen med dem hvis hjerte er helt med ham.\"",
                "Svaret er 2 Krønikebog 16:9a. Jehova spejder efter muligheder for at hjælpe de der gerne vil tjene ham.\nDesuden viser dette skriftsted også hans positive syn på os. Han spejder efter situationer hvor vi gerne vil gøre det gode og hvor han kan hjælpe os."
        );
        mcq.addAnswer("Esajas 6:2a", false);
        mcq.addAnswer("Josua 24:9b", false);
        mcq.addAnswer("2 Krønikebog 16:9a", true);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);

        /*
        mcq = new MultipleChoiceQuestion();
        mcq.setQuestion(
                "",
                "",
                ""
        );
        mcq.addAnswer("", true);
        mcq.addAnswer("", false);
        mcq.addAnswer("", false);
        mcq.addAnswer("", false);
        addQuestionToDatabase(mcq, Global.TYPE_MCQ, db);
        */
    }

    private void addTrueFalseQuestionsToDatabase(SQLiteDatabase db)
    {

        // True False questions
        MultipleChoiceQuestion tfq = new MultipleChoiceQuestion();

        tfq = new MultipleChoiceQuestion();
        tfq.setQuestion(
                "Nimrod var i Kain's slægtslinje",
                "",
                "Kain's slægtslinje sluttede ved vandfloden.\nNimrod levede efter vandfloden og var efterkommer af Kain's bror Set.\nJævnfør Indsigt bind 1 s. 1213-1214."
        );
        tfq.addAnswer("Sandt", false);
        tfq.addAnswer("Falsk", true);
        addQuestionToDatabase(tfq, Global.TYPE_TFQ, db);

        /*
        tfq = new MultipleChoiceQuestion();
        tfq.setQuestion(
                "",
                ""
        );
        tfq.addAnswer("Sandt Falsk", false);
        tfq.addAnswer("Sandt Falsk", true);
        addQuestionToDatabase(tfq, Global.TYPE_TFQ, db);
        */
    }

    private void addMultipleAnswerQuestionsToDatabase(SQLiteDatabase db)
    {

        // Multiple Answer Questions
        MultipleChoiceQuestion maq = new MultipleChoiceQuestion();

        maq = new MultipleChoiceQuestion();
        maq.setQuestion(
                "Hvad hed Noa's sønner?",
                "",
                "Noa var far til: Sem, Kam og Jafet. Jævnfør 1 Mosebog 6:10."
        );
        maq.addAnswer("Sem", true);
        maq.addAnswer("Kam", true);
        maq.addAnswer("Jafet", true);
        maq.addAnswer("Esau", false);
        maq.addAnswer("Jakob", false);
        maq.addAnswer("Set", false);
        addQuestionToDatabase(maq, Global.TYPE_MAQ, db);

        /*
        maq = new MultipleChoiceQuestion();
        maq.setQuestion(
                "",
                "",
                ""
        );
        maq.addAnswer("", true);
        maq.addAnswer("", true);
        maq.addAnswer("", true);
        maq.addAnswer("", true);
        maq.addAnswer("", false);
        maq.addAnswer("", false);
        maq.addAnswer("", false);
        maq.addAnswer("", false);
        addQuestionToDatabase(maq, Global.TYPE_MAQ, db);
        */
    }

    private void addSequenceQuestionsToDatabase(SQLiteDatabase db)
    {

        // Sequence questions
        SequenceQuestion sq;

        sq = new SequenceQuestion();
        sq.setQuestion(
                "Angiv den rigtige rækkefølge på de ti plager",
                "",
                "Rækkefølgen på plagerne var: Nilens vand blev til blod, frøer, myg, klæger, pest, bylder, hagl, græshopper, mørke, de førsteførdte dør."
        );
        sq.addAnswer("Nilens vand blev til blod", 0);
        sq.addAnswer("Frøer", 1);
        sq.addAnswer("Myg", 2);
        sq.addAnswer("Klæger", 3);
        sq.addAnswer("Pest", 4);
        sq.addAnswer("Bylder", 5);
        sq.addAnswer("Hagl", 6);
        sq.addAnswer("Græshopper", 7);
        sq.addAnswer("Mørke", 8);
        sq.addAnswer("De førstefødte dør", 9);
        addQuestionToDatabase(sq, Global.TYPE_SQ, db);

        /*
        sq = new SequenceQuestion();
        sq.setQuestion(
                "",
                ""
        );
        sq.addAnswer("", 0);
        sq.addAnswer("", 1);
        sq.addAnswer("", 2);
        sq.addAnswer("", 3);
        addQuestionToDatabase(sq, Global.TYPE_SQ, db);
        */
    }
}