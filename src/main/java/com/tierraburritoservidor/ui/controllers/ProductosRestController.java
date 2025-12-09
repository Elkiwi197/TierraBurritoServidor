package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Ingrediente;
import com.tierraburritoservidor.domain.service.ServiceIngredientes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/productos")
public class ProductosRestController {

    private final ServiceIngredientes serviceIngredientes;

    @PostMapping("/ingredientes/plato")
    public List<Ingrediente> getIngredientesByPlato(@RequestBody Plato plato){
        return serviceIngredientes.getIngredientesByPlato(plato);
    }

    @PostMapping("/extras/plato")
    public List<Ingrediente> getExtrasByPlato(@RequestBody Plato plato){
        return serviceIngredientes.getExtrasByPlato(plato);
    }

}
