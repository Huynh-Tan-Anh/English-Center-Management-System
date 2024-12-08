package com.example.studentmanagesystem.Chat_Activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentmanagesystem.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<String> {

    public MessageAdapter(Context context, List<String> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        return convertView;
    }
}

