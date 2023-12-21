package caches;

public class Publications {

    private final String idPublication;
    private final String nomUser;
    private final String contenue;
    private final String date;
    private final int likes;

    public Publications(String idPublication, String nomUser, String contenue, String date, int likes) {
        this.idPublication = idPublication;
        this.nomUser = nomUser;
        this.contenue = contenue;
        this.date = date;
        this.likes = likes;
    }

    public String getIdPublication() {
        return this.idPublication;
    }

    public String getNomUser() {
        return this.nomUser;
    }

    public String getContenue() {
        return this.contenue;
    }

    public String getDate() {
        return this.date;
    }

    public int getLikes() {
        return this.likes;
    }

    @Override
    public String toString() {
        return this.nomUser + ";" + this.contenue + ";" + this.date + ";" + this.likes;
    }

}
