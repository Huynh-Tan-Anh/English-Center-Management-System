package com.example.studentmanagesystem.Schedule_Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.studentmanagesystem.R;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<ScheduleItem> {

    public ScheduleAdapter(Context context, List<ScheduleItem> scheduleItems) {
        super(context, 0, scheduleItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleItem scheduleItem = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtClassName = convertView.findViewById(R.id.txtClassName);
            viewHolder.txtStudentCount = convertView.findViewById(R.id.txtStudentCount);
            viewHolder.txtDay = convertView.findViewById(R.id.txtDay);
            viewHolder.txtTime = convertView.findViewById(R.id.txtTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtClassName.setText(scheduleItem.getClassName());
        viewHolder.txtStudentCount.setText(String.valueOf(scheduleItem.getStudentCount()));
        viewHolder.txtDay.setText(scheduleItem.getDay());
        viewHolder.txtTime.setText(scheduleItem.getTime());

        return convertView;
    }

    static class ViewHolder {
        TextView txtClassName;
        TextView txtStudentCount;
        TextView txtDay;
        TextView txtTime;
    }
}
