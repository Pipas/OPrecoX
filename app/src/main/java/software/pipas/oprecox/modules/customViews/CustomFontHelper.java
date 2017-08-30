package software.pipas.oprecox.modules.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class CustomFontHelper
{
    public static void setCustomFont(TextView textview, String font, Context context)
    {
        if(font == null)
        {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null)
        {
            textview.setTypeface(tf);
        }
    }

}