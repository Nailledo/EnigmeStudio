package app.enigmeStudio.enigmeChampMine;

import java.util.ArrayList;

import app.enigmeStudio.enigmeChampMine.Mine;

public class GenererTabMines
{

    public static ArrayList<Mine> generer(int largeurEcran, int hauteurEcran, int niveau) {

        ArrayList<Mine> listeMines = new ArrayList<>();
        float rayonStandard = largeurEcran * 0.05f;
        float rayonStandardPlus = largeurEcran * 0.08f;
        float rayonMoyen = largeurEcran *0.14f;
        float rayonGros = largeurEcran * 0.25f;
        float rayonMini = largeurEcran * 0.02f;

        int aleaTunnel;

        if (niveau == 1)
        {
            //les 4 mines au milieu
            listeMines.add(new Mine(largeurEcran * 0.2f, hauteurEcran * 0.52f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.4f, hauteurEcran * 0.52f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.6f, hauteurEcran * 0.52f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.8f, hauteurEcran * 0.52f, rayonStandard));


            //Les mines de taille moyenne au milieu
            listeMines.add(new Mine(largeurEcran * 0.15f, hauteurEcran * 0.35f, rayonMoyen));
            listeMines.add(new Mine(largeurEcran * 0.45f, hauteurEcran * 0.35f, rayonMoyen));
            listeMines.add(new Mine(largeurEcran * 0.84f, hauteurEcran * 0.35f, rayonMoyen));

            // les mines en haut
            listeMines.add(new Mine(largeurEcran * 0.83f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.61f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.50f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.25f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.375f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.94f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.125f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.72f, hauteurEcran * 0.2f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0f, hauteurEcran * 0.2f, rayonStandard));

            aleaTunnel = (int) (Math.random() * listeMines.size());
            listeMines.get(aleaTunnel).setTunnel(true);

            // la grosse mine du bas
            listeMines.add(new Mine(largeurEcran * 0.5f, hauteurEcran * 0.75f, rayonGros));
        }
        else if (niveau == 2)
        {
            //Les minis mines

            listeMines.add(new Mine(largeurEcran * 0.84f, hauteurEcran * 0.45f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.16f, hauteurEcran * 0.45f, rayonMini));

            aleaTunnel = (int) (Math.random() * listeMines.size());
            listeMines.get(aleaTunnel).setTunnel(true);

            listeMines.add(new Mine(largeurEcran * 0.35f, hauteurEcran * 0.80f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.65f, hauteurEcran * 0.80f, rayonMini));

            listeMines.add(new Mine(largeurEcran * 0.42f, hauteurEcran * 0.69f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.58f, hauteurEcran * 0.69f, rayonMini));

            listeMines.add(new Mine(largeurEcran * 0.17f, hauteurEcran * 0.75f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.8f, hauteurEcran * 0.75f, rayonMini));

            listeMines.add(new Mine(largeurEcran * 0.25f, hauteurEcran * 0.26f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.44f, hauteurEcran * 0.28f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.56f, hauteurEcran * 0.28f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.75f, hauteurEcran * 0.26f, rayonMini));

            listeMines.add(new Mine(largeurEcran * 0.15f, hauteurEcran * 0.22f, rayonMini));
            listeMines.add(new Mine(largeurEcran * 0.85f, hauteurEcran * 0.22f, rayonMini));

            listeMines.add(new Mine(largeurEcran * 0.94f, hauteurEcran * 0.15f, rayonMini));


            //Les mines normal
            for (float i = 0.07f; i <= 0.95f; i += 0.13f)
            {
                listeMines.add(new Mine(largeurEcran * i, hauteurEcran * 0.18f, rayonStandard));
            }

            //
            listeMines.add(new Mine(largeurEcran * 0.2f, hauteurEcran * 0.35f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.4f, hauteurEcran * 0.35f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.6f, hauteurEcran * 0.35f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.8f, hauteurEcran * 0.35f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.3f, hauteurEcran * 0.48f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.5f, hauteurEcran * 0.48f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.7f, hauteurEcran * 0.48f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.4f, hauteurEcran * 0.61f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.6f, hauteurEcran * 0.61f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.5f, hauteurEcran * 0.74f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.25f, hauteurEcran * 0.88f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.75f, hauteurEcran * 0.88f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.5f, hauteurEcran * 0.88f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.05f, hauteurEcran * 0.35f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.95f, hauteurEcran * 0.35f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.05f, hauteurEcran * 0.55f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.95f, hauteurEcran * 0.55f, rayonStandard));

            listeMines.add(new Mine(largeurEcran * 0.2f, hauteurEcran * 0.54f, rayonStandardPlus));
            listeMines.add(new Mine(largeurEcran * 0.8f, hauteurEcran * 0.54f, rayonStandardPlus));
            listeMines.add(new Mine(largeurEcran * 0.35f, hauteurEcran * 0.41f, rayonStandard));
            listeMines.add(new Mine(largeurEcran * 0.65f, hauteurEcran * 0.41f, rayonStandard));


        }
        return listeMines;
    }
}