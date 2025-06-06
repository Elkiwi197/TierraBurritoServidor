package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.model.Producto;
import com.tierraburritoservidor.domain.service.ServiceProductos;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/productos")
public class ProductosRestController {

    private final ServiceProductos serviceProductos;

    @GetMapping("")
    public List<Producto> getAllProductos(){
        return serviceProductos.getAllProductos();
    }

    @GetMapping("/producto/{id}")
    public Producto getProductoById(@PathVariable int id){
        return serviceProductos.getProductoById(id);
    }

    @PostMapping("/producto/{nombreProducto}")
    public Producto getProductoByNombre(@PathVariable String nombreProducto){
        return serviceProductos.getProductoByNombre(nombreProducto);
    }

    @PostMapping("/ingredientes/plato")
    public List<Producto> getIngredientesByPlato(@RequestBody Plato plato){
        return serviceProductos.getIngredientesByPlato(plato);
    }

    @PostMapping("/extras/plato")
    public List<Producto> getExtrasByPlato(@RequestBody Plato plato){
        return serviceProductos.getExtrasByPlato(plato);
    }

}
