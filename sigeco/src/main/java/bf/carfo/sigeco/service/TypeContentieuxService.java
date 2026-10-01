package bf.carfo.sigeco.service;

import bf.carfo.sigeco.entity.TypeContentieux;
import bf.carfo.sigeco.exception.DoublonException;
import bf.carfo.sigeco.repository.TypeContentieuxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeContentieuxService {

    private final TypeContentieuxRepository repository;

    public TypeContentieuxService(TypeContentieuxRepository repository) {
        this.repository = repository;
    }

    public List<TypeContentieux> listerTous() {
        return repository.findAll();
    }

    public TypeContentieux creer(TypeContentieux type) {
        if (repository.existsByLibelleIgnoreCase(type.getLibelle())) {
            throw new DoublonException("Le type de contentieux « " + type.getLibelle() + " » existe déjà.");
        }
        return repository.save(type);
    }
}