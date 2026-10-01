package bf.carfo.sigeco.service;

import bf.carfo.sigeco.entity.TypeDocument;
import bf.carfo.sigeco.repository.TypeDocumentRepository;
import org.springframework.stereotype.Service;
import bf.carfo.sigeco.exception.DoublonException;

import java.util.List;

@Service
public class TypeDocumentService {

    private final TypeDocumentRepository repository;

    public TypeDocumentService(TypeDocumentRepository repository) {
        this.repository = repository;
    }

    public List<TypeDocument> listerTous() {
        return repository.findAll();
    }

    public TypeDocument creer(TypeDocument type) {
        if (repository.existsByLibelleIgnoreCase(type.getLibelle())) {
            throw new DoublonException("Le type de document « " + type.getLibelle() + " » existe déjà.");
        }
        return repository.save(type);
    }
}