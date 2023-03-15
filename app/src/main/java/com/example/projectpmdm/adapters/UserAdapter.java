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
import com.example.projectpmdm.models.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private static class ViewHolder {
        TextView name;
        TextView tlf;
    }

    Activity context;
    List<User> users;

    public UserAdapter(@NonNull Activity context, int resource, @NonNull List<User> users) {
        super(context, resource, users);

        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.user_row, null);

            holder = new ViewHolder();
            holder.name = row.findViewById(R.id.txtProductName);
            holder.tlf = row.findViewById(R.id.txtClientTlf);

            row.setTag(holder);
        } else
            holder = (ViewHolder) row.getTag();

        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.tlf.setText(user.getTlf());
        return row;
    }
}
