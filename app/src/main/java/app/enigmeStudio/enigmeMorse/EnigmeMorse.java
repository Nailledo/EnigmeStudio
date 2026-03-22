package app.enigmeStudio.enigmeMorse;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.enigmeStudio.R;

public class EnigmeMorse extends AppCompatActivity
{

    TextView message;

    TextView indice1;
    TextView indice2;

    EditText texte;

    Button btnIndice1;
    Button btnIndice2;
    Button btnIndice3;

    ImageView image;

    Vibrator vibreur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enigme_morse);
        this.message = (TextView) findViewById(R.id.message);
        this.texte = (EditText) findViewById(R.id.saisie);

        this.indice1 = (TextView) findViewById(R.id.txtIndice1);
        this.indice2 = (TextView) findViewById(R.id.txtIndice2);

        this.btnIndice1 = (Button) findViewById(R.id.btnIndice1);
        this.btnIndice2 = (Button) findViewById(R.id.btnIndice2);
        this.btnIndice3 = (Button) findViewById(R.id.btnIndice3);

        this.image = (ImageView) findViewById(R.id.coche);

        this.vibreur = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onDestroy()
    {
        super.onDestroy();
        this.vibreur.cancel();
    }

    public void vibrer(View view) {
        //Vibrator vibreur = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = {0,2000,1500,200,1500,200,50,200,50,200,1500,2000};
        int delaiVibration = 100;
        new Thread(new Runnable() {
            public void run()
            {

                vibreur.vibrate(pattern, -1);
                try {
                    Thread.sleep(delaiVibration);
                } catch(InterruptedException ie) {}

            }
        }).start() ;
    }

    public void valider(View view)
    {
        if(this.texte.getText().toString().equals("test") || this.texte.getText().toString().equals("TEST"))
        {
            this.message.setText("Gagné");
            this.message.setTextColor(Color.GREEN);
            this.message.setTextSize(30);

            this.indice1.setText("");
            this.indice2.setText("");

            this.btnIndice1.setEnabled(false);
            this.btnIndice2.setEnabled(false);
            this.btnIndice3.setEnabled(false);

            this.image.setImageResource(R.drawable.coche);
            //this.image.setVisibility(View.VISIBLE);

            //Fermer le clavier
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.texte.getWindowToken(), 0);
        }
    }

    public void afficherIndice1(View view)
    {
        this.indice1.setText(R.string.affichageIndice1);
        this.btnIndice2.setEnabled(true);
    }

    public void afficherIndice2(View view)
    {
        this.indice2.setText(R.string.affichageIndice2);
        this.btnIndice3.setEnabled(true);
    }

    public void afficherIndice3(View view)
    {
        this.indice1.setText("");
        this.indice2.setText("");

        this.image.setImageResource(R.drawable.morse);
        this.image.setVisibility(View.VISIBLE);
    }

    public void sortir(View view) {this.finish();}

}