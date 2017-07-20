package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.SubCategoryListAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;

public class CategoryDetails extends AppCompatActivity
{
    private int categoryIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        Intent intent = getIntent();
        categoryIndex = intent.getIntExtra("category", 0);

        TextView categoryTitle = (TextView) findViewById(R.id.categoryTitle);
        categoryTitle.setText(CategoryHandler.getCategories().get(categoryIndex).getTitle());

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
