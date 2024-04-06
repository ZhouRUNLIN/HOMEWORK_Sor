package restlet.annuaire;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Etudiant {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;

    public Etudiant() {}

    public Etudiant(String id, String nom, String prenom, String telephone) {
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "Etudiant [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", telephone=" + telephone + "]";
    }
}

