package com.axys.redeflexmobile.ui.register.customer.attachment;

import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.CNH;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.CPF;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.EMANCIPACAO;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.FACADE;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.RG_BACK;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType.RG_FRONT;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.ACQUISITION;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType.SUBADQUIRENCIA;
import static com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType.PHYSICAL;

import android.util.Log;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.di.module.SchedulerProvider;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterAttachmentType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterCustomerType;
import com.axys.redeflexmobile.shared.enums.register.EnumRegisterPersonType;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegister;
import com.axys.redeflexmobile.shared.models.customerregister.CustomerRegisterAttachment;
import com.axys.redeflexmobile.shared.mvp.BasePresenterImpl;
import com.axys.redeflexmobile.shared.util.StringUtils;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.shared.util.exception.ExceptionUtils;
import com.axys.redeflexmobile.ui.register.customer.RegisterCustomerView;
import com.axys.redeflexmobile.ui.register.customer.attachment.RegisterCustomerAttachmentAdapter.RegisterCustomerAttachmentAdapterItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Rogério Massa on 14/01/19.
 */

public class RegisterCustomerAttachmentPresenterImpl extends BasePresenterImpl<RegisterCustomerAttachmentView>
        implements RegisterCustomerAttachmentPresenter {

    private CustomerRegister customerRegister;
    private List<CustomerRegisterAttachment> attachments;

    public RegisterCustomerAttachmentPresenterImpl(RegisterCustomerAttachmentView view,
                                                   SchedulerProvider schedulerProvider,
                                                   ExceptionUtils exceptionUtils) {
        super(view, schedulerProvider, exceptionUtils);
    }

    @Override
    public void initializeData() {
        RegisterCustomerView parentActivity = getView().getParentActivity();
        if (parentActivity == null) return;
        this.customerRegister = parentActivity.getCustomerRegister();
        this.attachments = Stream.ofNullable(customerRegister.getAttachments())
                .map(CustomerRegisterAttachment::new)
                .toList();

        prepareAttachmentList();
    }

    @Override
    public void saveAttachment(CustomerRegisterAttachment item) {
        attachments.add(item);
        prepareAttachmentList();
    }

    @Override
    public void deleteAttachment(RegisterCustomerAttachmentAdapterItem item) {
        for (int i = 0; i < attachments.size(); i++) {
            CustomerRegisterAttachment attachment = attachments.get(i);
            if (attachment.getType().equals(item.type.getType()) && attachment.getId() != null) {
                attachment.setActivated(false);
                attachments.set(i, attachment);
            } else if (attachment.getType().equals(item.type.getType())) {
                attachments.remove(attachment);
            }
        }
        prepareAttachmentList();
    }

    @Override
    public void prepareAttachmentList() {
        EnumRegisterCustomerType customerType = customerRegister.getCustomerType();
        EnumRegisterPersonType personType = customerRegister.getPersonType();

        getView().setHeader(customerType == ACQUISITION || customerType == SUBADQUIRENCIA);

        // Ajusta o Numero da Sequencia do Titulo
        if (customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.ACQUISITION.getIdValue() || customerRegister.getCustomerType().getIdValue() == EnumRegisterCustomerType.SUBADQUIRENCIA.getIdValue())
        {
            if (customerRegister.getPersonType().getIdValue() == EnumRegisterPersonType.JURIDICAL.getIdValue())
                getView().setNumeroTitulo("8");
            else
                getView().setNumeroTitulo("7");
        }

        List<EnumRegisterAttachmentType> attachmentTypes;
        if ((customerType == ACQUISITION || customerType == SUBADQUIRENCIA) && personType == PHYSICAL) {
            attachmentTypes = EnumRegisterAttachmentType.getAcquisitionPhysicalAttachmentList();
        } else if (customerType == ACQUISITION || customerType == SUBADQUIRENCIA) {
            attachmentTypes = EnumRegisterAttachmentType.getAcquisitionJuridicalAttachmentList();
        } else if (personType == PHYSICAL) {
            attachmentTypes = EnumRegisterAttachmentType.getNonAcquisitionPhysicalAttachmentList();
        } else {
            attachmentTypes = EnumRegisterAttachmentType.getNonAcquisitionJuridicalAttachmentList();
        }

        List<RegisterCustomerAttachmentAdapterItem> attachments = Stream.of(attachmentTypes)
                .map(type -> new RegisterCustomerAttachmentAdapterItem(type, customerType, personType))
                .toList();

        for (CustomerRegisterAttachment item : this.attachments) {
            if (!item.isActivated()) {
                continue;
            }

            for (RegisterCustomerAttachmentAdapterItem itemList : attachments) {
                if (item.getType().equals(itemList.type.getType())) {
                    itemList.image = item.getFile();
                    itemList.imageSize = item.getFileSize();
                    itemList.situation = item.getSituation();
                    itemList.returnValue = item.getReturnValue();
                }
            }
        }
        getView().fillAttachments(attachments);
    }

    @Override
    public void doSave(boolean isBack) {

        if (isBack) {
            RegisterCustomerView parentActivity = getView().getParentActivity();
            CustomerRegister customerRegisterClone = parentActivity.getCustomerRegisterClone();
            customerRegisterClone.setAttachments(attachments);
            getView().onValidationSuccessBack();
            return;
        }

        CustomerRegisterAttachment facade = null;
        CustomerRegisterAttachment cnhHabilitacao = null, rgFrente = null, rgVerso = null, cpf = null, docEmancipacao = null;

        for (CustomerRegisterAttachment value : attachments) {
            if (FACADE.getType().equals(value.getType()) && value.isActivated()) {
                facade = value;
            }

            if (value.isActivated() && value.getSituation() == 3) {
                getView().showError(getView().getStringByResId(R.string.visit_prospect_attacment_error_block_status));
                return;
            }

            // Verifica se tem CNH
            if (customerRegister.getCustomerType().equals(ACQUISITION) || customerRegister.getCustomerType().equals(SUBADQUIRENCIA))
            {
                if (CNH.getType().equals(value.getType()) && value.isActivated()) {
                    cnhHabilitacao = value;
                } else if (CPF.getType().equals(value.getType()) && value.isActivated()) {
                    cpf = value;
                } else if (RG_FRONT.getType().equals(value.getType()) && value.isActivated()) {
                    rgFrente = value;
                } else if (RG_BACK.getType().equals(value.getType()) && value.isActivated()) {
                    rgVerso = value;
                } else if (EMANCIPACAO.getType().equals(value.getType()) && value.isActivated()) {
                    docEmancipacao = value;
                }
            }
        }

        if (facade == null) {
            getView().showError(getView().getStringByResId(R.string.visit_prospect_attacment_error_facade));
            return;
        }

        // Valida os documentos no caso de Adquirência
        if (customerRegister.getCustomerType().equals(ACQUISITION) || customerRegister.getCustomerType().equals(SUBADQUIRENCIA))
        {
            //if (cnhHabilitacao == null && rgFrente == null && rgVerso == null && cpf == null)
            //{
            //    getView().showError("Informe um documento Pessoal. Anexo obrigatório!");
            //    return;
            //}

            if (customerRegister.getPersonType().equals(PHYSICAL))
            {
                if (retornaMenorIdade(customerRegister.getBirthdate()) && docEmancipacao == null)
                {
                    getView().showError("Documento de Emancipação Obrigatório para menor de Idade!");
                    return;
                }
            }
        }

        customerRegister.setAttachments(attachments);
        getView().onValidationSuccess();
    }

    public boolean isNotValidAttachment(CustomerRegisterAttachment attachment) {
        return attachment == null || StringUtils.isEmpty(attachment.getFile());
    }

    private boolean retornaMenorIdade(Date pDataNascimento)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dataHoje = null;
            dataHoje = dateFormat.parse(dateFormat.format(new Date()));

            long idade = (Utilidades.diferencaDatas(dataHoje, pDataNascimento, TimeUnit.DAYS) / 365);

            if (idade < 18)
                return true;

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Redeflex", "Erro validateAcquisition: " + e.getMessage());
        }

        return false;
    }
}
