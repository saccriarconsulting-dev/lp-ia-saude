package com.axys.redeflexmobile.ui.component.flagsbank;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;

import java.util.ArrayList;
import java.util.List;


public class FlagsBank extends LinearLayout implements ItemFlagBankSelected {

    RecyclerView recyclerView;
    LinearLayoutManager HorizontalLayout;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    FlagsBankListAdapter adapter;
    List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> source;

    FlagsBankEventListener listener;
    public void setCardFlagsEventListener(FlagsBankEventListener listener){
        this.listener = listener;
    }

    public void setSelectItemEnabled(Boolean enabled){
        adapter.setSelectItemEnabled(enabled);
    }

    public FlagsBank(Context context) {
        this(context, null);
    }

    public FlagsBank(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlagsBank(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.component_flags_bank, this);
        recyclerView = (RecyclerView)findViewById(R.id.card_flags_recyclerview);
        RecyclerViewLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        source = new ArrayList<>();
        adapter = new FlagsBankListAdapter(source, this);
        HorizontalLayout = new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adapter);
    }

    public void updateSelected(Integer cardFlagId){
        adapter.setCardFlagIdSelected(cardFlagId);
    }

    public void updateData(List<com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank> data){
        this.source = data;
        this.adapter.updateData(source);
    }

    @Override
    public void onItemFlagBankSelected(com.axys.redeflexmobile.shared.models.clientinfo.FlagsBank flag) {
        if(listener != null){
            listener.onFlagBankSelected(flag);
        }
    }
}
