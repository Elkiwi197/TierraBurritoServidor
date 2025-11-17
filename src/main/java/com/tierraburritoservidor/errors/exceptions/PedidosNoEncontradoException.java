package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class PedidosNoEncontradoException extends RuntimeException {
    public PedidosNoEncontradoException() {
        super(ConstantesInfo.PEDIDOS_NO_ENCONTRADOS);
    }
}
