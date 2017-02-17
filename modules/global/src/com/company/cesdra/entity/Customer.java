package com.company.cesdra.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "CESDRA_CUSTOMER")
@Entity(name = "cesdra$Customer")
public class Customer extends StandardEntity {
    private static final long serialVersionUID = -915173921468062589L;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "CUSTOMER_TYPE", nullable = false)
    protected String customerType;

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType == null ? null : customerType.getId();
    }

    public CustomerType getCustomerType() {
        return customerType == null ? null : CustomerType.fromId(customerType);
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}