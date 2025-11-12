package com.tierraburritoservidor.ui.controllers;


import com.tierraburritoservidor.domain.model.Pedido;
import com.tierraburritoservidor.domain.service.ServicePedidos;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
@Log4j2
public class PedidosRestController {

    private final ServicePedidos servicePedidos;


    @GetMapping("/usuario/{correoCliente}")
    public List<Pedido> getPedidosByCorreoCliente(@PathVariable String correoCliente) {
        return servicePedidos.getPedidosByCorreoCliente(correoCliente);
    }

    @GetMapping("/enPreparacion")
    public List<Pedido> getPedidosEnPreparacion() {
        return  servicePedidos.getPedidosEnPreparacion();
    }

    @PostMapping("/anadirPedido")
    public String addPedido(@RequestBody Pedido pedido) {
        return servicePedidos.addPedido(pedido);
    }

    @PostMapping("/aceptarPedido")
    public String aceptarPedido(@RequestParam int idPedido, @RequestParam String correoRepartidor){
        return servicePedidos.aceptarPedido(idPedido, correoRepartidor);
    }

    @PostMapping("/cancelarPedido")
    public String cancelarPedido(@RequestParam int idPedido, @RequestParam String correo){
        return servicePedidos.cancelarPedido(idPedido, correo);
    }

    @GetMapping("/aceptado/{correoRepartidor}")
    public Pedido getPedidoAceptado(@PathVariable String correoRepartidor) {
        return servicePedidos.getPedidoAceptado(correoRepartidor);
    }
}
