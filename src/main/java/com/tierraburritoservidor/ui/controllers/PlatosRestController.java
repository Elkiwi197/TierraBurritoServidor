package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.service.ServicePlatos;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/platos")
public class PlatosRestController {

    private final ServicePlatos servicePlatos;

    @GetMapping("")
    public List<Plato> getAllPlatos() {
        return servicePlatos.getAllPlatos();
    }

    @GetMapping("/{id}")
    public Plato getPlatoById(@PathVariable int id) {
        return servicePlatos.getPlatoById(id);
    }

}
