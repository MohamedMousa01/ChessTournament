package com.prova.chess.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ImportoDTO {

    @NotNull
    @DecimalMin(value = "5.00", message = "L'importo minimo deve essere 5 euro")
    private Double importo;

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}
