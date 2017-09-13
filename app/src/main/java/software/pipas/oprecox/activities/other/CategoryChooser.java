package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.CategoryGridAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;
import software.pipas.oprecox.util.Settings;

public class CategoryChooser extends AppCompatActivity
{
    private GridView categoryListView;
    private CategoryGridAdapter categoryGridAdapter;
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        TextView categoriesTitle = (TextView) findViewById(R.id.categoriesTitle);
        CustomFontHelper.setCustomFont(categoriesTitle, "font/antipastopro-demibold.otf", getBaseContext());

        categoryListView = (GridView) findViewById(R.id.categoryGridView);
        categoryGridAdapter = new CategoryGridAdapter(getApplicationContext(), CategoryHandler.getCategories(), CategoryChooser.this);
        categoryListView.setAdapter(categoryGridAdapter);

        initiatePressMoreButton();


    }

    private void initiatePressMoreButton()
    {
        ImageView pressMore = (ImageView) findViewById(R.id.pressMore);
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

        listPopupWindow = new ListPopupWindow(CategoryChooser.this);
        ArrayList<String> options = new ArrayList<>();
        options.add("Selecionar todos");
        options.add("Deselecionar todos");
        listPopupWindow.setAdapter(new ArrayAdapter(CategoryChooser.this, R.layout.popup_list_item, options));
        listPopupWindow.setAnchorView(pressMore);
        listPopupWindow.setWidth((int) (200 * Settings.getDeviceDisplayMetrics().density + 0.5f));
        listPopupWindow.setHeight((int) (128 * Settings.getDeviceDisplayMetrics().density + 0.5f));

        listPopupWindow.setModal(true);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        CategoryHandler.validSelection();
        SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.putString("categories", CategoryHandler.saveToString());
        editor.apply();
        finish();
    }

    public void startCategoryDetailActivity(int position)
    {
        Intent myIntent = new Intent(CategoryChooser.this, CategoryDetails.class);
        myIntent.putExtra("category", position);
        startActivityForResult(myIntent, 1);
    }

    public void refreshGridView()
    {
        categoryGridAdapter.notifyDataSetChanged();
        categoryListView.setAdapter(categoryGridAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            refreshGridView();
        }
    }
}
