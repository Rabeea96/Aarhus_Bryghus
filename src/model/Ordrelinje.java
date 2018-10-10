package model;

public class Ordrelinje {

    private int antal;
    private Produktpris produktpris;

    public Ordrelinje(int antal, Produktpris produktpris, Ordre ordre) {
        this.antal = antal;
        setProduktpris(produktpris);
        ordre.addOrdrelinje(this);
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public Produktpris getProduktpris() {
        return produktpris;
    }

    public void setProduktpris(Produktpris produktpris) {
        this.produktpris = produktpris;
    }

}
