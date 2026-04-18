package com.axys.redeflexmobile.ui.base.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.ui.base.BaseAdapter.IBaseViewHolderClickListener;
import com.axys.redeflexmobile.ui.base.BaseAdapter.IBaseViewHolderLongClickListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author Vitor Otero on 18/10/17.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    protected IBaseViewHolderListener baseViewHolderListener;
    protected IBaseViewHolderExpandableListener baseViewHolderExpandableListener;
    protected IBaseViewHolderClickListener<T> clickListener;
    protected IBaseViewHolderLongClickListener<T> longClickListener;

    protected Context context;

    //region CONSTRUTORES
    public BaseViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderExpandableListener baseViewHolderExpandableListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
    }

    public BaseViewHolder(@NonNull View itemView,
                          IBaseViewHolderClickListener<T> clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.clickListener = clickListener;
    }

    public BaseViewHolder(@NonNull View itemView,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderExpandableListener baseViewHolderExpandableListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderClickListener<T> clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.clickListener = clickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderClickListener<T> clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.clickListener = clickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderClickListener<T> clickListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderClickListener<T> clickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.clickListener = clickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderClickListener<T> clickListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderClickListener<T> clickListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public BaseViewHolder(@NonNull View itemView, IBaseViewHolderListener baseViewHolderListener,
                          IBaseViewHolderExpandableListener baseViewHolderExpandableListener,
                          IBaseViewHolderClickListener<T> clickListener,
                          IBaseViewHolderLongClickListener<T> longClickListener) {
        super(itemView);
        this.context = itemView.getContext();
        this.baseViewHolderListener = baseViewHolderListener;
        this.baseViewHolderExpandableListener = baseViewHolderExpandableListener;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }
    //endregion

    public void bind(T object) {
    }

    public void bind(T object, int position) {
    }

    protected LinearLayout.LayoutParams prepareMargins(Context context, boolean isLast) {
        int marginNormal = (int) context.getResources().getDimension(R.dimen.spacing_normal);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        lParams.setMargins(marginNormal, marginNormal, marginNormal,
                isLast ? marginNormal : 0);
        return lParams;
    }

    public interface IBaseViewHolderListener {
        boolean isTheLast(int position);
    }

    public interface IBaseViewHolderExpandableListener {
        boolean isOpened(int position);

        void openClose(int position);
    }
}
