package app.enigmeStudio.enigmeMontre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import app.enigmeStudio.Outils.MethodeDessiner;
import app.enigmeStudio.Outils.Sauvegarde;
import app.enigmeStudio.R;

public class DessinerMontre extends View implements View.OnTouchListener
{
    private MethodeDessiner methodeDessiner;
    private EnigmeMontre    activite;

    private final Bitmap montre;
    private final Bitmap petiteAiguille;
    private final Bitmap grandeAiguille;

    private RectF zoneBoutonGrande = new RectF();
    private RectF zoneBoutonPetite = new RectF();
    private RectF zoneBoutonRetour = new RectF();
    private RectF zoneMontre       = new RectF();
    private RectF zoneBoutonInd1   = new RectF();
    private RectF zoneBoutonInd2   = new RectF();

    private int     viewWidth;
    private int     viewHeight;
    private float   angleGrandeVerrouille = 0f;
    private float   anglePetiteVerrouille = 0f;
    private Paint   pinceau;
    private float   cx, cy, lz;
    private int     etape = 0;
    private boolean indice1Appuye, indice2Appuye;
    private boolean enAttente = false;
    private Context context;

    public DessinerMontre(EnigmeMontre activite)
    {
        super(activite);
        this.activite = activite;
        setOnTouchListener(this);

        this.context = getContext();

        this.montre         = BitmapFactory.decodeResource(getResources(), R.drawable.montre);
        this.petiteAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.petite_aiguille);
        this.grandeAiguille = BitmapFactory.decodeResource(getResources(), R.drawable.grande_aiguille);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        this.pinceau         = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.methodeDessiner = new MethodeDessiner(this, this.pinceau);

        this.viewWidth  = w;
        this.viewHeight = h;
        this.zoneMontre = calculerZoneMontre(w, h);

        this.cx = this.zoneMontre.centerX();
        this.cy = this.zoneMontre.centerY();
        this.lz = this.zoneMontre.width();
    }

    @Override
    public boolean onTouch(View v, MotionEvent evenement)
    {
        if (evenement.getAction() != MotionEvent.ACTION_DOWN) return false;

        float touchX = evenement.getX();
        float touchY = evenement.getY();

        if ( this.etape == 0 && this.zoneBoutonPetite.contains(touchX, touchY))
        {
            this.anglePetiteVerrouille = this.activite.axeVersAngle(this.activite.getAcceleration()[1]);
            this.etape = 1;
            invalidate();
            return true;
        }

        if (this.etape == 1 && this.zoneBoutonGrande.contains(touchX, touchY))
        {
            this.angleGrandeVerrouille = this.activite.axeVersAngle(this.activite.getAcceleration()[0]);
            this.etape = 2;
            invalidate();
            return true;
        }

        if (this.zoneBoutonRetour.contains(touchX, touchY))
        {
            this.activite.finish();
            return true;
        }

        if (this.zoneBoutonInd1.contains(touchX, touchY))
        {
            this.indice1Appuye = true;
            invalidate();
            return true;
        }

        if (this.zoneBoutonInd2.contains(touchX, touchY))
        {
            this.indice2Appuye = true;
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        float anglePetite = (this.etape >= 1)
                ? this.anglePetiteVerrouille
                : this.activite.axeVersAngle(this.activite.getAcceleration()[1]);

        float angleGrande = (this.etape >= 2)
                ? this.angleGrandeVerrouille
                : this.activite.axeVersAngle(this.activite.getAcceleration()[0]);

        this.dessinerInfo(canvas );
        dessinerBoutons(canvas);

        MethodeDessiner.creerBtnRetour(canvas, this.pinceau, this.zoneBoutonRetour, "<");

        if (this.montre == null) return;

        canvas.drawBitmap(this.montre, null, this.zoneMontre, this.pinceau);
        this.methodeDessiner.dessinerAiguille(canvas, this.grandeAiguille, angleGrande,
                                                      this.lz * 0.40f, this.cx, this.cy);
        this.methodeDessiner.dessinerAiguille(canvas, this.petiteAiguille, anglePetite,
                                                      this.lz * 0.25f, this.cx, this.cy);
    }

    private void dessinerBoutons(Canvas canvas)
    {
        this.zoneBoutonPetite.set(this.viewWidth / 2 - 250, this.viewHeight / 2 + 300,
                                  this.viewWidth / 2 + 250, this.viewHeight / 2 + 430);
        this.zoneBoutonGrande.set(this.viewWidth / 2 - 250, this.viewHeight / 2 + 300,
                                  this.viewWidth / 2 + 250, this.viewHeight / 2 + 430);
        this.zoneBoutonRetour.set(20, 100, 150, 175);
        this.zoneBoutonInd1.set(this.viewWidth - 500, 100, this.viewWidth - 270, 250);
        this.zoneBoutonInd2.set(this.viewWidth - 250, 100, this.viewWidth - 20, 250);

        if (this.etape == 0)
            this.methodeDessiner.dessinerUnBouton(canvas, this.zoneBoutonPetite, this.context.getString(R.string.bloquer_petite_aiguille), true);
        else if (etape == 1)
            this.methodeDessiner.dessinerUnBouton(canvas, this.zoneBoutonGrande, this.context.getString(R.string.bloquer_grande_aiguille), true);

        this.methodeDessiner.dessinerUnBouton(canvas, this.zoneBoutonInd1, this.context.getString(R.string.indice_1_label), !this.indice1Appuye);
        this.methodeDessiner.dessinerUnBouton(canvas, this.zoneBoutonInd2, this.context.getString(R.string.indice_2_label),  this.indice1Appuye);
    }

    private RectF calculerZoneMontre(int largeur, int hauteur)
    {
        float echelle  = Math.min((float) largeur / this.montre.getWidth(),
                                  (float) hauteur / this.montre.getHeight()) * 0.65f;
        float mLargeur = this.montre.getWidth()  * echelle;
        float mHauteur = this.montre.getHeight() * echelle;
        float gauche   = (largeur - mLargeur) / 2f;
        float haut     = (hauteur - mHauteur) / 2f - 80f;

        return new RectF(gauche, haut, gauche + mLargeur, haut + mHauteur);
    }

    public void dessinerInfo(Canvas canvas )
    {
        this.pinceau.setColor(Color.WHITE);
        this.pinceau.setTextSize(38f);

        if (indice1Appuye)
        {
            canvas.drawText(this.context.getString(R.string.indice_1_texte_1), 20, this.viewHeight - 400, this.pinceau);
            canvas.drawText(this.context.getString(R.string.indice_1_texte_2), 150, viewHeight - 360, this.pinceau);
        }

        if (indice2Appuye)
            canvas.drawText(this.context.getString(R.string.indice_2_texte), 20, viewHeight - 300, this.pinceau);

        if ( this.etape == 2)
        {
            canvas.drawText(this.context.getString(R.string.heure_montre_format, this.activite.getHeureAiguille(),
                            this.activite.getMinuteAiguille()), 30, this.viewHeight / 10, this.pinceau);
        }

        if (etape == 0)
        {
            canvas.drawText(this.context.getString(R.string.etape_1_titre), this.viewWidth / 6, this.viewHeight / 4, this.pinceau);
            this.pinceau.setTextSize(34f);
            canvas.drawText(this.context.getString(R.string.etape_bloquer_info), this.viewWidth / 9, Math.round(this.viewHeight / 3.5), this.pinceau);
        }
        else if (etape == 1)
        {
            canvas.drawText(this.context.getString(R.string.etape_2_titre), viewWidth / 6, viewHeight / 4, this.pinceau);
            this.pinceau.setTextSize(34f);
            canvas.drawText(this.context.getString(R.string.etape_bloquer_info), viewWidth / 9, Math.round(viewHeight / 3.5), this.pinceau);
        }
        else if ( this.activite.estResolu() )
        {
            this.pinceau.setColor(Color.GREEN);
            this.pinceau.setTextSize(80f);
            this.pinceau.setFakeBoldText(true);
            canvas.drawText(this.context.getString(R.string.reussi_titre), viewWidth / 5, viewHeight / 4, this.pinceau);

            this.pinceau.setColor(Color.WHITE);
            this.pinceau.setTextSize(38f);
            this.pinceau.setFakeBoldText(false);
            canvas.drawText(this.context.getString(R.string.revenir_accueil_info), viewWidth / 5 + 50, Math.round(viewHeight / 3.5), this.pinceau);

            // SAUVEGARDE
            Sauvegarde.setEnigme2Reussi(true);

            if (!this.enAttente)
            {
                this.enAttente = true;
                postDelayed(() -> {
                    this.enAttente = false;
                    this.activite.finish();
                }, 3000);
            }
        }
        else
        {
            this.pinceau.setColor(Color.RED);
            this.pinceau.setTextSize(45f);
            canvas.drawText( this.context.getString(R.string.E1_erreur), viewWidth / 4, viewHeight / 5, this.pinceau);

            if (!this.enAttente)
            {
                this.enAttente = true;
                postDelayed(() -> {
                    this.etape                  = 0;
                    this.angleGrandeVerrouille  = 0f;
                    this.anglePetiteVerrouille  = 0f;
                    this.enAttente              = false;
                    invalidate();
                }, 3000);
            }
        }
    }

    public float getAngleGrandeVerrouille() { return this.angleGrandeVerrouille; }
    public float getAnglePetiteVerrouille() { return this.anglePetiteVerrouille; }
}
