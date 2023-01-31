package com.tv9news.shorts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tv9news.R;
import com.tv9news.shorts.interfaces.ShortsItemClick;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private Context context;
    private ShortsItemClick itemClick;

    public PagerAdapter(Context context, ShortsItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
    }

    @Override
    public int getCount() {
        return 100;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemView = mLayoutInflater.inflate(R.layout.video_short_row, container, false);
//        TextView numberTV = itemView.findViewById(R.id.number_tv);
//        numberTV.setText("number " + position);
        container.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.shortsClick("");
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }



}
