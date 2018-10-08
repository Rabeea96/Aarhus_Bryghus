package container;

import java.util.ArrayList;
import model.*;

public class Container {

    private ArrayList<Produktgruppe> produktgrupper = new ArrayList<>();
    private ArrayList<Produkt> produkter = new ArrayList<>();
    private ArrayList<Rundvisning> rundvisninger = new ArrayList<>();
    private ArrayList<Fadølsanlæg_udlejning> fadølsanlæg_udlejninger = new ArrayList<>();

    // produktgrupper
    public void addProduktgrupper(Produktgruppe produktgruppe) {
        produktgrupper.add(produktgruppe);
    }

    public ArrayList<Produktgruppe> getProduktgrupper() {
        return new ArrayList<>(produktgrupper);
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
}
