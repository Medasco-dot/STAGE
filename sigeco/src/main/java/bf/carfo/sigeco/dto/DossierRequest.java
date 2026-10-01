package bf.carfo.sigeco.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DossierRequest(

        @NotBlank(message = "Le numéro de dossier est obligatoire")
        @Size(max = 30, message = "Le numéro de dossier ne doit pas dépasser 30 caractères")
        String numeroDossier,

        @NotNull(message = "La date d'ouverture est obligatoire")
        @PastOrPresent(message = "La date d'ouverture ne peut pas être dans le futur")
        LocalDate dateOuverture,

        String resumeAffaire,

        String observation,

        @PositiveOrZero(message = "Le montant réclamé doit être positif")
        BigDecimal montantReclame,

        @PositiveOrZero(message = "Le risque financier doit être positif")
        BigDecimal risqueFinancier,

        @NotNull(message = "Le type de contentieux est obligatoire")
        Long typeContentieuxId
) {
}