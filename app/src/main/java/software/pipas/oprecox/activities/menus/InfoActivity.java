package software.pipas.oprecox.activities.menus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.BuildConfig;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class InfoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        initiateCustomFonts();
    }

    private void initiateCustomFonts()
    {
        ArrayList<TextView> titles = new ArrayList<>();
        ArrayList<TextView> tooltips = new ArrayList<>();

        TextView belowIconTooltip = (TextView) findViewById(R.id.belowIconTooltip);
        String appInfo = "O Preço X\nv" + BuildConfig.VERSION_NAME;
        belowIconTooltip.setText(appInfo);

        titles.add((TextView) findViewById(R.id.mainDeveloperName));
        titles.add((TextView) findViewById(R.id.multiplayerDeveloperName));
        titles.add((TextView) findViewById(R.id.designName));
        titles.add((TextView) findViewById(R.id.thanks));
        titles.add((TextView) findViewById(R.id.otherInfoTitle));
        titles.add((TextView) findViewById(R.id.infoTitleTextView));

        tooltips.add((TextView) findViewById(R.id.mainDeveloperTitle));
        tooltips.add((TextView) findViewById(R.id.multiplayerDeveloperTitle));
        tooltips.add((TextView) findViewById(R.id.designTitle));
        tooltips.add((TextView) findViewById(R.id.thanksNames));
        tooltips.add((TextView) findViewById(R.id.otherInfo));
        tooltips.add(belowIconTooltip);

        for(TextView tv : titles)
            CustomFontHelper.setCustomFont(tv, "font/antipastopro-demibold.otf", getBaseContext());

        for(TextView tv : tooltips)
            CustomFontHelper.setCustomFont(tv, "font/Comfortaa_Regular.ttf", getBaseContext());
    }

    public void pressMainDeveloper(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/pipas"));
        startActivity(i);
    }

    public void pressDeveloper(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/excelsi0r"));
        startActivity(i);
    }

    public void pressDesigner(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.behance.net/ines_sd_co3749"));
        startActivity(i);
    }
}
