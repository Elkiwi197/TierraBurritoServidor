package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesErrores;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException() {
        super(ConstantesErrores.PEDIDO_NO_ENCONTRADO);
    }
}
