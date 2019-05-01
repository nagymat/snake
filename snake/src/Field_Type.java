public class Field_Type
{
    protected Field actual;
    protected fieldtype fieldtype;

    public fieldtype GetType()
    {
        return this.fieldtype;
    }

    public void Deactivate()
    {
        this.actual = null;
    }
}
