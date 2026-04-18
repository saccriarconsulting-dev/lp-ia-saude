package com.axys.redeflexmobile.shared.enums.register;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.ui.component.customspinner.ICustomSpinnerDialogModel;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rogério Massa on 17/01/19.
 */

public enum EnumRegisterAttachmentType implements ICustomSpinnerDialogModel {

    FACADE(0, "FotoFachadaPDV", "Fachada"),
    CPF(1, "CPF", "CPF", "CPF do proprietário"),
    RG_FRONT(2, "RG", "Frente RG"),
    RG_BACK(3, "RG2", "Verso RG"),
    CNH(4, "CNH", "CNH"),
    PROOF_OF_RESIDENCE(5, "CompEnd", "Comprovante Endereço"),
    ADHESION_TERM(6, "AdesaoPOS", "Termo de Adesão POS"),
    CNPJ_CARD(7, "CartaoCnpj", "Cartão CNPJ"),
    IE(8, "IE", "Inscrição Estadual"),
    MEI(9, "MEI", "MEI"),
    ADDRESS_STATEMENT(10, "DecEnd", "Declaraçao de Endereço"),
    RENT_CONTRACT(11, "ContAluguel", "Contrato de Aluguel"),
    BANK_IDENTIFICATION(12, "IdBancaria", "Identificação Bancária"),
    PROPOSAL_FORM(13, "ficha", "Ficha da Proposta"),
    MEDIUM_TICKET_CARDS(14, "ticket", "Ticket Médio de cartões"),
    OTHERS(15, "outros", "Outros"),
    EMANCIPACAO(16, "Emancipacao", "Emancipação"),
    TAXAS_CONCORRENTE(17, "TaxasConcorrente", "Taxas de Concorrente");

    private int id;
    private String type;
    private String description;
    private String identifier;

    EnumRegisterAttachmentType(int id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    EnumRegisterAttachmentType(int id, String type, String description, String identifier) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.identifier = identifier;
    }

    public static EnumRegisterAttachmentType getEnumByType(String type) {
        return Stream.of(values())
                .filter(value1 -> value1.type.equals(type))
                .findFirst()
                .get();
    }

    public static List<EnumRegisterAttachmentType> getAcquisitionPhysicalAttachmentList() {
        //return Arrays.asList(FACADE, PROOF_OF_RESIDENCE, OTHERS);
        return Arrays.asList(FACADE, CNH, RG_FRONT, RG_BACK, CPF, EMANCIPACAO);
    }

    public static List<EnumRegisterAttachmentType> getAcquisitionJuridicalAttachmentList() {
        return Arrays.asList(FACADE, CNH, RG_FRONT, RG_BACK, CPF);
        //return Arrays.asList(FACADE, PROOF_OF_RESIDENCE, OTHERS);
    }

    public static List<EnumRegisterAttachmentType> getNonAcquisitionPhysicalAttachmentList() {
        return Arrays.asList(FACADE, CPF, RG_FRONT, RG_BACK, CNH, PROOF_OF_RESIDENCE, ADHESION_TERM, ADDRESS_STATEMENT);
    }

    public static List<EnumRegisterAttachmentType> getNonAcquisitionJuridicalAttachmentList() {
        return Arrays.asList(FACADE, CNPJ_CARD, IE, MEI, CPF, RG_FRONT, RG_BACK, CNH, PROOF_OF_RESIDENCE, ADHESION_TERM, ADDRESS_STATEMENT);
    }

    @Override
    public Integer getIdValue() {
        return id;
    }

    @Override
    public String getDescriptionValue() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }
}
