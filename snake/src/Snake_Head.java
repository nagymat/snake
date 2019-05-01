public class Snake_Head extends Field_Type
{
    private direction direction;
    private colour colour;
    private Snake_Body body;
    private fieldtype consumed_fieldtype;

    public Snake_Head(Field field, direction direction, Snake_Body snake_body, colour colour)
    {
        this.actual = field;
        this.fieldtype = fieldtype.SNAKE_HEAD;

        this.colour = colour;
        this.direction = direction;
        this.body = snake_body;
        this.consumed_fieldtype = null;
    }

    public void Move()
    {
        Field tmp_field = this.actual;                          // Lementjük azt a mezőt egy átmeneti változóba, ahol a kígyó feje éppen tartózkodik.
        Field next = this.actual.GetNeighbour(this.direction);  // Lekérjük a kígyó haladási irányában lévő mezőt,
        Field_Type next_fieldtype = next.GetFieldType();        // illetve annak Field_Type változóját,
        fieldtype type = next_fieldtype.GetType();              // valamint ennek a mezőnek a típusát.
        this.consumed_fieldtype = type;

        if((type == fieldtype.BARRIER) || (type == fieldtype.STRONG_HAZARD) || (type == fieldtype.SNAKE_BODY) || (type == fieldtype.SNAKE_HEAD))
        {
            this.Deactivate();                                  // Ha a kígyó rossz mezőre lép, meghal, ezért deaktiváljuk a kígyó fejét.
            return;
        }
                                                                // Ha nem lépett rossz mezőre a kígyó,
        this.actual = next;                                     // akkor átállítjuk a kígyó fejének mező változóját az új mezőre,
        next_fieldtype.Deactivate();                            // az előző mezőtípust deaktiváljuk
        next.SetFieldType(this);                    // az új mezőnek átállítjuk a Field_Type változóját a kígyó fejére.

        if((type == fieldtype.EMPTY) || (type == fieldtype.WEAK_HAZARD))        // Ha üres mezőre vagy gyenge akadályra léptünk,
        {
            this.body.Move(tmp_field,false);                      // Nem kell növelni a kígyó méretét.
        }
        else if((type == fieldtype.APPLE) || (type == fieldtype.BONUS))         // Ha almára vagy bónuszra léptünk,
        {
            this.body.Move(tmp_field,true);                       // és a méretét.
        }
    }

    public fieldtype GetConsumedFieldtype()
    {
        return this.consumed_fieldtype;
    }

    public void SetDirection(direction direction)
    {
        this.direction = direction;
    }
}
