package app.enigmeStudio;

import android.content.Context;
import android.graphics.*;
import android.hardware.*;
import android.os.Bundle;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class EnigmeMontre extends AppCompatActivity implements SensorEventListener
{
    private float[]       acceleration = new float[3];
    private SensorManager gestionnaire;
    private ViewDessin    vue;
    private int           heureCible;
    private int           minuteCible;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Calendar calendrier = Calendar.getInstance();
        heureCible  = calendrier.get(Calendar.HOUR_OF_DAY) % 12; // 0-11
        minuteCible = calendrier.get(Calendar.MINUTE);            // 0-59

        vue = new ViewDessin(this);
        setContentView(vue);

        gestionnaire = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Sensor accelerometre = gestionnaire.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

    // Vérifie avec les angles verrouillés (pas les valeurs live du capteur)
    private boolean estResolu()
    {
        float normMinute = normaliserAngle(vue.angleGrandeVerrouille);
        float normHeure  = normaliserAngle(vue.anglePetiteVerrouille);

        // Cible : angle réel sur un cadran d'horloge (12h = 0°, sens horaire)
        float cibleMinute = minuteCible * 6f;                            // 1 min = 6°
        float cibleHeure  = (heureCible * 60 + minuteCible) * 0.5f;     // 1 min = 0.5°

        return differenceAngulaire(normMinute, cibleMinute) <= 10f
                && differenceAngulaire(normHeure,  cibleHeure)  <= 10f;
    }

    private float normaliserAngle(float angle)
    {
        return (angle % 360 + 360) % 360;
    }

    private float differenceAngulaire(float a, float b)
    {
        float diff = Math.abs(a - b) % 360;
        return Math.min(diff, 360 - diff);
    }


    // -------------------------------------------------------------------------
    private class ViewDessin extends View implements View.OnTouchListener
    {
        private final Bitmap montre;
        private final Bitmap petiteAiguille;
        private final Bitmap grandeAiguille;

        private RectF zoneBoutonGrande = new RectF();
        private RectF zoneBoutonPetite = new RectF();

        // Angles verrouillés — accessibles depuis estResolu()
        float angleGrandeVerrouille = 0f;
        float anglePetiteVerrouille = 0f;

        // Étapes : 0 = régler grande aiguille, 1 = régler petite, 2 = terminé
        private int etape = 0;

        ViewDessin(Context contexte)
        {
            super(contexte);
            setOnTouchListener(this);

            montre         = BitmapFactory.decodeResource(getResources(), R.drawable.montre);
            petiteAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.petite_aiguille);
            grandeAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.grande_aiguille);
        }

        @Override
        public boolean onTouch(View v, MotionEvent evenement)
        {
            if (evenement.getAction() != MotionEvent.ACTION_DOWN) return false;

            float touchX = evenement.getX();
            float touchY = evenement.getY();

            if (etape == 0 && zoneBoutonGrande.contains(touchX, touchY))
            {
                // On stocke l'angle brut (sera normalisé dans estResolu)
                angleGrandeVerrouille = acceleration[0] * 36f;
                etape = 1;
                invalidate();
                return true;
            }

            if (etape == 1 && zoneBoutonPetite.contains(touchX, touchY))
            {
                anglePetiteVerrouille = acceleration[1] * 36f;
                etape = 2;
                invalidate();
                return true;
            }

            return false;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Paint pinceau = new Paint(Paint.ANTI_ALIAS_FLAG);
            int largeur   = getWidth();
            int hauteur   = getHeight();

            float angleGrande = (etape >= 1) ? angleGrandeVerrouille : acceleration[0] * 36f;
            float anglePetite = (etape >= 2) ? anglePetiteVerrouille : acceleration[1] * 36f;

            dessinerInfo(canvas, pinceau, largeur);
            dessinerBoutons(canvas, pinceau, largeur, hauteur);

            if (montre == null) return;

            RectF zoneMontre = calculerZoneMontre(largeur, hauteur);
            canvas.drawBitmap(montre, null, zoneMontre, pinceau);

            float cx = zoneMontre.centerX();
            float cy = zoneMontre.centerY();
            float lz = zoneMontre.width();

            dessinerAiguille(canvas, pinceau, grandeAiguille, cx, cy, angleGrande, lz * 0.40f);
            dessinerAiguille(canvas, pinceau, petiteAiguille, cx, cy, anglePetite, lz * 0.25f);
        }

        private void dessinerInfo(Canvas canvas, Paint pinceau, int largeur)
        {
            // Affichage de l'heure cible pour debug / aide
            pinceau.setColor(Color.WHITE);
            pinceau.setTextSize(38f);
            canvas.drawText(String.format("Cible : %02dh%02d", heureCible, minuteCible), 30, 60, pinceau);

            if (etape == 0)
                canvas.drawText("Étape 1 : placez la grande aiguille (minutes)", 30, 110, pinceau);
            else if (etape == 1)
                canvas.drawText("Étape 2 : placez la petite aiguille (heures)", 30, 110, pinceau);
            else if (estResolu())
            {
                pinceau.setColor(Color.GREEN);
                pinceau.setTextSize(80f);
                pinceau.setFakeBoldText(true);
                canvas.drawText("RÉUSSI !", largeur / 2f - 150, 170, pinceau);
            }
            else
            {
                pinceau.setColor(Color.RED);
                pinceau.setTextSize(45f);
                canvas.drawText("Mauvaise position, recommencez !", 30, 170, pinceau);
                // Reset après affichage du message (via post pour ne pas bloquer le UI thread)
                postDelayed(() -> { etape = 0; invalidate(); }, 2000);
            }
        }

        private void dessinerBoutons(Canvas canvas, Paint pinceau, int largeur, int hauteur)
        {
            float btnLargeur = largeur / 2f - 30f;
            float btnHauteur = 100f;
            float btnY       = hauteur - btnHauteur - 160f;

            zoneBoutonGrande.set(20,                  btnY, 20 + btnLargeur,    btnY + btnHauteur);
            zoneBoutonPetite.set(largeur / 2f + 10,   btnY, largeur - 20,       btnY + btnHauteur);

            dessinerUnBouton(canvas, pinceau, zoneBoutonGrande, "Grande aiguille", etape == 0);
            dessinerUnBouton(canvas, pinceau, zoneBoutonPetite, "Petite aiguille", etape == 1);
        }

        private void dessinerUnBouton(Canvas canvas, Paint pinceau, RectF zone,
                                      String texte, boolean actif)
        {
            pinceau.setStyle(Paint.Style.FILL);
            pinceau.setColor(actif ? Color.rgb(0, 150, 80) : Color.DKGRAY);
            canvas.drawRoundRect(zone, 20, 20, pinceau);

            pinceau.setStyle(Paint.Style.STROKE);
            pinceau.setStrokeWidth(3f);
            pinceau.setColor(actif ? Color.GREEN : Color.GRAY);
            canvas.drawRoundRect(zone, 20, 20, pinceau);

            pinceau.setStyle(Paint.Style.FILL);
            pinceau.setColor(actif ? Color.WHITE : Color.LTGRAY);
            pinceau.setTextSize(36f);
            pinceau.setFakeBoldText(actif);
            canvas.drawText(texte,
                    zone.centerX() - pinceau.measureText(texte) / 2f,
                    zone.centerY() + 13f,
                    pinceau);

            pinceau.setStyle(Paint.Style.FILL);
            pinceau.setStrokeWidth(0f);
            pinceau.setFakeBoldText(false);
        }

        private RectF calculerZoneMontre(int largeur, int hauteur)
        {
            float echelle  = Math.min((float) largeur / montre.getWidth(),
                    (float) hauteur / montre.getHeight()) * 0.65f;
            float mLargeur = montre.getWidth()  * echelle;
            float mHauteur = montre.getHeight() * echelle;
            float gauche   = (largeur - mLargeur) / 2f;
            float haut     = (hauteur - mHauteur) / 2f - 80f;

            return new RectF(gauche, haut, gauche + mLargeur, haut + mHauteur);
        }

        private void dessinerAiguille(Canvas canvas, Paint pinceau, Bitmap aiguille,
                                      float cx, float cy, float angle, float largeurCible)
        {
            if (aiguille == null) return;

            float echelle = largeurCible / aiguille.getWidth();
            float aw      = aiguille.getWidth()  * echelle;
            float ah      = aiguille.getHeight() * echelle;
            RectF zone    = new RectF(cx - aw / 2f, cy - ah / 2f, cx + aw / 4f, cy + ah / 2f);

            canvas.save();
            canvas.rotate(angle, cx, cy);
            canvas.drawBitmap(aiguille, null, zone, pinceau);
            canvas.restore();
        }
    }
}