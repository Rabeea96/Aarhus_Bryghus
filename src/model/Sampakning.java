package model;

public class Sampakning extends Produkt {

    private int antal_øl;
    private int antal_glas;

    // gaveæske - indeholder både øl og glas
    public Sampakning(String navn, Produktgruppe produktgruppe, int antal_øl, int antal_glas) {
        super(navn, produktgruppe);
        this.antal_øl = antal_øl;
        this.antal_glas = antal_glas;
        super.setNavn(navn + " " + antal_øl + " øl, " + antal_glas + " glas");
    }

    // trækasse - indeholder øl
    public Sampakning(String navn, Produktgruppe produktgruppe, int antal_øl) {
        super(navn, produktgruppe);
        this.antal_øl = antal_øl;
        super.setNavn(navn + " " + antal_øl + " øl");
    }

    public int getAntal_øl() {
        return antal_øl;
    }

    public void setAntal_øl(int antal_øl) {
        this.antal_øl = antal_øl;
    }

    public int getAntal_glas() {
        return antal_glas;
    }

    public void setAntal_glas(int antal_glas) {
        this.antal_glas = antal_glas;
    }

}
