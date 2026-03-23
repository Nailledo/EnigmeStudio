package app.enigmeStudio.Outils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import app.enigmeStudio.enigmeMontre.DessinerMontre;

public class MethodeDessiner
{
    private final Paint  pinceau;

    public MethodeDessiner(DessinerMontre vue, Paint pinceau)
    {
        this.pinceau = pinceau;
    }

    public void dessinerUnBouton(Canvas canvas, RectF zone, String texte, boolean actif)
    {
        this.pinceau.setStyle(Paint.Style.FILL);
        this.pinceau.setColor(actif ? Color.rgb(0, 150, 80) : Color.DKGRAY);
        canvas.drawRoundRect(zone, 20, 20, this.pinceau);

        this.pinceau.setStyle(Paint.Style.STROKE);
        this.pinceau.setStrokeWidth(3f);
        this.pinceau.setColor(actif ? Color.GREEN : Color.GRAY);
        canvas.drawRoundRect(zone, 20, 20, this.pinceau);

        this.pinceau.setStyle(Paint.Style.FILL);
        this.pinceau.setColor(actif ? Color.WHITE : Color.LTGRAY);
        this.pinceau.setTextSize(36f);
        this.pinceau.setFakeBoldText(actif);
        canvas.drawText(texte,
                zone.centerX() - this.pinceau.measureText(texte) / 2f,
                zone.centerY() + 13f,
                pinceau);

        this.pinceau.setStyle(Paint.Style.FILL);
        this.pinceau.setStrokeWidth(0f);
        this.pinceau.setFakeBoldText(false);
    }

    public void dessinerAiguille(Canvas canvas, Bitmap aiguille, float angle,
                                 float largeurCible, float cx, float cy)
    {
        if (aiguille == null) return;

        float echelle = largeurCible / aiguille.getWidth();
        float aw      = aiguille.getWidth()  * echelle;
        float ah      = aiguille.getHeight() * echelle;
        RectF zone    = new RectF(cx - aw / 2f, cy - ah / 2f, cx + aw / 4f, cy + ah / 2f);

        canvas.save();
        canvas.rotate(angle, cx, cy);
        canvas.drawBitmap(aiguille, null, zone, this.pinceau);
        canvas.restore();
    }

    // A RAJOUTER POUR UN BTN RETOUR
    /***
     * Attribut
     *         private RectF zoneBoutonRetour = new RectF();
     *
     * Dans la méthode OnDraw()
     *         zoneBoutonRetour.set(20, 100, 150, 175);
     *
     * Dans la méthode OnTouch
     *          if (evenement.getAction() != MotionEvent.ACTION_DOWN) return false;
     *
     *          float touchX = evenement.getX();
     *          float touchY = evenement.getY();
     *
     *          if ( zoneBoutonRetour.contains(touchX, touchY) )
     *          {
     *              finish();
     *              return true;
     *          }
     */
    public static void creerBtnRetour(Canvas canvas, RectF zone, String texte)
    {
        Paint pinceau = new Paint(Paint.ANTI_ALIAS_FLAG);

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(Color.DKGRAY);
        canvas.drawRoundRect(zone, 20, 20, pinceau);

        pinceau.setStyle(Paint.Style.STROKE);
        pinceau.setStrokeWidth(3f);
        pinceau.setColor(Color.GRAY);
        canvas.drawRoundRect(zone, 20, 20, pinceau);

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(Color.LTGRAY);
        pinceau.setTextSize(36f);
        pinceau.setFakeBoldText(true);
        canvas.drawText(texte,
                zone.centerX() - pinceau.measureText(texte) / 2f,
                zone.centerY() + 13f,
                pinceau);

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setStrokeWidth(0f);
        pinceau.setFakeBoldText(true);
    }

    public static void creerBtnIndice(Canvas canvas, Paint pinceau, RectF zone, String texte)
    {
        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(Color.DKGRAY);
        canvas.drawRoundRect(zone, 20, 20, pinceau);

        pinceau.setStyle(Paint.Style.STROKE);
        pinceau.setStrokeWidth(3f);
        pinceau.setColor(Color.GRAY);
        canvas.drawRoundRect(zone, 20, 20, pinceau);

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(Color.LTGRAY);
        pinceau.setTextSize(36f);
        pinceau.setFakeBoldText(true);
        canvas.drawText(texte,
                zone.centerX() - pinceau.measureText(texte) / 2f,
                zone.centerY() + 13f,
                pinceau);

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setStrokeWidth(0f);
        pinceau.setFakeBoldText(true);
    }
}