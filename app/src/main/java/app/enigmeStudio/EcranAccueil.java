package app.enigmeStudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.enigmeStudio.Outils.Sauvegarde;
import app.enigmeStudio.enigmeMontre.EnigmeMontre;

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
        if (!langueActuelle.equals(Sauvegarde.getLangue()))
        {
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

    public void enigmeMontre(View view)
    {
        Intent intentEnigmeMontre = new Intent(this, EnigmeMontre.class);
        startActivity(intentEnigmeMontre);
    }
}