package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.CategoryListAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;

public class CategoryChooser extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        ListView categoryListView = (ListView) findViewById(R.id.categoryListView);
        final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(CategoryHandler.getCategories(), getApplicationContext(), getContentResolver());
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Intent myIntent = new Intent(CategoryChooser.this, CategoryDetails.class);
                myIntent.putExtra("category", position);
                startActivity(myIntent);
            }
        });
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
}
