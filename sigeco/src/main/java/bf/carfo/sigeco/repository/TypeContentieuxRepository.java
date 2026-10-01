package bf.carfo.sigeco.repository;

import bf.carfo.sigeco.entity.TypeContentieux;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeContentieuxRepository extends JpaRepository<TypeContentieux, Long> {
    boolean existsByLibelleIgnoreCase(String libelle);
}
