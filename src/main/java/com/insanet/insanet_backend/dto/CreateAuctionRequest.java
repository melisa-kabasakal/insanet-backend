package com.insanet.insanet_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuctionRequest {
    @NotBlank(message = "Başlık boş olamaz")
    @Size(min = 3, max = 100, message = "Başlık 3-100 karakter arasında olmalıdır")
    private String title;

    @NotBlank(message = "Kategori boş olamaz")
    private String category;

    @NotBlank(message = "Konum boş olamaz")
    private String location;

    @NotNull(message = "Başlangıç fiyatı boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Başlangıç fiyatı 0'dan büyük olmalıdır")
    private BigDecimal startingPrice;

    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;

    private String imageUrl;

    @NotNull(message = "Başlangıç tarihi boş olamaz")
    @Future(message = "Başlangıç tarihi gelecekte olmalıdır")
    private LocalDateTime startDate;

    @NotNull(message = "Bitiş tarihi boş olamaz")
    @Future(message = "Bitiş tarihi gelecekte olmalıdır")
    private LocalDateTime endDate;

    @AssertTrue(message = "Bitiş tarihi başlangıç tarihinden sonra olmalıdır")
    private boolean isEndDateValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return endDate.isAfter(startDate);
    }
}
