package model;

public class Giv_rabat_i_procent implements Strategy_giv_rabat {

    @Override
    public double giv_rabat(double rabat, Ordre ordre) {
        double pris = ordre.samletpris();
        pris = pris * (1 - rabat / 100);

        return pris;
    }

}
