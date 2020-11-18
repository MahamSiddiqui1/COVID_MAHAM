package com.akdndhrc.covid_module.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akdndhrc.covid_module.CustomClass.Customer;
import com.akdndhrc.covid_module.R;

import java.util.ArrayList;


public class CustomerAdapter extends ArrayAdapter<Customer> {
    ArrayList<Customer> customers, tempCustomer, suggestions;

    public CustomerAdapter(Context context, ArrayList<Customer> objects) {
        super(context, R.layout.spinner_jins_layout, R.id.tvCustomer, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<Customer>(objects);
        this.suggestions = new ArrayList<Customer>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, null);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        Customer customer = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_jins_layout, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_jins_layout, parent, false);
        }
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.tvCustomer);
        ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.ivCustomerImage);
        View view = (View) convertView.findViewById(R.id.line);
        if (txtCustomer != null)
            txtCustomer.setText(customer.getFirstName() + " ");

        if (ivCustomerImage != null)
            ivCustomerImage.setImageResource(customer.getProfilePic());


        // Now assign alternate color for rows

        return convertView;
    }
}