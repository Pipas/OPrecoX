package software.pipas.oprecox.activities.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.adapters.CategoryGridAdapter;
import software.pipas.oprecox.modules.adapters.OptionsPopupAdapter;
import software.pipas.oprecox.modules.categories.CategoryHandler;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class CategoryChooser extends AppCompatActivity
{
    private GridView categoryListView;
    private CategoryGridAdapter categoryGridAdapter;
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        Boolean multiplayer = intent.getBooleanExtra("multiplayer", false);
        if(multiplayer)
            setTheme(R.style.MultiplayerTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        if(multiplayer)
        {
            RelativeLayout headerLayout = (RelativeLayout) findViewById(R.id.headerLayout);
            headerLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.purple));
        }

        CategoryHandler.checkIfRestart(this);

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

        listPopupWindow = new ListPopupWindow(CategoryChooser.this);
        ArrayList<String> options = new ArrayList<>();
        options.add("Selecionar todas");
        options.add("Anular seleção");
        listPopupWindow.setAdapter(new OptionsPopupAdapter(options, getApplicationContext(), getContentResolver()));
        listPopupWindow.setAnchorView(anchorView);
        listPopupWindow.setWidth((int) (200 * getResources().getDisplayMetrics().density + 0.5f));
        listPopupWindow.setHeight((int) (56 * options.size() * getResources().getDisplayMetrics().density + 0.5f));
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0)
                {
                    CategoryHandler.selectAll();
                    refreshGridView();
                    listPopupWindow.dismiss();
                }
                else
                {
                    CategoryHandler.deSelectAll();
                    refreshGridView();
                    listPopupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(CategoryHandler.validSelection())
        {
            SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
            editor.putString("categories", CategoryHandler.saveToString());
            editor.apply();
        }
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
