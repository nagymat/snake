import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game
{
    private Apple apple;
    private Bonus bonus;
    private Strong_Hazard strong_hazard;

    private List<Field> fields = new ArrayList<Field>();
    private List<Snake_Head> snakes = new ArrayList<Snake_Head>();
    private List<Weak_Hazard> weak_hazards = new ArrayList<Weak_Hazard>();

    private int timer;
    private difficulty difficulty;
    private List<Player> players = new ArrayList<Player>();

    private boolean isGameOver;
    private Player winner;

    public Game(boolean[] barriers, difficulty difficulty, Player playerOne)
    {
        this.isGameOver = false;
        this.winner = null;
        this.difficulty = difficulty;
        this.timer = 0;
        this.bonus = null;
        this.strong_hazard = null;

        this.CreateEmptyFields();
        for(int i=0; i<400; i++)
        {
            if(barriers[i] == true)
            {
                Barrier barrier = new Barrier(this.fields.get(i));
                this.fields.get(i).SetFieldType(barrier);
            }
        }

        this.snakes.add(this.CreateSnake(this.fields.get(8*20+11),direction.LEFT,4,colour.GREEN));
        this.players.add(playerOne);

        Field emptyfield = this.FindEmptyField();
        this.apple = new Apple(emptyfield);
        emptyfield.SetFieldType(this.apple);
    }

    public Game(boolean[] barriers, difficulty difficulty, Player playerOne, Player playerTwo)
    {
        this.isGameOver = false;
        this.winner = null;
        this.difficulty = difficulty;
        this.timer = 0;
        this.bonus = null;
        this.strong_hazard = null;

        this.CreateEmptyFields();
        for(int i=0; i<400; i++)
        {
            if(barriers[i] == true)
            {
                Barrier barrier = new Barrier(this.fields.get(i));
                this.fields.get(i).SetFieldType(barrier);
            }
        }

        this.snakes.add(this.CreateSnake(this.fields.get(8*20+11),direction.LEFT,4,colour.GREEN));
        this.snakes.add(this.CreateSnake(this.fields.get(11*20+8),direction.RIGHT,4,colour.BLUE));
        this.players.add(playerOne);
        this.players.add(playerTwo);

        Field emptyfield = this.FindEmptyField();
        this.apple = new Apple(emptyfield);
        emptyfield.SetFieldType(this.apple);
    }

    public void Step()
    {
        boolean multiplayer = (this.snakes.size() > 1);

        for(int i=0; i < this.snakes.size(); i++)               // Végigmegyünk a kígyókat tartalmazó listán:
        {
            Snake_Head snake = this.snakes.get(i);
            Player player = this.players.get(i);

            snake.Move();                                       // mindegyik kígyót léptetjük,
            fieldtype type = snake.GetConsumedFieldtype();      // majd megvizsgáljuk, milyen típusú mezőre lépett.

            if((type == fieldtype.BARRIER) || (type == fieldtype.STRONG_HAZARD) || (type == fieldtype.SNAKE_BODY) || (type == fieldtype.SNAKE_HEAD))
            {
                this.isGameOver = true;                         // Ha rossz mezőre lépett, a kígyó meghal, a játék véget ér.

                if(multiplayer == true)
                {
                    if(i==0) this.winner = players.get(1);
                    else this.winner = players.get(0);
                }
                else
                {
                    this.winner = player;
                }
            }
            else if(type == fieldtype.APPLE)                    // Ha almára lépett a kígyó
            {
                Field emptyfield = this.FindEmptyField();
                this.apple = new Apple(emptyfield);             // akkor létrehozunk egy új almát,
                emptyfield.SetFieldType(this.apple);            // a mező típusát frissítjük.
                switch (this.difficulty)                        // a játékos pontszámát pedig növeljük a nehézségi szintnek megfelelően
                {
                    case EASY:
                        player.IncreaseScore(1);
                        break;
                    case MEDIUM:
                        player.IncreaseScore(2);
                        break;
                    case HARD:
                        player.IncreaseScore(3);
                        break;
                }
            }
            else if(type == fieldtype.BONUS)
            {
                player.IncreaseScore(this.bonus.GetTimer());
                this.bonus = null;
            }
            else if(type == fieldtype.WEAK_HAZARD)
            {
                this.Destroy_Weak_Hazards();
                player.DecreaseScore(10);
            }
        }

        if(this.bonus != null)                                  // Megvizsgáljuk, hogy van-e éppen a játékban aktív bónusz.
        {
            this.bonus.DecreaseTimer();                         // Ha van, akkor csökkentjük a számlálóját, majd megvizsgáljuk, hogy
            if(this.bonus.CheckIfActive() == false) this.bonus = null;      // lejárt-e az idő. Ha igen, akkor: bonus = null.
        }
        if(this.strong_hazard != null)                                      // Megvizsgáljuk, hogy van-e éppen a játékban aktív erős veszély.
        {
            this.strong_hazard.DecreaseTimer();                             // Ha van, akkor csökkentjük a számlálóját, majd megvizsgáljuk,
            if(this.strong_hazard.CheckIfActive() == false) this.strong_hazard = null;    // hogy lejárt-e az idő. Ha igen, akkor strong_hazard = null.
        }
        if(this.weak_hazards.isEmpty() != true)                 // Megvizsgáljuk, hogy vannak-e a játékban aktív gyenge veszélyek.
        {
            boolean destroy_weak_hazards = false;               // Felveszünk egy változót, ami azt fogja jelezni, hogy kell-e törölni a gyenge
            for (Weak_Hazard weak_hazard : this.weak_hazards)   // veszélyeket. Végigmegyünk az ezeket tároló listán és csökkentjük az egyes
            {                                                   // veszélyek számlálóit. Majd megvizsgáljuk, hogy van-e olyan gyenge veszély,
                weak_hazard.DecreaseTimer();                    // aminek lejárt a számlálója.
                if(weak_hazard.CheckIfActive() == false) destroy_weak_hazards = true;   // Ha van ilyen, az említett változót igazba állítjuk.
            }
            if(destroy_weak_hazards == true) this.Destroy_Weak_Hazards();   // Ha a változó igaz, meghívjuk a függvényt, ami törli a veszélyeket.
        }

        this.timer++;                // Növeljük a számláló értékét
        int timelimit=0;
        switch (this.difficulty)     // Nehézségi szinttől függően meghatározzuk azt az időt, amikor új bónuszokat és veszélyeket kell generálni
        {
            case EASY:
                timelimit = 180;
                break;
            case MEDIUM:
                timelimit = 120;
                break;
            case HARD:
                timelimit = 60;
                break;
        }
        if(this.timer == timelimit)  // Ha elérjük ezt az időpontot, új bónuszokat és veszélyeket generálunk.
        {
            Field emptyfield;

            emptyfield = this.FindEmptyField();
            this.bonus = new Bonus(emptyfield, 45);
            emptyfield.SetFieldType(this.bonus);

            emptyfield = this.FindEmptyField();
            this.strong_hazard = new Strong_Hazard(emptyfield,45);
            emptyfield.SetFieldType(this.strong_hazard);

            Create_Weak_Hazards(45);
            this.timer = 0;
        }
    }

    public boolean CheckIfGameOver()
    {
        return this.isGameOver;
    }

    public Player GetWinner()
    {
        return this.winner;
    }

    private Snake_Head CreateSnake(Field field, direction direction, int length, colour colour)
    {
        Snake_Body snake_body = new Snake_Body(field);
        field.SetFieldType(snake_body);
        for(int i=0; i<(length-2); i++)
        {
            field = field.GetNeighbour(direction);
            snake_body = new Snake_Body(field,snake_body);
            field.SetFieldType(snake_body);
        }
        field = field.GetNeighbour(direction);
        Snake_Head snake_head = new Snake_Head(field, direction, snake_body, colour);
        field.SetFieldType(snake_head);
        return snake_head;
    }

    private void SetSnakeDirection(int snakeID, direction direction)
    {
        snakes.get(snakeID).SetDirection(direction);
    }

    private void Create_Weak_Hazards(int time)
    {
        Field weak_hazard_field = this.FindEmpty2x2Field();
        weak_hazards.clear();
        weak_hazards.add(new Weak_Hazard(weak_hazard_field,time));
        weak_hazard_field.SetFieldType(weak_hazards.get(0));
        weak_hazards.add(new Weak_Hazard((weak_hazard_field.GetNeighbour(direction.RIGHT)),time));
        weak_hazard_field.GetNeighbour(direction.RIGHT).SetFieldType(weak_hazards.get(1));
        weak_hazards.add(new Weak_Hazard((weak_hazard_field.GetNeighbour(direction.UP)),time));
        weak_hazard_field.GetNeighbour(direction.UP).SetFieldType(weak_hazards.get(2));
        weak_hazards.add(new Weak_Hazard((weak_hazard_field.GetNeighbour(direction.RIGHT).GetNeighbour(direction.UP)),time));
        weak_hazard_field.GetNeighbour(direction.RIGHT).GetNeighbour(direction.UP).SetFieldType(weak_hazards.get(3));
    }

    private void Destroy_Weak_Hazards()
    {
        for(Weak_Hazard weak_hazard : weak_hazards)
        {
            Field weak_hazard_field = weak_hazard.actual;
            if((weak_hazard_field.GetFieldType().GetType() != fieldtype.SNAKE_HEAD) || (weak_hazard_field.GetFieldType().GetType() != fieldtype.SNAKE_BODY))
            {
                Empty_Field empty_field = new Empty_Field(weak_hazard_field);
                weak_hazard_field.SetFieldType(empty_field);
            }
        }
        weak_hazards.clear();
    }

    private void CreateEmptyFields()
    {
        this.fields.clear();
        for(int i=0; i<400; i++) this.fields.add(new Field());
        for(int row=0; row<20; row++)
        {
            for(int collumn=0; collumn<20; collumn++)
            {
                int actual = row*20 + collumn;
                int upper, lower, right, left;

                if(row == 0) upper = 19*20 + collumn;
                else upper = (row-1)*20 + collumn;

                if(row == 19) lower = collumn;
                else lower = (row+1)*20 + collumn;

                if(collumn == 19) right = row*20;
                else right = actual+1;

                if(collumn == 0) left = row*20+19;
                else left = actual-1;

                this.fields.get(actual).SetNeighbours(
                                                    this.fields.get(upper),
                                                    this.fields.get(lower),
                                                    this.fields.get(left),
                                                    this.fields.get(right));
            }
        }
    }

    private Field FindEmptyField()
    {
        Field random_field = null;
        do
        {
            Random random = new Random();
            random_field = fields.get(random.nextInt(fields.size()));
        }
        while (random_field.GetFieldType().GetType() != fieldtype.EMPTY);

        return random_field;
    }

    private Field FindEmpty2x2Field()
    {
        Field random_field = null;
        fieldtype[] fieldtypes = new fieldtype[4];
        boolean fields_are_empty;
        do
        {
            fields_are_empty = true;
            Random random = new Random();
            random_field = fields.get(random.nextInt(fields.size()));
            fieldtypes[0] = random_field.GetFieldType().GetType();
            fieldtypes[1] = random_field.GetNeighbour(direction.RIGHT).GetFieldType().GetType();
            fieldtypes[2] = random_field.GetNeighbour(direction.UP).GetFieldType().GetType();
            fieldtypes[3] = random_field.GetNeighbour(direction.RIGHT).GetNeighbour(direction.UP).GetFieldType().GetType();
            for(int i=0; i<4; i++) if(fieldtypes[i] != fieldtype.EMPTY) fields_are_empty = false;
        }
        while (fields_are_empty == false);

        return random_field;
    }
}
