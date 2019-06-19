package com.korlab.foodex.delivery.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.korlab.foodex.delivery.Data.Address;
import com.korlab.foodex.delivery.Data.Name;
import com.korlab.foodex.delivery.Data.Task;
import com.korlab.foodex.delivery.MainActivity;
import com.korlab.foodex.delivery.R;
import com.korlab.foodex.delivery.Technical.Helper;

import java.util.Date;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    Context mContext;

    private static class ViewHolder {
        CardView card;
        TextView name;
        TextView phone;
        TextView startTime, endTime;
        TextView startAddress, endAddress;
        ImageView startImage, endImage;
        LinearLayout dateFiller, addressFiller;
        ImageView dateFillerDot, addressFillerDot;
        ImageView plus, minus;
        TextView counterBag;
        ImageView done;
        TextView textDelivered, textNotDelivered;
        TextView markText;

        RadioGroup radioGroup;
        RadioButton radioPackage;
        RadioButton radioBag;
    }

    public TaskAdapter(List<Task> data, Context context) {
        super(context, R.layout.component_task_card, data);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task item = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.component_task_card, parent, false);
            viewHolder.card = convertView.findViewById(R.id.card);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.phone = convertView.findViewById(R.id.phone);
            viewHolder.startTime = convertView.findViewById(R.id.start_time);
            viewHolder.endTime = convertView.findViewById(R.id.end_time);
            viewHolder.startAddress = convertView.findViewById(R.id.start_address);
            viewHolder.endAddress = convertView.findViewById(R.id.end_address);
            viewHolder.startImage = convertView.findViewById(R.id.start_image);
            viewHolder.endImage = convertView.findViewById(R.id.end_image);
            viewHolder.dateFiller = convertView.findViewById(R.id.date_filler);
            viewHolder.dateFillerDot = convertView.findViewById(R.id.date_filler_dot);
            viewHolder.addressFiller = convertView.findViewById(R.id.address_filler);
            viewHolder.addressFillerDot = convertView.findViewById(R.id.address_filler_dot);
            viewHolder.plus = convertView.findViewById(R.id.plus);
            viewHolder.minus = convertView.findViewById(R.id.minus);
            viewHolder.counterBag = convertView.findViewById(R.id.counter_bag);
            viewHolder.done = convertView.findViewById(R.id.done);
            viewHolder.textDelivered = convertView.findViewById(R.id.text_delivered);
            viewHolder.textNotDelivered = convertView.findViewById(R.id.text_not_delivered);
            viewHolder.markText = convertView.findViewById(R.id.mark);
            viewHolder.radioGroup = convertView.findViewById(R.id.input_radio);
            viewHolder.radioBag = convertView.findViewById(R.id.radio_bag);
            viewHolder.radioPackage = convertView.findViewById(R.id.radio_package);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initTask(item, viewHolder);
        viewHolder.plus.setOnClickListener(v -> {
            int newCount = item.getCountBags() + 1;
            item.setCountBags(newCount);
            saveState(item, viewHolder);
        });
        viewHolder.minus.setOnClickListener(v -> {
            int newCount = item.getCountBags() - 1;
            item.setCountBags(newCount);
            saveState(item, viewHolder);
        });
        viewHolder.done.setOnClickListener(v -> {
            item.setDone(!item.isDone());
            saveState(item, viewHolder);
        });
        viewHolder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_bag:
                    item.setType(Task.Type.BAG);
                    break;
                case R.id.radio_package:
                    item.setType(Task.Type.PACKAGE);
                    break;
            }
            saveState(item, viewHolder);
        });
        return convertView;
    }

    private void saveState(Task item, ViewHolder viewHolder) {
        // todo save
        initTask(item, viewHolder);
    }

    private void initTask(Task item, ViewHolder viewHolder) {
        Activity a = MainActivity.getInstance();
        Name name = item.getName();
        Address startAddress = item.getStartAddress();
        Address endAddress = item.getEndAddress();
        Date startTime = item.getStartTime();
        Date endTime = item.getEndTime();
        viewHolder.name.setText(name.getFullName());
        viewHolder.phone.setText(item.getPhone());
        viewHolder.startTime.setText(startTime.getHours() + ":" + startTime.getMinutes() + ", " + startTime.getDate() + ' ' + startTime.getMonth());
        viewHolder.endTime.setText(endTime.getHours() + ":" + endTime.getMinutes() + ", " + endTime.getDate() + ' ' + endTime.getMonth());
        viewHolder.startAddress.setText(startAddress.getStreet() + ' ' + startAddress.getHouse() + " ap. " + startAddress.getFlat());
        viewHolder.endAddress.setText(endAddress.getStreet() + ' ' + endAddress.getHouse() + " ap. " + endAddress.getFlat());

        Helper.log("item.getType().getType(): " + item.getType().getType());
        if (item.getType().getType().equals("Bag")) {
            viewHolder.radioBag.setChecked(true);
        } else if (item.getType().getType().equals("Package")) {
            viewHolder.radioPackage.setChecked(true);
        }

        Helper.log("item.isDone(): " + item.isDone());
        if (item.isDone()) {
            viewHolder.card.setCardBackgroundColor(a.getResources().getColor(R.color.colworCardInnactive));
            viewHolder.endImage.setImageDrawable(a.getDrawable(R.drawable.shipped_active));
            viewHolder.dateFiller.setBackgroundColor(a.getResources().getColor(R.color.colorPrimary));
            viewHolder.dateFillerDot.setColorFilter(a.getResources().getColor(R.color.colorPrimary));
            viewHolder.addressFiller.setBackgroundColor(a.getResources().getColor(R.color.colorPrimary));
            viewHolder.addressFillerDot.setColorFilter(a.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.card.setCardBackgroundColor(a.getResources().getColor(R.color.white));
            viewHolder.endImage.setImageDrawable(a.getDrawable(R.drawable.shipped));
            viewHolder.dateFiller.setBackgroundColor(a.getResources().getColor(R.color.colorInnactive));
            viewHolder.dateFillerDot.setColorFilter(a.getResources().getColor(R.color.colorInnactive));
            viewHolder.addressFiller.setBackgroundColor(a.getResources().getColor(R.color.colorInnactive));
            viewHolder.addressFillerDot.setColorFilter(a.getResources().getColor(R.color.colorInnactive));
        }
        viewHolder.counterBag.setText(item.getCountBags() + "");
        if(item.getCountBags() == item.getStartCountBags()) {
            viewHolder.counterBag.setTextColor(a.getResources().getColor(R.color.colorCountInnactive));
        } else if(item.getCountBags() > item.getStartCountBags()) {
            viewHolder.counterBag.setTextColor(a.getResources().getColor(R.color.green));
        } else {
            viewHolder.counterBag.setTextColor(a.getResources().getColor(R.color.red));
        }
        if (item.isDone()) {
            viewHolder.textDelivered.setVisibility(View.VISIBLE);
            viewHolder.textNotDelivered.setVisibility(View.GONE);
            viewHolder.done.setImageDrawable(a.getResources().getDrawable(R.drawable.undone));
            viewHolder.markText.setText("Mark as undelivered");
        } else {
            viewHolder.textDelivered.setVisibility(View.GONE);
            viewHolder.textNotDelivered.setVisibility(View.VISIBLE);
            viewHolder.done.setImageDrawable(a.getResources().getDrawable(R.drawable.done));
            viewHolder.markText.setText("Mark as delivered");
        }
    }
}