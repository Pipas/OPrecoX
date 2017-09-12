package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.wasabeef.blurry.Blurry;
import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.SubCategoryListAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class CategoryDetails extends AppCompatActivity
{
    private int categoryIndex;

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
            params.setMargins(0, params.topMargin + getStatusBarHeight(), 0, params.bottomMargin);
            centerLayout.setLayoutParams(params);
        }

        Intent intent = getIntent();
        categoryIndex = intent.getIntExtra("category", 0);

        initiateViews();
    }

    public int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initiateViews()
    {
        TextView categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        categoryTitle.setText(CategoryHandler.getCategories().get(categoryIndex).getTitle());
        CustomFontHelper.setCustomFont(categoryTitle, "font/antipastopro-demibold.otf", getBaseContext());

        ImageView parentCategoryImage = (ImageView) findViewById(R.id.parentCategoryImage);
        parentCategoryImage.setImageResource(CategoryHandler.getCategories().get(categoryIndex).getImageId());

        ImageView blurryBackground = (ImageView) findViewById(R.id.blurryBackground);
        Blurry.with(getBaseContext()).radius(25).color(Color.argb(150,200,200,200)).from(BitmapFactory.decodeResource(getResources(), CategoryHandler.getCategories().get(categoryIndex).getImageId())).into(blurryBackground);

        ListView categoryListView = (ListView) findViewById(R.id.subCategories);
        final SubCategoryListAdapter categoryListAdapter = new SubCategoryListAdapter(CategoryHandler.getCategories().get(categoryIndex).getSubCategories(), getApplicationContext(), getContentResolver());
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
