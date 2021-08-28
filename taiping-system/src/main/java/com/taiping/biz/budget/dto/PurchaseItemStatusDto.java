package com.taiping.biz.budget.dto;

public class PurchaseItemStatusDto {
    private String key;
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PurchaseItemStatusDto newInstance(String key, String name) {
        PurchaseItemStatusDto dto = new PurchaseItemStatusDto();
        dto.setKey(key);
        dto.setName(name);
        return dto;
    }
}
