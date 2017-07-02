package software.pipas.oprecox.activities.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.buildware.widget.indeterm.BuildConfig;
import com.buildware.widget.indeterm.IndeterminateCheckBox;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.categories.Categories;

public class CategoryChooser extends AppCompatActivity {

    private IndeterminateCheckBox checkanimals;
    private IndeterminateCheckBox checkbaby;
    private IndeterminateCheckBox checkcars;
    private IndeterminateCheckBox checksports;
    private IndeterminateCheckBox checkhouses;
    private IndeterminateCheckBox checkleisure;
    private IndeterminateCheckBox checkfashion;
    private IndeterminateCheckBox checkfurniture;
    private IndeterminateCheckBox checktechnology;
    private IndeterminateCheckBox checkphones;
    private IndeterminateCheckBox checkothers;
    private CheckBox checkall;
    private CheckBox[] checkcatg = new CheckBox[35];

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.confirm:
                confirmSelection();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);
        setTitle("Categorias");


        initiateAnimals();

        initiateBaby();

        initiateCars();

        initiateSports();

        initiateHouses();

        inititateLeisure();

        initiateFashion();

        initiateFurniture();

        initiateTechnology();

        inititatePhones();

        inititateOthers();

        initiateCheckAll();

    }

    private void initiateAnimals()
    {
        checkanimals = (IndeterminateCheckBox) findViewById(R.id.checkanimals);
        checkcatg[1] = (CheckBox) findViewById(R.id.checkcatg1);
        if(Categories.checkSelected(1))
        {
            checkcatg[1].setChecked(true);
            checkanimals.setIndeterminate(true);
        }
        checkcatg[2] = (CheckBox) findViewById(R.id.checkcatg2);
        if(Categories.checkSelected(2))
        {
            checkcatg[2].setChecked(true);
            if(checkanimals.isIndeterminate())
                checkanimals.setChecked(true);
            else
                checkanimals.setIndeterminate(true);
        }
        LinearLayout buttonanimals = (LinearLayout) findViewById(R.id.buttonanimals);
        buttonanimals.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuanimals);
            }
        });
        checkanimals.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[1].setChecked(state);
                    checkcatg[2].setChecked(state);
                }
            }
        });
        checkcatg[1].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(1);
                    if(checkanimals.isIndeterminate())
                        checkanimals.setChecked(true);
                    else
                        checkanimals.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(1);
                    if(checkanimals.isIndeterminate())
                        checkanimals.setChecked(false);
                    else
                        checkanimals.setIndeterminate(true);
                }
            }
        });
        checkcatg[2].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(2);
                    if(checkanimals.isIndeterminate())
                        checkanimals.setChecked(true);
                    else
                        checkanimals.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(2);
                    if(checkanimals.isIndeterminate())
                        checkanimals.setChecked(false);
                    else
                        checkanimals.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg1 = (LinearLayout) findViewById(R.id.buttoncatg1);
        buttoncatg1.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[1].toggle();}});
        LinearLayout buttoncatg2 = (LinearLayout) findViewById(R.id.buttoncatg2);
        buttoncatg2.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[2].toggle();}});
    }

    private void initiateBaby()
    {
        checkbaby = (IndeterminateCheckBox) findViewById(R.id.checkbaby);
        checkcatg[3] = (CheckBox) findViewById(R.id.checkcatg3);
        if(Categories.checkSelected(3))
        {
            checkcatg[3].setChecked(true);
            checkbaby.setIndeterminate(true);
        }
        checkcatg[4] = (CheckBox) findViewById(R.id.checkcatg4);
        if(Categories.checkSelected(4))
        {
            checkcatg[4].setChecked(true);
            checkbaby.setIndeterminate(true);
        }
        checkcatg[5] = (CheckBox) findViewById(R.id.checkcatg5);
        if(Categories.checkSelected(5))
        {
            checkcatg[5].setChecked(true);
            if(checkcatg[4].isChecked() && checkcatg[3].isChecked())
                checkbaby.setChecked(true);
            else
                checkbaby.setIndeterminate(true);
        }
        LinearLayout buttonbaby = (LinearLayout) findViewById(R.id.buttonbaby);
        buttonbaby.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenubaby);
            }
        });
        checkbaby.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[3].setChecked(state);
                    checkcatg[4].setChecked(state);
                    checkcatg[5].setChecked(state);
                }
            }
        });
        checkcatg[3].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(3);
                    if(checkcatg[4].isChecked() && checkcatg[5].isChecked())
                        checkbaby.setChecked(true);
                    else
                        checkbaby.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(3);
                    if(!checkcatg[4].isChecked() && !checkcatg[5].isChecked())
                        checkbaby.setChecked(false);
                    else
                        checkbaby.setIndeterminate(true);
                }
            }
        });
        checkcatg[4].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(4);
                    if(checkcatg[3].isChecked() && checkcatg[5].isChecked())
                        checkbaby.setChecked(true);
                    else
                        checkbaby.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(4);
                    if(!checkcatg[3].isChecked() && !checkcatg[5].isChecked())
                        checkbaby.setChecked(false);
                    else
                        checkbaby.setIndeterminate(true);
                }
            }
        });
        checkcatg[5].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(5);
                    if(checkcatg[4].isChecked() && checkcatg[3].isChecked())
                        checkbaby.setChecked(true);
                    else
                        checkbaby.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(5);
                    if(!checkcatg[4].isChecked() && !checkcatg[3].isChecked())
                        checkbaby.setChecked(false);
                    else
                        checkbaby.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg3 = (LinearLayout) findViewById(R.id.buttoncatg3);
        buttoncatg3.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[3].toggle();}});
        LinearLayout buttoncatg4 = (LinearLayout) findViewById(R.id.buttoncatg4);
        buttoncatg4.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[4].toggle();}});
        LinearLayout buttoncatg5 = (LinearLayout) findViewById(R.id.buttoncatg5);
        buttoncatg5.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[5].toggle();}});
    }

    private void initiateCars()
    {
        checkcars = (IndeterminateCheckBox) findViewById(R.id.checkcars);
        checkcatg[6] = (CheckBox) findViewById(R.id.checkcatg6);
        if(Categories.checkSelected(6))
        {
            checkcatg[6].setChecked(true);
            checkcars.setIndeterminate(true);
        }
        checkcatg[7] = (CheckBox) findViewById(R.id.checkcatg7);
        if(Categories.checkSelected(7))
        {
            checkcatg[7].setChecked(true);
            checkcars.setIndeterminate(true);
        }
        checkcatg[8] = (CheckBox) findViewById(R.id.checkcatg8);
        if(Categories.checkSelected(8))
        {
            checkcatg[8].setChecked(true);
            if(checkcatg[6].isChecked() && checkcatg[7].isChecked())
                checkcars.setChecked(true);
            else
                checkcars.setIndeterminate(true);
        }
        LinearLayout buttoncars = (LinearLayout) findViewById(R.id.buttoncars);
        buttoncars.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenucars);
            }
        });
        checkcars.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[6].setChecked(state);
                    checkcatg[7].setChecked(state);
                    checkcatg[8].setChecked(state);
                }
            }
        });
        checkcatg[6].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(6);
                    if(checkcatg[7].isChecked() && checkcatg[8].isChecked())
                        checkcars.setChecked(true);
                    else
                        checkcars.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(6);
                    if(!checkcatg[7].isChecked() && !checkcatg[8].isChecked())
                        checkcars.setChecked(false);
                    else
                        checkcars.setIndeterminate(true);
                }
            }
        });
        checkcatg[7].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(7);
                    if(checkcatg[6].isChecked() && checkcatg[8].isChecked())
                        checkcars.setChecked(true);
                    else
                        checkcars.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(7);
                    if(!checkcatg[6].isChecked() && !checkcatg[8].isChecked())
                        checkcars.setChecked(false);
                    else
                        checkcars.setIndeterminate(true);
                }
            }
        });
        checkcatg[8].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(8);
                    if(checkcatg[7].isChecked() && checkcatg[6].isChecked())
                        checkcars.setChecked(true);
                    else
                        checkcars.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(8);
                    if(!checkcatg[7].isChecked() && !checkcatg[6].isChecked())
                        checkcars.setChecked(false);
                    else
                        checkcars.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg6 = (LinearLayout) findViewById(R.id.buttoncatg6);
        buttoncatg6.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[6].toggle();}});
        LinearLayout buttoncatg7 = (LinearLayout) findViewById(R.id.buttoncatg7);
        buttoncatg7.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[7].toggle();}});
        LinearLayout buttoncatg8 = (LinearLayout) findViewById(R.id.buttoncatg8);
        buttoncatg8.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[8].toggle();}});
    }

    private void initiateSports()
    {
        checksports = (IndeterminateCheckBox) findViewById(R.id.checksports);
        checkcatg[9] = (CheckBox) findViewById(R.id.checkcatg9);
        if(Categories.checkSelected(9))
        {
            checkcatg[9].setChecked(true);
            checksports.setIndeterminate(true);
        }
        checkcatg[10] = (CheckBox) findViewById(R.id.checkcatg10);
        if(Categories.checkSelected(10))
        {
            checkcatg[10].setChecked(true);
            if(checksports.isIndeterminate())
                checksports.setChecked(true);
            else
                checksports.setIndeterminate(true);
        }
        LinearLayout buttonsports = (LinearLayout) findViewById(R.id.buttonsports);
        buttonsports.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenusports);
            }
        });
        checksports.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[9].setChecked(state);
                    checkcatg[10].setChecked(state);
                }
            }
        });
        checkcatg[9].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(9);
                    if(checksports.isIndeterminate())
                        checksports.setChecked(true);
                    else
                        checksports.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(9);
                    if(checksports.isIndeterminate())
                        checksports.setChecked(false);
                    else
                        checksports.setIndeterminate(true);
                }
            }
        });
        checkcatg[10].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(10);
                    if(checksports.isIndeterminate())
                        checksports.setChecked(true);
                    else
                        checksports.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(10);
                    if(checksports.isIndeterminate())
                        checksports.setChecked(false);
                    else
                        checksports.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg9 = (LinearLayout) findViewById(R.id.buttoncatg9);
        buttoncatg9.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[9].toggle();}});
        LinearLayout buttoncatg10 = (LinearLayout) findViewById(R.id.buttoncatg10);
        buttoncatg10.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[10].toggle();}});
    }

    private void initiateHouses()
    {
        checkhouses = (IndeterminateCheckBox) findViewById(R.id.checkhouses);
        checkcatg[11] = (CheckBox) findViewById(R.id.checkcatg11);
        if(Categories.checkSelected(11))
        {
            checkcatg[11].setChecked(true);
            checkhouses.setIndeterminate(true);
        }
        checkcatg[12] = (CheckBox) findViewById(R.id.checkcatg12);
        if(Categories.checkSelected(12))
        {
            checkcatg[12].setChecked(true);
            checkhouses.setIndeterminate(true);
        }
        checkcatg[13] = (CheckBox) findViewById(R.id.checkcatg13);
        if(Categories.checkSelected(13))
        {
            checkcatg[13].setChecked(true);
            if(checkcatg[11].isChecked() && checkcatg[12].isChecked())
                checkhouses.setChecked(true);
            else
                checkhouses.setIndeterminate(true);
        }
        LinearLayout buttonhouses = (LinearLayout) findViewById(R.id.buttonhouses);
        buttonhouses.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuhouses);
            }
        });
        checkhouses.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[11].setChecked(state);
                    checkcatg[12].setChecked(state);
                    checkcatg[13].setChecked(state);
                }
            }
        });
        checkcatg[11].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(11);
                    if(checkcatg[12].isChecked() && checkcatg[13].isChecked())
                        checkhouses.setChecked(true);
                    else
                        checkhouses.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(11);
                    if(!checkcatg[12].isChecked() && !checkcatg[13].isChecked())
                        checkhouses.setChecked(false);
                    else
                        checkhouses.setIndeterminate(true);
                }
            }
        });
        checkcatg[12].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(12);
                    if(checkcatg[11].isChecked() && checkcatg[13].isChecked())
                        checkhouses.setChecked(true);
                    else
                        checkhouses.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(12);
                    if(!checkcatg[11].isChecked() && !checkcatg[13].isChecked())
                        checkhouses.setChecked(false);
                    else
                        checkhouses.setIndeterminate(true);
                }
            }
        });
        checkcatg[13].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(13);
                    if(checkcatg[12].isChecked() && checkcatg[11].isChecked())
                        checkhouses.setChecked(true);
                    else
                        checkhouses.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(13);
                    if(!checkcatg[12].isChecked() && !checkcatg[11].isChecked())
                        checkhouses.setChecked(false);
                    else
                        checkhouses.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg11 = (LinearLayout) findViewById(R.id.buttoncatg11);
        buttoncatg11.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[11].toggle();}});
        LinearLayout buttoncatg12 = (LinearLayout) findViewById(R.id.buttoncatg12);
        buttoncatg12.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[12].toggle();}});
        LinearLayout buttoncatg13 = (LinearLayout) findViewById(R.id.buttoncatg13);
        buttoncatg13.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[13].toggle();}});
    }

    private void inititateLeisure()
    {
        checkleisure = (IndeterminateCheckBox) findViewById(R.id.checkleisure);
        checkcatg[14] = (CheckBox) findViewById(R.id.checkcatg14);
        if(Categories.checkSelected(14))
        {
            checkcatg[14].setChecked(true);
            checkleisure.setIndeterminate(true);
        }
        checkcatg[15] = (CheckBox) findViewById(R.id.checkcatg15);
        if(Categories.checkSelected(15))
        {
            checkcatg[15].setChecked(true);
            checkleisure.setIndeterminate(true);
        }
        checkcatg[16] = (CheckBox) findViewById(R.id.checkcatg16);
        if(Categories.checkSelected(16))
        {
            checkcatg[16].setChecked(true);
            checkleisure.setIndeterminate(true);
        }
        checkcatg[17] = (CheckBox) findViewById(R.id.checkcatg17);
        if(Categories.checkSelected(17))
        {
            checkcatg[17].setChecked(true);
            checkleisure.setIndeterminate(true);
        }
        checkcatg[18] = (CheckBox) findViewById(R.id.checkcatg18);
        if(Categories.checkSelected(18))
        {
            checkcatg[18].setChecked(true);
            checkleisure.setIndeterminate(true);
        }
        checkcatg[19] = (CheckBox) findViewById(R.id.checkcatg19);
        if(Categories.checkSelected(19))
        {
            checkcatg[19].setChecked(true);
            if(checkcatg[14].isChecked() && checkcatg[15].isChecked() && checkcatg[16].isChecked() && checkcatg[17].isChecked() && checkcatg[18].isChecked())
                checkleisure.setChecked(true);
            else
                checkleisure.setIndeterminate(true);
        }
        LinearLayout buttonleisure = (LinearLayout) findViewById(R.id.buttonleisure);
        buttonleisure.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuleisure);
            }
        });
        checkleisure.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[14].setChecked(state);
                    checkcatg[15].setChecked(state);
                    checkcatg[16].setChecked(state);
                    checkcatg[17].setChecked(state);
                    checkcatg[18].setChecked(state);
                    checkcatg[19].setChecked(state);
                }
            }
        });
        checkcatg[14].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(14);
                    if(checkcatg[15].isChecked() && checkcatg[16].isChecked() && checkcatg[17].isChecked() && checkcatg[18].isChecked() && checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(14);
                    if(!checkcatg[15].isChecked() && !checkcatg[16].isChecked() && !checkcatg[17].isChecked() && !checkcatg[18].isChecked() && !checkcatg[19].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        checkcatg[15].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(15);
                    if(checkcatg[14].isChecked() && checkcatg[16].isChecked() && checkcatg[17].isChecked() && checkcatg[18].isChecked() && checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(15);
                    if(!checkcatg[14].isChecked() && !checkcatg[16].isChecked() && !checkcatg[17].isChecked() && !checkcatg[18].isChecked() && !checkcatg[19].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        checkcatg[16].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(16);
                    if(checkcatg[15].isChecked() && checkcatg[14].isChecked() && checkcatg[17].isChecked() && checkcatg[18].isChecked() && checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(16);
                    if(!checkcatg[15].isChecked() && !checkcatg[14].isChecked() && !checkcatg[17].isChecked() && !checkcatg[18].isChecked() && !checkcatg[19].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        checkcatg[17].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(17);
                    if(checkcatg[15].isChecked() && checkcatg[16].isChecked() && checkcatg[14].isChecked() && checkcatg[18].isChecked() && checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(17);
                    if(!checkcatg[15].isChecked() && !checkcatg[16].isChecked() && !checkcatg[14].isChecked() && !checkcatg[18].isChecked() && !checkcatg[19].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        checkcatg[18].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(18);
                    if(checkcatg[15].isChecked() && checkcatg[16].isChecked() && checkcatg[17].isChecked() && checkcatg[14].isChecked() && checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(18);
                    if(!checkcatg[15].isChecked() && !checkcatg[16].isChecked() && !checkcatg[17].isChecked() && !checkcatg[14].isChecked() && !checkcatg[19].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        checkcatg[19].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(19);
                    if(checkcatg[15].isChecked() && checkcatg[16].isChecked() && checkcatg[17].isChecked() && checkcatg[18].isChecked() && checkcatg[14].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(19);
                    if(!checkcatg[15].isChecked() && !checkcatg[16].isChecked() && !checkcatg[17].isChecked() && !checkcatg[18].isChecked() && !checkcatg[14].isChecked())
                        checkleisure.setChecked(false);
                    else
                        checkleisure.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg14 = (LinearLayout) findViewById(R.id.buttoncatg14);
        buttoncatg14.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[14].toggle();}});
        LinearLayout buttoncatg15 = (LinearLayout) findViewById(R.id.buttoncatg15);
        buttoncatg15.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[15].toggle();}});
        LinearLayout buttoncatg16 = (LinearLayout) findViewById(R.id.buttoncatg16);
        buttoncatg16.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[16].toggle();}});
        LinearLayout buttoncatg17 = (LinearLayout) findViewById(R.id.buttoncatg17);
        buttoncatg17.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[17].toggle();}});
        LinearLayout buttoncatg18 = (LinearLayout) findViewById(R.id.buttoncatg18);
        buttoncatg18.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[18].toggle();}});
        LinearLayout buttoncatg19 = (LinearLayout) findViewById(R.id.buttoncatg19);
        buttoncatg19.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[19].toggle();}});
    }

    private void initiateFashion()
    {
        checkfashion = (IndeterminateCheckBox) findViewById(R.id.checkfashion);
        checkcatg[20] = (CheckBox) findViewById(R.id.checkcatg20);
        if(Categories.checkSelected(20))
        {
            checkcatg[20].setChecked(true);
            checkfashion.setIndeterminate(true);
        }
        checkcatg[21] = (CheckBox) findViewById(R.id.checkcatg21);
        if(Categories.checkSelected(21))
        {
            checkcatg[21].setChecked(true);
            checkfashion.setIndeterminate(true);
        }
        checkcatg[22] = (CheckBox) findViewById(R.id.checkcatg22);
        if(Categories.checkSelected(22))
        {
            checkcatg[22].setChecked(true);
            checkfashion.setIndeterminate(true);
        }
        checkcatg[23] = (CheckBox) findViewById(R.id.checkcatg23);
        if(Categories.checkSelected(23))
        {
            checkcatg[23].setChecked(true);
            checkfashion.setIndeterminate(true);
        }
        checkcatg[24] = (CheckBox) findViewById(R.id.checkcatg24);
        if(Categories.checkSelected(24))
        {
            checkcatg[24].setChecked(true);
            if(checkcatg[20].isChecked() && checkcatg[21].isChecked() && checkcatg[22].isChecked() && checkcatg[23].isChecked())
                checkfashion.setChecked(true);
            else
                checkfashion.setIndeterminate(true);
        }
        LinearLayout buttonfashion = (LinearLayout) findViewById(R.id.buttonfashion);
        buttonfashion.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenufashion);
            }
        });
        checkfashion.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[20].setChecked(state);
                    checkcatg[21].setChecked(state);
                    checkcatg[22].setChecked(state);
                    checkcatg[23].setChecked(state);
                    checkcatg[24].setChecked(state);
                }
            }
        });
        checkcatg[20].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(20);
                    if(checkcatg[21].isChecked() && checkcatg[22].isChecked() && checkcatg[23].isChecked() && checkcatg[24].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(20);
                    if(!checkcatg[21].isChecked() && !checkcatg[22].isChecked() && !checkcatg[23].isChecked() && !checkcatg[24].isChecked())
                        checkfashion.setChecked(false);
                    else
                        checkfashion.setIndeterminate(true);
                }
            }
        });
        checkcatg[21].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(21);
                    if(checkcatg[20].isChecked() && checkcatg[22].isChecked() && checkcatg[23].isChecked() && checkcatg[24].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(21);
                    if(!checkcatg[20].isChecked() && !checkcatg[22].isChecked() && !checkcatg[23].isChecked() && !checkcatg[24].isChecked())
                        checkfashion.setChecked(false);
                    else
                        checkfashion.setIndeterminate(true);
                }
            }
        });
        checkcatg[22].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(22);
                    if(checkcatg[21].isChecked() && checkcatg[20].isChecked() && checkcatg[23].isChecked() && checkcatg[24].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(22);
                    if(!checkcatg[21].isChecked() && !checkcatg[20].isChecked() && !checkcatg[23].isChecked() && !checkcatg[24].isChecked())
                        checkfashion.setChecked(false);
                    else
                        checkfashion.setIndeterminate(true);
                }
            }
        });
        checkcatg[23].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(23);
                    if(checkcatg[21].isChecked() && checkcatg[22].isChecked() && checkcatg[20].isChecked() && checkcatg[24].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(23);
                    if(!checkcatg[21].isChecked() && !checkcatg[22].isChecked() && !checkcatg[20].isChecked() && !checkcatg[24].isChecked())
                        checkfashion.setChecked(false);
                    else
                        checkfashion.setIndeterminate(true);
                }
            }
        });
        checkcatg[24].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(24);
                    if(checkcatg[21].isChecked() && checkcatg[22].isChecked() && checkcatg[23].isChecked() && checkcatg[20].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(24);
                    if(!checkcatg[21].isChecked() && !checkcatg[22].isChecked() && !checkcatg[23].isChecked() && !checkcatg[20].isChecked())
                        checkfashion.setChecked(false);
                    else
                        checkfashion.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg20 = (LinearLayout) findViewById(R.id.buttoncatg20);
        buttoncatg20.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[20].toggle();}});
        LinearLayout buttoncatg21 = (LinearLayout) findViewById(R.id.buttoncatg21);
        buttoncatg21.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[21].toggle();}});
        LinearLayout buttoncatg22 = (LinearLayout) findViewById(R.id.buttoncatg22);
        buttoncatg22.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[22].toggle();}});
        LinearLayout buttoncatg23 = (LinearLayout) findViewById(R.id.buttoncatg23);
        buttoncatg23.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[23].toggle();}});
        LinearLayout buttoncatg24 = (LinearLayout) findViewById(R.id.buttoncatg24);
        buttoncatg24.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[24].toggle();}});
    }

    private void initiateFurniture()
    {
        checkfurniture = (IndeterminateCheckBox) findViewById(R.id.checkfurniture);
        checkcatg[25] = (CheckBox) findViewById(R.id.checkcatg25);
        if(Categories.checkSelected(25))
        {
            checkcatg[25].setChecked(true);
            checkfurniture.setIndeterminate(true);
        }
        checkcatg[26] = (CheckBox) findViewById(R.id.checkcatg26);
        if(Categories.checkSelected(26))
        {
            checkcatg[26].setChecked(true);
            checkfurniture.setIndeterminate(true);
        }
        checkcatg[27] = (CheckBox) findViewById(R.id.checkcatg27);
        if(Categories.checkSelected(27))
        {
            checkcatg[27].setChecked(true);
            checkfurniture.setIndeterminate(true);
        }
        checkcatg[28] = (CheckBox) findViewById(R.id.checkcatg28);
        if(Categories.checkSelected(28))
        {
            checkcatg[28].setChecked(true);
            if(checkcatg[25].isChecked() && checkcatg[26].isChecked() && checkcatg[27].isChecked())
                checkfurniture.setChecked(true);
            else
                checkfurniture.setIndeterminate(true);
        }
        LinearLayout buttonfurniture = (LinearLayout) findViewById(R.id.buttonfurniture);
        buttonfurniture.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenufurniture);
            }
        });
        checkfurniture.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[25].setChecked(state);
                    checkcatg[26].setChecked(state);
                    checkcatg[27].setChecked(state);
                    checkcatg[28].setChecked(state);
                }
            }
        });
        checkcatg[25].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(25);
                    if(checkcatg[26].isChecked() && checkcatg[27].isChecked() && checkcatg[28].isChecked())
                        checkfurniture.setChecked(true);
                    else
                        checkfurniture.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(25);
                    if(!checkcatg[26].isChecked() && !checkcatg[27].isChecked() && !checkcatg[28].isChecked())
                        checkfurniture.setChecked(false);
                    else
                        checkfurniture.setIndeterminate(true);
                }
            }
        });
        checkcatg[26].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(26);
                    if(checkcatg[25].isChecked() && checkcatg[27].isChecked() && checkcatg[28].isChecked())
                        checkfurniture.setChecked(true);
                    else
                        checkfurniture.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(26);
                    if(!checkcatg[25].isChecked() && !checkcatg[27].isChecked() && !checkcatg[28].isChecked())
                        checkfurniture.setChecked(false);
                    else
                        checkfurniture.setIndeterminate(true);
                }
            }
        });
        checkcatg[27].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(27);
                    if(checkcatg[26].isChecked() && checkcatg[25].isChecked() && checkcatg[28].isChecked())
                        checkfurniture.setChecked(true);
                    else
                        checkfurniture.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(27);
                    if(!checkcatg[26].isChecked() && !checkcatg[25].isChecked() && !checkcatg[28].isChecked())
                        checkfurniture.setChecked(false);
                    else
                        checkfurniture.setIndeterminate(true);
                }
            }
        });
        checkcatg[28].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(28);
                    if(checkcatg[26].isChecked() && checkcatg[27].isChecked() && checkcatg[25].isChecked())
                        checkfurniture.setChecked(true);
                    else
                        checkfurniture.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(28);
                    if(!checkcatg[26].isChecked() && !checkcatg[27].isChecked() && !checkcatg[25].isChecked())
                        checkfurniture.setChecked(false);
                    else
                        checkfurniture.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg25 = (LinearLayout) findViewById(R.id.buttoncatg25);
        buttoncatg25.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[25].toggle();}});
        LinearLayout buttoncatg26 = (LinearLayout) findViewById(R.id.buttoncatg26);
        buttoncatg26.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[26].toggle();}});
        LinearLayout buttoncatg27 = (LinearLayout) findViewById(R.id.buttoncatg27);
        buttoncatg27.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[27].toggle();}});
        LinearLayout buttoncatg28 = (LinearLayout) findViewById(R.id.buttoncatg28);
        buttoncatg28.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[28].toggle();}});
    }

    private void initiateTechnology()
    {
        checktechnology = (IndeterminateCheckBox) findViewById(R.id.checktechnology);
        checkcatg[29] = (CheckBox) findViewById(R.id.checkcatg29);
        if(Categories.checkSelected(29))
        {
            checkcatg[29].setChecked(true);
            checktechnology.setIndeterminate(true);
        }
        checkcatg[30] = (CheckBox) findViewById(R.id.checkcatg30);
        if(Categories.checkSelected(30))
        {
            checkcatg[30].setChecked(true);
            checktechnology.setIndeterminate(true);
        }
        checkcatg[31] = (CheckBox) findViewById(R.id.checkcatg31);
        if(Categories.checkSelected(31))
        {
            checkcatg[31].setChecked(true);
            checktechnology.setIndeterminate(true);
        }
        checkcatg[32] = (CheckBox) findViewById(R.id.checkcatg32);
        if(Categories.checkSelected(32))
        {
            checkcatg[32].setChecked(true);
            if(checkcatg[29].isChecked() && checkcatg[30].isChecked() && checkcatg[31].isChecked())
                checktechnology.setChecked(true);
            else
                checktechnology.setIndeterminate(true);
        }
        LinearLayout buttontechnology = (LinearLayout) findViewById(R.id.buttontechnology);
        buttontechnology.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenutechnology);
            }
        });
        checktechnology.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[29].setChecked(state);
                    checkcatg[30].setChecked(state);
                    checkcatg[31].setChecked(state);
                    checkcatg[32].setChecked(state);
                }
            }
        });
        checkcatg[29].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(29);
                    if(checkcatg[30].isChecked() && checkcatg[31].isChecked() && checkcatg[32].isChecked())
                        checktechnology.setChecked(true);
                    else
                        checktechnology.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(29);
                    if(!checkcatg[30].isChecked() && !checkcatg[31].isChecked() && !checkcatg[32].isChecked())
                        checktechnology.setChecked(false);
                    else
                        checktechnology.setIndeterminate(true);
                }
            }
        });
        checkcatg[30].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(30);
                    if(checkcatg[29].isChecked() && checkcatg[31].isChecked() && checkcatg[32].isChecked())
                        checktechnology.setChecked(true);
                    else
                        checktechnology.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(30);
                    if(!checkcatg[29].isChecked() && !checkcatg[31].isChecked() && !checkcatg[32].isChecked())
                        checktechnology.setChecked(false);
                    else
                        checktechnology.setIndeterminate(true);
                }
            }
        });
        checkcatg[31].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(31);
                    if(checkcatg[30].isChecked() && checkcatg[29].isChecked() && checkcatg[32].isChecked())
                        checktechnology.setChecked(true);
                    else
                        checktechnology.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(31);
                    if(!checkcatg[30].isChecked() && !checkcatg[29].isChecked() && !checkcatg[32].isChecked())
                        checktechnology.setChecked(false);
                    else
                        checktechnology.setIndeterminate(true);
                }
            }
        });
        checkcatg[32].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(32);
                    if(checkcatg[30].isChecked() && checkcatg[31].isChecked() && checkcatg[29].isChecked())
                        checktechnology.setChecked(true);
                    else
                        checktechnology.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(32);
                    if(!checkcatg[30].isChecked() && !checkcatg[31].isChecked() && !checkcatg[29].isChecked())
                        checktechnology.setChecked(false);
                    else
                        checktechnology.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg29 = (LinearLayout) findViewById(R.id.buttoncatg29);
        buttoncatg29.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[29].toggle();}});
        LinearLayout buttoncatg30 = (LinearLayout) findViewById(R.id.buttoncatg30);
        buttoncatg30.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[30].toggle();}});
        LinearLayout buttoncatg31 = (LinearLayout) findViewById(R.id.buttoncatg31);
        buttoncatg31.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[31].toggle();}});
        LinearLayout buttoncatg32 = (LinearLayout) findViewById(R.id.buttoncatg32);
        buttoncatg32.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[32].toggle();}});
    }

    private void inititatePhones()
    {
        checkphones = (IndeterminateCheckBox) findViewById(R.id.checkphones);
        checkcatg[33] = (CheckBox) findViewById(R.id.checkcatg33);
        if(Categories.checkSelected(33))
        {
            checkcatg[33].setChecked(true);
            checkphones.setIndeterminate(true);
        }
        checkcatg[34] = (CheckBox) findViewById(R.id.checkcatg34);
        if(Categories.checkSelected(34))
        {
            checkcatg[34].setChecked(true);
            if(checkphones.isIndeterminate())
                checkphones.setChecked(true);
            else
                checkphones.setIndeterminate(true);
        }
        LinearLayout buttonphones = (LinearLayout) findViewById(R.id.buttonphones);
        buttonphones.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuphones);
            }
        });
        checkphones.setOnStateChangedListener(new IndeterminateCheckBox.OnStateChangedListener()
        {
            @Override
            public void onStateChanged(IndeterminateCheckBox check, @Nullable Boolean state)
            {
                if (state != null)
                {
                    checkcatg[33].setChecked(state);
                    checkcatg[34].setChecked(state);
                }
            }
        });
        checkcatg[33].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(33);
                    if(checkcatg[34].isChecked())
                        checkphones.setChecked(true);
                    else
                        checkphones.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(33);
                    if(!checkcatg[34].isChecked())
                        checkphones.setChecked(false);
                    else
                        checkphones.setIndeterminate(true);
                }
            }
        });
        checkcatg[34].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(34);
                    if(checkcatg[33].isChecked())
                        checkphones.setChecked(true);
                    else
                        checkphones.setIndeterminate(true);
                }
                else
                {
                    Categories.remove(34);
                    if(!checkcatg[33].isChecked())
                        checkphones.setChecked(false);
                    else
                        checkphones.setIndeterminate(true);
                }
            }
        });
        LinearLayout buttoncatg33 = (LinearLayout) findViewById(R.id.buttoncatg33);
        buttoncatg33.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[33].toggle();}});
        LinearLayout buttoncatg34 = (LinearLayout) findViewById(R.id.buttoncatg34);
        buttoncatg34.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[34].toggle();}});
    }

    private void inititateOthers()
    {
        checkothers = (IndeterminateCheckBox) findViewById(R.id.checkothers);
        if(Categories.checkSelected(35))
        {
            checkothers.setChecked(true);
        }
        LinearLayout buttonothers = (LinearLayout) findViewById(R.id.buttonothers);
        buttonothers.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkothers.toggle();
            }
        });
        checkothers.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                {
                    Categories.add(35);
                }
                else
                {
                    Categories.remove(35);
                }
            }
        });
    }

    private void initiateCheckAll()
    {
        checkall = (CheckBox) findViewById(R.id.checkall);
        if(Categories.checkAllSelected())
            checkall.setChecked(true);
        LinearLayout buttonall = (LinearLayout) findViewById(R.id.buttonall);
        buttonall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkall.toggle();
            }
        });
        checkall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkanimals.setChecked(isChecked);
                checkbaby.setChecked(isChecked);
                checkcars.setChecked(isChecked);
                checksports.setChecked(isChecked);
                checkhouses.setChecked(isChecked);
                checkleisure.setChecked(isChecked);
                checkfashion.setChecked(isChecked);
                checkfurniture.setChecked(isChecked);
                checktechnology.setChecked(isChecked);
                checkphones.setChecked(isChecked);
                checkothers.setChecked(isChecked);
                checkall.setChecked(isChecked);
            }
        });
    }

    private void toggleViewByID(int id)
    {
        View view = findViewById(id);
        if(view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
        else
            view.setVisibility(View.VISIBLE);
    }


    private void confirmSelection()
    {
        Intent returnIntent = new Intent();
        if(Categories.getSelected().isEmpty())
            Categories.selectAll();
        SharedPreferences.Editor editor = getSharedPreferences("gameSettings", MODE_PRIVATE).edit();
        editor.apply();

        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        confirmSelection();
    }
}
