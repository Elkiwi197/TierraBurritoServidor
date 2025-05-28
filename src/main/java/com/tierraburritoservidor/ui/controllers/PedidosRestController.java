package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.service.ServicePedidos;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidosRestController {

    private final ServicePedidos servicePedidos;

    @GetMapping("/pedido/{id}")
    public Pedido getPedidoById(@PathVariable int id){
        return servicePedidos.getPedidoById(id);
    }

    @GetMapping("/usuario/{id}")
    public List<Pedido> getPedidosByUsuario(@PathVariable int id){
        return servicePedidos.getPedidoByUsuarioId(id);
    }

}
