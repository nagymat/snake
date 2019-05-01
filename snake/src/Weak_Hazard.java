public class Weak_Hazard extends Timed_Field_Type
{
    public Weak_Hazard(Field field, int time)
    {
        this.actual = field;
        this.fieldtype = fieldtype.WEAK_HAZARD;
        this.isactive = true;
        this.timer = time;
    }
}
