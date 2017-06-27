package software.pipas.oprecox.modules.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Pipas_ on 16/04/2017.
 */

public class SquareViewPager extends ViewPager
{
    public SquareViewPager(Context context)
    {
        super(context);
    }

    public SquareViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int squareSize = getMeasuredWidth();
        setMeasuredDimension(squareSize, squareSize);
    }
}
