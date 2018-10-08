package model;

public class Ordrelinje {

    private int antal;
    private Produkt produkt;

    public Ordrelinje(int antal, Produkt produkt, Ordre ordre) {
        this.antal = antal;
        setProdukt(produkt);
        ordre.addOrdrelinje(this);
    }

    public int getAntal() {
        return antal;
    }

    public void setAntal(int antal) {
        this.antal = antal;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

}
