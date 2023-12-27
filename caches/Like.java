package caches;

public class Like {

    private final int idCompte;
    private final int idPublication;

    public Like(int idCompte, int idPublication) {
        this.idCompte = idCompte;
        this.idPublication = idPublication;
    }

    public int getIdCompte() {
        return this.idCompte;
    }

    public int getIdPublication() {
        return this.idPublication;
    }

}
