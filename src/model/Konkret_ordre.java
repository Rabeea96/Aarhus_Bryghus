package model;

import java.time.LocalDate;

public class Konkret_ordre extends Ordre {

    // ordre uden rabat
    public Konkret_ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        super(betalingsmiddel, dato, prisliste);
    }

    // ordre med rabat
    public Konkret_ordre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste,
            Strategy_giv_rabat strategy, double rabat) {
        super(betalingsmiddel, dato, prisliste, strategy, rabat);
    }

    @Override
    public double samletpris() {
        double pris = 0;

        for (Ordrelinje o : super.getOrdrelinjer()) {

            if (super.getPrisliste().getNavn().equals(o.getProduktpris().getPrisliste().getNavn())) {
                pris = pris + o.getAntal() * o.getProduktpris().getPris();
            }
        }

        return pris;
    }

    // denne metode kører kun hvis rabat_angivet == true - det bliver tjekket i
    // controlleren når salget skal registreres
    @Override
    public double samletpris_med_rabat() {

        double pris = executeStrategy(super.getRabat(), this);

        return pris;
    }

}
