public class Field
{
    private Field upper;
    private Field lower;
    private Field left;
    private Field right;
    private Field_Type field_type;

    public Field()
    {
        this.upper = null;
        this.lower = null;
        this.right = null;
        this.left = null;
        this.field_type = new Empty_Field(this);
    }

    public Field_Type GetFieldType()
    {
        return this.field_type;
    }

    public void SetFieldType(Field_Type newfieldtype)
    {
        this.field_type = newfieldtype;
    }

    public void SetNeighbours(Field upper, Field lower, Field left, Field right)
    {
        this.upper = upper;
        this.lower = lower;
        this.left = left;
        this.right = right;
    }

    public Field GetNeighbour(direction direction)
    {
        Field neighbour = null;
        switch (direction)
        {
            case UP:
                neighbour = this.upper;
                break;
            case DOWN:
                neighbour =  this.lower;
                break;
            case LEFT:
                neighbour =  this.left;
                break;
            case RIGHT:
                neighbour =  this.right;
                break;
        }
        return neighbour;
    }
}
