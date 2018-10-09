package container;

import java.util.ArrayList;

import model.*;

public class Container {

    private ArrayList<Produktgruppe> produktgrupper = new ArrayList<>();
    private ArrayList<Prisliste> prislister = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private ArrayList<Rundvisning> rundvisninger = new ArrayList<>();
    private ArrayList<Fadølsanlæg_udlejning> fadølsanlæg_udlejninger = new ArrayList<>();
    private ArrayList<String> salg = new ArrayList<>();
    private ArrayList<Ordre> ordrer = new ArrayList<>();

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

    // rundvisninger
    public void addRundvisninger(Rundvisning rundvisning) {
        rundvisninger.add(rundvisning);
    }

    public ArrayList<Rundvisning> getRundvisninger() {
        return new ArrayList<>(rundvisninger);
    }

    public void removeRundvisning(Rundvisning rundvisning) {
        rundvisninger.remove(rundvisning);
    }

    // fadølsanlæg udlejninger
    public void addFadølsanlæg_udlejning(Fadølsanlæg_udlejning fadølsanlæg_udlejning) {
        fadølsanlæg_udlejninger.add(fadølsanlæg_udlejning);
    }

    public ArrayList<Fadølsanlæg_udlejning> getFadølsanlæg_udlejninger() {
        return new ArrayList<>(fadølsanlæg_udlejninger);
    }

    public void removeFadølsanlæg_udlejning(Fadølsanlæg_udlejning fadølsanlæg_udlejning) {
        fadølsanlæg_udlejninger.remove(fadølsanlæg_udlejning);
    }

    // salg
    public void addSalg(String etSalg) {
        salg.add(etSalg);
    }

    public void removeSalg(String etSalg) {
        salg.remove(etSalg);
    }

    public ArrayList<String> getSalg() {
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

}
