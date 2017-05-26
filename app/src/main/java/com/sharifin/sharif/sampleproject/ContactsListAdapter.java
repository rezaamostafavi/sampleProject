package com.sharifin.sharif.sampleproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mostafavi on 5/26/2017.
 */

public class ContactsListAdapter extends RecyclerView.Adapter {

    private Context context;
    private JSONArray data;

    public ContactsListAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lst_item_contact, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        JSONObject object = data.optJSONObject(position);
        MyHolder myHolder = (MyHolder) holder;

        myHolder.tvName.setText(object.optString("name"));
        String mobiles = "Phones: ";
        final JSONArray phones = object.optJSONArray("phones");
        for (int i = 0; i < phones.length(); i++) {
            AppCompatButton btnPhone = new AppCompatButton(context);
            btnPhone.setText(phones.optString(i));
            final int finalI = i;
            btnPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phones.optString(finalI)));
                    context.startActivity(intent);
                }
            });
            myHolder.panelPhones.addView(btnPhone);
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.length() : 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout panelPhones;

        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            panelPhones = (LinearLayout) itemView.findViewById(R.id.panelMobile);
        }
    }
}
