package com.axys.redeflexmobile.shared.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.CallReason;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author lucasmarciano on 17/07/20
 */
@SuppressLint("InflateParams")
public
class CallReasonAdapter extends ArrayAdapter<CallReason> implements Serializable {

    private final List<CallReason> mValues;
    private final int mLayoutResourceId;
    private final Context mContext;

    public CallReasonAdapter(Context context, int textViewResourceId, List<CallReason> values) {
        super(context, textViewResourceId, values);
        mLayoutResourceId = textViewResourceId;
        mContext = context;
        mValues = values;
    }

    public int getCount() {
        return mValues.size();
    }

    public CallReason getItem(int position) {
        return mValues.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public @NotNull View getView(int position, View convertView, @NotNull ViewGroup parent) {
        CallReason callReason = mValues.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.txtValor)).setText(callReason.getDescription());
        convertView.setTag(callReason.getId());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
        CallReason callReason = mValues.get(position);
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.txtValor)).setText(callReason.getDescription());
        convertView.setTag(callReason.getId());
        return convertView;
    }
}
