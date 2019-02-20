package com.m.m.moment.memorize.frame.manager;

import android.content.Context;
import android.graphics.Typeface;

public class TypefaceManager {

    Typeface typeface;
    Typeface typeface_bold;

    Typeface QuickTypeface;
    Typeface QuickTypeface_light;


    public TypefaceManager(Context context) {

        typeface = Typeface.createFromAsset(context.getAssets(), "typ2.otf");
        typeface_bold = Typeface.createFromAsset(context.getAssets(), "typ2_bold.otf");

        QuickTypeface = Typeface.createFromAsset(context.getAssets(), "quicksand_regular.otf");
        QuickTypeface_light = Typeface.createFromAsset(context.getAssets(), "quicksand_light.otf");

    }


    public Typeface getTypeNormal() {
        return typeface;
    }

    public Typeface getTypeBold() {
        return typeface_bold;
    }


    public Typeface getQuickTypeNormal() {
        return QuickTypeface;
    }


    public Typeface getQuickTypefaceLight() {
        return QuickTypeface_light;
    }
}
