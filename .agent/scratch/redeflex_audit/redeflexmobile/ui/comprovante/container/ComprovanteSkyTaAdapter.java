package com.axys.redeflexmobile.ui.comprovante.container;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ComprovanteSkyTaAdapter extends ArrayAdapter<Cliente> {

    private final Context context;
    private final int resource;

    private final List<Cliente> original;
    private final List<Cliente> filtrada;

    public ComprovanteSkyTaAdapter(@NonNull Context context,
                                   int resource,
                                   @NonNull List<Cliente> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.original = new ArrayList<>(objects);
        this.filtrada = new ArrayList<>(objects);
    }

    @Override
    public int getCount() {
        return filtrada.size();
    }

    @Nullable
    @Override
    public Cliente getItem(int position) {
        return filtrada.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tv = view.findViewById(R.id.item_comprovante_sky_ta_tv_pesquisa);

        Cliente cliente = getItem(position);
        tv.setText(cliente == null ? "" : getDescriptionValue(cliente));

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private final Filter filter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Cliente c = (Cliente) resultValue;
            return getDescriptionValue(c);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null) {
                results.values = new ArrayList<>(original);
                results.count = original.size();
                return results;
            }

            String query = constraint.toString().toLowerCase().trim();
            if (query.length() < 1) {
                results.values = new ArrayList<>(original);
                results.count = original.size();
                return results;
            }

            List<Cliente> sugestoes = new ArrayList<>();

            for (Cliente c : original) {
                String nome = safeLower(c.getNomeFantasia());
                String sgv  = safeLower(c.getCodigoSGV());

                if (nome.contains(query) || sgv.contains(query)) {
                    sugestoes.add(c);
                }
            }

            results.values = sugestoes;
            results.count = sugestoes.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtrada.clear();
            if (results != null && results.values != null) {
                filtrada.addAll((List<Cliente>) results.values);
            }
            notifyDataSetChanged();
        }
    };

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private static String getDescriptionValue(Cliente c) {
        String nome = c.getNomeFantasia() == null ? "" : c.getNomeFantasia();
        String sgv  = c.getCodigoSGV() == null ? "" : c.getCodigoSGV();
        return nome + " - " + sgv;
    }
}

