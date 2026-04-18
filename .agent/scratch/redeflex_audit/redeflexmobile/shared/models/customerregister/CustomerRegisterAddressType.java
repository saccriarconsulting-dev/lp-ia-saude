package com.axys.redeflexmobile.shared.models.customerregister;

import com.annimon.stream.Stream;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rogério Massa on 06/02/19.
 */

public class CustomerRegisterAddressType {

    private boolean checked;
    private EnumRegisterAddressType type;
    private boolean required;

    public CustomerRegisterAddressType(EnumRegisterAddressType enumType, boolean required) {
        this.type = enumType;
        this.required = required;
    }

    public static List<CustomerRegisterAddressType> getDefaultList() {
        return Arrays.asList(new CustomerRegisterAddressType(EnumRegisterAddressType.MAIN, true),
                new CustomerRegisterAddressType(EnumRegisterAddressType.INSTALLATION, false),
                new CustomerRegisterAddressType(EnumRegisterAddressType.COLLECTION, true));
    }

    public static List<CustomerRegisterAddressType> getAcquisitionJuridicalList() {
        return Stream.ofNullable(getDefaultList())
                .map(customerRegisterAddressType -> {
                    if (EnumRegisterAddressType.INSTALLATION.equals(customerRegisterAddressType.type)) {
                        customerRegisterAddressType.setRequired(true);
                    }
                    return customerRegisterAddressType;
                })
                .toList();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public EnumRegisterAddressType getType() {
        return type;
    }

    public void setType(EnumRegisterAddressType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerRegisterAddressType that = (CustomerRegisterAddressType) o;

        if (checked != that.checked) return false;
        if (required != that.required) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = (checked ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        return result;
    }

    public enum EnumRegisterAddressType {

        MAIN(1, "ENDEREÇO PRINCIPAL"),
        INSTALLATION(2, "ENDEREÇO DE INSTALAÇÃO"),
        COLLECTION(3, "ENDEREÇO DO PROPRIETÁRIO");

        private int value;
        private String description;

        EnumRegisterAddressType(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public static EnumRegisterAddressType getEnumByValue(int value) {
            return Stream.ofNullable(values())
                    .filter(item -> item.value == value)
                    .findFirst()
                    .orElse(null);
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }
    }
}
