package dk.bonnicksens.birkb.bibelquiz;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by BirkB on 01-09-2015.
 */
public class QuestionList {

    private static ArrayList<QuestionTypeIndex> questionList = new ArrayList<>();

    public void addQuestionToList(String type, int index)
    {
        questionList.add(new QuestionTypeIndex(type, index));
    }

    public void clearList()
    {
        questionList.clear();
    }

    public QuestionTypeIndex getNextQuestion()
    {
        QuestionTypeIndex questionTypeIndex = questionList.get(0);
        //questionList.remove(0);
        return questionTypeIndex;
    }

    public  void removeNextQuestion()
    {
        questionList.remove(0);
    }

    public String getNextQuestionType()
    {
        return questionList.get(0).getType();
    }

    public int getQuestionsCount()
    {
        return questionList.size();
    }

    public void mixQuestions()
    {
        mix();
        mix();
    }

    private void mix()
    {
        for (int i = 0; i < questionList.size(); i++)
        {
            // Tag et spørgsmål ud
            QuestionTypeIndex question = questionList.get(i);
            questionList.remove(i);
            // Find random placering
            Random random = new Random();
            float randomFloat = random.nextFloat();
            int numberOfQuestions = questionList.size();
            int index = Math.round(randomFloat * (float) numberOfQuestions);
            // Indsæt spørgsmål på placering
            questionList.add(index, question);
        }
    }
}
