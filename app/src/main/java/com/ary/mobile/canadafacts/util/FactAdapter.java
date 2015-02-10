package com.ary.mobile.canadafacts.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ary.mobile.canadafacts.R;
import com.ary.mobile.canadafacts.http.Communicator;
import com.ary.mobile.canadafacts.model.Fact;

/**
 * Created by gamiprashant on 10/02/2015.
 *
 * Array Adapter for ListView of facts
 */
public class FactAdapter extends ArrayAdapter<Fact> {

    public static final String TAG = "FactAdapter";

    //////////////////////////////////////////////////////////////////
    //Class for View Holder Pattern
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    static class ViewHolder {
        TextView textTitle;
        TextView textDescription;
        NetworkImageView imageItem;
    }

    private Context mContext;
    protected LayoutInflater mInflater;


    //////////////////////////////////////////////////////////////////
    public FactAdapter(Context context) {
        super(context, R.layout.item_fact);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //////////////////////////////////////////////////////////////////
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "position=" + position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fact, parent, false);
            holder = new ViewHolder();
            holder.textTitle = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.textDescription = (TextView) convertView.findViewById(R.id.itemDescription);
            holder.imageItem = (NetworkImageView) convertView.findViewById(R.id.itemImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Fact fact= getItem(position);
        holder.textTitle.setText(fact.title);
        holder.textDescription.setText(fact.description);
        //If Image url is not empty, show the image, else hide it
        if(!(fact.imageUrl == null || fact.imageUrl.isEmpty())) {

            //We are using Volley framework which will take care of image loading and caching
            //As we are setting the network image here, it will only get called when view is requested
            Log.d(TAG, "Image URL = " + fact.imageUrl);
            holder.imageItem.setVisibility(View.VISIBLE);
            holder.imageItem.setDefaultImageResId(R.mipmap.placeholder);
            holder.imageItem.setImageUrl(fact.imageUrl,
                    Communicator.getInstance(mContext).getImageLoader());
        } else {
            holder.imageItem.setVisibility(View.GONE);
        }

        return convertView;
    }

}
