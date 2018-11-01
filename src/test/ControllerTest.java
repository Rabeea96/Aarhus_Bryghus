package test;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;

import model.*;
import controller.Controller;
import org.junit.Test;

public class ControllerTest {

    private Controller controller;
    private Produktgruppe produktgruppe;
    private Produkt produkt;
    private Produktpris produktpris;
    private Prisliste prisliste;
    private Ordre ordre_udenRabat;
    private Ordre ordre_medRabat;

    @Before
    public void setUp() throws Exception {
        controller = Controller.getInstance();
        produktgruppe = controller.createProduktgruppe("Produktgruppe");
        produkt = controller.createSimpel_produkt("Produkt", produktgruppe);
        produktpris = controller.createProduktpris(prisliste, 100, produkt);
        prisliste = controller.createPrisliste("Prisliste");

        ordre_udenRabat = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste);
        ordre_medRabat = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste,
                new Giv_rabat_i_kroner(), 10);

        controller.createOrdrelinje(1, produktpris, ordre_udenRabat);
        controller.createOrdrelinje(1, produktpris, ordre_medRabat);
    }

    // test af 3 ikke-trivielle metoder

    // createProduktgruppe
    @Test
    public void createProduktgruppe_Test1() {
        assertNotNull(produktgruppe);
    }

    @Test
    public void createProduktgruppe_Test2() {
        assertEquals("Produktgruppe", produktgruppe.getNavn());
    }

    @Test
    public void createProduktgruppe_Test3() {
        assertTrue(controller.getProduktgrupper().contains(produktgruppe));
    }

    // createSimpelProdukt
    @Test
    public void createSimpelProdukt_Test1() {
        assertNotNull(produkt);
    }

    @Test
    public void createSimpelProdukt_Test2() {
        assertEquals("Produkt", produkt.getNavn());
    }

    @Test
    public void createSimpelProdukt_Test3() {
        assertTrue(controller.getProdukter().contains(produkt));
    }

    @Test
    public void createSimpelProdukt_Test4() {
        assertTrue(produktgruppe.getProdukter().contains(produkt));
    }

    // createPrisliste
    @Test
    public void createPrisliste_Test1() {
        assertNotNull(prisliste);
    }

    @Test
    public void createPrisliste_Test2() {
        assertEquals("Prisliste", prisliste.getNavn());
    }

    @Test
    public void createPrisliste_Test3() {
        assertTrue(controller.getPrislister().contains(prisliste));
    }

    // test af en central use case - UC1: Registrere et produktsalg
    @Test
    public void registrereSalg_Test1() {
        assertNotNull(ordre_udenRabat);
    }

    @Test
    public void registrereSalg_Test2() {
        assertTrue(controller.getOrdrer().contains(ordre_udenRabat));
    }

    @Test
    public void registrereSalg_Test3() {
        assertEquals(Betalingsmiddel.DANKORT, ordre_udenRabat.getBetalingsmiddel());
    }

    @Test
    public void registrereSalg_Test4() {
        assertEquals(LocalDate.now(), ordre_udenRabat.getDato());
    }

    @Test
    public void registrereSalg_Test5() {
        assertEquals(prisliste, ordre_udenRabat.getPrisliste());
    }

    @Test
    public void registrereSalg_Test6() {
        produktgruppe = controller.createProduktgruppe("Produktgruppe");
        produkt = controller.createSimpel_produkt("Produkt", produktgruppe);
        produktpris = controller.createProduktpris(prisliste, 100, produkt);
        prisliste = controller.createPrisliste("Prisliste");

        // prisen på produktet i ordrelinjen er sat til 100 og der er kun ét produkt
        ordre_udenRabat = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste);
        controller.createOrdrelinje(1, produktpris, ordre_udenRabat);

        assertEquals(100, ordre_udenRabat.samletpris(), 0.001);
    }

    @Test
    public void registrereSalg_Test7() {
        assertNotNull(ordre_medRabat);
    }

    @Test
    public void registrereSalg_Test8() {
        assertTrue(ordre_medRabat.getStrategy() instanceof Giv_rabat_i_kroner);
    }

    @Test
    public void registrereSalg_Test9() {
        assertEquals(10, ordre_medRabat.getRabat(), 0.001);
    }

    @Test
    public void registrereSalg_Test10() {
        assertEquals(1, ordre_medRabat.getOrdrelinjer().size());
    }

    @Test
    public void registrereSalg_Test11() {
        produktgruppe = controller.createProduktgruppe("Produktgruppe");
        produkt = controller.createSimpel_produkt("Produkt", produktgruppe);
        produktpris = controller.createProduktpris(prisliste, 100, produkt);
        prisliste = controller.createPrisliste("Prisliste");

        // der gives 10 kroner rabat dvs. 100 - 10 = 90
        ordre_medRabat = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste,
                new Giv_rabat_i_kroner(), 10);
        controller.createOrdrelinje(1, produktpris, ordre_medRabat);

        assertEquals(90, ordre_medRabat.samletpris_med_rabat(), 0.001);
    }

}
