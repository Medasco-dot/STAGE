package bf.carfo.sigeco.repository;

import bf.carfo.sigeco.entity.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDocumentRepository extends JpaRepository<TypeDocument, Long> {
    boolean existsByLibelleIgnoreCase(String libelle);
}