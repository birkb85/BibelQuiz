package dk.bonnicksens.birkb.bibelquiz;

import java.util.ArrayList;

/**
 * Created by BirkB on 31-08-2015.
 */
public class SequenceQuestion
{

    private String question;
    private String question2;
    private String info;

    ArrayList<SequenceAnswer> answers;

    public SequenceQuestion()
    {
        answers = new ArrayList<>();
        this.question = "";
        this.question2 = "";
        this.info = "";
    }

    public SequenceQuestion(String question, String question2, String info)
    {
        answers = new ArrayList<>();
        this.question = question;
        this.question2 = question2;
        this.info = info;
    }

    public void setQuestion(String question, String question2, String info)
    {
        this.question = question;
        this.question2 = question2;
        this.info = info;
    }

    public String getQuestion()
    {
        return this.question;
    }

    public String getQuestion2()
    {
        return this.question2;
    }

    public String getInfo()
    {
        return this.info;
    }

    public SequenceAnswer getAnswer(int index)
    {
        return answers.get(index);
    }

    public int getAnswerCount()
    {
        return answers.size();
    }

    public void addAnswer(String answer, int sequenceNumber)
    {
        answers.add(new SequenceAnswer(answer, sequenceNumber));
    }

    // Klasse der holder alle svarene
    public class SequenceAnswer {

        private String answer;
        private int sequenceNumber;

        public SequenceAnswer()
        {

        }

        public SequenceAnswer(String answer, int sequenceNumber)
        {
            this.answer = answer;
            this.sequenceNumber = sequenceNumber;
        }

        public void setAnswer(String answer, int sequenceNumber)
        {
            this.answer = answer;
            this.sequenceNumber = sequenceNumber;
        }

        public String getAnswer()
        {
            return this.answer;
        }

        public int getSequenceNumber()
        {
            return this.sequenceNumber;
        }
    }
}
