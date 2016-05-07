package com.dazzcoder.morenews.view;/**
 * Created by zc on 2015/12/25.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2015-12-25
 * Time: 20:37
 * ReadMe:
 */

public class OtherGridView extends GridView {
    public OtherGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
