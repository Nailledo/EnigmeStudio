package app.enigmeStudio;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EnigmeMontre extends AppCompatActivity implements SensorEventListener
{
    private float[]       acceleration = new float[3];
    private SensorManager gestionnaire;
    private DessinerMontre vue;
    int                    heureCible;
    int                    minuteCible;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Calendar calendrier = Calendar.getInstance();
        heureCible  = calendrier.get(Calendar.HOUR_OF_DAY) % 12;
        minuteCible = calendrier.get(Calendar.MINUTE);

        vue = new DessinerMontre(this);
        setContentView(vue);

        gestionnaire = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Sensor accelerometre = gestionnaire.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometre != null)
            gestionnaire.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gestionnaire.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent evenement)
    {
        acceleration = evenement.values.clone();
        vue.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor capteur, int precision) {}

    float[] getAcceleration() { return acceleration; }

    boolean estResolu()
    {
        float normMinute = normaliserAngle(vue.angleGrandeVerrouille + 277.6f);
        float normHeure  = normaliserAngle(vue.anglePetiteVerrouille - 66.3f);

        float cibleMinute = minuteCible * 6f;
        float cibleHeure  = (heureCible * 60 + minuteCible) * 0.5f;

        float minuteAiguille = normMinute / 6f;
        float heureAiguille  = (normHeure / 0.5f / 60f) % 12;

        System.out.println("Aiguille : " + (int)heureAiguille + "h" + (int)minuteAiguille);
        System.out.println("Cible    : " + heureCible + "h" + minuteCible);

        return differenceAngulaire(normMinute, cibleMinute) <= 20f
                && differenceAngulaire(normHeure, cibleHeure) <= 20f;
    }

    float normaliserAngle(float angle)
    {
        return (angle % 360 + 360) % 360;
    }

    private float differenceAngulaire(float a, float b)
    {
        float diff = Math.abs(a - b) % 360;
        return Math.min(diff, 360 - diff);
    }

    float axeVersAngle(float valeur)
    {
        float deg = valeur * 18f;
        return (deg + 360) % 360;
    }
}