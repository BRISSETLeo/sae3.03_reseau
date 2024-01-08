package enums;

public enum CheminFONT {

    THE_SMILE("file:./client/css/police/Poppins-Regular.ttf");

    private String chemin;

    CheminFONT(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}
