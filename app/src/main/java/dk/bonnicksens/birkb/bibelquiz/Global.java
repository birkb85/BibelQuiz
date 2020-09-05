package dk.bonnicksens.birkb.bibelquiz;

import android.util.Log;

import java.security.Timestamp;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

/**
 * Created by LinetteB on 06-09-2015.
 */
public class Global {

    private static final String TAG = "Global";

    // Public defineret strenge
    // Typer af spørgsmål
    public static final String TYPE_MCQ = "MCQ"; // Multiple Choice Question, mange svarmuligheder et rigtigt svar.
    public static final String TYPE_TFQ = "TFQ"; // True False Question, et sandt og et forkert svar.
    public static final String TYPE_MAQ = "MAQ"; // Multiple Answer Question, mange svarmuligheder, flere rigtige svar.
    public static final String TYPE_SQ = "SQ"; // Sequence Question.
    // Put extras værdier
    public static final String PUT_INFO = "INFO";
    public static final String PUT_RIGHTANSWER = "RIGHTANSWER";
    public static final String PUT_POINTS = "POINTS";
    public static final String PUT_POINTSTOTAL = "POINTSTOTAL";
    // Shared preferences
    public static final int PREFERENCE_MODE_PRIVATE = 0;
    public static final String PREFERENCE_FILE = "PREFERENCE_FILE";
    public static final String PRE_SHOWMCQ = "SHOWMCQ";
    public static final String PRE_SHOWTFQ = "SHOWTFQ";
    public static final String PRE_SHOWMAQ = "SHOWMAQ";
    public static final String PRE_SHOWSQ = "SHOWSQ";

    // Spørgsmål
    private static int numberOfQuestions = 0;
    private static int numberOfQuestionsCorrect = 0;

    public void setNumberOfQuestions(int number)
    {
        this.numberOfQuestions = number;
    }

    public int getNumberOfQuestions()
    {
        return numberOfQuestions;
    }

    public void addToNumberOfQuestionsCorrect()
    {
        numberOfQuestionsCorrect++;
    }

    public int getNumberOfQuestionsCorrectCount()
    {
        return numberOfQuestionsCorrect;
    }

    public void resetNumberOfQuestionsCorrect()
    {
        numberOfQuestionsCorrect = 0;
    }

    // Point
    private static int numberOfPoints = 0;
    private static int numberOfPointsTotal = 0;

    public void addToNumberOfPointsTotal(int points)
    {
        this.numberOfPointsTotal += points;
    }

    public int getNumberOfPointsTotal()
    {
        return numberOfPointsTotal;
    }

    public void addToNumberOfPoints(int points)
    {
        numberOfPoints += points;
    }

    public int getNumberOfPoints()
    {
        return numberOfPoints;
    }

    public void resetPoints()
    {
        numberOfPoints = 0;
        numberOfPointsTotal = 0;
    }

    // Tidstagning
    private static Long startTime;
    private static Long endTime;

    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
        //Log.d(TAG, "startTime: " + startTime.toString());
    }

    public void setEndTime()
    {
        endTime = System.currentTimeMillis();
        //Log.d(TAG, "endTime: " + endTime.toString());
    }

    public String getTimeElapsed()
    {

        Long millis = endTime - startTime;
        String timeString = "";

        int hours = (int)Math.floor((millis / (60 * 60 * 1000)));
        millis -= (long)hours * 60 * 60 * 1000;
        if (hours > 0) {
            if (hours < 10) timeString += "0";
            timeString += String.valueOf(hours) + ":";
        }

        int minutes = (int)Math.floor((millis / (60 * 1000)));
        millis -= (long)minutes * 60 * 1000;
        if (minutes < 10) timeString += "0";
        timeString += String.valueOf(minutes) + ":";

        int seconds = (int)Math.floor((millis / 1000));
        if (seconds < 10) timeString += "0";
        timeString += String.valueOf(seconds);

        return timeString;

        /*
        Time time = new Time(endTime - startTime);
        Log.d(TAG, "timeElapsed: " + String.valueOf(endTime - startTime));
        return time.toString();
        */
    }
}
