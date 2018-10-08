package controller;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;

import container.*;

public class Controller {

    // Container instans
    Container container = Container.getInstance();

    // Controller instans - singleton designpattern
    private static Controller controller;

    private Controller() {
    }

    public static synchronized Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    // opretter en produktgruppe
    public Produktgruppe createProduktgruppe(String navn) {
        Produktgruppe produktgruppe = new Produktgruppe(navn);
        return produktgruppe;
    }

    // opretter et produkt
    public Produkt createProdukt(String navn) {
        Produkt produkt = new Produkt(navn);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et produktpris ud fra et produktobjekt
    public Produktpris createProduktpris(Prisliste prisliste, double pris, Produkt produkt) {
        Produktpris produktpris = produkt.createProduktpris(prisliste, pris);
        return produktpris;
    }

    // opretter en prisliste
    public Prisliste createPrisliste(String navn) {
        Prisliste prisliste = new Prisliste(navn);
        container.addPrisliste(prisliste);
        return prisliste;
    }

    // opretter en ordre
    public Ordre createOrdre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        Ordre ordre = new Ordre(betalingsmiddel, dato, prisliste);
        return ordre;
    }

    // opretter en ordrelinje
    public Ordrelinje createOrdrelinje(int antal, Produkt produkt, Ordre ordre) {
        Ordrelinje ordrelinje = new Ordrelinje(antal, produkt, ordre);
        return ordrelinje;
    }

    // henter produkter ud fra en prisliste
    public ArrayList<String> henteProdukterIPrisliste(Prisliste prisliste) {
        ArrayList<String> produkter_fra_prisliste = new ArrayList<>();

        for (Produkt p : container.getProdukter()) {
            for (Produktpris pris : p.getProduktpriser()) {

                if (pris.getPrisliste().equals(prisliste)) {

                    produkter_fra_prisliste.add(p.getNavn() + " " + pris.getPris());
                }
            }
        }
        return produkter_fra_prisliste;
    }

    // beregner pris på en ordre
    public double beregnPris(Ordre ordre) {
        double pris = 0;
        // ArrayList<String> produkter_i_prisliste = new ArrayList<>();
        // produkter_i_prisliste =
        // controller.henteProdukterIPrisliste(ordre.getPrisliste());
        //
        // for (int i = 0; i < produkter_i_prisliste.size(); i++) {
        // controller.henteProdukterIPrisliste(ordre.getPrisliste()).get(i).get
        // }

        return pris;
    }

    // opretter nogle objekter
    public void createSomeObjects() {

        // produktgruppe
        Produktgruppe flaske = controller.createProduktgruppe("flaske");
        Produktgruppe fadøl = controller.createProduktgruppe("fadøl");

        // produkt
        Produkt klosterbryg = controller.createProdukt("Klosterbryg");
        Produkt extrapilsner = controller.createProdukt("Extra pilsner");
        Produkt jazzclassic = controller.createProdukt("Jazz Classic");

        // prisliste
        Prisliste butik = controller.createPrisliste("Butik");
        Prisliste fredagsbar = controller.createPrisliste("Fredagsbar");

        // produktpris
        controller.createProduktpris(butik, 36, klosterbryg);
        controller.createProduktpris(fredagsbar, 50, klosterbryg);
        controller.createProduktpris(butik, 36, extrapilsner);
        controller.createProduktpris(fredagsbar, 30, jazzclassic);
        controller.createProduktpris(butik, 36, jazzclassic);

        // sammenkoble produkter
        flaske.addProdukt(klosterbryg);
        flaske.addProdukt(extrapilsner);
        fadøl.addProdukt(jazzclassic);

        // ordre
        Ordre ordre1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 8), butik);

        controller.createOrdrelinje(10, klosterbryg, ordre1);
        controller.createOrdrelinje(5, extrapilsner, ordre1);
        controller.createOrdrelinje(3, jazzclassic, ordre1);

        controller.beregnPris(ordre1);
    }

}
