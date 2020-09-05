package dk.bonnicksens.birkb.bibelquiz;

import java.util.ArrayList;

/**
 * Created by BirkB on 31-08-2015.
 */
public class MultipleChoiceQuestion
{
    private String question;
    private String question2;
    private String info;

    ArrayList<MultipleChoiceAnswer> answers;

    public MultipleChoiceQuestion()
    {
        answers = new ArrayList<>();
        this.question = "";
        this.question2 = "";
        this.info = "";
    }

    public MultipleChoiceQuestion(String question, String question2, String info)
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

    public MultipleChoiceAnswer getAnswer(int index)
    {
        return answers.get(index);
    }

    public int getAnswerCount()
    {
        return answers.size();
    }

    public void addAnswer(String answer, Boolean rightAnswer)
    {
        answers.add(new MultipleChoiceAnswer(answer, rightAnswer));
    }

    // Klasse der holder alle svarene
    public class MultipleChoiceAnswer {

        private String answer;
        private Boolean rightAnswer;

        public MultipleChoiceAnswer()
        {

        }

        public MultipleChoiceAnswer(String answer, Boolean rightAnswer)
        {
            //this.questionId = questionId;
            this.answer = answer;
            this.rightAnswer = rightAnswer;
        }

        public void setAnswer(String answer, Boolean rightAnswer)
        {
            this.answer = answer;
            this.rightAnswer = rightAnswer;
        }

        public String getAnswer()
        {
            return this.answer;
        }

        public Boolean getRightAnswer()
        {
            return this.rightAnswer;
        }
    }
}