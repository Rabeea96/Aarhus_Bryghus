package model;

public class Rundvisning extends Produkt {

    public Rundvisning(String navn, Produktgruppe produktgruppe) {
        super(navn, produktgruppe);
        super.setNavn(navn + ", pr. person");
    }

}
