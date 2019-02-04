package com.aryan.android.kangaroocust;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin1 on 13/3/18.
 */

public class HotelImageAdapter extends RecyclerView.Adapter<HotelImageAdapter.HotelImageViewHolder> {

    private List<String> mHotelImages;
    private LayoutInflater mInflater;
    private Context context;

    public class HotelImageViewHolder extends RecyclerView.ViewHolder {
        final HotelImageAdapter mAdapter;
        public final ImageView hotelImageView;
        public HotelImageViewHolder(View itemView,HotelImageAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            hotelImageView = (ImageView) itemView.findViewById(R.id.hotel_image_view);
        }
    }

    public HotelImageAdapter(Context context, List<String> mHotelImages) {
        mInflater = LayoutInflater.from(context);
        this.mHotelImages = mHotelImages;
        this.context = context;
    }
    @Override
    public HotelImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.hotel_images_list_item, parent, false);
        return new HotelImageAdapter.HotelImageViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(HotelImageViewHolder holder, int position) {
        final String mCurrentHotelImage = mHotelImages.get(position);
       // holder.hotelImageView.setImageResource(mCurrentHotelImage);
        Picasso.get().load(mCurrentHotelImage).into(holder.hotelImageView);
    }

    @Override
    public int getItemCount() {
        return mHotelImages.size();
    }


}
