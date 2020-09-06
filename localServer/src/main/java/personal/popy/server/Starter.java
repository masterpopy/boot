package personal.popy.server;

import personal.popy.server.support.WebFactory;

public class Starter {

    public static void main(String[] args) {
        try {
            new WebFactory().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
