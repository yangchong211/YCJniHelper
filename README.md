# YCCardView
滑动卡片控件

## 目录介绍
- 1.使用说明
- 2.功能说明
- 3.图片展示
- 4.其他介绍

## 1.使用说明
- **1.1 直接在项目build文件中添加库即可：compile 'cn.yc:YCCardViewLib:1.1'**
- 关于具体的使用方法，可以直接参考代码

- **1.2 在布局中，如下所示**
```
    <com.ns.yc.yccardviewlib.CardViewLayout
        android:id="@+id/cardView"
        android:layout_marginTop="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="5dp"
        android:paddingTop="5dp"
        app:displayCount="1.8"
        app:interval="10dp"
        app:scaleStep="0.32"
        app:sizeRatio="1.12" />
```

- **1.3 在代码中设置适配器**
```
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
```
- 请参考代码，已经做出了很详细的注释

## 2.功能说明

## 3.图片展示
- 3.1 案例展示
- ![image](https://github.com/yangchong211/image/blob/master/image/slideview/yccardview.gif)

## 4.其他介绍
**4.1版本更新说明**
- v1.0 将之前新芽项目的代码，抽取封装成库
- v1.1 解决了View在滑动过程中不能复用的问题

**4.2本人写的综合案例**
- [案例](https://github.com/yangchong211/LifeHelper)
- [说明及截图](https://github.com/yangchong211/LifeHelper/blob/master/README.md)
- 模块：新闻，音乐，视频，图片，唐诗宋词，快递，天气，记事本，阅读器等等
- 接口：七牛，阿里云，天行，干货集中营，极速数据，追书神器等等
- [持续更新目录说明](http://www.jianshu.com/p/53017c3fc75d)

**4.3其他**
- 知乎：https://www.zhihu.com/people/yang-chong-69-24/pins/posts
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- github：https://github.com/yangchong211
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/

