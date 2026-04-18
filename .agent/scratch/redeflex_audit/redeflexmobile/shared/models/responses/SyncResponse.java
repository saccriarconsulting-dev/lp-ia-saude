package com.axys.redeflexmobile.shared.models.responses;

public class SyncResponse {
    private boolean success;
    private int updatedCount;
    // Outros campos conforme contrato de API

    // Construtor vazio para Gson
    public SyncResponse() { }

    // getters e setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getUpdatedCount() { return updatedCount; }
    public void setUpdatedCount(int updatedCount) { this.updatedCount = updatedCount; }
}

