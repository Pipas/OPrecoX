package software.pipas.oprecox.modules.customViews;

import android.content.Context;
import android.util.AttributeSet;

public class CustomButton extends android.support.v7.widget.AppCompatButton
{

    public CustomButton(Context context)
    {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, "font/antipastopro-demibold.otf", context);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, "font/antipastopro-demibold.otf", context);
    }
}