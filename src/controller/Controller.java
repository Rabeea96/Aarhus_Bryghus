package controller;

import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

    // opretter et simpel produkt
    public Produkt createSimpel_produkt(String navn, Produktgruppe produktgruppe) {
        Produkt produkt = new Simpel_produkt(navn, produktgruppe);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et klippekort
    public Produkt createKlippekort(String navn, Produktgruppe produktgruppe) {
        Produkt produkt = new Klippekort(navn, produktgruppe);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et sampakning
    public Produkt createSampakning(String navn, Produktgruppe produktgruppe, int antal_øl, int antal_glas) {
        Produkt produkt = new Sampakning(navn, produktgruppe, antal_øl, antal_glas);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et rundvisning
    public Produkt createRundvisning(String navn, Produktgruppe produktgruppe, LocalDate dato, LocalTime tidspunkt) {
        Produkt produkt = new Rundvisning(navn, produktgruppe, dato, tidspunkt);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et fadølsanlæg udlejning
    public Produkt createFadølsanlæg_udlejning(String navn, Produktgruppe produktgruppe, LocalDate startDato,
            LocalDate slutDato) {
        Produkt produkt = new Fadølsanlæg_udlejning(navn, produktgruppe, startDato, slutDato);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et produktpris ud fra et produktobjekt
    public Produktpris createProduktpris(Prisliste prisliste, double pris, Produkt produkt) {
        Produktpris produktpris = produkt.createProduktpris(prisliste, pris, produkt);
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
    public Ordrelinje createOrdrelinje(int antal, Produktpris produktpris, Ordre ordre) {
        Ordrelinje ordrelinje = new Ordrelinje(antal, produktpris, ordre);
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
        double pris = ordre.samletpris();

        controller.registrereSalg(ordre);

        return pris;
    }

    // henter salgsoplysninger
    public void registrereSalg(Ordre ordre) {
        double pris = 0;
        // string til at gemme salget
        String salg = "";

        Map<String, Integer> produkter_fra_prisliste = new HashMap<>();
        produkter_fra_prisliste = controller.henteProdukterIPrisliste(ordre.getPrisliste());

        for (String key : produkter_fra_prisliste.keySet()) {

            for (int i = 0; i < ordre.getOrdrelinjer().size(); i++) {
                if (key.equals(ordre.getOrdrelinjer().get(i).getProduktpris().getProdukt().getNavn())) {
                    double produktPris = produkter_fra_prisliste.get(key);
                    int produktAntal = ordre.getOrdrelinjer().get(i).getAntal();
                    String produktNavn = ordre.getOrdrelinjer().get(i).getProduktpris().getProdukt().getNavn();
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
        Produktgruppe rundvisning_gruppe = controller.createProduktgruppe("rundvisning");

        // produkt
        Produkt klosterbryg = controller.createSimpel_produkt("Klosterbryg", flaske);
        Produkt extrapilsner = controller.createSimpel_produkt("Extra pilsner", flaske);
        Produkt jazzclassic = controller.createSimpel_produkt("Jazz Classic", fadøl);
        Produkt rundvisning = controller.createRundvisning("rundvisning", rundvisning_gruppe, LocalDate.of(2018, 10, 8),
                LocalTime.of(15, 00));

        // prisliste
        Prisliste butik = controller.createPrisliste("Butik");
        Prisliste fredagsbar = controller.createPrisliste("Fredagsbar");

        // produktpris
        Produktpris klosterbryg_butik = controller.createProduktpris(butik, 36, klosterbryg);
        Produktpris klosterbryg_fredagsbar = controller.createProduktpris(fredagsbar, 50, klosterbryg);
        Produktpris extrapilsner_butik = controller.createProduktpris(butik, 36, extrapilsner);
        Produktpris jazzclassic_fredagsbar = controller.createProduktpris(fredagsbar, 30, jazzclassic);
        Produktpris jazzclassic_butik = controller.createProduktpris(butik, 36, jazzclassic);
        Produktpris rundvisning_butik = controller.createProduktpris(butik, 100, rundvisning);

        // ordre
        Ordre ordre1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 8), butik);
        Ordre ordre2 = controller.createOrdre(Betalingsmiddel.KONTANT, LocalDate.of(2018, 10, 9), butik);
        Ordre ordre3 = controller.createOrdre(Betalingsmiddel.MOBILEPAY, LocalDate.of(2018, 10, 8), butik);

        controller.createOrdrelinje(10, klosterbryg_butik, ordre1);
        controller.createOrdrelinje(5, extrapilsner_butik, ordre1);
        controller.createOrdrelinje(3, jazzclassic_butik, ordre1);

        controller.createOrdrelinje(6, klosterbryg_butik, ordre2);
        controller.createOrdrelinje(2, extrapilsner_butik, ordre2);
        controller.createOrdrelinje(7, jazzclassic_butik, ordre2);

        controller.createOrdrelinje(20, rundvisning_butik, ordre3);

        // beregner pris på ordre
        // controller.beregnPris(ordre1);
        // controller.beregnPris(ordre2);
        // controller.beregnPris(ordre3);

    }

}
