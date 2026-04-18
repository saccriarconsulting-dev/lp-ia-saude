package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

/**
 * @author Rogério Massa on 30/10/18.
 */

public enum EnumRegisterStatus {

    SCHEDULED(0, "PENDENTE DE SINCRONIZAÇÃO", R.drawable.status_label_yellow),
    SENT(1, "ENVIADO PARA O CADASTRO", R.drawable.status_label_grey),/*ENVIADO CADASTRO*/
    ALREADY_REGISTERED(2, "JÁ EXISTE UM CLIENTE CADASTRADO COM ESSE CPF/CNPJ", R.drawable.status_label_yellow),
    INVALID_DOCUMENT(3, "DOCUMENTOS INVÁLIDOS, FAVOR REENVIAR CONFORME REGRA", R.drawable.status_label_yellow),/*DOCUMENTOS INVÁLIDOS*/
    VALID_DOCUMENT(4, "ENVIADO PARA O CADASTRO", R.drawable.status_label_grey),/*DOCUMENTOS VÁLIDOS*/
    WITH_PENDENCIES(5, "ENVIADO PARA O CADASTRO", R.drawable.status_label_grey),/*CADASTRO COM PÊNDENCIA(S) NO SERASA*/
    APPROVED(6, "CADASTRO APROVADO", R.drawable.status_label_green),
    APPROVED_TWO(7, "CADASTRO APROVADO", R.drawable.status_label_green),
    DISAPPROVED(8, "DEVOLVIDO AO VENDEDOR", R.drawable.status_label_yellow),/*CADASTRO NEGADO*/
    DISAPPROVED_ACQUISITION(8, "CADASTRO NEGADO", R.drawable.status_label_yellow),
    MIGRATION_DISAPPROVED(8, "MIGRACAO NEGADA", R.drawable.status_label_yellow),
    SENT_ACQUISITION(9, "ENVIADO ADQUIRÊNCIA", R.drawable.status_label_grey),
    SEND_TRANSACTIONING(10, "ATIVO TRANSACIONANDO", R.drawable.status_label_green),
    INACTIVE(11, "CADASTRO INATIVO", R.drawable.status_label_yellow),
    VALIDATED_TAX(12, "VALIDAÇÃO TAXA MDR", R.drawable.status_label_green),
    WAITING_CONFIRMATION(99, "AGUARDANDO CONFIRMAÇÃO CLIENTE", R.drawable.status_label_yellow),
    PENDENTE_MIGRACAO(90, "PENDENTE DE MIGRAÇÃO", R.drawable.status_label_red);

    private final int value;
    private final String title;
    private final int background;

    EnumRegisterStatus(int value, String title, int background) {
        this.value = value;
        this.title = title;
        this.background = background;
    }

    public static EnumRegisterStatus getEnumByValue(Integer value) {
        return Stream.of(values())
                .filter(item -> value != null && item.getValue() == value)
                .findFirst()
                .orElse(null);
    }

    public static EnumRegisterStatus getEnumByValue(int value, EnumRegisterCustomerType customerType) {
        return Stream.of(values())
                .filter(item -> {
                    switch (customerType) {
                        case MIGRATION:
                            if (item.getValue() == value && item.equals(MIGRATION_DISAPPROVED)) {
                                return true;
                            } else if (item.getValue() == value && item.equals(DISAPPROVED_ACQUISITION)) {
                                return false;
                            } else if (item.getValue() == value && item.equals(DISAPPROVED)) {
                                return false;
                            } else {
                                return item.value == value;
                            }
                        case ACQUISITION:
                            if (item.getValue() == value && item.equals(MIGRATION_DISAPPROVED)) {
                                return false;
                            } else if (item.getValue() == value && item.equals(DISAPPROVED_ACQUISITION)) {
                                return true;
                            } else if (item.getValue() == value && item.equals(DISAPPROVED)) {
                                return false;
                            } else {
                                return item.value == value;
                            }
                        default:
                            return item.value == value;
                    }
                })
                .findFirst()
                .orElse(SCHEDULED);
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public int getBackground() {
        return background;
    }
}
