public class Snake_Body extends Field_Type
{
    private Snake_Body next;

    public Snake_Body(Field field)
    {
        this.actual = field;
        this.fieldtype = fieldtype.SNAKE_BODY;
        this.next = null;
    }

    public Snake_Body(Field field, Snake_Body next)
    {
        this.actual = field;
        this.fieldtype = fieldtype.SNAKE_BODY;
        this.next = next;
    }

    public void Move(Field newfield, boolean append_needed)
    {
        Field tmp_field = this.actual;              // Lementjük azt a mezőt egy átmeneti változóba, ahol a kígyótest éppen tartózkodik.

        this.actual = newfield;                     // Átállítjuk a kígyó test mező változóját az új mezőre
        newfield.SetFieldType(this);    // Az új mezőnek átállítjuk a Field_Type változóját arra a kígyótestre, ami éppen rálépett
                                                    // erre a mezőre.

        if (this.next != null)                      // Ha van további kígyótest,
        {
            next.Move(tmp_field, append_needed);    // léptetjük őt is.
        }
        else                                                            // Ha véget ért a kígyó,
        {
            if(append_needed == true)                                   // és meg kell növelni a méretét,
            {
                this.next = new Snake_Body(tmp_field);                  // akkor létrehozunk egy új kígyótest elemet
                tmp_field.SetFieldType(this.next);                      // a jelenlegi mező Field_Type változójának pedig átadjuk ezt.
            }
            else
            {
                Empty_Field empty_field = new Empty_Field(tmp_field);   // Ha nem kell méretet növelni, akkor üres mezőt hozunk létre
                tmp_field.SetFieldType(empty_field);                    // és átadjuk ezt a jelenlegi mező Field_Type változójának.
            }
        }
    }
}
