package app.enigmeStudio;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import app.enigmeStudio.Outils.Sauvegarde;
import app.enigmeStudio.enigmeChampMine.ChampsDeMines;
import app.enigmeStudio.enigmeMontre.EnigmeMontre;
import app.enigmeStudio.enigmeMorse.EnigmeMorse;

public class EcranAccueil extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_accueil);

        if (savedInstanceState != null)
        {
            if (savedInstanceState.getBoolean("E1", false)) Sauvegarde.setEnigme1Reussi(true);
            if (savedInstanceState.getBoolean("E2", false)) Sauvegarde.setEnigme2Reussi(true);
            if (savedInstanceState.getBoolean("E3", false)) Sauvegarde.setEnigme3Reussi(true);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        String langueActuelle = getResources().getConfiguration().locale.getLanguage();
        String langueSauvegardee = Sauvegarde.getLangue();

        if (!langueActuelle.equals(langueSauvegardee))
        {
            // Appliquer la langue sauvegardée avant de recréer l'écran
            Locale newLocale = new Locale(langueSauvegardee);
            Locale.setDefault(newLocale);
            Configuration config = new Configuration();
            config.setLocale(newLocale);
            
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            recreate();
            return;
        }

        new Sauvegarde().majTextView(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bagOfData)
    {
        super.onSaveInstanceState(bagOfData);
        if (Sauvegarde.isEnigme1Reussi()) bagOfData.putBoolean("E1", true);
        if (Sauvegarde.isEnigme2Reussi()) bagOfData.putBoolean("E2", true);
        if (Sauvegarde.isEnigme3Reussi()) bagOfData.putBoolean("E3", true);
    }

    public void parametre(View view)
    {
        Intent intentParametre = new Intent(this, Parametre.class);
        startActivity(intentParametre);
    }

    public void enigmeMorse(View view)
    {
        Intent intentEnigmeMorse = new Intent(this, EnigmeMorse.class);
        startActivity(intentEnigmeMorse);
    }

    public void enigmeMontre(View view)
    {
        Intent intentEnigmeMontre = new Intent(this, EnigmeMontre.class);
        startActivity(intentEnigmeMontre);
    }

    public void enigmeChampMine(View view)
    {
        Intent intentEnigmeChampMine = new Intent(this, ChampsDeMines.class);
        startActivity(intentEnigmeChampMine);
    }
}
