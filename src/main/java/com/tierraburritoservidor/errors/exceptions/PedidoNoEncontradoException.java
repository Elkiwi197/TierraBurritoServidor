package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException() {
        super(ConstantesInfo.PEDIDO_NO_ENCONTRADO);
    }
}
