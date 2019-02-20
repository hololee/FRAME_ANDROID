package com.m.m.moment.memorize.frame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.m.m.moment.memorize.frame.R;
import com.m.m.moment.memorize.frame.manager.TypefaceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HelpFragment extends Fragment {

    private final static int PAGE0 = 0;
    private final static int PAGE1 = 1;
    private final static int PAGE2 = 2;
    private final static int PAGE3 = 3;
    private final static int PAGE4 = 4;
    private final static int PAGE5 = 5;

    TypefaceManager typefaceManager;
    int pageNumber;
    Context mContext;


    public HelpFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        View help = null;
        TextView title;
        TextView sub_title;
        ImageView helper;
        ImageView background;
        typefaceManager = new TypefaceManager(mContext);



        switch (pageNumber) {
            case PAGE0:
                help = inflater.inflate(R.layout.fragment_help, container, false);
                title = help.findViewById(R.id.help_title);
                sub_title = help.findViewById(R.id.help_subtitle);
                helper = help.findViewById(R.id.help_image);
                background = help.findViewById(R.id.back_img);

                title.setTypeface(typefaceManager.getTypeNormal());
                sub_title.setTypeface(typefaceManager.getTypeNormal());


                //시작페이지.
                title.setText("");
                sub_title.setText("");
                helper.setVisibility(View.GONE);
                background.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(R.drawable.help1).apply(new RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(background);

                break;
            case PAGE1:
                help = inflater.inflate(R.layout.fragment_help, container, false);
                title = help.findViewById(R.id.help_title);
                sub_title = help.findViewById(R.id.help_subtitle);
                helper = help.findViewById(R.id.help_image);
                background = help.findViewById(R.id.back_img);

                title.setTypeface(typefaceManager.getTypeNormal());
                sub_title.setTypeface(typefaceManager.getTypeNormal());


                //등록페이지.
                title.setText(getString(R.string.help1_title));
                sub_title.setText(getString(R.string.help1_subtitle));
                background.setVisibility(View.GONE);
                helper.setVisibility(View.VISIBLE);
                Glide.with(mContext).asGif().load(R.drawable.add_g).apply(new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(helper);
                break;
            case PAGE2:
                help = inflater.inflate(R.layout.fragment_help, container, false);
                title = help.findViewById(R.id.help_title);
                sub_title = help.findViewById(R.id.help_subtitle);
                helper = help.findViewById(R.id.help_image);
                background = help.findViewById(R.id.back_img);

                title.setTypeface(typefaceManager.getTypeNormal());
                sub_title.setTypeface(typefaceManager.getTypeNormal());


                //수정 및 삭제.
                title.setText(getString(R.string.help2_title));
                sub_title.setText(getString(R.string.help2_subtitle));
                background.setVisibility(View.GONE);
                helper.setVisibility(View.VISIBLE);
                Glide.with(mContext).asGif().load(R.drawable.move_g).apply(new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(helper);
                break;
            case PAGE3:
                help = inflater.inflate(R.layout.fragment_help, container, false);
                title = help.findViewById(R.id.help_title);
                sub_title = help.findViewById(R.id.help_subtitle);
                helper = help.findViewById(R.id.help_image);
                background = help.findViewById(R.id.back_img);

                title.setTypeface(typefaceManager.getTypeNormal());
                sub_title.setTypeface(typefaceManager.getTypeNormal());


                //바로가기, 뒤로가기.
                title.setText(getString(R.string.help3_title));
                sub_title.setText(getString(R.string.help3_subtitle));
                background.setVisibility(View.GONE);
                helper.setVisibility(View.VISIBLE);
                Glide.with(mContext).asGif().load(R.drawable.edit_g).apply(new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(helper);
                break;
            case PAGE4:
                help = inflater.inflate(R.layout.fragment_help, container, false);
                title = help.findViewById(R.id.help_title);
                sub_title = help.findViewById(R.id.help_subtitle);
                helper = help.findViewById(R.id.help_image);
                background = help.findViewById(R.id.back_img);

                title.setTypeface(typefaceManager.getTypeNormal());
                sub_title.setTypeface(typefaceManager.getTypeNormal());


                //공유하기.
                title.setText(getString(R.string.help4_title));
                sub_title.setText(getString(R.string.help4_subtitle));
                background.setVisibility(View.GONE);
                helper.setVisibility(View.VISIBLE);
                Glide.with(mContext).asGif().load(R.drawable.share_g).apply(new RequestOptions().fitCenter().override(Target.SIZE_ORIGINAL).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(helper);
                break;
            case PAGE5:
                //구매 소개.
                help = inflater.inflate(R.layout.fragment_help2, container, false);
                ImageView intro1 = help.findViewById(R.id.intro1);
                //set intro_img image.
                Glide.with(mContext).load(R.drawable.intro_img).apply(new RequestOptions().override(1080,6986).centerInside().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(intro1);
                break;

        }


        return help;
    }

    public void initPage(int pageNumber) {

        this.pageNumber = pageNumber;
    }
}
