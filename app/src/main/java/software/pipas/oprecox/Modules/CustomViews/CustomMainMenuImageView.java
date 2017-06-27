package software.pipas.oprecox.modules.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by Pipas_ on 19/04/2017.
 */

public class CustomMainMenuImageView extends AppCompatImageView
{
    public CustomMainMenuImageView(Context context)
    {
        super(context);
    }

    public CustomMainMenuImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMainMenuImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int halfSize = getMeasuredHeight() / 2;
        setMeasuredDimension(getMeasuredWidth(), halfSize);
    }
}
