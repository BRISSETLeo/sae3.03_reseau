package caches;

public class Follow {

    private final int idCompte;
    private final int idCompteFollow;

    public Follow(int idCompte, int idCompteFollow) {
        this.idCompte = idCompte;
        this.idCompteFollow = idCompteFollow;
    }

    public int getIdCompte() {
        return this.idCompte;
    }

    public int getIdCompteFollow() {
        return this.idCompteFollow;
    }

}
