package software.pipas.oprecox.application;


import android.app.Application;
import android.util.Log;

import software.pipas.oprecox.modules.categories.Categories;

/**
 * Created by nuno_ on 27-Jun-17.
 */

public class OPrecoX extends Application
{
    /*private Categories categories;
    private static OPrecoX singleton;

    public static OPrecoX getInstance(){
        return singleton;
    }*/

    @Override
    public void onCreate()
    {
        super.onCreate();
        /*singleton = this;

        categories = new Categories();*/
    }

    /*public Categories getCategories()
    {
        return categories;
    }*/

}
