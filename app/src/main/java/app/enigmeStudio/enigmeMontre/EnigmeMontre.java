package app.enigmeStudio.enigmeMontre;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EnigmeMontre extends AppCompatActivity implements SensorEventListener
{
    private float[]        acceleration;
    private SensorManager  gestionnaire;
    private DessinerMontre vue;
    private int            heureCible;
    private int            minuteCible;
    private float          normMinute;
    private float          normHeure;

    @Override
    protected void onCreate(Bundle bagOfData)
    {
        super.onCreate(bagOfData);

        this.acceleration = new float[3];

        // Heure actuelle
        Calendar calendrier = Calendar.getInstance();
        this.heureCible  = calendrier.get(Calendar.HOUR_OF_DAY) % 12;
        this.minuteCible = calendrier.get(Calendar.MINUTE);

        this.vue = new DessinerMontre(this);
        this.vue.setBackgroundColor(Color.BLACK);
        setContentView(this.vue);

        this.gestionnaire = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Sensor accelerometre = this.gestionnaire.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometre != null)
            this.gestionnaire.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        this.gestionnaire.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent evenement)
    {
        this.acceleration = evenement.values.clone();
        this.vue.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor capteur, int precision) {}

    public float[] getAcceleration   () { return this.acceleration; }
    public int     getMinuteAiguille () { return (int)(this.normMinute / 6f); }
    public int     getHeureAiguille  () { return (int)(this.normHeure / 0.5f / 60f) % 12; }

    public boolean estResolu()
    {
        this.normMinute = normaliserAngle(this.vue.getAngleGrandeVerrouille() + 277.6f);
        this.normHeure  = normaliserAngle(this.vue.getAnglePetiteVerrouille() - 66.3f);

        float cibleMinute = this.minuteCible * 6f;
        float cibleHeure  = (this.heureCible * 60 + this.minuteCible) * 0.5f;

        return differenceAngulaire(normMinute, cibleMinute) <= 20f
                && differenceAngulaire(normHeure,  cibleHeure)  <= 20f;
    }

    private float normaliserAngle(float angle)  { return (angle % 360 + 360) % 360; }

    private float differenceAngulaire(float a, float b)
    {
        float diff = Math.abs(a - b) % 360;
        return Math.min(diff, 360 - diff);
    }

    public float axeVersAngle(float valeur)
    {
        float deg = valeur * 18f;
        return (deg + 360) % 360;
    }
}