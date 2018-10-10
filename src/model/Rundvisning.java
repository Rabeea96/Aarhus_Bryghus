package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rundvisning extends Produkt {

    private LocalDate dato;
    private LocalTime tidspunkt;

    public Rundvisning(String navn, Produktgruppe produktgruppe, LocalDate dato, LocalTime tidspunkt) {
        super(navn, produktgruppe);
        this.dato = dato;
        this.tidspunkt = tidspunkt;
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

    @Override
    public double beregnPris() {
        // TODO Auto-generated method stub
        return 0;
    }

}
