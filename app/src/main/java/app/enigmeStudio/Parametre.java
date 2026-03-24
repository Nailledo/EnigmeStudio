package app.enigmeStudio;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import app.enigmeStudio.Outils.Sauvegarde;

public class Parametre extends AppCompatActivity implements OnItemSelectedListener
{
    public final static int SELECT  = 0;
    public final static int FRENCH  = 1;
    public final static int ENGLISH = 2;
    public final static int ITALIAN = 3;

    private Spinner languageChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        languageChooser = findViewById(R.id.sp_langues);
        languageChooser.setOnItemSelectedListener(this);
    }

    public void finish(View view)
    {
        this.finish();
    }

    public void reinitialiserProgression(View view)
    {
        Sauvegarde.reinitialiserProgression();
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == SELECT)
            return;

        String langue       = "";
        String sLangueToast = "";
        switch (position)
        {
            case ENGLISH: langue = "en";  sLangueToast = "English" ; break;
            case ITALIAN: langue = "it";  sLangueToast = "Italiano"; break;
            case FRENCH : langue = "fr";  sLangueToast = "Français"; break;
            default: return;
        }

        Locale newLocale = new Locale(langue);
        String currentlangue = getResources().getConfiguration().locale.getLanguage();

        if (!currentlangue.equals(langue))
        {
            Locale.setDefault(newLocale);

            Configuration config = new Configuration();
            config.setLocale(newLocale);

            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            this.getApplicationContext().getResources().updateConfiguration(config, getResources().getDisplayMetrics());

            Sauvegarde.setLangue( langue );
            this.recreate();

            Toast.makeText(this, " -> " + sLangueToast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
