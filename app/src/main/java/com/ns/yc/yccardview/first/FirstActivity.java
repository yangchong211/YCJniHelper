package com.ns.yc.yccardview.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ns.yc.yccardview.R;
import com.ns.yc.yccardviewlib.card.CardViewLayout;

import java.util.ArrayList;

/**
 * Created by PC on 2017/10/20.
 * 作者：PC
 */

public class FirstActivity extends AppCompatActivity {

    private CardViewLayout cardView;
    private ArrayList<Integer> image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        init();
    }

    private void init() {
        cardView = (CardViewLayout) findViewById(R.id.cardView);
        initDataList();
        initCardView();
    }

    private void initDataList() {
        image = ImageApi.getNarrowImage();
    }

    private void initCardView() {

        cardView.setAdapter(new CardViewLayout.Adapter() {

            class ViewHolder {
                ImageView imageView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_card_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
                    view.setTag(viewHolder);
                }
                viewHolder.imageView.setImageResource(image.get(position));
            }

            @Override
            public int getItemCount() {
                return image.size();
            }

            @Override
            public void displaying(int position) {

            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                Toast.makeText(FirstActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
