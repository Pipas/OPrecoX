package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.CategoryGridAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class CategoryChooser extends AppCompatActivity
{
    private GridView categoryListView;
    private CategoryGridAdapter categoryGridAdapter;

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

        Log.d("TEST", "TEST");
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
