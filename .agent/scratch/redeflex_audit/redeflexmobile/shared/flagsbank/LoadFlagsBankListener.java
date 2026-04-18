package com.axys.redeflexmobile.shared.flagsbank;

import com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank;

import java.util.List;

public interface LoadFlagsBankListener {
    void onLoadFlagsBankError();
    void onLoadFlagsBankEmpty();
    void onLoadFlagsBankSuccess(List<FlagsBank> list);
}
