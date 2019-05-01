public class Timed_Field_Type extends Field_Type
{
    protected int timer;
    protected boolean isactive;

    public void DecreaseTimer()
    {
        this.timer--;
        if(this.timer <= 0)
        {
            this.Deactivate();
            this.isactive = false;
        }
    }

    public boolean CheckIfActive()
    {
        return this.isactive;
    }
}
