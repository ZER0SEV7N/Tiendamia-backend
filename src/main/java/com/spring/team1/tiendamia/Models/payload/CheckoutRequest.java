package com.spring.team1.tiendamia.Models.payload;

import lombok.Data;

@Data
public class CheckoutRequest {
    private DireccionDto direccion;
    private PagoSimulado pago;
    private Double total;

    @Data
    public static class DireccionDto {
        private String direccion;
        private String distrito;
        private String provincia;
        private String departamento;
        private String referencia;
    }   

    @Data
    public static class PagoSimulado {
        private String pasarela;
        private String customer_token;
        private String ultimos_cuatro;
        private Integer marca_tarjeta_id;
    }


}
