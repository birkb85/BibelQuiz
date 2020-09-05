package dk.bonnicksens.birkb.bibelquiz;

/**
 * Created by BirkB on 06-09-2015.
 */
public class QuestionTypeIndex
{
    // En klasse der holder type og index på spørgsmål

    private String type;
    private int index;

    public QuestionTypeIndex(String type, int index)
    {
        this.type = type;
        this.index = index;
    }

    public String getType()
    {
        return type;
    }

    public int getIndex()
    {
        return index;
    }
}
