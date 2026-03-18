package app.enigmeStudio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class DessinerMontre extends View implements View.OnTouchListener
{
    private final EnigmeMontre activite;

    private final Bitmap montre;
    private final Bitmap petiteAiguille;
    private final Bitmap grandeAiguille;

    private RectF zoneBoutonGrande = new RectF();
    private RectF zoneBoutonPetite = new RectF();
    private RectF zoneBoutonRetour = new RectF();
    private RectF zoneMontre       = new RectF();
    private RectF zoneBoutonInd1   = new RectF();
    private RectF zoneBoutonInd2   = new RectF();

    private int   viewWidth, viewHeight;
    float         angleGrandeVerrouille = 0f;
    float         anglePetiteVerrouille = 0f;
    Paint         pinceau;
    float         cx, cy, lz;
    private int   etape = 0;
    private boolean indice1Appuye, indice2Appuye;

    DessinerMontre(EnigmeMontre activite)
    {
        super(activite);
        this.activite = activite;
        setOnTouchListener(this);

        montre         = BitmapFactory.decodeResource(getResources(), R.drawable.montre);
        petiteAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.petite_aiguille);
        grandeAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.grande_aiguille);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        pinceau    = new Paint(Paint.ANTI_ALIAS_FLAG);
        viewWidth  = w;
        viewHeight = h;
        zoneMontre = calculerZoneMontre(w, h);
        cx = zoneMontre.centerX();
        cy = zoneMontre.centerY();
        lz = zoneMontre.width();
    }

    @Override
    public boolean onTouch(View v, MotionEvent evenement)
    {
        if (evenement.getAction() != MotionEvent.ACTION_DOWN) return false;

        float touchX = evenement.getX();
        float touchY = evenement.getY();

        if (etape == 0 && zoneBoutonPetite.contains(touchX, touchY))
        {
            anglePetiteVerrouille = activite.axeVersAngle(activite.getAcceleration()[1]);
            etape = 1;
            invalidate();
            return true;
        }

        if (etape == 1 && zoneBoutonGrande.contains(touchX, touchY))
        {
            angleGrandeVerrouille = activite.axeVersAngle(activite.getAcceleration()[0]);
            etape = 2;
            invalidate();
            return true;
        }

        if (zoneBoutonRetour.contains(touchX, touchY))
        {
            activite.finish();
            return true;
        }

        if (zoneBoutonInd1.contains(touchX, touchY))
        {
            indice1Appuye = true;
            invalidate();
            return true;
        }

        if (zoneBoutonInd2.contains(touchX, touchY))
        {
            indice2Appuye = true;
            invalidate();
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        float anglePetite = (etape >= 1)
                ? anglePetiteVerrouille
                : activite.axeVersAngle(activite.getAcceleration()[1]);

        float angleGrande = (etape >= 2)
                ? angleGrandeVerrouille
                : activite.axeVersAngle(activite.getAcceleration()[0]);

        dessinerInfo(canvas);
        dessinerBoutons(canvas);
        Parametre.creerBtnRetour(canvas, pinceau, zoneBoutonRetour, "<");

        if (montre == null) return;

        canvas.drawBitmap(montre, null, zoneMontre, pinceau);
        dessinerAiguille(canvas, grandeAiguille, angleGrande, lz * 0.40f);
        dessinerAiguille(canvas, petiteAiguille, anglePetite, lz * 0.25f);
    }

    private void dessinerInfo(Canvas canvas)
    {
        pinceau.setColor(Color.WHITE);
        pinceau.setTextSize(38f);

        if (indice1Appuye)
        {
            canvas.drawText("Indice : Inclinez le téléphone sur les côtés pour les minutes,", 20, viewHeight - 400, pinceau);
            canvas.drawText("et d'avant en arrière pour les heures.", 150, viewHeight - 360, pinceau);
        }

        if (indice2Appuye)
            canvas.drawText("Indice : Quelle heure est-il ?", 20, viewHeight - 300, pinceau);

        if (etape == 0)
        {
            canvas.drawText("Étape 1 : placez la petite aiguille (heures)", viewWidth / 6, viewHeight / 4, pinceau);
            pinceau.setTextSize(34f);
            canvas.drawText("Vous pouvez bloquer l'aiguille avec le bouton ci-dessous", viewWidth / 9, Math.round(viewHeight / 3.5), pinceau);
        }
        else if (etape == 1)
        {
            canvas.drawText("Étape 2 : placez la grande aiguille (minutes)", viewWidth / 6, viewHeight / 4, pinceau);
            pinceau.setTextSize(34f);
            canvas.drawText("Vous pouvez bloquer l'aiguille avec le bouton ci-dessous", viewWidth / 9, Math.round(viewHeight / 3.5), pinceau);
        }
        else if (activite.estResolu())
        {
            pinceau.setColor(Color.GREEN);
            pinceau.setTextSize(80f);
            pinceau.setFakeBoldText(true);
            canvas.drawText("Vous avez réussi !", viewWidth / 5, viewHeight / 4, pinceau);

            pinceau.setColor(Color.WHITE);
            pinceau.setTextSize(38f);
            pinceau.setFakeBoldText(false);
            canvas.drawText("Vous pouvez revenir à l'accueil.", viewWidth / 5 + 50, Math.round(viewHeight / 3.5), pinceau);

            // NIVEAU REUSSI -> SAUVEGARDE ICI
        }
        else
        {
            pinceau.setColor(Color.RED);
            pinceau.setTextSize(45f);
            canvas.drawText("Ce n'est pas la bonne heure...", viewWidth / 4, viewHeight / 5, pinceau);

            postDelayed(() -> { etape = 0; invalidate(); }, 3000);
        }
    }

    private void dessinerBoutons(Canvas canvas)
    {
        zoneBoutonPetite.set(viewWidth / 2 - 250, viewHeight / 2 + 300, viewWidth / 2 + 250, viewHeight / 2 + 430);
        zoneBoutonGrande.set(viewWidth / 2 - 250, viewHeight / 2 + 300, viewWidth / 2 + 250, viewHeight / 2 + 430);
        zoneBoutonRetour.set(20, 100, 150, 175);
        zoneBoutonInd1.set(viewWidth - 500, 100, viewWidth - 270, 250);
        zoneBoutonInd2.set(viewWidth - 250, 100, viewWidth - 20, 250);

        if (etape == 0)
            dessinerUnBouton(canvas, zoneBoutonPetite, "Bloquer la petite aiguille", true);
        else if (etape == 1)
            dessinerUnBouton(canvas, zoneBoutonGrande, "Bloquer la grande aiguille", true);

        dessinerUnBouton(canvas, zoneBoutonInd1, "Indice 1", !indice1Appuye);
        dessinerUnBouton(canvas, zoneBoutonInd2, "Indice 2",  indice1Appuye);
    }

    private void dessinerUnBouton(Canvas canvas, RectF zone, String texte, boolean actif)
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

    private void dessinerAiguille(Canvas canvas, Bitmap aiguille, float angle, float largeurCible)
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