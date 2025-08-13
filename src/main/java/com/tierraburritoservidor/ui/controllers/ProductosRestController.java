package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.service.ServiceProductos;
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

    private final ServiceProductos serviceProductos;

    @PostMapping("/ingredientes/plato")
    public List<Producto> getIngredientesByPlato(@RequestBody Plato plato){
        return serviceProductos.getIngredientesByPlato(plato);
    }

    @PostMapping("/extras/plato")
    public List<Producto> getExtrasByPlato(@RequestBody Plato plato){
        return serviceProductos.getExtrasByPlato(plato);
    }

}
