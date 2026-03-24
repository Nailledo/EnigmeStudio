package app.enigmeStudio.enigmeMontre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import app.enigmeStudio.Outils.MethodeDessiner;
import app.enigmeStudio.Outils.Sauvegarde;
import app.enigmeStudio.R;

public class DessinerMontre extends View implements View.OnTouchListener
{
    private MethodeDessiner methodeDessiner;
    private EnigmeMontre activite;

    private final Bitmap montre;
    private final Bitmap petiteAiguille;
    private final Bitmap grandeAiguille;

    private RectF zoneBoutonGrande = new RectF();
    private RectF zoneBoutonPetite = new RectF();
    private RectF zoneBoutonRetour = new RectF();
    private RectF zoneMontre       = new RectF();
    private RectF zoneBoutonInd1   = new RectF();
    private RectF zoneBoutonInd2   = new RectF();

    private int viewWidth;
    private int viewHeight;
    private float angleGrandeVerrouille = 0f;
    private float anglePetiteVerrouille = 0f;
    private Paint pinceau;
    private float cx, cy, lz;
    private int etape = 0;
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

    private RectF calculerZoneMontre(int largeur, int hauteur)
    {
        float echelle = ((float) largeur / this.montre.getWidth()) * 0.60f;
        float mLargeur = this.montre.getWidth() * echelle;
        float mHauteur = this.montre.getHeight() * echelle;
        float gauche = (largeur - mLargeur) / 2f;
        float haut = (hauteur - mHauteur) * 0.45f;
        return new RectF(gauche, haut, gauche + mLargeur, haut + mHauteur);
    }

    @Override
    public boolean onTouch(View v, MotionEvent evenement)
    {
        if (evenement.getAction() == MotionEvent.ACTION_DOWN )
        {
            float touchX = evenement.getX();
            float touchY = evenement.getY();

            if (etape == 1 && zoneBoutonGrande.contains(touchX, touchY))
            {
                this.angleGrandeVerrouille = activite.axeVersAngle(activite.getAcceleration()[0]);
                this.etape = 2;
            }

            if (etape == 0 && zoneBoutonPetite.contains(touchX, touchY))
            {
                this.anglePetiteVerrouille = activite.axeVersAngle(activite.getAcceleration()[1]);
                this.etape = 1;
            }

            if (zoneBoutonRetour.contains(touchX, touchY) )
                activite.finish();

            if (zoneBoutonInd1.contains(touchX, touchY))
                indice1Appuye = true;

            if ( indice1Appuye && zoneBoutonInd2.contains(touchX, touchY) )
                indice2Appuye = true;

            invalidate();
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        float anglePetite = (etape >= 1) ? anglePetiteVerrouille : activite.axeVersAngle(activite.getAcceleration()[1]);
        float angleGrande = (etape >= 2) ? angleGrandeVerrouille : activite.axeVersAngle(activite.getAcceleration()[0]);

        this.dessinerInfo   (canvas);
        this.dessinerBoutons(canvas);
        MethodeDessiner.creerBtnRetour(canvas, zoneBoutonRetour, context.getString( R.string.Retour ) );

        if (montre != null)
        {
            canvas.drawBitmap(montre, null, zoneMontre, pinceau);
            methodeDessiner.dessinerAiguille(canvas, grandeAiguille, angleGrande, lz * 0.40f, cx, cy);
            methodeDessiner.dessinerAiguille(canvas, petiteAiguille, anglePetite, lz * 0.25f, cx, cy);
        }
    }

    private void dessinerBoutons(Canvas canvas)
    {
        float cX = this.viewWidth  * 0.5f;
        float bW = this.viewWidth  * 0.70f;
        float bH = this.viewHeight * 0.07f;
        float bY = this.zoneMontre.bottom + (viewHeight * 0.05f);

        this.zoneBoutonPetite.set(cX - bW / 2, bY, cX + bW / 2, bY + bH);
        this.zoneBoutonGrande.set(cX - bW / 2, bY, cX + bW / 2, bY + bH);

        // Boutons Indices en haut à droite
        float iW = viewWidth * 0.22f;
        float iH = viewHeight * 0.06f;
        this.zoneBoutonInd1.set(viewWidth * 0.52f, viewHeight * 0.03f, viewWidth * 0.52f + iW, viewHeight * 0.03f + iH);
        this.zoneBoutonInd2.set(viewWidth * 0.76f, viewHeight * 0.03f, viewWidth * 0.98f, viewHeight * 0.03f + iH);

        this.zoneBoutonRetour.set(viewWidth * 0.03f, viewHeight * 0.03f, viewWidth * 0.25f, viewHeight * 0.08f);

        if (etape == 0)
            this.methodeDessiner.dessinerUnBouton(canvas, zoneBoutonPetite, context.getString(R.string.bloquer_petite_aiguille), true);
        else if (etape == 1)
            this.methodeDessiner.dessinerUnBouton(canvas, zoneBoutonGrande, context.getString(R.string.bloquer_grande_aiguille), true);

        this.methodeDessiner.dessinerUnBouton(canvas, zoneBoutonInd1, context.getString(R.string.indice_1_label), !indice1Appuye);
        this.methodeDessiner.dessinerUnBouton(canvas, zoneBoutonInd2, context.getString(R.string.indice_2_label), indice1Appuye);
    }

    public void dessinerInfo(Canvas canvas)
    {
        float tTitre    = this.viewHeight * 0.024f;
        float tSous     = this.viewHeight * 0.016f;
        float xCentre   = this.viewWidth  * 0.5f;
        float yTexteBas = this.zoneMontre.top - (viewHeight * 0.02f);

        pinceau.setTextAlign(Paint.Align.CENTER);

        if (etape == 0 || etape == 1)
        {
            pinceau.setColor(Color.WHITE);
            pinceau.setTextSize(tSous);
            canvas.drawText(this.context.getString(R.string.txt_explication1), xCentre, yTexteBas - tSous, pinceau);
            canvas.drawText(this.context.getString(R.string.txt_explication2), xCentre, yTexteBas, pinceau);

            pinceau.setTextSize(tTitre);
            String titre = (etape == 0) ? context.getString(R.string.etape_1_titre) : context.getString(R.string.etape_2_titre);
            canvas.drawText(titre, xCentre, yTexteBas - (tSous * 3.5f), pinceau);

        }
        else if (activite.estResolu())
        {
            pinceau.setColor(Color.GREEN);
            pinceau.setTextSize(viewHeight * 0.05f);
            pinceau.setFakeBoldText(true);
            canvas.drawText(context.getString(R.string.reussi_titre), xCentre, yTexteBas - tSous, pinceau);

            pinceau.setColor(Color.WHITE);
            pinceau.setFakeBoldText(false);
            pinceau.setTextSize(tSous);
            canvas.drawText(context.getString(R.string.revenir_accueil_info), xCentre, yTexteBas + tSous, pinceau);

            Sauvegarde.setEnigme2Reussi(true);
            if (!enAttente)
            {
                enAttente = true;
                postDelayed(() -> activite.finish(), 3000);
            }
        }
        else if (etape == 2)
        {
            pinceau.setColor(Color.RED);
            pinceau.setTextSize(tTitre);
            canvas.drawText(context.getString(R.string.E1_erreur), xCentre, yTexteBas - tSous, pinceau);

            pinceau.setColor(Color.WHITE);
            pinceau.setTextSize(tSous);
            String h = activite.getHeureAiguille() + "h" + String.format("%02d", activite.getMinuteAiguille())+ "min";
            canvas.drawText(h, xCentre, yTexteBas + (tSous * 0.5f), pinceau);

            if (!enAttente)
            {
                enAttente = true;
                postDelayed(() -> {
                    etape = 0;
                    angleGrandeVerrouille = 0f;
                    anglePetiteVerrouille = 0f;
                    enAttente = false;
                    invalidate();
                }, 3000);
            }
        }

        pinceau.setTextAlign(Paint.Align.LEFT);
        pinceau.setTextSize(tSous);
        pinceau.setColor(Color.WHITE);
        float yI = viewHeight * 0.88f;
        float xM = viewWidth * 0.06f;

        if (indice1Appuye)
        {
            canvas.drawText(context.getString(R.string.indice_1_texte_1), xM, yI, pinceau);
            canvas.drawText(context.getString(R.string.indice_1_texte_2), xM, yI + (tSous * 1.5f), pinceau);
        }
        if (indice2Appuye)
            canvas.drawText(context.getString(R.string.indice_2_texte), xM, yI + (tSous * 3.5f), pinceau);
    }

    public float getAngleGrandeVerrouille() { return angleGrandeVerrouille; }
    public float getAnglePetiteVerrouille() { return anglePetiteVerrouille; }
}