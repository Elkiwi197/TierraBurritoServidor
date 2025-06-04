package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.model.Plato;
import com.tierraburritoservidor.domain.service.ServicePedidos;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidosRestController {

    private final ServicePedidos servicePedidos;

    @GetMapping("/pedido/{id}")
    public Pedido addPlatoPedidoActual(@PathVariable int id) {
        return servicePedidos.getPedidoById(id);
    }

    @GetMapping("/usuario/{correoCliente}")
    public Pedido getPedidoActualByUsuario(@PathVariable String correoCliente) {
        return servicePedidos.getPedidoActual(correoCliente);
    }

    @PostMapping("/anadirPedido")
    public Pedido addPlatoPedidoActual(@RequestBody Pedido pedido) {
        return servicePedidos.addPedido(pedido);
    }

    @PostMapping("/anadirPlato{correoCliente}")
    public void addPlatoPedidoActual(@RequestBody Plato plato, @PathVariable String correoCliente) {
        servicePedidos.addPlato(plato, correoCliente);
    }

}
