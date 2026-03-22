package app.enigmeStudio.enigmeChampMine;

public class Arrive
{
	private float x;
	private float y;
	private int rayon;

	public Arrive(float x, float y, int rayon)
	{
		this.x =x;
		this.y =y;
		this.rayon = rayon;
	}

	public float getX() {return x;}

	public float getY() {return y;}

	public int getRayon() {return rayon;}

	public boolean gagner(float jX, float jY, int jR)
	{
		float dX = Math.abs(jX-this.x);
		float dY = Math.abs(jY - this.y);
		float distJMine = (float) Math.sqrt(dX*dX + dY * dY);
		float sommeRayon = this.rayon + jR ;

		return sommeRayon >= distJMine;

	}


}
