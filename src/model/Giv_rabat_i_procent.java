package model;

import controller.Controller;

public class Giv_rabat_i_procent implements Strategy_giv_rabat {

    // Controller instans
    Controller controller = Controller.getInstance();

    @Override
    public double giv_rabat(double rabat, Ordre ordre) {
        double pris = controller.beregnPris(ordre);
        pris = pris * (1 - rabat / 100);

        return pris;
    }

}
