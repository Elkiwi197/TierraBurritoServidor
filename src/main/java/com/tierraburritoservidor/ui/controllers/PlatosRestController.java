package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/productos")
public class PlatosRestController {

    @GetMapping("/platos")
    public List<Producto> getPlatos(){
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(
                1,
                "Tacos del pastor",
                5.99,
                "foto.jpg"
        ));
        return productos;
    }

}
