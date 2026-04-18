package com.axys.redeflexmobile.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao.viana on 20/01/2017.
 */

public class GenericaDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    private List<GenericaItem> items = new ArrayList<>();
    private ArrayAdapter<GenericaItem> listAdapter;
    private ListView listView;
    private SearchableItem<GenericaItem> searchableItem;
    private OnSearchTextChanged onSearchTextChanged;
    private SearchView searchView;
    private String titulo;
    private String positiveButtonText;
    private DialogInterface.OnClickListener onClickListener;

    public static GenericaDialog newInstance(List<GenericaItem> items) {
        GenericaDialog multiSelectExpandableFragment = new GenericaDialog();
        multiSelectExpandableFragment.setItems(items);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        List<Fragment> frags = manager.getFragments();
        Optional<Fragment> fragmento = Stream.of(frags)
                .filter(frag -> frag.getTag() != null && frag.getTag().equals(tag))
                .findFirst();

        if (fragmento.isEmpty() || !fragmento.get().isAdded()) {
            super.show(manager, tag);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        String strPositiveButton = positiveButtonText == null ? "OK" : positiveButtonText;
        alertDialog.setPositiveButton(strPositiveButton, onClickListener);

        String strTitle = titulo == null ? "Selecionar item" : titulo;
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setTitle(String strTitle) {
        titulo = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        positiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        positiveButtonText = strPositiveButtonText;
        this.onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem<GenericaItem> searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        searchView = rootView.findViewById(R.id.search);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

        listView = rootView.findViewById(R.id.listItems);

        listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(listAdapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            GenericaItem temp = (GenericaItem) parent.getAdapter().getItem(position);
            if (temp != null) {
                searchableItem.onSearchableItemClicked(temp);
                getDialog().dismiss();
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            ((ArrayAdapter) listView.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) listView.getAdapter()).getFilter().filter(s);
        }
        if (null != onSearchTextChanged) {
            onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    private void setItems(List<GenericaItem> items) {
        this.items = items;
    }

    public interface SearchableItem<GenericaItem> {
        void onSearchableItemClicked(GenericaItem item);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }

    public interface GenericaItem {

        String getId();

        String getDescricao();
    }
}