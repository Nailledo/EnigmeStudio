package app.enigmeStudio.enigmeChampMine;

public class Mine
{
    private float x;
    private float y;
    private boolean tunnel;
    private float rayon;

    public Mine (float x, float y, float rayon)
    {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
    }

    public void setTunnel (boolean estTunnel)
    {
        this.tunnel = estTunnel;
    }

    public float getX () {return this.x;}
    public float getY () {return this.y;}
    public boolean estTunnel () {return this.tunnel;}
    public float getRayon () {return this.rayon;}

    public boolean contactJoueur(float jX, float jY, int jR,int zoneDetect)
    {
        float dX = Math.abs(jX-this.x);
        float dY = Math.abs(jY - this.y);
        float distJMine = (float) Math.sqrt(dX*dX + dY * dY);
        float sommeRayon = this.rayon + jR + zoneDetect;

        return sommeRayon >= distJMine;


    }
}
