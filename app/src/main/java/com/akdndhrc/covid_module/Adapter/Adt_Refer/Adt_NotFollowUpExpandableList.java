package com.akdndhrc.covid_module.Adapter.Adt_Refer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akdndhrc.covid_module.LHW_App.LHW_NavigationActivities.LHW_ReferActivities.NotFollowUp_Fragment;
import com.akdndhrc.covid_module.LHW_App.LHW_Navigation_Activity;
import com.akdndhrc.covid_module.DatabaseFiles.Lister;
import com.akdndhrc.covid_module.R;
import com.akdndhrc.covid_module.Utils;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Adt_NotFollowUpExpandableList extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<String>> _listDataChild;

    EditText et_record_date, et_refusal, et_Others;
    Button btn_jamaa_kre;
    CheckBox checkbox_refusal, checkbox_not_available, checkbox_service_not_avail, checkbox_service_avail;
    LinearLayout ll_service_avail, ll_service_not_avail;
    Spinner sp_facility;

    String monthf, dayf, yearf = "null";
    private int mYear, mMonth, mDay;
    //String[] data;

    String cb_service_avail = "0", cb_service_not_avail = "0", cb_not_available = "0", cb_refusal = "0", facility_uid = "0", referal_reason = "none";

    Dialog alertDialog, progressDialog;
    NotFollowUp_Fragment notFollowUp_fragment;


    public Adt_NotFollowUpExpandableList(Context context, List<String> listDataHeader,
                                         HashMap<String, ArrayList<String>> listChildData, NotFollowUp_Fragment notFollowUp_fragment) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.notFollowUp_fragment = notFollowUp_fragment;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final ViewHolder holder;
        final String childText = (String) getChild(groupPosition, childPosition);


        Log.d("000999", "Name: " + childText);
        /*Log.d("000999","Gender: "+childText.split("@")[2]);
        Log.d("000999","Age: "+childText.split("@")[3]);*/

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_lv_not_followup_child_layout, null);

            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvReferalReason = (TextView) convertView.findViewById(R.id.tvReferalReason);

            holder.btnFollowUp = (Button) convertView.findViewById(R.id.btnFollowUp);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        // data = childText.split("@");

//        holder.hp_lv_tv_inside_outside.setText(childText.split("@")[0]);
        holder.tvName.setText(childText.split("@")[0]);
        holder.tvTime.setText(childText.split("@")[1]);
        holder.tvDate.setText(childText.split("@")[2]);
        holder.tvReferalReason.setText(childText.split("@")[3]);


        holder.btnFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   Log.d("000700", "AlL Data: " + childText);
                Log.d("000700", "Name: " + childText.split("@")[0]);
                Log.d("000700", "Time: " + childText.split("@")[1]);
                Log.d("000700", "Date: " + childText.split("@")[2]);
              /*  Log.d("000700", "ReferalReason: " + childText.split("@")[3]);
                Log.d("000700", "Form: " + childText.split("@")[4]);
                Log.d("000700", "Member_UID: " + childText.split("@")[5]);
                Log.d("000700", "ReferalFacility: " + childText.split("@")[6]);
                Log.d("000700", "Added_on: " + childText.split("@")[7]);
                Log.d("000700", "Type: " + childText.split("@")[8]);
                Log.d("000700", "JSON DATA: " + childText.split("@")[9]);*/


              alertDialog = new Dialog(_context);
                LayoutInflater layout = LayoutInflater.from(_context);
                final View dialogView = layout.inflate(R.layout.dialog_refer_not_followup_layout, null);

                alertDialog.setContentView(dialogView);
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                //ImageVIew
                ImageView iv_close = dialogView.findViewById(R.id.iv_close);
                et_record_date = dialogView.findViewById(R.id.et_record_date);
                et_Others = dialogView.findViewById(R.id.et_Others);
                et_refusal = dialogView.findViewById(R.id.et_refusal);

                //CheckBox
                checkbox_service_avail = dialogView.findViewById(R.id.checkbox_service_avail);
                checkbox_service_not_avail = dialogView.findViewById(R.id.checkbox_service_not_avail);
                checkbox_not_available = dialogView.findViewById(R.id.checkbox_not_available);
                checkbox_refusal = dialogView.findViewById(R.id.checkbox_refusal);

                //Spinner
                sp_facility = dialogView.findViewById(R.id.sp_facility);

                //LinearLayout
                ll_service_avail = dialogView.findViewById(R.id.ll_service_avail);
                ll_service_not_avail = dialogView.findViewById(R.id.ll_service_not_avail);

                //Button
                btn_jamaa_kre = dialogView.findViewById(R.id.submit);


                spinner_data();


                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                et_record_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowDateDialog();
                    }
                });


                checkbox_service_avail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkbox_service_avail.isChecked()) {
                            ll_service_avail.setVisibility(View.VISIBLE);
                            ll_service_not_avail.setVisibility(View.GONE);
                            cb_service_avail = "1";

                            checkbox_service_not_avail.setChecked(false);
                            cb_service_not_avail = "0";

                            referal_reason = "service avail";
                            Log.d("000700", "IF CB 1: " + cb_service_avail);
                            Log.d("000700", "IF CB 11: " + cb_service_not_avail);

                        } else {
                            ll_service_avail.setVisibility(View.GONE);
                            cb_service_avail = "0";
                            Log.d("000700", "ELSE CB 1: " + cb_service_avail);
                            Log.d("000700", "ELSE CB 11: " + cb_service_not_avail);
                        }
                    }
                });

                checkbox_service_not_avail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkbox_service_not_avail.isChecked()) {
                            ll_service_not_avail.setVisibility(View.VISIBLE);
                            ll_service_avail.setVisibility(View.GONE);
                            cb_service_not_avail = "1";

                            checkbox_service_avail.setChecked(false);
                            cb_service_avail = "0";

                            Log.d("000700", "IF CB 2: " + cb_service_not_avail);
                            Log.d("000700", "IF CB 22: " + cb_service_avail);
                        } else {
                            ll_service_not_avail.setVisibility(View.GONE);
                            cb_service_not_avail = "0";
                            Log.d("000700", " ELSE CB 2: " + cb_service_not_avail);
                            Log.d("000700", " ELSE CB 22: " + cb_service_avail);
                        }
                    }
                });


                checkbox_not_available.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkbox_not_available.isChecked()) {
                            cb_not_available = "1";
                            cb_refusal = "0";
                            Log.d("000700", "CB 3: " + cb_not_available);

                            referal_reason = "service not available";
                            checkbox_refusal.setChecked(false);
                            et_refusal.setVisibility(View.GONE);
                        } else {
                            cb_not_available = "0";
                            et_refusal.setVisibility(View.GONE);
                            Log.d("000700", "CB 3: " + cb_not_available);
                        }
                    }
                });

                checkbox_refusal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkbox_refusal.isChecked()) {
                            et_refusal.setVisibility(View.VISIBLE);
                            cb_refusal = "1";
                            cb_not_available = "0";
                            Log.d("000700", "CB 4: " + cb_refusal);

                            referal_reason = "service refusal";
                            checkbox_not_available.setChecked(false);

                        } else {
                            et_refusal.setVisibility(View.GONE);
                            cb_refusal = "0";
                            Log.d("000700", "CB 4: " + cb_refusal);
                        }
                    }
                });


                btn_jamaa_kre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //    ((Activity)v.getContext()).recreate();


                        if (et_record_date.getText().toString().isEmpty()) {
                            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی تاریخ اندراج  درج کریں.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(v.getResources().getColor(android.R.color.black));
                            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(15);
                            snackbar.setDuration(3000);
                            snackbar.show();
                            return;
                        }

                        if (!checkbox_service_avail.isChecked() && !checkbox_service_not_avail.isChecked()) {

                            // Toast.makeText(_context, "برائے مہربانی سروس چیک باکس کو منتخب کریں", Toast.LENGTH_SHORT).show();
                            final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی سروس چیک باکس کو منتخب کریں.", Snackbar.LENGTH_SHORT);
                            View mySbView = snackbar.getView();
                            mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                            mySbView.setBackgroundColor(v.getResources().getColor(android.R.color.black));
                            TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(15);
                            snackbar.setDuration(3000);
                            snackbar.show();

                            return;
                        }

                        if (cb_service_avail.equalsIgnoreCase("1")) {

                            if (sp_facility.getSelectedItemPosition() == 0) {
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(v.getResources().getColor(android.R.color.black));
                                TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(15);
                                snackbar.setDuration(3000);
                                snackbar.show();
                                return;
                            } else {

                            }
                        } else if (cb_service_not_avail.equalsIgnoreCase("1")) {

                            if (!checkbox_not_available.isChecked() && !checkbox_refusal.isChecked()) {
                                final Snackbar snackbar = Snackbar.make(v, "برائے مہربانی سروس دستیاب نہیں کے چیک باکس کو منتخب کریں.", Snackbar.LENGTH_SHORT);
                                View mySbView = snackbar.getView();
                                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                mySbView.setBackgroundColor(v.getResources().getColor(android.R.color.black));
                                TextView textView = (TextView) mySbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTextSize(15);
                                snackbar.setDuration(3000);
                                snackbar.show();
                                return;
                            } else {
                                // Toast.makeText(_context, "B", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //  Toast.makeText(_context, "C", Toast.LENGTH_SHORT).show();
                        }
                        if (sp_facility.getSelectedItemPosition() != 20) {
                            et_Others.setText("none");
                        }

                        alertDialog.dismiss();

                        progressDialog = new Dialog(_context);
                        LayoutInflater layout = LayoutInflater.from(_context);
                        final View dialogView = layout.inflate(R.layout.lay_dialog_loading3, null);

                        progressDialog.setContentView(dialogView);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        progressDialog.show();


                        ReferalFormSubmit(childText);

                    }
                });

            }
        });


        return convertView;
    }

    private void spinner_data() {

        try {

            Utils.setSpinnerHealthFacilityReferred(_context, sp_facility);
            sp_facility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (sp_facility.getSelectedItemPosition() > 0) {
                        try {
                            Lister ls = new Lister(_context);
                            ls.createAndOpenDB();

                            String[][] mData = ls.executeReader("SELECT uid from FACILITY where name ='" + sp_facility.getSelectedItem() + "'");
                            Log.d("000700", "UID: " + mData[0][0]);
                            facility_uid = mData[0][0];
                            Log.d("000700", "Array: " + facility_uid);
                        } catch (Exception e) {
                            Log.d("000700", "Err: " + e.getMessage());
                        }

                    }

                    if (sp_facility.getSelectedItemPosition() == 20) {
                        et_Others.setVisibility(View.VISIBLE);
                    } else {
                        et_Others.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Log.d("0000999", "Exception: " + e.getMessage());
        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {

        //return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        List childList = _listDataChild.get(_listDataHeader.get(groupPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.size();
        }
        return 0;

    }


    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_stats_parent_list, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tvName);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        tv_count.setText(getGroup(groupPosition).toString().split("@")[0]);
        lblListHeader.setText(getGroup(groupPosition).toString().split("@")[1]);

        //lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class ViewHolder {
        TextView tvName, tvDate, tvTime, tvReferalReason;
        Button btnFollowUp;


    }

    public void ShowDateDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(_context, R.style.DatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        if (monthOfYear <= 8) {

                            monthf = "0" + String.valueOf(monthOfYear + 1);

                        } else {
                            monthf = String.valueOf(monthOfYear + 1);
                        }
                        if (dayOfMonth <= 9) {

                            dayf = "0" + String.valueOf(dayOfMonth);
                        } else {
                            dayf = String.valueOf(dayOfMonth);
                        }
                        yearf = String.valueOf(year);
                        //datetwo.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                        et_record_date.setText(yearf + "-" + monthf + "-" + dayf);

                        Log.d("000700", "IS Tareekh SY: " + et_record_date.getText().toString());

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();


    }

    public void ReferalFormSubmit(final String childText) {
        try {

            Lister ls = new Lister(_context);
            ls.createAndOpenDB();

            JSONObject jobj = new JSONObject();
            String added_on = String.valueOf(System.currentTimeMillis());

            //  jobj.put("referal_reason", "" + data[3]);

            if (childText.split("@")[3].equalsIgnoreCase("none")) {
                jobj.put("referal_reason", referal_reason);
            } else {
                jobj.put("referal_reason", "" + childText.split("@")[3]);
            }

            if (childText.split("@")[6].equalsIgnoreCase("none")) {
                jobj.put("referal_facility", "" + String.valueOf(sp_facility.getSelectedItem()));//spinner
            } else {
                jobj.put("referal_facility", "" + childText.split("@")[6]);//spinner
            }
            jobj.put("referal_type", "" + childText.split("@")[4]);//spinner
            jobj.put("updated_record_date", "" + et_record_date.getText().toString());
            jobj.put("cb_service_avail", cb_service_avail);
            jobj.put("facility_uid", facility_uid);
            jobj.put("sp_facility", sp_facility.getSelectedItem());
            jobj.put("sp_facility_pos", String.valueOf(sp_facility.getSelectedItemPosition() - 1));
            jobj.put("et_others", et_Others.getText().toString());
            jobj.put("cb_service_not_avail", cb_service_not_avail);
            jobj.put("cb_not_available", cb_not_available);
            jobj.put("cb_refusal", cb_refusal);
            jobj.put("et_refusal_reason", et_refusal.getText().toString());
            jobj.put("followup", "1");


            Log.d("000700", "JSON: " + jobj);
            Log.d("000700", "USERID: " + LHW_Navigation_Activity.login_useruid);

            String ans1 = "insert into REFERAL (member_uid, record_data, data,added_by, is_synced,added_on)" +
                    "values" +
                    "(" +
                    "'" + childText.split("@")[5] + "'," +
                    "'" + et_record_date.getText().toString() + "'," +
                    "'" + jobj + "'," +
                    "'" + LHW_Navigation_Activity.login_useruid + "'," +
                    "'0'," +
                    "'" + added_on + "'" +
                    ")";

            Boolean res = ls.executeNonQuery(ans1);
            Log.d("000700", "Data: " + ans1);
            Log.d("000700", "Query: " + res.toString());

            if (res == true) {
                update_data(jobj.getString("referal_reason"), jobj.getString("referal_facility"), childText);

                final Snackbar snackbar = Snackbar.make(NotFollowUp_Fragment.rootView, "ریفر ہوگیا ہے.", Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(_context.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(_context.getResources().getColorStateList(R.color.green_color));
                }
                snackbar.setDuration(4000);
                snackbar.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        ((NotFollowUp_Fragment) notFollowUp_fragment).prepareListData();
                    }
                }, 2000);

                //  ((Activity)v.getContext()).recreate();

            } else {
                progressDialog.dismiss();
                alertDialog.show();
                final Snackbar snackbar = Snackbar.make(NotFollowUp_Fragment.rootView, "ریفر نہیں ہوا.", Snackbar.LENGTH_SHORT);
                View mySbView = snackbar.getView();
                mySbView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mySbView.setBackgroundColor(_context.getResources().getColor(android.R.color.black));
                TextView textView = mySbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp, 0, 0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setCompoundDrawableTintList(_context.getResources().getColorStateList(R.color.hp_listview_textview_redcolor));
                }
                snackbar.setDuration(4000);
                snackbar.show();

            }

        } catch (Exception e) {
            progressDialog.dismiss();
            alertDialog.show();
            Log.d("000700", "Err: " + e.getMessage());
            Toast.makeText(_context, "Something wrong !!", Toast.LENGTH_SHORT).show();
        }

    }

    private void update_data(String referal_reason, String referal_facility, String childText) {

        if (childText.split("@")[8].equalsIgnoreCase("0")) {
            try {

                Lister ls = new Lister(_context);
                ls.createAndOpenDB();

                JSONObject jobj = new JSONObject(childText.split("@")[9]);

                if (jobj.has("referal_reason")) {
                    jobj.put("referal_reason", "" + referal_reason);
                    jobj.put("referal_facility", "" + referal_facility);
                }

                String update_record = "UPDATE CBEMARI SET " +
                        "data='" + jobj.toString() + "'" +
                        "WHERE member_uid = '" + childText.split("@")[5] + "' AND record_data='" + childText.split("@")[2] + "'AND added_on='" + childText.split("@")[7] + "' ";

                ls.executeNonQuery(update_record);

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000700", "Updated BEMARI FORM: " + update_record);
                Log.d("000700", "Query: " + res.toString());

                //  Toast.makeText(_context, "Successfully UPDATE ..", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("000700", "Error:: " + e.getMessage());
                Toast.makeText(_context, "Something wrong to update table !!", Toast.LENGTH_SHORT).show();
            }

        } else if (childText.split("@")[8].equalsIgnoreCase("1")) {
            try {

                Lister ls = new Lister(_context);
                ls.createAndOpenDB();

                JSONObject jobj = new JSONObject(childText.split("@")[9]);

                if (jobj.has("referal_reason")) {
                    jobj.put("referal_reason", "" + referal_reason);
                    jobj.put("referal_facility", "" + referal_facility);
                }

                String update_record = "UPDATE MBEMARI SET " +
                        "data='" + jobj.toString() + "'" +
                        "WHERE member_uid = '" + childText.split("@")[5] + "' AND record_data='" + childText.split("@")[2] + "'AND added_on='" + childText.split("@")[7] + "' ";

                ls.executeNonQuery(update_record);

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000700", "Updated MBEMARI FORM: " + update_record);
                Log.d("000700", "Query: " + res.toString());

                // Toast.makeText(_context, "Successfully UPDATE ..", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("000700", "Error:: " + e.getMessage());
                Toast.makeText(_context, "Something wrong to update table !!", Toast.LENGTH_SHORT).show();
            }

        } else if (childText.split("@")[8].equalsIgnoreCase("2")) {
            try {

                Lister ls = new Lister(_context);
                ls.createAndOpenDB();

                JSONObject jobj = new JSONObject(childText.split("@")[9]);

                if (jobj.has("reason_refer")) {
                    jobj.put("reason_refer", "" + referal_reason);
                    jobj.put("facility_refer", "" + referal_facility);
                }

                String update_record = "UPDATE MFPLAN SET " +
                        "data='" + jobj.toString() + "'" +
                        "WHERE member_uid = '" + childText.split("@")[5] + "' AND record_data='" + childText.split("@")[2] + "'AND added_on='" + childText.split("@")[7] + "' ";

                ls.executeNonQuery(update_record);

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000700", "Updated MFPLAN FORM: " + update_record);
                Log.d("000700", "Query: " + res.toString());

                //  Toast.makeText(_context, "Successfully UPDATE ..", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("000700", "Error:: " + e.getMessage());
                Toast.makeText(_context, "Something wrong to update table !!", Toast.LENGTH_SHORT).show();
            }

        } else if (childText.split("@")[8].equalsIgnoreCase("3")) {
            try {

                Lister ls = new Lister(_context);
                ls.createAndOpenDB();

                JSONObject jobj = new JSONObject(childText.split("@")[9]);

                if (jobj.has("referal_reason")) {
                    jobj.put("referal_reason", "" + referal_reason);
                    jobj.put("referal_facility", "" + referal_facility);
                }

                String update_record = "UPDATE REFERAL SET " +
                        "data='" + jobj.toString() + "'" +
                        "WHERE member_uid = '" + childText.split("@")[5] + "' AND record_data='" + childText.split("@")[2] + "'AND added_on='" + childText.split("@")[7] + "' ";

                ls.executeNonQuery(update_record);

                Boolean res = ls.executeNonQuery(update_record);
                Log.d("000700", "Updated REFERAL FORM: " + update_record);
                Log.d("000700", "Query: " + res.toString());

                //  Toast.makeText(_context, "Successfully UPDATE ..", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Log.d("000700", "Error:: " + e.getMessage());
                Toast.makeText(_context, "Something wrong to update table !!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.d("000700", "ELSEEEEEEE");
        }

    }
}