package com.axys.redeflexmobile.ui.component;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.NumberUtils;
import com.axys.redeflexmobile.ui.dialog.SearchableListDialog;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by joao.viana on 20/01/2017.
 */

public class SearchableSpinner extends AppCompatSpinner implements SearchableListDialog.SearchableItem {
    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private List _items;
    private SearchableListDialog _searchableListDialog;

    private boolean _isDirty;
    private ArrayAdapter _arrayAdapter;
    private String _strHintText;
    private boolean _isFromInit;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    private void init() {
        _items = new ArrayList();
        _searchableListDialog = SearchableListDialog.newInstance(_items);
        _searchableListDialog.setOnSearchableItemClickListener(this);
//        setOnTouchListener(this);

        _arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(_strHintText)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout.simple_list_item_1, new String[]{_strHintText});
            _isFromInit = true;
            setAdapter(arrayAdapter);
        }

        Disposable touchDisposable = RxView.touches(this)
                .throttleFirst(NumberUtils.FROZEN_DURATION, TimeUnit.MILLISECONDS)
                .subscribe(motionEvent -> {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (null != _arrayAdapter) {

                            // Refresh content #6
                            // Change Start
                            // Description: The items were only set initially, not reloading the data in the
                            // spinner every time it is loaded with items in the adapter.
                            _items.clear();
                            for (int i = 0; i < _arrayAdapter.getCount(); i++) {
                                _items.add(_arrayAdapter.getItem(i));
                            }
                            // Change end.

                            _searchableListDialog.show(
                                    scanForActivity(_context).getSupportFragmentManager(),
                                    SearchableListDialog.class.getSimpleName()
                            );
                        }
                    }
                }, Timber::e);
        disposables.add(touchDisposable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disposables.dispose();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!_isFromInit) {
            _arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout.simple_list_item_1, new String[]{_strHintText});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }
        } else {
            _isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    public void setAdapterClean(SpinnerAdapter adapter) {
        _arrayAdapter = (ArrayAdapter) adapter;
        _isDirty = false;
        ArrayAdapter arrayAdapter = new ArrayAdapter(_context, android.R.layout.simple_list_item_1, new String[]{_strHintText});
        super.setAdapter(arrayAdapter);
    }

    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(_items.indexOf(item));
        if (!_isDirty) {
            _isDirty = true;
            setAdapter(_arrayAdapter);
            setSelection(_items.indexOf(item));
        }
    }

    public void updateStateAdapter() {
        _isDirty = true;
        setAdapter(_arrayAdapter);
    }

    public void setTitle(String strTitle) {
        _searchableListDialog.setTitle(strTitle);
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText);
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText, onClickListener);
    }

    public void setOnSearchTextChangedListener(SearchableListDialog.OnSearchTextChanged onSearchTextChanged) {
        _searchableListDialog.setOnSearchTextChangedListener(onSearchTextChanged);
    }

    private AppCompatActivity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (AppCompatActivity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}