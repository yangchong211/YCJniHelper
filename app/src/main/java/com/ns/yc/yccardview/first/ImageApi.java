package com.ns.yc.yccardview.first;

import com.ns.yc.yccardview.R;

import java.util.ArrayList;

/**
 * Created by PC on 2017/10/20.
 * 作者：PC
 */

public class ImageApi {


    static final int[] NarrowImage = {
            R.drawable.wall01,
            R.drawable.wall02,
            R.drawable.wall03,
            R.drawable.wall04,
            R.drawable.wall05,
            R.drawable.wall06,
            R.drawable.wall07,
            R.drawable.wall08,
            R.drawable.wall09,
            R.drawable.wall10,
            R.drawable.wall11,
            R.drawable.wall12
    };
    public static ArrayList<Integer> getNarrowImage(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < NarrowImage.length; i++) {
            arrayList.add(NarrowImage[i]);
        }
        return arrayList;
    }

}
