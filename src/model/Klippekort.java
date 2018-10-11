package model;

import java.time.LocalDate;

import container.Container;

public class Klippekort extends Produkt {

    // Container instans
    Container container = Container.getInstance();
    private int antal_klip;

    public Klippekort(String navn, Produktgruppe produktgruppe) {
        super(navn, produktgruppe);
        setAntal_klip(4);
    }

    public int getAntal_klip() {
        return antal_klip;
    }

    public void setAntal_klip(int antal_klip) {
        this.antal_klip = antal_klip;
    }

    @Override
    public void beregnPris() {
        // TODO Auto-generated method stub
    }

}