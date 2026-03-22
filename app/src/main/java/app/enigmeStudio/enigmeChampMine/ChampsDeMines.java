package app.enigmeStudio.enigmeChampMine;

import android.app.Activity;
import android.os.Bundle;

public class ChampsDeMines extends Activity
{
    private ChampsMinesView champsMines;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.champsMines = new ChampsMinesView(this);
        setContentView(this.champsMines);

    }
}