package com.tierraburritoservidor.errors;

import com.tierraburritoservidor.common.ConstantesErrores;

public class ProductoNoEncontradoException  extends RuntimeException {
    public ProductoNoEncontradoException() {
        super(ConstantesErrores.PRODUCTO_NO_ENCONTRADO);
    }
}
