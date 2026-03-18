package app.enigmeStudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class EcranAccueil extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_accueil);
    }

    public void parametre(View view)
    {
        Intent intentParametre = new Intent(this, Parametre.class);
        startActivity(intentParametre);
    }

    public void enigmeMontre(View view)
    {
        Intent intentEnigmeMontre = new Intent(this, app.enigmeStudio.EnigmeMontre.class);
        startActivity(intentEnigmeMontre);
    }
}