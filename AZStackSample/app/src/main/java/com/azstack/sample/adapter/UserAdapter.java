package com.azstack.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.azstack.sample.R;
import com.azstack.sample.model.User;

import java.util.List;

/**
 * Created by luannguyen on 11/5/2015.
 */
public class UserAdapter extends BaseAdapter {

    private List<User> data;
    private LayoutInflater mLayoutInflater;

    public UserAdapter(Context context, List<User> users) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        data = users;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        User user = data.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.user_row, null);
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String name = user.getName();
        holder.tvName.setText(name);
        return view;
    }

    private class ViewHolder {
        TextView tvName;
    }
}
