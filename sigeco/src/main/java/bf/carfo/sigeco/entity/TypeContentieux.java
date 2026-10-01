package bf.carfo.sigeco.entity;

import jakarta.persistence.*;

@Entity
@Table(name= "type_contentieux")
public class TypeContentieux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_contentieux" )
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    // -- Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}