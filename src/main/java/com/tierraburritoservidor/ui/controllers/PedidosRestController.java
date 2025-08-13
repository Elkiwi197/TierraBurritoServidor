package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.service.ServicePedidos;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidosRestController {

    private final ServicePedidos servicePedidos;


    @GetMapping("/usuario/{correoCliente}")
    public List<Pedido> getPedidosByCorreo(@PathVariable String correoCliente) {
        return servicePedidos.getPedidosByCorreo(correoCliente);
    }

    @PostMapping("/anadirPedido")
    public String addPlatoPedidoActual(@RequestBody Pedido pedido) {
        return servicePedidos.addPedido(pedido);
    }


}
