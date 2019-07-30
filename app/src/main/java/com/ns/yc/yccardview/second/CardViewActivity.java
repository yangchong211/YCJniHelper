package com.ns.yc.yccardview.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.ns.yc.yccardview.R;

public class CardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        final CardView cardView = (CardView) findViewById(R.id.card_view);
        ((SeekBar) findViewById(R.id.seek_elevation)).setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                super.onProgressChanged(seekBar, progress, fromUser);
                cardView.setCardElevation(progress);
            }
        });
        ((SeekBar) findViewById(R.id.seek_padding)).setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                super.onProgressChanged(seekBar, progress, fromUser);
                cardView.setContentPadding(progress, progress, progress, progress);
            }
        });
        ((SeekBar) findViewById(R.id.seek_margin)).setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                super.onProgressChanged(seekBar, progress, fromUser);
                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(progress, progress, progress, progress);
                    cardView.requestLayout();
                }
            }
        });
    }

}
