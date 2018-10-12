package controller;

import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public Produkt createRundvisning(String navn, Produktgruppe produktgruppe, LocalDate dato, LocalTime tidspunkt,
            boolean studierabat) {
        Produkt produkt = new Rundvisning(navn, produktgruppe, dato, tidspunkt, studierabat);
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

    // opretter en fustage ud fra et fadølsanlæg_udlejnings objekt
    public Fustage createFustage(int liter, String navn, int pris, Fadølsanlæg_udlejning fadølsanlæg_udlejning,
            Produkt produkt) {
        Fustage fustage = fadølsanlæg_udlejning.createFustage(liter, navn, pris);
        produkt.beregnPris();
        return fustage;
    }

    // opretter en kulsyre ud fra et fadølsanlæg_udlejnings objekt
    public Kulsyre createKulsyre(String navn, int pris, Fadølsanlæg_udlejning fadølsanlæg_udlejning, Produkt produkt) {
        Kulsyre kulsyre = fadølsanlæg_udlejning.createKulsyre(navn, pris);
        produkt.beregnPris();
        return kulsyre;
    }

    // opretter et anlæg ud fra et fadølsanlæg_udlejnings objekt
    public Anlæg createAnlæg(String navn, int pris, Fadølsanlæg_udlejning fadølsanlæg_udlejning, Produkt produkt) {
        Anlæg anlæg = fadølsanlæg_udlejning.createAnlæg(navn, pris);
        produkt.beregnPris();
        return anlæg;
    }

    // opretter et produktpris ud fra et produktobjekt
    public Produktpris createProduktpris(Prisliste prisliste, double pris, Produkt produkt) {
        Produktpris produktpris = produkt.createProduktpris(prisliste, pris, produkt);
        produkt.beregnPris();
        return produktpris;
    }

    // opretter en prisliste
    public Prisliste createPrisliste(String navn) {
        Prisliste prisliste = new Prisliste(navn);
        container.addPrisliste(prisliste);
        return prisliste;
    }

    // opretter en ordre uden rabat
    public Ordre createOrdre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        Ordre ordre = new Ordre(betalingsmiddel, dato, prisliste);
        container.addOrdre(ordre);
        return ordre;
    }

    // opretter en ordre med rabat
    public Ordre createOrdre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste,
            Strategy_giv_rabat strategy, double rabat) {
        Ordre ordre = new Ordre(betalingsmiddel, dato, prisliste, strategy, rabat);
        container.addOrdre(ordre);
        return ordre;
    }

    // opretter en ordrelinje
    public Ordrelinje createOrdrelinje(int antal, Produktpris produktpris, Ordre ordre) {
        Ordrelinje ordrelinje = new Ordrelinje(antal, produktpris, ordre);
        return ordrelinje;
    }

    // opretter en salg
    public Salg createSalg(ArrayList<String> produktNavn, ArrayList<Integer> produktPris,
            ArrayList<Integer> produktAntal, double samletPris, Betalingsmiddel betalingsmiddel, LocalDate dato,
            Ordre ordre) {
        Salg salg = new Salg(produktNavn, produktPris, produktAntal, samletPris, betalingsmiddel, dato, ordre);
        container.addSalg(salg);
        return salg;
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

    // beregner pris på en ordre - salget bliver samtidig registreret
    public double beregnPris(Ordre ordre) {
        double pris = ordre.samletpris();

        if (ordre.isRabat_angivet() == true) {
            pris = ordre.samletpris_med_rabat();
        }

        controller.registrereSalg(ordre);

        return pris;
    }

    // gemmer salgsoplysninger
    public void registrereSalg(Ordre ordre) {
        ArrayList<String> produktNavn = new ArrayList<>();
        ArrayList<Integer> produktPris = new ArrayList<>();
        ArrayList<Integer> produktAntal = new ArrayList<>();

        // prisliste med produkter
        Map<String, Integer> produkter_fra_prisliste = new HashMap<>();
        produkter_fra_prisliste = controller.henteProdukterIPrisliste(ordre.getPrisliste());

        for (String key : produkter_fra_prisliste.keySet()) {

            for (int i = 0; i < ordre.getOrdrelinjer().size(); i++) {

                // hvis produktnavnet fra prislisten (dvs. key) matcher produktnavnet fra
                // ordrelinjen
                if (key.equals(ordre.getOrdrelinjer().get(i).getProduktpris().getProdukt().getNavn())) {

                    // produkt-oplysningerne gemmes
                    produktPris.add(produkter_fra_prisliste.get(key));
                    produktAntal.add(ordre.getOrdrelinjer().get(i).getAntal());
                    produktNavn.add(ordre.getOrdrelinjer().get(i).getProduktpris().getProdukt().getNavn());
                }
            }
        }
        // den samlede pris på salget gemmes samt betalingsmiddel og dato
        double samletPris = ordre.samletpris();
        if (ordre.isRabat_angivet() == true) {
            samletPris = ordre.samletpris_med_rabat();
        }

        Betalingsmiddel betalingsmiddel = ordre.getBetalingsmiddel();
        LocalDate dato = ordre.getDato();

        // salget oprettes
        controller.createSalg(produktNavn, produktPris, produktAntal, samletPris, betalingsmiddel, dato, ordre);
    }

    // oversigt over dagens salg
    public void getDagenssalg(LocalDate dato) {

        for (Salg s : container.getSalg()) {
            if (s.getDato().equals(dato)) {
                System.out.println(s);
            }
        }
    }

    // henter alle solgte klippekort
    public int getAntal_solgte_klippekort() {
        int antal = 0;

        for (Salg s : container.getSalg()) {
            for (Ordrelinje o : s.getOrdre().getOrdrelinjer()) {
                if (o.getProduktpris().getProdukt().getNavn().equals("Klippekort")) {
                    antal = antal + o.getAntal();
                }
            }
        }
        return antal;
    }

    // MANGLER INFORMATION OM HVORNÅR OG HVORDAN HVER KLIP BLIVER BRUGT
    // henter antal af brugte klip for en given periode
    public int getAntal_brugte_klip(LocalDate startDato, LocalDate slutDato) {
        int antal = 0;

        for (Salg s : container.getSalg()) {
            // hvis salgsdatoen er efter eller lige med startdato OG hvis salgsdatoen er før
            // eller lige med slutdato
            if ((s.getDato().isAfter(startDato) || s.getDato().equals(startDato))
                    && (s.getDato().isBefore(slutDato) || s.getDato().equals(slutDato))) {

                if (s.getBetalingsmiddel().equals(Betalingsmiddel.KLIPPEKORT)) {
                    antal++;
                }
            }
        }

        return antal;
    }

    // opretter nogle objekter
    public void createSomeObjects() {

        // produktgruppe
        Produktgruppe flaske = controller.createProduktgruppe("flaske");
        Produktgruppe fadøl = controller.createProduktgruppe("fadøl");
        Produktgruppe rundvisning_gruppe = controller.createProduktgruppe("rundvisning");
        Produktgruppe sampakning = controller.createProduktgruppe("sampakning");
        Produktgruppe klippekort_gruppe = controller.createProduktgruppe("klippekort");
        Produktgruppe udlejninger = controller.createProduktgruppe("udlejninger");

        // produkt
        Produkt klosterbryg = controller.createSimpel_produkt("Klosterbryg", flaske);
        Produkt extrapilsner = controller.createSimpel_produkt("Extra pilsner", flaske);
        Produkt jazzclassic = controller.createSimpel_produkt("Jazz Classic", fadøl);
        Produkt rundvisning = controller.createRundvisning("Rundvisning pr. person", rundvisning_gruppe,
                LocalDate.of(2018, 10, 8), LocalTime.of(16, 00), true);
        Produkt gaveæske_2øl_2glas = controller.createSampakning("Gaveæske", sampakning, 2, 2);
        Produkt klippekort = controller.createKlippekort("Klippekort", klippekort_gruppe);

        Produkt fadølsanlæg_udlejning = controller.createFadølsanlæg_udlejning("Fadølsanlæg udlejning1", udlejninger,
                LocalDate.of(2018, 10, 12), LocalDate.of(2018, 10, 14));
        controller.createFustage(20, "Klosterbryg", 775, (Fadølsanlæg_udlejning) fadølsanlæg_udlejning,
                fadølsanlæg_udlejning);
        controller.createAnlæg("1-hane", 250, (Fadølsanlæg_udlejning) fadølsanlæg_udlejning, fadølsanlæg_udlejning);
        controller.createKulsyre("6 kg", 400, (Fadølsanlæg_udlejning) fadølsanlæg_udlejning, fadølsanlæg_udlejning);

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
        Produktpris gaveæske_2øl_2glas_butik = controller.createProduktpris(butik, 100, gaveæske_2øl_2glas);
        Produktpris klippekort_butik = controller.createProduktpris(butik, 100, klippekort);
        Produktpris Fadølsanlæg_udlejning_butik = controller.createProduktpris(butik, 0, fadølsanlæg_udlejning);

        // ordre
        Ordre ordre1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 8), butik);
        Ordre ordre2 = controller.createOrdre(Betalingsmiddel.KONTANT, LocalDate.of(2018, 10, 9), butik);
        Ordre ordre3 = controller.createOrdre(Betalingsmiddel.MOBILEPAY, LocalDate.of(2018, 10, 10), butik,
                new Giv_rabat_i_procent(), 5);
        Ordre ordre4 = controller.createOrdre(Betalingsmiddel.KLIPPEKORT, LocalDate.of(2018, 10, 11), butik);
        Ordre ordre5 = controller.createOrdre(Betalingsmiddel.KLIPPEKORT, LocalDate.of(2018, 10, 11), butik);
        Ordre ordre6 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 14), butik);

        // ordrelinje
        controller.createOrdrelinje(10, klosterbryg_butik, ordre1);
        controller.createOrdrelinje(5, extrapilsner_butik, ordre1);
        controller.createOrdrelinje(3, jazzclassic_butik, ordre1);

        controller.createOrdrelinje(6, klosterbryg_butik, ordre2);
        controller.createOrdrelinje(2, extrapilsner_butik, ordre2);
        controller.createOrdrelinje(7, jazzclassic_butik, ordre2);

        controller.createOrdrelinje(20, rundvisning_butik, ordre3);

        controller.createOrdrelinje(5, gaveæske_2øl_2glas_butik, ordre4);

        controller.createOrdrelinje(5, klippekort_butik, ordre5);
        controller.createOrdrelinje(3, klippekort_butik, ordre5);
        controller.createOrdrelinje(2, klippekort_butik, ordre5);

        controller.createOrdrelinje(1, Fadølsanlæg_udlejning_butik, ordre6);

    }

}
