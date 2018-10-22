package container;

import java.util.ArrayList;

import model.*;

public class Container {

    private ArrayList<Produktgruppe> produktgrupper = new ArrayList<>();
    private ArrayList<Prisliste> prislister = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private ArrayList<Salg> salg = new ArrayList<>();
    private ArrayList<Ordre> ordrer = new ArrayList<>();
    private ArrayList<Ordre> udlejninger = new ArrayList<>();

    // Container instans - singleton designpattern
    private static Container container;

    private Container() {
    }

    public static synchronized Container getInstance() {
        if (container == null) {
            container = new Container();
        }
        return container;
    }

    // produktgrupper
    public void addProduktgrupper(Produktgruppe produktgruppe) {
        produktgrupper.add(produktgruppe);
    }

    public ArrayList<Produktgruppe> getProduktgrupper() {
        return new ArrayList<>(produktgrupper);
    }

    // prisliste
    public void addPrisliste(Prisliste prisliste) {
        prislister.add(prisliste);
    }

    public ArrayList<Prisliste> getPrislister() {
        return new ArrayList<>(prislister);
    }

    // produkter
    public void addProdukter(Produkt produkt) {
        produkter.add(produkt);
    }

    public ArrayList<Produkt> getProdukter() {
        return new ArrayList<>(produkter);
    }

    // salg
    public void addSalg(Salg salg) {
        this.salg.add(salg);
    }

    public void removeSalg(Salg salg) {
        this.salg.remove(salg);
    }

    public ArrayList<Salg> getSalg() {
        return new ArrayList<>(salg);
    }

    // ordrer
    public void addOrdre(Ordre ordre) {
        ordrer.add(ordre);
    }

    public void removeOrdre(Ordre ordre) {
        ordrer.remove(ordre);
    }

    public ArrayList<Ordre> getOrdre() {
        return new ArrayList<>(ordrer);
    }

    // udlejningsordrer
    public void addUdlejning(Ordre ordre) {
        udlejninger.add(ordre);
    }

    public void removeUdlejning(Ordre ordre) {
        udlejninger.remove(ordre);
    }

    public ArrayList<Ordre> getUdlejninger() {
        return new ArrayList<>(udlejninger);
    }

}
