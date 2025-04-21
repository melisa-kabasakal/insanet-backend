package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBidRequest {
    @NotNull(message = "Teklif tutarı boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Teklif tutarı 0'dan büyük olmalıdır")
    @Digits(integer = 10, fraction = 2, message = "Teklif tutarı en fazla 10 basamak ve 2 ondalık hane içerebilir")
    private BigDecimal amount;

    @Size(max = 500, message = "Not en fazla 500 karakter olabilir")
    private String note;
}
