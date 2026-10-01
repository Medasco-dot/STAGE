package bf.carfo.sigeco.controller;

import bf.carfo.sigeco.entity.TypeContentieux;
import bf.carfo.sigeco.service.TypeContentieuxService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types-contentieux")
public class TypeContentieuxController {

    private final TypeContentieuxService service;

    public TypeContentieuxController(TypeContentieuxService service) {
        this.service = service;
    }

    @GetMapping
    public List<TypeContentieux> lister() {
        return service.listerTous();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeContentieux creer(@RequestBody TypeContentieux type) {
        return service.creer(type);
    }
}