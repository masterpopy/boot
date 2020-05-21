package personal.popy.localserver;

import personal.popy.localserver.support.WebFactory;

public class Starter {

    public static void main(String[] args) {
        try {
            new WebFactory().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
