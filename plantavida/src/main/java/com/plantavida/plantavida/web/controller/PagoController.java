package com.plantavida.plantavida.web.controller;

import com.plantavida.plantavida.service.PagoService;
import com.plantavida.plantavida.service.dto.PagoRequestDto;
import com.plantavida.plantavida.service.dto.PagoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;
    @Value("${mercadopago.email.vendedor}")
    private String emailVendedor;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagoResponseDto> realizarPago(@RequestBody PagoRequestDto request) {
        try {
            // Captura el compradorId
            Long compradorId = Long.valueOf(request.getCompradorId());
            System.out.println("Comprador ID recibido: " + compradorId);

            // Procesar el pago a través del servicio
            PagoResponseDto response = pagoService.procesarPago(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Manejo de errores específicos
            return new ResponseEntity<>(new PagoResponseDto("FAILURE", null, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo genérico de excepciones
            return new ResponseEntity<>(new PagoResponseDto("FAILURE", null, "Error interno al procesar el pago."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirNotificacion(@RequestBody String payload) {
        // Log para seguimiento del webhook
        System.out.println("Notificación recibida: " + payload);

        // Procesa la notificación y actualiza el estado en la base de datos según sea necesario
        return ResponseEntity.ok("Notificación procesada exitosamente.");
    }
}

