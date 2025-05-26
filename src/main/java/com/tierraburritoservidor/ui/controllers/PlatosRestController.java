package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.Plato;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/productos")
public class PlatosRestController {

    @GetMapping("/platos")
    public List<Plato> getPlatos(){
        List<Plato> platos = new ArrayList<>();
        platos.add(new Plato(
                1,
                "Tacos del pastor",
                5.99,
                "foto.jpg"
        ));
        return platos;
    }

}
