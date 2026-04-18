package com.axys.redeflexmobile.ui.base;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.adquirencia.EmptyListObject;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;
import com.axys.redeflexmobile.ui.base.holder.EmptyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vitor Otero on 18/10/17.
 */

public abstract class BaseAdapter<V, W extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> implements
        BaseViewHolder.IBaseViewHolderListener {

    private static final int VIEW_TYPE_EMPTY = 1000;
    private static final int MINIMUM_EMPTY_LIST = 1;

    protected IBaseViewHolderClickListener<V> clickListener;
    protected IBaseViewHolderLongClickListener<V> longClickListener;

    private List<V> model = new ArrayList<>();
    private EmptyListObject emptyListObject;

    public BaseAdapter() {
        clickListener = null;
        longClickListener = null;
    }

    public BaseAdapter(IBaseViewHolderClickListener<V> clickListener) {
        this.clickListener = clickListener;
    }

    public BaseAdapter(IBaseViewHolderLongClickListener<V> longClickListener) {
        this.longClickListener = longClickListener;

    }

    public BaseAdapter(IBaseViewHolderClickListener<V> clickListener,
                       IBaseViewHolderLongClickListener<V> longClickListener) {
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @LayoutRes
    public abstract int getLayoutItem();

    public abstract BaseViewHolder holder(View view);

    protected List<V> getModel() {
        return model;
    }

    public void setList(List<V> model) {
        this.model = model;
        notifyDataSetChanged();
    }

    public void setEmptyListObject(@NonNull EmptyListObject emptyListObject) {
        this.emptyListObject = emptyListObject;
    }

    @Override
    public int getItemCount() {
        if (model.isEmpty() && this.emptyListObject != null) {
            return MINIMUM_EMPTY_LIST;
        }
        return model.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (model.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutItem(), parent, false);
        if (VIEW_TYPE_EMPTY == viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_empty_list, parent, false);
            return new EmptyViewHolder(view, emptyListObject);
        }
        return holder(view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        W holder = (W) baseViewHolder;
        if (getModel().isEmpty()) {
            return;
        }
        holder.bind(getModel().get(i), i);
    }

    @Override
    public boolean isTheLast(int position) {
        return getModel().size() - MINIMUM_EMPTY_LIST == position;
    }

    public interface IBaseViewHolderClickListener<V> {
        void onClickListener(V v, View view);
    }

    public interface IBaseViewHolderLongClickListener<V> {
        void onLongClickListener(V v, View view);
    }
}