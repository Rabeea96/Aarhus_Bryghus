package model;

public class Giv_rabat_i_kroner implements Strategy_giv_rabat {

    @Override
    public double giv_rabat(double rabat, Ordre ordre) {
        double pris = ordre.samletpris();
        pris = pris - rabat;

        return pris;
    }
}



