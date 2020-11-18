package com.akdndhrc.covid_module.Adapter.Adt_SyncImages;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akdndhrc.covid_module.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mani on 25/08/2017.
 */

public class Adt_SyncImages extends BaseAdapter {

    Context ctx;
    ArrayList<HashMap<String, String>> arrayList;
    LayoutInflater inflater;
    private final int THUMBSIZE = 106;


    public Adt_SyncImages(Context ctx, ArrayList<HashMap<String, String>> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.custom_lv_item_sync_images, null);
            holder.txt_vacine_name = (TextView) view.findViewById(R.id.txt_vacine_name);
            holder.iv_images = (ImageView) view.findViewById(R.id.iv_images);
            holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();

        }

        holder.iv_images.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(arrayList.get(position).get("image_location")), THUMBSIZE, THUMBSIZE));
        holder.txt_vacine_name.setText(arrayList.get(position).get("vaccine_name"));

       /* try {
            File file = new File(arrayList.get(position).get("image_location"));
            if (file.exists()) {
                Log.d("000666", "IF");
                // holder.iv_images.setImageBitmap(decodeFile(arrayList.get(position)));

            } else {
                Log.d("000666", "else");
                arrayList.get(position).remove("image_location");
                notifyDataSetChanged();
              //  holder.iv_images.setImageDrawable(ctx.getResources().getDrawable(R.drawable.no_image_100dp));
            }
        }catch (Exception e)
        {
            Log.d("000666", "Adtp_Error" + e.getMessage());
        }*/


        return view;
    }

    public static class ViewHolder {

        protected TextView txt_vacine_name;
        RelativeLayout rl;
        ImageView iv_images;
    }
}

