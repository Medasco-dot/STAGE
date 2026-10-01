package bf.carfo.sigeco.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dossier")
public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_dossier", nullable = false, unique = true, length = 30)
    private String numeroDossier;

    @Column(name = "date_ouverture", nullable = false)
    private LocalDate dateOuverture;

    @Column(columnDefinition = "TEXT")
    private String resumeAffaire;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(name = "montant_reclame", precision = 15, scale = 2)
    private BigDecimal montantReclame;

    @Column(name = "risque_financier", precision = 15, scale = 2)
    private BigDecimal risqueFinancier;

    @ManyToOne(optional = false)
    @JoinColumn(name = "num_contentieux", nullable = false)
    private TypeContentieux typeContentieux;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDossier() {
        return numeroDossier;
    }

    public void setNumeroDossier(String numeroDossier) {
        this.numeroDossier = numeroDossier;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getResumeAffaire() {
        return resumeAffaire;
    }

    public void setResumeAffaire(String resumeAffaire) {
        this.resumeAffaire = resumeAffaire;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public BigDecimal getMontantReclame() {
        return montantReclame;
    }

    public void setMontantReclame(BigDecimal montantReclame) {
        this.montantReclame = montantReclame;
    }

    public BigDecimal getRisqueFinancier() {
        return risqueFinancier;
    }

    public void setRisqueFinancier(BigDecimal risqueFinancier) {
        this.risqueFinancier = risqueFinancier;
    }

    public TypeContentieux getTypeContentieux() {
        return typeContentieux;
    }

    public void setTypeContentieux(TypeContentieux typeContentieux) {
        this.typeContentieux = typeContentieux;
    }
}