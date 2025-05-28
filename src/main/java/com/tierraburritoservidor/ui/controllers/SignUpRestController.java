package com.tierraburritoservidor.ui.controllers;

import com.tierraburritoservidor.domain.model.TipoUsuario;
import com.tierraburritoservidor.domain.model.Usuario;
import com.tierraburritoservidor.domain.service.ServiceUsuarios;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpRestController {

    private final ServiceUsuarios serviceUsuarios;

    @PostMapping("/cliente")
    public ResponseEntity<Integer> signUpCliente(@RequestBody Usuario usuario) {
        usuario.setTipoUsuario(TipoUsuario.CLIENTE);
        int id = serviceUsuarios.addUsuario(usuario);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(id);
    }

    @PostMapping("/repartidor")
    public ResponseEntity<Integer> signUpRepartidor(@RequestBody Usuario usuario) {
        usuario.setTipoUsuario(TipoUsuario.REPARTIDOR);
        int id = serviceUsuarios.addUsuario(usuario);
        return ResponseEntity.status(HttpServletResponse.SC_CREATED).body(id);
    }
}
