package app.enigmeStudio;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Parametre extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
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
    public static void creerBtnRetour(Canvas canvas, Paint pinceau, RectF zone, String texte)
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