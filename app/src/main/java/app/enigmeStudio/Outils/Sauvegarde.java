package app.enigmeStudio.Outils;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import java.util.Locale;

import app.enigmeStudio.R;

public class Sauvegarde
{
    private static boolean enigme1Reussi;
    private static boolean enigme2Reussi;
    private static boolean enigme3Reussi;
    private static String  langue = "fr";

    public void majTextView(Activity activity)
    {
        TextView textViewN1 = activity.findViewById(R.id.textViewN1);
        TextView textViewN2 = activity.findViewById(R.id.textViewN2);
        TextView textViewN3 = activity.findViewById(R.id.textViewN3);

        if (textViewN1 != null)
        {
            if (enigme1Reussi) { textViewN1.setText(R.string.reussi);  textViewN1.setTextColor(Color.GREEN); }
            else               { textViewN1.setText(R.string.nonfait); textViewN1.setTextColor(Color.RED);   }
        }
        if (textViewN2 != null)
        {
            if (enigme2Reussi) { textViewN2.setText(R.string.reussi);  textViewN2.setTextColor(Color.GREEN); }
            else               { textViewN2.setText(R.string.nonfait); textViewN2.setTextColor(Color.RED);   }
        }
        if (textViewN3 != null)
        {
            if (enigme3Reussi) { textViewN3.setText(R.string.reussi);  textViewN3.setTextColor(Color.GREEN); }
            else               { textViewN3.setText(R.string.nonfait); textViewN3.setTextColor(Color.RED);   }
        }
    }

    public static void setEnigme1Reussi(boolean estReussi) { if (estReussi) enigme1Reussi = true; }
    public static void setEnigme2Reussi(boolean estReussi) { if (estReussi) enigme2Reussi = true; }
    public static void setEnigme3Reussi(boolean estReussi) { if (estReussi) enigme3Reussi = true; }

    public static boolean isEnigme1Reussi()                { return enigme1Reussi; }
    public static boolean isEnigme2Reussi()                { return enigme2Reussi; }
    public static boolean isEnigme3Reussi()                { return enigme3Reussi; }

    public static void   setLangue(String l) { langue = l;                  }
    public static Locale getLangue()         { return new Locale(langue);   }

    public static void reinitialiserProgression()
    {
        enigme1Reussi = false;
        enigme2Reussi = false;
        enigme3Reussi = false;
    }
}