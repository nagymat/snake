public class Strong_Hazard extends Timed_Field_Type
{
    public Strong_Hazard(Field field, int time)
    {
        this.actual = field;
        this.fieldtype = fieldtype.STRONG_HAZARD;
        this.isactive = true;
        this.timer = time;
    }
}
