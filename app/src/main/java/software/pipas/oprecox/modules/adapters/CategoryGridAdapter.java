package software.pipas.oprecox.modules.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.other.CategoryChooser;
import software.pipas.oprecox.modules.categories.ParentCategory;
import software.pipas.oprecox.modules.customViews.CustomFontHelper;

public class CategoryGridAdapter extends BaseAdapter
{

    private Context mContext;
    private ArrayList<ParentCategory> categories;
    private CategoryChooser activty;

    public CategoryGridAdapter(Context context, ArrayList<ParentCategory> categories, CategoryChooser activty)
    {
        this.mContext = context;
        this.categories = categories;
        this.activty = activty;
    }

    @Override
    public int getCount()
    {
        return categories.size();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ParentCategory category = categories.get(position);

        if (convertView == null)
        {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.parent_category_item, null);
        }

        ImageView parentCategoryImage = (ImageView) convertView.findViewById(R.id.parentCategoryImage);
        TextView parentCategoryTitle = (TextView) convertView.findViewById(R.id.parentCategoryTitle);

        CardView cardViewClickable = (CardView) convertView.findViewById(R.id.cardViewClickable);
        cardViewClickable.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                activty.startCategoryDetailActivity(position);
             }
         });
        cardViewClickable.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                category.toggleAll();
                activty.refreshGridView();
                return true;
            }
        });

        if(!category.isSelected())
            parentCategoryImage.setColorFilter(Color.argb(150,200,200,200));

        parentCategoryImage.setImageResource(category.getImageId());
        parentCategoryTitle.setText(category.getTitle());

        CustomFontHelper.setCustomFont(parentCategoryTitle, "font/antipastopro-demibold.otf", mContext);

        return convertView;
    }

}

