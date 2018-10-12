package model;

import java.time.LocalDate;

public class Fadølsanlæg_udlejning extends Produkt {

    private LocalDate startDato;
    private LocalDate slutDato;

    public Fadølsanlæg_udlejning(String navn, Produktgruppe produktgruppe, LocalDate startDato, LocalDate slutDato) {
        super(navn, produktgruppe);
        this.startDato = startDato;
        this.slutDato = slutDato;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    // beregner prisen for en fadælsanlæg - det dækker over en fustage, anlæg og
    // kulsyre
    @Override
    public void beregnPris() {
        // TODO Auto-generated method stub
    }

}
