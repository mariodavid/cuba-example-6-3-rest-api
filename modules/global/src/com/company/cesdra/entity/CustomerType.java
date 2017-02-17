package com.company.cesdra.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum CustomerType implements EnumClass<String> {

    NEW("NEW"),
    OLD("OLD");

    private String id;

    CustomerType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CustomerType fromId(String id) {
        for (CustomerType at : CustomerType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}