package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.common.Constantes;
import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import com.tierraburritoservidor.util.MailComponent;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpRestController {

    private final ServiceUsuarios serviceUsuarios;
    private final MailComponent mailComponent;

    @PostMapping("/cliente")
    public ResponseEntity<Integer> signUpCliente(@RequestBody Usuario usuario) {
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        String codigo = serviceUsuarios.crearUsuarioDesactivado(usuario);
        int id = serviceUsuarios.getUsuarioByCorreo(usuario.getCorreo()).getId();
        mailComponent.mandarCorreoActivacion(usuario.getCorreo(), id, codigo);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(id);
    }

    @PostMapping("/repartidor")
    public ResponseEntity<Integer> signUpRepartidor(@RequestBody Usuario usuario) {
        usuario.setTipoUsuario(TipoUsuario.REPARTIDOR);
        String codigo = serviceUsuarios.crearUsuarioDesactivado(usuario);
        int id = serviceUsuarios.getUsuarioByCorreo(usuario.getCorreo()).getId();
        mailComponent.mandarCorreoActivacion(usuario.getCorreo(), id, codigo);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(id);
    }

    @GetMapping("/activar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable int id, @RequestParam String codigo) {
        serviceUsuarios.activarUsuario(id, codigo);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(Constantes.USUARIO_ACTIVADO);
    }
}
