package bf.carfo.sigeco.controller;

import bf.carfo.sigeco.entity.TypeDocument;
import bf.carfo.sigeco.service.TypeDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types-document")
public class TypeDocumentController {

    private final TypeDocumentService service;

    public TypeDocumentController(TypeDocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<TypeDocument> lister() {
        return service.listerTous();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeDocument creer(@RequestBody TypeDocument type) {
        return service.creer(type);
    }
}