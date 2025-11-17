package com.tierraburritoservidor.errors.exceptions;

import com.tierraburritoservidor.common.ConstantesInfo;

public class ProductoNoEncontradoException  extends RuntimeException {
    public ProductoNoEncontradoException() {
        super(ConstantesInfo.PRODUCTO_NO_ENCONTRADO);
    }
}
