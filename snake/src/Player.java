public class Player
{
    String[] name;
    int score;

    public Player(String[] name)
    {
        this.name = name;
        this.score = 0;
    }

    public void IncreaseScore(int value)
    {
        this.score += value;
    }

    public void DecreaseScore(int value)
    {
        this.score -= value;
        if(this.score < 0) this.score = 0;
    }

    public int GetScore()
    {
        return this.score;
    }
}
