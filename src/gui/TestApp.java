package gui;

import container.Container;
import controller.*;
import model.*;

public class TestApp {

    public static void main(String[] args) {
        // Controller instans
        Controller controller = Controller.getInstance();

        // Container instans
        Container container = Container.getInstance();

        controller.createSomeObjects();

        // for (Produktgruppe p : container.getProduktgrupper()) {
        // System.out.println(p.getNavn() + ": " + p.getProdukter());
        // }

        // for (Ordre o : container.getOrdre()) {
        // kalder beregnpris på alle ordrer for at salget registreres på alle ordrer
        // controller.beregnPris(o);
        // }

        // oversigt over dagens salg
        // System.out.println(controller.getDagenssalg(LocalDate.of(2018, 10, 8)));

        for (Salg s : container.getSalg()) {
            System.out.println(s);
        }

        // System.out.println(controller.getAntal_brugte_klip(LocalDate.of(2018, 10, 8),
        // LocalDate.of(2018, 10, 12)));

        // System.out.println(controller.getAntal_solgte_klippekort(LocalDate.of(2018,
        // 10, 8), LocalDate.of(2018, 10, 12)));

    }

}
