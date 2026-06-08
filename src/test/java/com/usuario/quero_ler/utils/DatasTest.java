package com.usuario.quero_ler.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.usuario.quero_ler.exceptions.especies.DataInvalidaException;
import com.fasterxml.jackson.annotation.JsonFormat.Value;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatasTest {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    public record DtoDate(LocalDate data) {
    }

    public record DtoDateTime(LocalDateTime data) {
    }

    @Test
    void localDate_serializa_e_desserializa_no_formato_ddMMyyyy() {
        LocalDate date = LocalDate.of(2015, 6, 3);
        String expected = "03/06/2015";

        mapper.configOverride(LocalDate.class)
                .setFormat(Value.forPattern("dd/MM/yyyy"));

        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(new DtoDate(date)));
        assertTrue(json.contains("\"data\":\"" + expected + "\""));

        DtoDate parsed = assertDoesNotThrow(() -> mapper.readValue("{\"data\":\"" + expected + "\"}", DtoDate.class));
        assertEquals(date, parsed.data());
    }

    @Test
    void localDateTime_serializa_e_desserializa_no_formato_ddMMyyyy_HHmmss() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 28, 14, 30, 15);
        String expected = "28/05/2026 14:30:15";

        mapper.configOverride(LocalDateTime.class)
                .setFormat(Value.forPattern("dd/MM/yyyy HH:mm:ss"));

        String json = assertDoesNotThrow(() -> mapper.writeValueAsString(new DtoDateTime(now)));
        assertTrue(json.contains("\"data\":\"" + expected + "\""));

        DtoDateTime parsed = assertDoesNotThrow(
                () -> mapper.readValue("{\"data\":\"" + expected + "\"}", DtoDateTime.class));
        assertEquals(now, parsed.data());
    }

    @Test
    void quando_formato_for_invalido_deve_referenciar_DataInvalidaException() {
        mapper.configOverride(LocalDate.class)
                .setFormat(Value.forPattern("dd/MM/yyyy"));

        String invalido = "{\"data\":\"2015-06-03\"}";

        assertThrows(InvalidFormatException.class, () -> mapper.readValue(invalido, DtoDate.class));

        DataInvalidaException ex = new DataInvalidaException("Formato de data inválido");

        assertEquals("Formato de data inválido", ex.getMessage());
    }

}
