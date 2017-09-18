package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.OptionsPopupAdapter;
import software.pipas.oprecox.modules.adapters.SubCategoryListAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;
import software.pipas.oprecox.util.Util;

public class CategoryDetails extends AppCompatActivity
{
    private int categoryIndex;
    private ListPopupWindow listPopupWindow;
    private ListView categoryListView;
    private SubCategoryListAdapter categoryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout centerLayout = (LinearLayout) findViewById(R.id.centerLayout);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) centerLayout.getLayoutParams();
            params.setMargins(0, params.topMargin + Util.getStatusBarHeight(getResources()), 0, params.bottomMargin);
            centerLayout.setLayoutParams(params);

            LinearLayout pressMoreLayout = (LinearLayout) findViewById(R.id.pressMoreLayout);
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) pressMoreLayout.getLayoutParams();
            params2.setMargins(0, params2.topMargin + Util.getStatusBarHeight(getResources()), 0, params2.bottomMargin);
            pressMoreLayout.setLayoutParams(params2);
        }

        Intent intent = getIntent();
        categoryIndex = intent.getIntExtra("category", 0);

        initiateViews();

        initiatePressMoreButton();
    }

    private void initiateViews()
    {
        TextView categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        categoryTitle.setText(CategoryHandler.getCategories().get(categoryIndex).getTitle());
        CustomFontHelper.setCustomFont(categoryTitle, "font/antipastopro-demibold.otf", getBaseContext());

        ImageView parentCategoryImage = (ImageView) findViewById(R.id.parentCategoryImage);
        parentCategoryImage.setImageResource(CategoryHandler.getCategories().get(categoryIndex).getBigImageId());

        ImageView blurryBackground = (ImageView) findViewById(R.id.blurryBackground);
        Blurry.with(getBaseContext()).radius(25).color(Color.argb(150,200,200,200)).from(BitmapFactory.decodeResource(getResources(), CategoryHandler.getCategories().get(categoryIndex).getBigImageId())).into(blurryBackground);

        categoryListView = (ListView) findViewById(R.id.subCategories);
        categoryListAdapter = new SubCategoryListAdapter(CategoryHandler.getCategories().get(categoryIndex).getSubCategories(), getApplicationContext(), getContentResolver());
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                CheckBox subCategoryCheckbox = (CheckBox) v.findViewById(R.id.subCategoryCheckbox);
                subCategoryCheckbox.toggle();
                CategoryHandler.getCategories().get(categoryIndex).toggleSubCategory(position);
            }
        });
    }

    private void initiatePressMoreButton()
    {
        ImageView pressMore = (ImageView) findViewById(R.id.pressMore);
        View anchorView = findViewById(R.id.anchorView);
        pressMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(listPopupWindow.isShowing())
                    listPopupWindow.dismiss();
                else
                    listPopupWindow.show();
            }
        });

        listPopupWindow = new ListPopupWindow(CategoryDetails.this);
        ArrayList<String> options = new ArrayList<>();
        options.add("Selecionar todas");
        options.add("Anular seleção");
        listPopupWindow.setAdapter(new OptionsPopupAdapter(options, getApplicationContext(), getContentResolver()));
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setWidth((int) (200 * Settings.getDeviceDisplayMetrics().density + 0.5f));
        listPopupWindow.setHeight((int) (56 * options.size() * Settings.getDeviceDisplayMetrics().density + 0.5f));
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    CategoryHandler.getCategories().get(categoryIndex).selectAll();
                    refreshListView();
                    listPopupWindow.dismiss();
                }
                else
                {
                    CategoryHandler.getCategories().get(categoryIndex).deSelectAll();
                    refreshListView();
                    listPopupWindow.dismiss();
                }
            }
        });
    }

    public void refreshListView()
    {
        categoryListAdapter.notifyDataSetChanged();
        categoryListView.setAdapter(categoryListAdapter);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
