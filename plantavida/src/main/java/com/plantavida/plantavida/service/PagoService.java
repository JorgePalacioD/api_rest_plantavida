package com.plantavida.plantavida.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plantavida.plantavida.persistence.entity.CompradorEntity;
import com.plantavida.plantavida.persistence.repository.CompradorRepository;
import com.plantavida.plantavida.service.dto.PagoRequestDto;
import com.plantavida.plantavida.service.dto.PagoResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;
    @Value("${mercadopago.email.vendedor}") // Define el email del vendedor en application.properties
    private String emailVendedor;

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private RestTemplate restTemplate;

    public PagoResponseDto procesarPago(PagoRequestDto request) {
        PagoResponseDto response = new PagoResponseDto();

        // Correo del vendedor asociado a tu cuenta de Mercado Pago
        String vendedorEmail = "plantavida636@gmail.com";
        System.out.println("Email comprador recibido: " + request.getEmailComprador());

        // Validar que el comprador no sea el mismo que el vendedor
        if (request.getEmailComprador().equalsIgnoreCase(vendedorEmail)) {
            response.setStatus("FAILURE");
            response.setMessage("No puedes realizar un pago usando el mismo correo que el vendedor.");
            return response;
        }

        try {
            // Configurar el cuerpo de la solicitud
            String url = "https://api.mercadopago.com/checkout/preferences";

            String requestBody = """
            {
              "items": [
                {
                  "title": "%s",
                  "quantity": 1,
                  "unit_price": %f
                }
              ],
              "payer": {
                "email": "%s"
              }
            }
        """.formatted(request.getDescription(), request.getAmount(), request.getEmailComprador());

            // Configurar la cabecera de autorización
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // Enviar la solicitud HTTP POST a Mercado Pago
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonResponse = mapper.readTree(responseEntity.getBody());
                String preferenceId = jsonResponse.get("id").asText();
                String initPoint = jsonResponse.get("init_point").asText();

                response.setStatus("PENDING");
                response.setTransactionId(preferenceId);
                response.setMessage("Pago creado correctamente. Usa el siguiente enlace para pagar: " + initPoint);

                // Guardar en base de datos
                CompradorEntity comprador = compradorRepository.findById(request.getCompradorId())
                        .orElseThrow(() -> new IllegalArgumentException("Comprador no encontrado con ID: " + request.getCompradorId()));
                comprador.addTransaction(preferenceId);
                compradorRepository.save(comprador);
            } else {
                response.setStatus("FAILURE");
                response.setMessage("Error al crear el pago en Mercado Pago.");
            }

        } catch (Exception e) {
            response.setStatus("FAILURE");
            response.setMessage("Error al procesar el pago: " + e.getMessage());
        }

        return response;
    }

}
