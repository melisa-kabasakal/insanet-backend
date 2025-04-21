package com.insanet.insanet_backend.enums;

public enum CategoryType {
    YAPI_MALZEMELERI("Yapı Malzemeleri"),
    INSAAT_MALZEMELERI("İnşaat Malzemeleri"),
    HIRDAVAT("Hırdavat Malzemeleri"),
    ELEKTRIK("Elektrik Malzemeleri");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
