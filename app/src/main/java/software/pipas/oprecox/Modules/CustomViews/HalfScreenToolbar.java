package software.pipas.oprecox.modules.customViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

public class HalfScreenToolbar extends Toolbar
{
    public HalfScreenToolbar(Context context)
    {
        super(context);
    }

    public HalfScreenToolbar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public HalfScreenToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int halfSize = getMeasuredHeight() / 2;
        setMeasuredDimension(getMeasuredWidth(), halfSize);
    }
}
