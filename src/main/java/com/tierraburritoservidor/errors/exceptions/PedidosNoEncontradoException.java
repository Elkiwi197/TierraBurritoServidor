package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class PedidosNoEncontradoException extends RuntimeException {
    public PedidosNoEncontradoException() {
        super(ConstantesErrores.PEDIDOS_NO_ENCONTRADOS);
    }
}
