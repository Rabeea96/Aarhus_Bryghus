package model;

public class Produktpris {

    private double pris;
    private Prisliste prisliste;
    private Produkt produkt;

    public Produktpris(Prisliste prisliste, double pris, Produkt produkt) {
        this.prisliste = prisliste;
        this.pris = pris;
        setProdukt(produkt);
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

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    @Override
    public String toString() {
        return prisliste + " " + pris + "kr.";
    }

}
