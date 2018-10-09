package controller;

import model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
        container.addProduktgrupper(produktgruppe);
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
        container.addOrdre(ordre);
        return ordre;
    }

    // opretter en ordrelinje
    public Ordrelinje createOrdrelinje(int antal, Produkt produkt, Ordre ordre) {
        Ordrelinje ordrelinje = new Ordrelinje(antal, produkt, ordre);
        return ordrelinje;
    }

    // henter produkter ud fra en prisliste
    public Map<String, Integer> henteProdukterIPrisliste(Prisliste prisliste) {
        Map<String, Integer> produkter_fra_prisliste = new HashMap<>();

        for (Produkt p : container.getProdukter()) {
            for (Produktpris priser : p.getProduktpriser()) {

                if (priser.getPrisliste().equals(prisliste)) {

                    produkter_fra_prisliste.put(p.getNavn(), (int) priser.getPris());
                }
            }
        }
        return produkter_fra_prisliste;
    }

    // beregner pris på en ordre
    public double beregnPris(Ordre ordre) {
        double pris = 0;
        Map<String, Integer> produkter_fra_prisliste = new HashMap<>();
        produkter_fra_prisliste = controller.henteProdukterIPrisliste(ordre.getPrisliste());

        // string til at gemme salget
        String salg = "";

        for (String key : produkter_fra_prisliste.keySet()) {

            for (int i = 0; i < ordre.getOrdrelinjer().size(); i++) {
                if (key.equals(ordre.getOrdrelinjer().get(i).getProdukt().getNavn())) {
                    double produktPris = produkter_fra_prisliste.get(key);
                    int produktAntal = ordre.getOrdrelinjer().get(i).getAntal();
                    String produktNavn = ordre.getOrdrelinjer().get(i).getProdukt().getNavn();
                    pris = pris + (produktPris * produktAntal);

                    // salg-oplysningerne gemmes
                    salg = salg + "Produkt: " + produktNavn + " | Produktpris: " + produktPris + " | Antal: "
                            + produktAntal + "\n";
                }
            }
        }
        // den samlede pris på salget gemmes samt betalingsmiddel
        salg = salg + "Samlet pris: " + pris + " kr. \n";
        salg = salg + "Betalingsmiddel: " + ordre.getBetalingsmiddel() + " \n";
        salg = salg + "Salgsdato: " + ordre.getDato() + " \n";
        container.addSalg(salg);

        return pris;
    }

    // oversigt over dagens salg
    public void getDagenssalg(LocalDate dato) {

        for (int i = 0; i < container.getSalg().size(); i++) {
            if (container.getSalg().get(i).contains(dato + "")) {
                System.out.println(container.getSalg().get(i));
            }
        }

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
        Ordre ordre2 = controller.createOrdre(Betalingsmiddel.KONTANT, LocalDate.of(2018, 10, 9), butik);

        controller.createOrdrelinje(10, klosterbryg, ordre1);
        controller.createOrdrelinje(5, extrapilsner, ordre1);
        controller.createOrdrelinje(3, jazzclassic, ordre1);

        controller.createOrdrelinje(6, klosterbryg, ordre2);
        controller.createOrdrelinje(2, extrapilsner, ordre2);
        controller.createOrdrelinje(7, jazzclassic, ordre2);

        // beregner pris på ordre
        controller.beregnPris(ordre1);
        controller.beregnPris(ordre2);

    }

}
