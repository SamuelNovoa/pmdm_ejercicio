package com.example.projectpmdm.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectpmdm.R;
import com.example.projectpmdm.models.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private static class ViewHolder {
        TextView name;
    }

    Activity context;
    List<Product> products;

    public ProductAdapter(@NonNull Activity context, int resource, @NonNull List<Product> products) {
        super(context, resource, products);

        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.product_row, null);

            holder = new ViewHolder();
            holder.name = row.findViewById(R.id.txtProductName);

            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();

        Product product = products.get(position);
        holder.name.setText(product.getName());
        return row;
    }
}
