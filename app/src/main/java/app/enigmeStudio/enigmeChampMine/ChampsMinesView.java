package app.enigmeStudio.enigmeChampMine;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import android.os.Vibrator ;

public class ChampsMinesView extends View implements  View.OnTouchListener
{
	private Arrive arrive;
	private float posDepartJX;
	private float posDepartJY;
	private int rayonJ;
	private Paint styleMines = new Paint();
	private Paint styleJoueur = new Paint();
	private Paint styleArrive = new Paint();
	private Paint styleTexte = new Paint();
	private Paint styleTest = new Paint();  // a enlever

	private ArrayList<Mine> tabMines;

	private int cptNiveau = 1;
	private int cptMort = 0;

	private float joueurX;
	private float joueurY;
	private float cibleX;
	private float cibleY;

	private boolean enMouvement;
	private Vibrator vibor;


	public ChampsMinesView(Context context)
	{
		super(context);
		setFocusable(true);

		this.vibor = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

		this.styleMines.setColor(Color.RED);
		this.styleMines.setStyle(Paint.Style.FILL);

		this.styleArrive.setColor(Color.GREEN);
		this.styleArrive.setStyle(Paint.Style.FILL);

		this.styleJoueur.setColor(Color.BLUE);
		this.styleJoueur.setStyle(Paint.Style.FILL);

		this.styleTest.setColor(Color.MAGENTA);    // a enlever
		this.styleTest.setStyle(Paint.Style.FILL);  // a enlever

		this.styleTexte.setColor(Color.WHITE);
		this.styleTexte.setTextSize(55f); // Taille de la police
		this.styleTexte.setTextAlign(Paint.Align.LEFT);

		this.setOnTouchListener(this);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		this.tabMines = GenererTabMines.generer(w, h, this.cptNiveau);

		this.arrive = new Arrive(w/2, 130,40);

		this.posDepartJX = w/2;
		this.posDepartJY = h- 130;
		this.rayonJ = 20;

		this.joueurX = this.posDepartJX;
		this.joueurY = this.posDepartJY;

	}



	public void onDraw(Canvas canva)
	{
		super.onDraw(canva);

		canva.drawColor(Color.BLACK);

		if(this.enMouvement)
		{
			float vitesse = 10f;
			float distanceX = this.cibleX - this.joueurX;
			float distanceY = this.cibleY - this.joueurY;
			float distanceTot = (float) Math.sqrt((distanceY * distanceY)+(distanceX*distanceX));

			if (distanceTot > vitesse)
			{
				this.joueurX += (distanceX / distanceTot) * vitesse;
				this.joueurY += (distanceY / distanceTot) * vitesse;

				canva.drawCircle(this.joueurX, this.joueurY, this.rayonJ, this.styleJoueur);
				this.contactMine();
				this.procheTunnel();
				this.gagner();
				this.invalidate();
			}
			else
			{
				this.joueurX = this.cibleX;
				this.joueurY = this.cibleY;
				this.enMouvement = false;
				canva.drawCircle(this.joueurX, this.joueurY, this.rayonJ, this.styleJoueur);
			}

		}
		else
			canva.drawCircle(this.joueurX, this.joueurY, 20, this.styleJoueur);


		for (Mine m : this.tabMines)
			if (m.estTunnel())							// a enlever
				canva.drawCircle(m.getX(), m.getY(), m.getRayon(), this.styleTest); // a enlever
			else
				canva.drawCircle(m.getX(), m.getY(), m.getRayon(), this.styleMines);

		canva.drawCircle(this.arrive.getX(), this.arrive.getY(), this.arrive.getRayon(), this.styleArrive);

		if (this.cptMort >= 2)
			canva.drawText("Indice : Cherche la mine qui vibre", 0f, 250f, this.styleTexte);


	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent)
	{
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
		{
			this.cibleX =  motionEvent.getX();
			this.cibleY =  motionEvent.getY();
			this.enMouvement = true;

			this.invalidate();

			return true;
		}
		return false;
	}

	public void contactMine()
	{
		for (Mine m : this.tabMines)
		{
			if (m.contactJoueur(this.joueurX, this.joueurY, this.rayonJ,0))
			{
				if (m.estTunnel())
				{
					this.joueurX = this.arrive.getX() * 2 - 80;
					this.joueurY = this.arrive.getY() + 100;

				}
				else
				{
					this.vibor.vibrate(300);

					this.joueurX = this.posDepartJX;
					this.joueurY = this.posDepartJY;
					this.cptMort++;
				}

				this.enMouvement = false;
				this.invalidate();
				return;
			}
		}
	}

	public void procheTunnel()
	{
		for (Mine m : this.tabMines)
		{
			if (m.estTunnel())
				if (m.contactJoueur(this.joueurX, this.joueurY, this.rayonJ, 35))
				{
					System.out.println("brrrrrrrrrrbrrrbbrbrbbrbrbrbbrbr"); // a enlevé
					vibor.vibrate(20);
					return;
				}
		}
	}

	public void gagner()
	{
		if (this.arrive.gagner(this.joueurX,this.joueurY,this.rayonJ))
		{
			if (this.cptNiveau == 1)
			{
				this.cptNiveau ++;
				this.tabMines = GenererTabMines.generer(this.getWidth(), this.getHeight(), this.cptNiveau);

				this.joueurY = this.posDepartJY;
				this.joueurX = this.posDepartJX;

				this.cibleX = this.posDepartJX;
				this.cibleY = this.posDepartJY;
				this.enMouvement = false;
				this.cptMort = 0;
			}
			else{
				//jsp quoi faire
			}

		}
	}



}
