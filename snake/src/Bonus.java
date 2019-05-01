public class Bonus extends Timed_Field_Type
{
    public Bonus(Field field, int time)
    {
        this.actual = field;
        this.fieldtype = fieldtype.BONUS;
        this.isactive = true;
        this.timer = time;
    }

    public int GetTimer()
    {
        return this.timer;
    }
}
