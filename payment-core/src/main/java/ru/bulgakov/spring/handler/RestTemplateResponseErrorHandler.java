package ru.bulgakov.spring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ru.bulgakov.spring.dto.payment.PaymentErrorDtoRs;
import ru.bulgakov.spring.exception.IntegrationException;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        String errorDetails = extractErrorDetails(response);

        log.error("Ошибка при вызове {} {}: Status {}, Response: {}",
                method, url, statusCode, errorDetails);

        String userMessage = determineUserMessage(statusCode, errorDetails);

        throw new IntegrationException(
                buildInternalMessage(url, method, statusCode, errorDetails),
                userMessage
        );
    }

    private String extractErrorDetails(ClientHttpResponse response) {
        try {
            PaymentErrorDtoRs errorDto = objectMapper.readValue(response.getBody(), PaymentErrorDtoRs.class);
            return errorDto.message();
        } catch (Exception e) {
            return "Не удалось прочитать тело ошибки";
        }
    }

    private String determineUserMessage(HttpStatusCode statusCode, String errorDetails) {
        if (statusCode.is5xxServerError()) {
            return "Сервис временно недоступен. Попробуйте позже";
        } else if (statusCode == HttpStatus.NOT_FOUND) {
            return "Запрашиваемый ресурс не найден";
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            return "Неверный запрос: " + (errorDetails != null ? errorDetails : "");
        } else {
            return "Произошла ошибка при обработке запроса";
        }
    }

    private String buildInternalMessage(URI url, HttpMethod method, HttpStatusCode statusCode, String errorDetails) {
        return String.format("Ошибка интеграции: %s %s - Status: %s, Details: %s",
                method, url, statusCode, errorDetails);
    }
}
