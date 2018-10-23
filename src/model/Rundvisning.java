package model;

public class Rundvisning extends Produkt {

    // studierabat er sat til 10%

    public Rundvisning(String navn, Produktgruppe produktgruppe) {
        super(navn, produktgruppe);
    }

    @Override
    public void beregnPris() {

    }

}
