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
        Produkt produkt = new Simpelt_produkt(navn, produktgruppe);
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

    // opretter en fustage
    public Produkt createFustage(String navn, Produktgruppe produktgruppe, int liter) {
        Produkt produkt = new Fustage(navn, produktgruppe, liter);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter en kulsyre
    public Produkt createKulsyre(String navn, Produktgruppe produktgruppe, int kg) {
        Produkt produkt = new Kulsyre(navn, produktgruppe, kg);
        container.addProdukter(produkt);
        return produkt;
    }

    // opretter et anlæg
    public Produkt createAnlæg(String navn, Produktgruppe produktgruppe, int antalHaner) {
        Produkt produkt = new Kulsyre(navn, produktgruppe, antalHaner);
        container.addProdukter(produkt);
        return produkt;
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

    // opretter en konkret-ordre uden rabat
    public Ordre createOrdre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste) {
        Ordre ordre = new Konkret_ordre(betalingsmiddel, dato, prisliste);
        container.addOrdre(ordre);
        return ordre;
    }

    // opretter en konkret-ordre med rabat
    public Ordre createOrdre(Betalingsmiddel betalingsmiddel, LocalDate dato, Prisliste prisliste,
            Strategy_giv_rabat strategy, double rabat) {
        Ordre ordre = new Konkret_ordre(betalingsmiddel, dato, prisliste, strategy, rabat);
        container.addOrdre(ordre);
        return ordre;
    }

    // opretter en fadølsanlægsudlejning konkret-ordre uden rabat
    public Ordre FadølsAnlægsUdlejning_konkret_ordre(Betalingsmiddel betalingsmiddel, LocalDate dato,
            Prisliste prisliste, LocalDate startDato, LocalDate slutDato, LocalTime tidspunkt,
            ArrayList<Produkt> fustager, ArrayList<Produkt> kulsyrer, ArrayList<Produkt> anlæg) {
        Ordre ordre = new FadølsAnlægsUdlejning_konkret_ordre(betalingsmiddel, dato, tidspunkt, startDato, slutDato,
                prisliste, fustager, kulsyrer, anlæg);
        container.addUdlejning(ordre);
        return ordre;
    }

    // opretter en ordrelinje
    public Ordrelinje createOrdrelinje(int antal, Produktpris produktpris, Ordre ordre) {
        Ordrelinje ordrelinje = new Ordrelinje(antal, produktpris, ordre);
        return ordrelinje;
    }

    // opretter en salg
    public Salg createSalg(int counter, ArrayList<String> produktNavn, ArrayList<Integer> produktPris,
            ArrayList<Integer> produktAntal, double samletPris, Betalingsmiddel betalingsmiddel, LocalDate dato,
            Ordre ordre) {
        Salg salg = new Salg(counter, produktNavn, produktPris, produktAntal, samletPris, betalingsmiddel, dato, ordre);
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
        controller.createSalg(ordre.getOrdreCounter(), produktNavn, produktPris, produktAntal, samletPris,
                betalingsmiddel, dato, ordre);
    }

    // oversigt over dagens salg
    public ArrayList<Salg> getDagenssalg(LocalDate dato) {
        ArrayList<Salg> dagenssalg = new ArrayList<>();
        for (Salg s : container.getSalg()) {
            if (s.getDato().equals(dato)) {
                dagenssalg.add(s);
            }
        }
        return dagenssalg;
    }

    // henter alle solgte klippekort
    public int getAntal_solgte_klippekort(LocalDate startDato, LocalDate slutDato) {
        int antal = 0;

        for (Salg s : container.getSalg()) {
            for (Ordrelinje o : s.getOrdre().getOrdrelinjer()) {
                // hvis salgsdatoen er efter eller lige med startdato OG hvis salgsdatoen er før
                // eller lige med slutdato
                if ((s.getDato().isAfter(startDato) || s.getDato().equals(startDato))
                        && (s.getDato().isBefore(slutDato) || s.getDato().equals(slutDato))) {
                    if (o.getProduktpris().getProdukt().getNavn().equals("Klippekort")) {
                        antal = antal + o.getAntal();
                    }
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

    // liste over aktive udlejninger
    public ArrayList<Ordre> getAktiveUdlejninger() {
        ArrayList<Ordre> aktiveUdlejninger = new ArrayList<>();
        LocalDate todays_date = LocalDate.now();
        LocalTime rightNow_time = LocalTime.now();

        for (Ordre o : container.getUdlejninger()) {

            // hvis det er slutdatoen for udlejningen men før tidspunktet for returnering
            if (todays_date.equals(o.getSlutDato()) && rightNow_time.isBefore(o.getTidspunkt())) {
                aktiveUdlejninger.add(o);

                // hvis det er startdatoen for udlejningen og efter eller lig med tidspunktet
                // for returnering
            } else if (todays_date.equals(o.getStartDato()) && (rightNow_time.isAfter(o.getTidspunkt()))
                    || rightNow_time.equals(o.getTidspunkt())) {
                aktiveUdlejninger.add(o);

                // hvis det er efter startdatoen og samtidig før slutdatoen for udlejningen
            } else if (todays_date.isAfter(o.getStartDato()) && todays_date.isBefore(o.getSlutDato())) {
                aktiveUdlejninger.add(o);

            }
        }

        return aktiveUdlejninger;
    }

    // opretter nogle objekter
    public void createSomeObjects() {

        // produktgruppe
        Produktgruppe flaske = controller.createProduktgruppe("flaske");
        Produktgruppe fadøl = controller.createProduktgruppe("fadøl");
        Produktgruppe rundvisning_gruppe = controller.createProduktgruppe("rundvisning");
        Produktgruppe sampakning = controller.createProduktgruppe("sampakning");
        Produktgruppe klippekort_gruppe = controller.createProduktgruppe("klippekort");
        Produktgruppe fustage = controller.createProduktgruppe("fustage");
        Produktgruppe kulsyre = controller.createProduktgruppe("kulsyre");
        Produktgruppe anlæg = controller.createProduktgruppe("anlæg");

        // produkt
        Produkt klosterbryg = controller.createSimpel_produkt("Klosterbryg", flaske);
        Produkt extrapilsner = controller.createSimpel_produkt("Extra pilsner", flaske);
        Produkt jazzclassic = controller.createSimpel_produkt("Jazz Classic", fadøl);
        Produkt rundvisning = controller.createRundvisning("Rundvisning pr. person", rundvisning_gruppe,
                LocalDate.of(2018, 10, 8), LocalTime.of(16, 00), true);
        Produkt gaveæske_2øl_2glas = controller.createSampakning("Gaveæske", sampakning, 2, 2);
        Produkt klippekort = controller.createKlippekort("Klippekort", klippekort_gruppe);
        Produkt klosterbryg_20liter = controller.createFustage("Klosterbryg 20 liter", fustage, 20);
        Produkt kulsyre_6kg = controller.createKulsyre("6 kg", kulsyre, 6);
        Produkt anlæg_1hane = controller.createAnlæg("1-hane", anlæg, 1);

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
        Produktpris klosterbryg_20liter_butik = controller.createProduktpris(butik, 775, klosterbryg_20liter);
        Produktpris kulsyre_6kg_butik = controller.createProduktpris(butik, 400, kulsyre_6kg);
        Produktpris anlæg_1hane_butik = controller.createProduktpris(butik, 250, anlæg_1hane);

        // konkret ordre
        Ordre ordre1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 8), butik);
        Ordre ordre2 = controller.createOrdre(Betalingsmiddel.KONTANT, LocalDate.of(2018, 10, 9), butik);
        Ordre ordre3 = controller.createOrdre(Betalingsmiddel.MOBILEPAY, LocalDate.of(2018, 10, 10), butik,
                new Giv_rabat_i_procent(), 5);
        Ordre ordre4 = controller.createOrdre(Betalingsmiddel.KLIPPEKORT, LocalDate.of(2018, 10, 11), butik);
        Ordre ordre5 = controller.createOrdre(Betalingsmiddel.KLIPPEKORT, LocalDate.of(2018, 10, 11), butik);
        Ordre ordre7 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.of(2018, 10, 14), fredagsbar);

        // en udlejnings-ordre
        ArrayList<Produkt> fustage_liste = new ArrayList<>();
        ArrayList<Produkt> kulsyre_liste = new ArrayList<>();
        ArrayList<Produkt> anlæg_liste = new ArrayList<>();
        fustage_liste.add(klosterbryg_20liter);
        kulsyre_liste.add(kulsyre_6kg);
        anlæg_liste.add(anlæg_1hane);
        Ordre ordre6 = controller.FadølsAnlægsUdlejning_konkret_ordre(Betalingsmiddel.DANKORT,
                LocalDate.of(2018, 10, 14), butik, LocalDate.of(2018, 10, 14), LocalDate.of(2018, 10, 22),
                LocalTime.of(18, 00), fustage_liste, kulsyre_liste, anlæg_liste);

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

        controller.createOrdrelinje(1, klosterbryg_20liter_butik, ordre6);
        controller.createOrdrelinje(1, kulsyre_6kg_butik, ordre6);
        controller.createOrdrelinje(1, anlæg_1hane_butik, ordre6);

        controller.createOrdrelinje(1, klosterbryg_fredagsbar, ordre7);
        controller.createOrdrelinje(2, jazzclassic_fredagsbar, ordre7);

        for (Ordre o : container.getOrdre()) {
            // kalder beregnpris på alle ordrer for at salget registreres på alle ordrer
            controller.beregnPris(o);
        }

        for (Ordre o : container.getUdlejninger()) {
            // kalder beregnpris på alle ordrer for at salget registreres på alle ordrer
            controller.beregnPris(o);
        }

    }

}