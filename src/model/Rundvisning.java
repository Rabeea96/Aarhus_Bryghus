package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rundvisning extends Produkt {

    private LocalDate dato;
    private LocalTime tidspunkt;
    private boolean studierabat; // studierabat er sat til 10%

    public Rundvisning(String navn, Produktgruppe produktgruppe, LocalDate dato, LocalTime tidspunkt,
            boolean studierabat) {
        super(navn, produktgruppe);
        this.dato = dato;
        this.tidspunkt = tidspunkt;
        this.studierabat = studierabat;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public LocalTime getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(LocalTime tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    public boolean isStudierabat() {
        return studierabat;
    }

    public void setStudierabat(boolean studierabat) {
        this.studierabat = studierabat;
    }

    @Override
    public void beregnPris() {
        double pris = 100;
        // hvis tidspunktet for rundvisningen er kl 16 eller efter kl 16
        if (tidspunkt.isAfter(LocalTime.of(16, 00)) || tidspunkt.equals(LocalTime.of(16, 00))) {
            pris = 120;
        }

        if (isStudierabat() == true) {
            // studierabat er sat til 10%
            pris = pris * (1 - 10 / 100.0);
        }

        for (Produktpris p : super.getProduktpriser()) {
            // hvis produktnavnet i produktpriser har samme produktnavn
            if (p.getProdukt().getNavn().equals(super.getNavn())) {
                p.setPris(pris);
            }
        }
    }

}
