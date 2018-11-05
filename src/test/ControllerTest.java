package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Before;
import model.*;
import controller.Controller;
import org.junit.Test;

public class ControllerTest {

    private Controller controller = Controller.getInstance();
    private Produktgruppe produktgruppe1, produktgruppe2;
    private Produkt produkt1, produkt2;
    private Produktpris produktpris1;
    private Prisliste prisliste1, prisliste2;
    private Ordre ordre_udenRabat1, ordre_udenRabat2;
    private Ordre ordre_medRabat1;

    @Before
    public void setUp() throws Exception {
        controller = Controller.getInstance();
        produktgruppe1 = controller.createProduktgruppe("Rundvisning");
        produkt1 = controller.createSimpel_produkt("Klosterbryg", produktgruppe1);
        prisliste1 = controller.createPrisliste("Butik");
        produktpris1 = controller.createProduktpris(prisliste1, 100, produkt1);

        ordre_udenRabat1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste1);
        ordre_medRabat1 = controller.createOrdre(Betalingsmiddel.DANKORT, LocalDate.now(), prisliste1,
                new Giv_rabat_i_kroner(), 10);

        controller.createOrdrelinje(1, produktpris1, ordre_udenRabat1);
        controller.createOrdrelinje(1, produktpris1, ordre_medRabat1);
    }

    // test af 3 ikke-trivielle metoder

    // createProduktgruppe
    @Test
    public void createProduktgruppe_Test1() {
        assertNotNull(produktgruppe1);
    }

    @Test
    public void createProduktgruppe_Test2() {
        assertEquals("Rundvisning", produktgruppe1.getNavn());
    }

    @Test
    public void createProduktgruppe_Test3() {
        assertTrue(controller.getProduktgrupper().contains(produktgruppe1));
    }

    @Test
    public void createProduktgruppe_Test4() {
        try {
            produktgruppe2 = controller.createProduktgruppe("");
            produktgruppe2.getNavn();

        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Angiv et navn for produktgruppen");
        }
    }

    // createSimpelProdukt
    @Test
    public void createSimpelProdukt_Test1() {
        assertNotNull(produkt1);
    }

    @Test
    public void createSimpelProdukt_Test2() {
        assertEquals("Klosterbryg", produkt1.getNavn());
    }

    @Test
    public void createSimpelProdukt_Test3() {
        assertTrue(controller.getProdukter().contains(produkt1));
    }

    @Test
    public void createSimpelProdukt_Test4() {
        assertTrue(produktgruppe1.getProdukter().contains(produkt1));
    }

    @Test
    public void createSimpelProdukt_Test5() {
        try {
            produkt2 = controller.createSimpel_produkt("", produktgruppe1);
            produkt2.getNavn();

        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Angiv et navn for produktet");
        }
    }

    // createPrisliste
    @Test
    public void createPrisliste_Test1() {
        assertNotNull(prisliste1);
    }

    @Test
    public void createPrisliste_Test2() {
        assertEquals("Butik", prisliste1.getNavn());
    }

    @Test
    public void createPrisliste_Test3() {
        assertTrue(controller.getPrislister().contains(prisliste1));
    }

    @Test
    public void createPrisliste_Test4() {
        try {
            prisliste2 = controller.createPrisliste("");
            prisliste2.getNavn();

        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Angiv et navn for prislisten");
        }
    }

    // test af en central use case - UC1: Registrere et produktsalg
    @Test
    public void registrereSalg_Test1() {
        assertNotNull(ordre_udenRabat1);
    }

    @Test
    public void registrereSalg_Test2() {
        assertTrue(controller.getOrdrer().contains(ordre_udenRabat1));
    }

    @Test
    public void registrereSalg_Test3() {
        assertEquals(Betalingsmiddel.DANKORT, ordre_udenRabat1.getBetalingsmiddel());
    }

    @Test
    public void registrereSalg_Test4() {
        assertEquals(LocalDate.now(), ordre_udenRabat1.getDato());
    }

    @Test
    public void registrereSalg_Test5() {
        assertEquals(prisliste1, ordre_udenRabat1.getPrisliste());
    }

    @Test
    public void registrereSalg_Test6() {
        // prisen på produktet i ordrelinjen er sat til 100 og der er kun ét produkt
        assertEquals(100, controller.beregnPris(ordre_udenRabat1), 0.001);
    }

    @Test
    public void registrereSalg_Test7() {
        assertNotNull(ordre_medRabat1);
    }

    @Test
    public void registrereSalg_Test8() {
        assertTrue(ordre_medRabat1.getStrategy() instanceof Giv_rabat_i_kroner);
    }

    @Test
    public void registrereSalg_Test9() {
        assertEquals(10, ordre_medRabat1.getRabat(), 0.001);
    }

    @Test
    public void registrereSalg_Test10() {
        assertEquals(1, ordre_medRabat1.getOrdrelinjer().size());
    }

    @Test
    public void registrereSalg_Test11() {
        // der er givet 10 kroner rabat dvs. 100 - 10 = 90
        assertEquals(90, controller.beregnPris(ordre_medRabat1), 0.001);
    }

    @Test
    public void registrereSalg_Test12() {
        // salget registreres
        controller.registrereSalg(ordre_medRabat1);
        assertEquals(ordre_medRabat1, controller.getSalg().get(2).getOrdre());
    }

    @Test
    public void registrereSalg_Test13() {
        try {
            controller.registrereSalg(ordre_udenRabat2);

        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Ordren er tom");
        }
    }

}
