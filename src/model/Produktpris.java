package model;

public class Produktpris {

    private double pris;
    private Prisliste prisliste;

    public Produktpris(Prisliste prisliste, double pris) {
        this.prisliste = prisliste;
        this.pris = pris;
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }

    public Prisliste getPrisliste() {
        return prisliste;
    }

    public void setPrisliste(Prisliste prisliste) {
        this.prisliste = prisliste;
    }

}
