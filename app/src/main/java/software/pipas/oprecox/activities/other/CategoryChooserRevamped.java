package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.CategoryListAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;

public class CategoryChooserRevamped extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser_revamped);

        ListView categoryListView = (ListView) findViewById(R.id.categoryListView);
        final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(CategoryHandler.getCategories(), getApplicationContext(), getContentResolver());
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id)
            {
                Log.d("TEST", categoryListAdapter.getItem(position).getTitle());
            }
        });
    }
}
