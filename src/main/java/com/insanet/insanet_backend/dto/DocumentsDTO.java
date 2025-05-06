package com.insanet.insanet_backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class DocumentsDTO {

    @Column(name = "tax_certificate_url")
    private String taxCertificate ;

    @Column(name = "trade_registry_url")
    private String tradeRegistry;

    @Column(name = "signature_certificate_url")
    private String signatureCertificate ;

    @Column(name = "chamber_of_commerce_url")
    private String chamberOfCommerce;
}
