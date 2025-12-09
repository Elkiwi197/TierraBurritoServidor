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

    @PostMapping("/repartirPedido")
    public String repartirPedido(@RequestParam int idPedido, @RequestParam String correoRepartidor){
        return servicePedidos.repartirPedido(idPedido, correoRepartidor);
    }

    @PostMapping("/cancelarPedido")
    public String cancelarPedido(@RequestParam int idPedido, @RequestParam String correoRepartidor){
        return servicePedidos.cancelarPedido(idPedido, correoRepartidor);
    }

    @PostMapping("/entregarPedido")
    public String entregarPedido(@RequestParam int idPedido, @RequestParam String correoRepartidor){
        return servicePedidos.entregarPedido(idPedido, correoRepartidor);
    }

    @PostMapping("/noRepartirEstePedido")
    public String noRepartirEstePedido(@RequestParam int idPedido){
        return servicePedidos.noRepartirEstePedido(idPedido);
    }

    @GetMapping("/enReparto/{correoRepartidor}")
    public Pedido getPedidoEnRepartoByRepartidor(@PathVariable String correoRepartidor) {
        return servicePedidos.getPedidoEnRepartoByRepartidor(correoRepartidor);
    }

    @GetMapping("/repartidos/{correoRepartidor}")
    public List<Pedido> getPedidosRepartidos(@PathVariable String correoRepartidor) {
        return servicePedidos.getPedidosRepartidos(correoRepartidor);
    }
}
