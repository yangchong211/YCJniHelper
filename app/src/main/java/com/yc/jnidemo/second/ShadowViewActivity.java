package com.yc.jnidemo.second;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.yc.jnidemo.R;
import com.yc.shadow.ShadowView;


public class ShadowViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_view);

        ShadowView shadow_view = (ShadowView) findViewById(R.id.shadow_view);
        shadow_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        RecyclerView view_recycler = (RecyclerView) findViewById(R.id.view_recycler);
        view_recycler.setLayoutManager(new LinearLayoutManager(this));
        view_recycler.setAdapter(new ShadowViewRecyclerAdapter(shadow_view));
    }

    private class ShadowViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ShadowView shadowCardView;

        public ShadowViewRecyclerAdapter(ShadowView shadowCardView) {
            this.shadowCardView = shadowCardView;
        }

        @Override
        public int getItemViewType(int position) {
            int viewType;
            switch (SeekItem.values()[position]) {
                case FOREGROUND_COLOR:
                case BACKGROUND_COLOR:
                case SHADOW_COLOR:
                    viewType = R.layout.list_item_color_select;
                    break;
                default:
                    viewType = R.layout.list_item_seek_select;
                    break;
            }
            return viewType;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == R.layout.list_item_color_select) {
                return new ShadowViewColorItemHolder(inflater.inflate(R.layout.list_item_color_select, parent, false));
            }
            return new ShadowViewSeekItemHolder(inflater.inflate(R.layout.list_item_seek_select, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (holder instanceof ShadowViewSeekItemHolder) {
                ((ShadowViewSeekItemHolder) holder).bind(SeekItem.values()[position], shadowCardView);
            } else if (holder instanceof ShadowViewColorItemHolder) {
                ((ShadowViewColorItemHolder) holder).bind(SeekItem.values()[position], shadowCardView);
                ((ShadowViewColorItemHolder) holder).onClickColor = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(position);
                    }
                };
            }
        }

        @Override
        public int getItemCount() {
            return SeekItem.values().length;
        }

        class ShadowViewSeekItemHolder extends RecyclerView.ViewHolder {

            private final TextView tvTitle;
            private final TextView tvValue;
            private final SeekBar seekBar;

            public ShadowViewSeekItemHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.text_title);
                tvValue = (TextView) itemView.findViewById(R.id.text_value);
                seekBar = (SeekBar) itemView.findViewById(R.id.seek_bar);
            }

            void bind(SeekItem seekItem, final ShadowView shadowCardView) {
                tvTitle.setText(seekItem.title);
                seekBar.setOnSeekBarChangeListener(null);
                switch (seekItem) {
                    case SHADOW_RADIUS:
                        tvValue.setText(String.valueOf((int) shadowCardView.getShadowRadius()));
                        seekBar.setProgress((int) shadowCardView.getShadowRadius());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowRadius(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case PADDING:
                        tvValue.setText(String.valueOf(shadowCardView.getPaddingLeft()));
                        seekBar.setProgress(shadowCardView.getPaddingLeft());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setPadding(progress, progress, progress, progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case SHADOW_MARGIN:
                        tvValue.setText(String.valueOf(shadowCardView.getShadowMarginLeft()));
                        seekBar.setProgress(shadowCardView.getShadowMarginLeft());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowMargin(progress, progress, progress, progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case SHADOW_MARGIN_LEFT:
                        tvValue.setText(String.valueOf(shadowCardView.getShadowMarginLeft()));
                        seekBar.setProgress(shadowCardView.getShadowMarginLeft());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowMarginLeft(progress);
                                tvValue.setText(String.valueOf(progress));
                                shadowCardView.requestLayout();
                            }
                        });
                        break;
                    case SHADOW_MARGIN_TOP:
                        tvValue.setText(String.valueOf(shadowCardView.getShadowMarginTop()));
                        seekBar.setProgress(shadowCardView.getShadowMarginTop());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowMarginTop(progress);
                                tvValue.setText(String.valueOf(progress));
                                shadowCardView.requestLayout();
                            }
                        });
                        break;
                    case SHADOW_MARGIN_RIGHT:
                        tvValue.setText(String.valueOf(shadowCardView.getShadowMarginRight()));
                        seekBar.setProgress(shadowCardView.getShadowMarginRight());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowMarginRight(progress);
                                tvValue.setText(String.valueOf(progress));
                                shadowCardView.requestLayout();
                            }
                        });
                        break;
                    case SHADOW_MARGIN_BOTTOM:
                        tvValue.setText(String.valueOf(shadowCardView.getShadowMarginBottom()));
                        seekBar.setProgress(shadowCardView.getShadowMarginBottom());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowMarginBottom(progress);
                                tvValue.setText(String.valueOf(progress));
                                shadowCardView.requestLayout();
                            }
                        });
                        break;
                    case CORNER_RADIUS:
                        tvValue.setText(String.valueOf((int) shadowCardView.getCornerRadiusTL()));
                        seekBar.setProgress((int) shadowCardView.getCornerRadiusTL());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setCornerRadius(progress, progress, progress, progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case CORNER_RADIUS_TOP_LEFT:
                        tvValue.setText(String.valueOf((int) shadowCardView.getCornerRadiusTL()));
                        seekBar.setProgress((int) shadowCardView.getCornerRadiusTL());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setCornerRadiusTL(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case CORNER_RADIUS_TOP_RIGHT:
                        tvValue.setText(String.valueOf((int) shadowCardView.getCornerRadiusTR()));
                        seekBar.setProgress((int) shadowCardView.getCornerRadiusTR());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setCornerRadiusTR(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case CORNER_RADIUS_BOTTOM_RIGHT:
                        tvValue.setText(String.valueOf((int) shadowCardView.getCornerRadiusBR()));
                        seekBar.setProgress((int) shadowCardView.getCornerRadiusBR());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setCornerRadiusBR(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case CORNER_RADIUS_BOTTOM_LEFT:
                        tvValue.setText(String.valueOf((int) shadowCardView.getCornerRadiusBL()));
                        seekBar.setProgress((int) shadowCardView.getCornerRadiusBL());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setCornerRadiusBL(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case SHADOW_DX:
                        tvValue.setText(String.valueOf((int) shadowCardView.getShadowDx()));
                        seekBar.setProgress((int) shadowCardView.getShadowDx());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowDx(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                    case SHADOW_DY:
                        tvValue.setText(String.valueOf((int) shadowCardView.getShadowDy()));
                        seekBar.setProgress((int) shadowCardView.getShadowDy());
                        seekBar.setOnSeekBarChangeListener(new OnSeekProgressChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                super.onProgressChanged(seekBar, progress, fromUser);
                                shadowCardView.setShadowDy(progress);
                                tvValue.setText(String.valueOf(progress));
                            }
                        });
                        break;
                        default:
                            break;
                }
            }
        }

        private class ShadowViewColorItemHolder extends RecyclerView.ViewHolder {

            private final TextView tvColorTitle;
            private final FlexboxLayout flexboxLayout;
            private View.OnClickListener onClickColor;

            public ShadowViewColorItemHolder(@NonNull View itemView) {
                super(itemView);
                tvColorTitle = (TextView) itemView.findViewById(R.id.text_color_title);
                flexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.view_flex);
            }

            void bind(SeekItem seekItem, final ShadowView shadowCardView) {
                tvColorTitle.setText(seekItem.title);
                LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                flexboxLayout.removeAllViews();
                switch (seekItem) {
                    case SHADOW_COLOR:
                        for (final ShadowColorEnum item : ShadowColorEnum.values()) {
                            View view = createView(inflater, item.ordinal());
                            view.setBackgroundColor(Color.parseColor(item.color));
                            view.setSelected(Color.parseColor(item.color) == shadowCardView.getShadowColor());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shadowCardView.setShadowColor(Color.parseColor(item.color));
                                    onClickColor.onClick(v);
                                }
                            });
                        }
                        break;
                    case FOREGROUND_COLOR:
                        for (final ForegroundColorEnum itemf : ForegroundColorEnum.values()) {
                            View view = createView(inflater, itemf.ordinal());
                            view.setBackgroundColor(Color.parseColor(itemf.color));
                            view.setSelected(Color.parseColor(itemf.color) == shadowCardView.getForegroundColor());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shadowCardView.setForegroundColor(Color.parseColor(itemf.color));
                                    onClickColor.onClick(v);
                                }
                            });
                        }
                        break;
                    case BACKGROUND_COLOR:
                        for (final BackgroundColorEnum itemb : BackgroundColorEnum.values()) {
                            View view = createView(inflater, itemb.ordinal());
                            view.setBackgroundColor(Color.parseColor(itemb.color));
                            view.setSelected(Color.parseColor(itemb.color) == shadowCardView.getBackgroundColor());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    shadowCardView.setBackgroundColor(Color.parseColor(itemb.color));
                                    onClickColor.onClick(v);
                                }
                            });
                        }
                        break;
                        default:
                            break;
                }
            }

            private View createView(LayoutInflater inflater, int position) {
                View view = inflater.inflate(R.layout.view_item_color, (ViewGroup) flexboxLayout, false);
                view.setTag(position);
                FlexboxLayout.LayoutParams p = new FlexboxLayout.LayoutParams(dp2px(42f, itemView.getContext()), dp2px(24f, itemView.getContext()));
                int margin = dp2px(4f, itemView.getContext());
                p.setMargins(margin, margin, margin, margin);
                flexboxLayout.addView(view, p);
                return view;
            }

            private int dp2px(float dipValue, Context context) {
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                return (int) (dipValue * metrics.density + 0.5f);
            }

        }
    }
}