package gui;

import container.Container;
import controller.*;

public class MainApp {

    public static void main(String[] args) {
        // Controller instans
        Controller controller = Controller.getInstance();

        // Container instans
        Container container = Container.getInstance();

        controller.createSomeObjects();

    }

}
