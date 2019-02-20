package com.m.m.moment.memorize.frame.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.activity.ShareActivity;
import com.m.m.moment.memorize.frame.item.cardListItem;
import com.m.m.moment.memorize.frame.manager.DateManager;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

public class CardFragment extends Fragment {

    int pageNumber, totalNumber;
    Context mContext;
    cardListItem mItem;

    public CardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View card = inflater.inflate(R.layout.fragment_card, container, false);

        ImageView img = card.findViewById(R.id.img);
        TextView title = card.findViewById(R.id.title);
        TextView date = card.findViewById(R.id.date);
        TextView content = card.findViewById(R.id.content);
        ImageButton share = card.findViewById(R.id.share);

        TextView pageNumberMark = card.findViewById(R.id.page_number);

        //set Typeface.
        TypefaceManager typefaceManager = new TypefaceManager(mContext);
        title.setTypeface(typefaceManager.getTypeNormal());
        date.setTypeface(typefaceManager.getTypeNormal());
        content.setTypeface(typefaceManager.getTypeNormal());
        pageNumberMark.setTypeface(typefaceManager.getTypeNormal());

        //set item data.
        Glide.with(mContext).load(mItem.getImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(img);
        title.setText(mItem.getTitle());
        date.setText(DateManager.getDateText(mItem.getDate()));
        content.setText(mItem.getContent());


        //share item.
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ShareActivity.class);
                intent.putExtra("img", mItem.getImg().toString());
                intent.putExtra("title", mItem.getTitle());
                intent.putExtra("date", DateManager.getDateText(mItem.getDate()));
                intent.putExtra("content", mItem.getContent());
                startActivity(intent);
            }
        });


        //set page number.
        pageNumberMark.setText(String.valueOf(this.pageNumber - 1) + " / " + String.valueOf(this.totalNumber));


        return card;
    }

    public void initPage(cardListItem item, int pageNumber, int totalNumber) {
        this.pageNumber = pageNumber;
        this.totalNumber = totalNumber;
        this.mItem = item;
    }
}
