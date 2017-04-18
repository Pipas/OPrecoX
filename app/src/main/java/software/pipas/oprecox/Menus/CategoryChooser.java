package software.pipas.oprecox.Menus;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import software.pipas.oprecox.Categories;
import software.pipas.oprecox.R;

import java.util.ArrayList;

public class CategoryChooser extends AppCompatActivity {

    Categories c;
    CheckBox checkanimals;
    CheckBox checkbaby;
    CheckBox checkcars;
    CheckBox checksports;
    CheckBox checkhouses;
    CheckBox checkleisure;
    CheckBox checkfashion;
    CheckBox checkfurniture;
    CheckBox checktechnology;
    CheckBox checkphones;
    CheckBox checkothers;
    CheckBox checkall;
    CheckBox[] checkcatg = new CheckBox[36];

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
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

        Intent intent = getIntent();
        ArrayList<String> selected = intent.getStringArrayListExtra("categories");
        c = new Categories();
        c.setSelected(selected);

        //#############################ANIMALS#######################

        checkanimals = (CheckBox) findViewById(R.id.checkanimals);
        checkcatg[1] = (CheckBox) findViewById(R.id.checkcatg1);
        if(c.checkSelected(1))
        {
            checkcatg[1].setChecked(true);
            checkanimals.setChecked(true);
        }
        checkcatg[2] = (CheckBox) findViewById(R.id.checkcatg2);
        if(c.checkSelected(2))
        {
            checkcatg[2].setChecked(true);
            checkanimals.setChecked(true);
        }
        setTooltip(1, 2, R.id.tooltipanimals);
        LinearLayout buttonanimals = (LinearLayout) findViewById(R.id.buttonanimals);
        buttonanimals.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuanimals);
                if(checkanimals.getVisibility() == View.GONE)
                {
                    if (checkcatg[1].isChecked() || checkcatg[2].isChecked())
                        checkanimals.setChecked(true);
                    else
                        checkanimals.setChecked(false);
                }
                toggleViewByID(R.id.checkanimals);
                setViewByID(R.id.tooltipanimals, checkanimals.getVisibility());
                if(checkanimals.getVisibility() == View.VISIBLE)
                    setTooltip(1, 2, R.id.tooltipanimals);
            }
        });
        checkanimals.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[1].setChecked(true);
                        checkcatg[2].setChecked(true);
                    }
                    else
                    {
                        checkcatg[1].setChecked(false);
                        checkcatg[2].setChecked(false);
                    }
                    setTooltip(1, 2, R.id.tooltipanimals);
                }
            }
        });
        checkcatg[1].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(1);
                else
                    c.remove(1);
            }
        });
        checkcatg[2].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(2);
                else
                    c.remove(2);
            }
        });
        LinearLayout buttoncatg1 = (LinearLayout) findViewById(R.id.buttoncatg1);
        buttoncatg1.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[1].toggle();}});
        LinearLayout buttoncatg2 = (LinearLayout) findViewById(R.id.buttoncatg2);
        buttoncatg2.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[2].toggle();}});

        //#############################BABY#######################

        setTooltip(3, 5, R.id.tooltipbaby);
        checkbaby = (CheckBox) findViewById(R.id.checkbaby);
        checkcatg[3] = (CheckBox) findViewById(R.id.checkcatg3);
        if(c.checkSelected(3))
        {
            checkcatg[3].setChecked(true);
            checkbaby.setChecked(true);
        }
        checkcatg[4] = (CheckBox) findViewById(R.id.checkcatg4);
        if(c.checkSelected(4))
        {
            checkcatg[4].setChecked(true);
            checkbaby.setChecked(true);
        }
        checkcatg[5] = (CheckBox) findViewById(R.id.checkcatg5);
        if(c.checkSelected(5))
        {
            checkcatg[5].setChecked(true);
            checkbaby.setChecked(true);
        }
        LinearLayout buttonbaby = (LinearLayout) findViewById(R.id.buttonbaby);
        buttonbaby.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenubaby);
                if(checkbaby.getVisibility() == View.GONE)
                {
                    if (checkcatg[3].isChecked() || checkcatg[4].isChecked() || checkcatg[5].isChecked())
                        checkbaby.setChecked(true);
                    else
                        checkbaby.setChecked(false);
                }
                toggleViewByID(R.id.checkbaby);
                setViewByID(R.id.tooltipbaby, checkbaby.getVisibility());
                if(checkbaby.getVisibility() == View.VISIBLE)
                    setTooltip(3, 5, R.id.tooltipbaby);
            }
        });
        checkbaby.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[3].setChecked(true);
                        checkcatg[4].setChecked(true);
                        checkcatg[5].setChecked(true);
                    }
                    else
                    {
                        checkcatg[3].setChecked(false);
                        checkcatg[4].setChecked(false);
                        checkcatg[5].setChecked(false);
                    }
                    setTooltip(3, 5, R.id.tooltipbaby);
                }
            }
        });
        checkcatg[3].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(3);
                else
                    c.remove(3);
            }
        });
        checkcatg[4].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(4);
                else
                    c.remove(4);
            }
        });
        checkcatg[5].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(5);
                else
                    c.remove(5);
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

        //#############################CARS#######################

        setTooltip(6, 8, R.id.tooltipcars);
        checkcars = (CheckBox) findViewById(R.id.checkcars);
        checkcatg[6] = (CheckBox) findViewById(R.id.checkcatg6);
        if(c.checkSelected(6))
        {
            checkcatg[6].setChecked(true);
            checkcars.setChecked(true);
        }
        checkcatg[7] = (CheckBox) findViewById(R.id.checkcatg7);
        if(c.checkSelected(7))
        {
            checkcatg[7].setChecked(true);
            checkcars.setChecked(true);
        }
        checkcatg[8] = (CheckBox) findViewById(R.id.checkcatg8);
        if(c.checkSelected(8))
        {
            checkcatg[8].setChecked(true);
            checkcars.setChecked(true);
        }
        LinearLayout buttoncars = (LinearLayout) findViewById(R.id.buttoncars);
        buttoncars.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenucars);
                if(checkcars.getVisibility() == View.GONE)
                {
                    if (checkcatg[6].isChecked() || checkcatg[7].isChecked() || checkcatg[8].isChecked())
                        checkcars.setChecked(true);
                    else
                        checkcars.setChecked(false);
                }
                toggleViewByID(R.id.checkcars);
                setViewByID(R.id.tooltipcars, checkcars.getVisibility());
                if(checkcars.getVisibility() == View.VISIBLE)
                    setTooltip(6, 8, R.id.tooltipcars);
            }
        });
        checkcars.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[6].setChecked(true);
                        checkcatg[7].setChecked(true);
                        checkcatg[8].setChecked(true);
                    }
                    else
                    {
                        checkcatg[6].setChecked(false);
                        checkcatg[7].setChecked(false);
                        checkcatg[8].setChecked(false);
                    }
                    setTooltip(6, 8, R.id.tooltipcars);
                }
            }
        });
        checkcatg[6].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(6);
                else
                    c.remove(6);
            }
        });
        checkcatg[7].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(7);
                else
                    c.remove(7);
            }
        });
        checkcatg[8].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(8);
                else
                    c.remove(8);
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

        //#############################SPORTS#######################

        setTooltip(9, 10, R.id.tooltipsports);
        checksports = (CheckBox) findViewById(R.id.checksports);
        checkcatg[9] = (CheckBox) findViewById(R.id.checkcatg9);
        if(c.checkSelected(9))
        {
            checkcatg[9].setChecked(true);
            checksports.setChecked(true);
        }
        checkcatg[10] = (CheckBox) findViewById(R.id.checkcatg10);
        if(c.checkSelected(10))
        {
            checkcatg[10].setChecked(true);
            checksports.setChecked(true);
        }
        LinearLayout buttonsports = (LinearLayout) findViewById(R.id.buttonsports);
        buttonsports.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenusports);
                if(checksports.getVisibility() == View.GONE)
                {
                    if (checkcatg[9].isChecked() || checkcatg[10].isChecked())
                        checksports.setChecked(true);
                    else
                        checksports.setChecked(false);
                }
                toggleViewByID(R.id.checksports);
                setViewByID(R.id.tooltipsports, checksports.getVisibility());
                if(checksports.getVisibility() == View.VISIBLE)
                    setTooltip(9, 10, R.id.tooltipsports);
            }
        });
        checksports.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[9].setChecked(true);
                        checkcatg[10].setChecked(true);
                    }
                    else
                    {
                        checkcatg[9].setChecked(false);
                        checkcatg[10].setChecked(false);
                    }
                    setTooltip(9, 10, R.id.tooltipsports);
                }
            }
        });
        checkcatg[9].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(9);
                else
                    c.remove(9);
            }
        });
        checkcatg[10].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(10);
                else
                    c.remove(10);
            }
        });
        LinearLayout buttoncatg9 = (LinearLayout) findViewById(R.id.buttoncatg9);
        buttoncatg9.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[9].toggle();}});
        LinearLayout buttoncatg10 = (LinearLayout) findViewById(R.id.buttoncatg10);
        buttoncatg10.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[10].toggle();}});

        //#############################HOUSES#######################

        setTooltip(11, 13, R.id.tooltiphouses);
        checkhouses = (CheckBox) findViewById(R.id.checkhouses);
        checkcatg[11] = (CheckBox) findViewById(R.id.checkcatg11);
        if(c.checkSelected(11))
        {
            checkcatg[11].setChecked(true);
            checkhouses.setChecked(true);
        }
        checkcatg[12] = (CheckBox) findViewById(R.id.checkcatg12);
        if(c.checkSelected(12))
        {
            checkcatg[12].setChecked(true);
            checkhouses.setChecked(true);
        }
        checkcatg[13] = (CheckBox) findViewById(R.id.checkcatg13);
        if(c.checkSelected(13))
        {
            checkcatg[13].setChecked(true);
            checkhouses.setChecked(true);
        }
        LinearLayout buttonhouses = (LinearLayout) findViewById(R.id.buttonhouses);
        buttonhouses.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuhouses);
                if(checkhouses.getVisibility() == View.GONE)
                {
                    if (checkcatg[11].isChecked() || checkcatg[12].isChecked() || checkcatg[13].isChecked())
                        checkhouses.setChecked(true);
                    else
                        checkhouses.setChecked(false);
                }
                toggleViewByID(R.id.checkhouses);
                setViewByID(R.id.tooltiphouses, checkhouses.getVisibility());
                if(checkhouses.getVisibility() == View.VISIBLE)
                    setTooltip(11, 13, R.id.tooltiphouses);
            }
        });
        checkhouses.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[11].setChecked(true);
                        checkcatg[12].setChecked(true);
                        checkcatg[13].setChecked(true);
                    }
                    else
                    {
                        checkcatg[11].setChecked(false);
                        checkcatg[12].setChecked(false);
                        checkcatg[13].setChecked(false);
                    }
                    setTooltip(11, 13, R.id.tooltiphouses);
                }
            }
        });
        checkcatg[11].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(11);
                else
                    c.remove(11);
            }
        });
        checkcatg[12].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(12);
                else
                    c.remove(12);
            }
        });
        checkcatg[13].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(13);
                else
                    c.remove(13);
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

        //#############################LEISURE#######################

        setTooltip(14, 19, R.id.tooltipleisure);
        checkleisure = (CheckBox) findViewById(R.id.checkleisure);
        checkcatg[14] = (CheckBox) findViewById(R.id.checkcatg14);
        if(c.checkSelected(14))
        {
            checkcatg[14].setChecked(true);
            checkleisure.setChecked(true);
        }
        checkcatg[15] = (CheckBox) findViewById(R.id.checkcatg15);
        if(c.checkSelected(15))
        {
            checkcatg[15].setChecked(true);
            checkleisure.setChecked(true);
        }
        checkcatg[16] = (CheckBox) findViewById(R.id.checkcatg16);
        if(c.checkSelected(16))
        {
            checkcatg[16].setChecked(true);
            checkleisure.setChecked(true);
        }
        checkcatg[17] = (CheckBox) findViewById(R.id.checkcatg17);
        if(c.checkSelected(17))
        {
            checkcatg[17].setChecked(true);
            checkleisure.setChecked(true);
        }
        checkcatg[18] = (CheckBox) findViewById(R.id.checkcatg18);
        if(c.checkSelected(18))
        {
            checkcatg[18].setChecked(true);
            checkleisure.setChecked(true);
        }
        checkcatg[19] = (CheckBox) findViewById(R.id.checkcatg19);
        if(c.checkSelected(19))
        {
            checkcatg[19].setChecked(true);
            checkleisure.setChecked(true);
        }
        LinearLayout buttonleisure = (LinearLayout) findViewById(R.id.buttonleisure);
        buttonleisure.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuleisure);
                if(checkleisure.getVisibility() == View.GONE)
                {
                    if (checkcatg[14].isChecked() || checkcatg[15].isChecked() || checkcatg[16].isChecked() || checkcatg[17].isChecked() || checkcatg[18].isChecked() || checkcatg[19].isChecked())
                        checkleisure.setChecked(true);
                    else
                        checkleisure.setChecked(false);
                }
                toggleViewByID(R.id.checkleisure);
                setViewByID(R.id.tooltipleisure, checkleisure.getVisibility());
                if(checkleisure.getVisibility() == View.VISIBLE)
                    setTooltip(14, 19, R.id.tooltipleisure);
            }
        });
        checkleisure.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[14].setChecked(true);
                        checkcatg[15].setChecked(true);
                        checkcatg[16].setChecked(true);
                        checkcatg[17].setChecked(true);
                        checkcatg[18].setChecked(true);
                        checkcatg[19].setChecked(true);
                    }
                    else
                    {
                        checkcatg[14].setChecked(false);
                        checkcatg[15].setChecked(false);
                        checkcatg[16].setChecked(false);
                        checkcatg[17].setChecked(false);
                        checkcatg[18].setChecked(false);
                        checkcatg[19].setChecked(false);
                    }
                    setTooltip(14, 19, R.id.tooltipleisure);
                }
            }
        });
        checkcatg[14].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(14);
                else
                    c.remove(14);
            }
        });
        checkcatg[15].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(15);
                else
                    c.remove(15);
            }
        });
        checkcatg[16].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(16);
                else
                    c.remove(16);
            }
        });
        checkcatg[17].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(17);
                else
                    c.remove(17);
            }
        });
        checkcatg[18].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(18);
                else
                    c.remove(18);
            }
        });
        checkcatg[19].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(19);
                else
                    c.remove(19);
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

        //#############################FASHION#######################

        setTooltip(25, 24, R.id.tooltipfashion);
        checkfashion = (CheckBox) findViewById(R.id.checkfashion);
        checkcatg[20] = (CheckBox) findViewById(R.id.checkcatg20);
        if(c.checkSelected(20))
        {
            checkcatg[20].setChecked(true);
            checkfashion.setChecked(true);
        }
        checkcatg[21] = (CheckBox) findViewById(R.id.checkcatg21);
        if(c.checkSelected(21))
        {
            checkcatg[21].setChecked(true);
            checkfashion.setChecked(true);
        }
        checkcatg[22] = (CheckBox) findViewById(R.id.checkcatg22);
        if(c.checkSelected(22))
        {
            checkcatg[22].setChecked(true);
            checkfashion.setChecked(true);
        }
        checkcatg[23] = (CheckBox) findViewById(R.id.checkcatg23);
        if(c.checkSelected(23))
        {
            checkcatg[23].setChecked(true);
            checkfashion.setChecked(true);
        }
        checkcatg[24] = (CheckBox) findViewById(R.id.checkcatg24);
        if(c.checkSelected(24))
        {
            checkcatg[24].setChecked(true);
            checkfashion.setChecked(true);
        }
        LinearLayout buttonfashion = (LinearLayout) findViewById(R.id.buttonfashion);
        buttonfashion.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenufashion);
                if(checkfashion.getVisibility() == View.GONE)
                {
                    if (checkcatg[20].isChecked() || checkcatg[21].isChecked() || checkcatg[22].isChecked() || checkcatg[23].isChecked() || checkcatg[24].isChecked())
                        checkfashion.setChecked(true);
                    else
                        checkfashion.setChecked(false);
                }
                toggleViewByID(R.id.checkfashion);
                setViewByID(R.id.tooltipfashion, checkfashion.getVisibility());
                if(checkfashion.getVisibility() == View.VISIBLE)
                    setTooltip(20, 24, R.id.tooltipfashion);
            }
        });
        checkfashion.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[20].setChecked(true);
                        checkcatg[21].setChecked(true);
                        checkcatg[22].setChecked(true);
                        checkcatg[23].setChecked(true);
                        checkcatg[24].setChecked(true);
                    }
                    else
                    {
                        checkcatg[20].setChecked(false);
                        checkcatg[21].setChecked(false);
                        checkcatg[22].setChecked(false);
                        checkcatg[23].setChecked(false);
                        checkcatg[24].setChecked(false);
                    }
                    setTooltip(20, 24, R.id.tooltipfashion);
                }
            }
        });
        checkcatg[20].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(20);
                else
                    c.remove(20);
            }
        });
        checkcatg[21].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(21);
                else
                    c.remove(21);
            }
        });
        checkcatg[22].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(22);
                else
                    c.remove(22);
            }
        });
        checkcatg[23].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(23);
                else
                    c.remove(23);
            }
        });
        checkcatg[24].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(24);
                else
                    c.remove(24);
            }
        });
        LinearLayout buttoncatg20 = (LinearLayout) findViewById(R.id.buttoncatg20);
        buttoncatg20.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[20].toggle();}});
        LinearLayout buttoncatg21 = (LinearLayout) findViewById(R.id.buttoncatg21);
        buttoncatg21.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[20].toggle();}});
        LinearLayout buttoncatg22 = (LinearLayout) findViewById(R.id.buttoncatg22);
        buttoncatg22.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[22].toggle();}});
        LinearLayout buttoncatg23 = (LinearLayout) findViewById(R.id.buttoncatg23);
        buttoncatg23.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[23].toggle();}});
        LinearLayout buttoncatg24 = (LinearLayout) findViewById(R.id.buttoncatg24);
        buttoncatg24.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[24].toggle();}});

        //#############################FURNITURE#######################

        setTooltip(25, 28, R.id.tooltipfurniture);
        checkfurniture = (CheckBox) findViewById(R.id.checkfurniture);
        checkcatg[25] = (CheckBox) findViewById(R.id.checkcatg25);
        if(c.checkSelected(25))
        {
            checkcatg[25].setChecked(true);
            checkfurniture.setChecked(true);
        }
        checkcatg[26] = (CheckBox) findViewById(R.id.checkcatg26);
        if(c.checkSelected(26))
        {
            checkcatg[26].setChecked(true);
            checkfurniture.setChecked(true);
        }
        checkcatg[27] = (CheckBox) findViewById(R.id.checkcatg27);
        if(c.checkSelected(27))
        {
            checkcatg[27].setChecked(true);
            checkfurniture.setChecked(true);
        }
        checkcatg[28] = (CheckBox) findViewById(R.id.checkcatg28);
        if(c.checkSelected(28))
        {
            checkcatg[28].setChecked(true);
            checkfurniture.setChecked(true);
        }
        LinearLayout buttonfurniture = (LinearLayout) findViewById(R.id.buttonfurniture);
        buttonfurniture.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenufurniture);
                if(checkfurniture.getVisibility() == View.GONE)
                {
                    if (checkcatg[25].isChecked() || checkcatg[26].isChecked() || checkcatg[27].isChecked() || checkcatg[28].isChecked())
                        checkfurniture.setChecked(true);
                    else
                        checkfurniture.setChecked(false);
                }
                toggleViewByID(R.id.checkfurniture);
                setViewByID(R.id.tooltipfurniture, checkfurniture.getVisibility());
                if(checkfurniture.getVisibility() == View.VISIBLE)
                    setTooltip(25, 28, R.id.tooltipfurniture);
            }
        });
        checkfurniture.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[25].setChecked(true);
                        checkcatg[26].setChecked(true);
                        checkcatg[27].setChecked(true);
                        checkcatg[28].setChecked(true);
                    }
                    else
                    {
                        checkcatg[25].setChecked(false);
                        checkcatg[26].setChecked(false);
                        checkcatg[27].setChecked(false);
                        checkcatg[28].setChecked(false);
                    }
                    setTooltip(25, 28, R.id.tooltipfurniture);
                }
            }
        });
        checkcatg[25].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(25);
                else
                    c.remove(25);
            }
        });
        checkcatg[26].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(26);
                else
                    c.remove(26);
            }
        });
        checkcatg[27].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(27);
                else
                    c.remove(27);
            }
        });
        checkcatg[28].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(28);
                else
                    c.remove(28);
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

        //#############################TECHNOLOGY#######################

        setTooltip(29, 32, R.id.tooltiptechnology);
        checktechnology = (CheckBox) findViewById(R.id.checktechnology);
        checkcatg[29] = (CheckBox) findViewById(R.id.checkcatg29);
        if(c.checkSelected(29))
        {
            checkcatg[29].setChecked(true);
            checktechnology.setChecked(true);
        }
        checkcatg[30] = (CheckBox) findViewById(R.id.checkcatg30);
        if(c.checkSelected(30))
        {
            checkcatg[30].setChecked(true);
            checktechnology.setChecked(true);
        }
        checkcatg[31] = (CheckBox) findViewById(R.id.checkcatg31);
        if(c.checkSelected(31))
        {
            checkcatg[31].setChecked(true);
            checktechnology.setChecked(true);
        }
        checkcatg[32] = (CheckBox) findViewById(R.id.checkcatg32);
        if(c.checkSelected(32))
        {
            checkcatg[32].setChecked(true);
            checktechnology.setChecked(true);
        }
        LinearLayout buttontechnology = (LinearLayout) findViewById(R.id.buttontechnology);
        buttontechnology.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenutechnology);
                if(checktechnology.getVisibility() == View.GONE)
                {
                    if (checkcatg[29].isChecked() || checkcatg[30].isChecked() || checkcatg[31].isChecked() || checkcatg[32].isChecked())
                        checktechnology.setChecked(true);
                    else
                        checktechnology.setChecked(false);
                }
                toggleViewByID(R.id.checktechnology);
                setViewByID(R.id.tooltiptechnology, checktechnology.getVisibility());
                if(checktechnology.getVisibility() == View.VISIBLE)
                    setTooltip(29, 32, R.id.tooltiptechnology);
            }
        });
        checktechnology.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[29].setChecked(true);
                        checkcatg[30].setChecked(true);
                        checkcatg[31].setChecked(true);
                        checkcatg[32].setChecked(true);
                    }
                    else
                    {
                        checkcatg[29].setChecked(false);
                        checkcatg[30].setChecked(false);
                        checkcatg[31].setChecked(false);
                        checkcatg[32].setChecked(false);
                    }
                    setTooltip(29, 32, R.id.tooltiptechnology);
                }
            }
        });
        checkcatg[29].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(29);
                else
                    c.remove(29);
            }
        });
        checkcatg[30].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(30);
                else
                    c.remove(30);
            }
        });
        checkcatg[31].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(31);
                else
                    c.remove(31);
            }
        });
        checkcatg[32].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(32);
                else
                    c.remove(32);
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

        //#############################PHONES#######################

        setTooltip(33, 34, R.id.tooltipphones);
        checkphones = (CheckBox) findViewById(R.id.checkphones);
        checkcatg[33] = (CheckBox) findViewById(R.id.checkcatg33);
        if(c.checkSelected(33))
        {
            checkcatg[33].setChecked(true);
            checkphones.setChecked(true);
        }
        checkcatg[34] = (CheckBox) findViewById(R.id.checkcatg34);
        if(c.checkSelected(34))
        {
            checkcatg[34].setChecked(true);
            checkphones.setChecked(true);
        }
        LinearLayout buttonphones = (LinearLayout) findViewById(R.id.buttonphones);
        buttonphones.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuphones);
                if(checkphones.getVisibility() == View.GONE)
                {
                    if (checkcatg[33].isChecked() || checkcatg[34].isChecked())
                        checkphones.setChecked(true);
                    else
                        checkphones.setChecked(false);
                }
                toggleViewByID(R.id.checkphones);
                setViewByID(R.id.tooltipphones, checkphones.getVisibility());
                if(checkphones.getVisibility() == View.VISIBLE)
                    setTooltip(33, 34, R.id.tooltipphones);
            }
        });
        checkphones.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[33].setChecked(true);
                        checkcatg[34].setChecked(true);
                    }
                    else
                    {
                        checkcatg[33].setChecked(false);
                        checkcatg[34].setChecked(false);
                    }
                    setTooltip(33, 34, R.id.tooltipphones);
                }
            }
        });
        checkcatg[33].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(33);
                else
                    c.remove(33);
            }
        });
        checkcatg[34].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(34);
                else
                    c.remove(34);
            }
        });
        LinearLayout buttoncatg33 = (LinearLayout) findViewById(R.id.buttoncatg33);
        buttoncatg33.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[33].toggle();}});
        LinearLayout buttoncatg34 = (LinearLayout) findViewById(R.id.buttoncatg34);
        buttoncatg34.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[34].toggle();}});

        //#############################OTHER#######################

        setTooltip(35, 35, R.id.tooltipothers);
        checkothers = (CheckBox) findViewById(R.id.checkothers);
        checkcatg[35] = (CheckBox) findViewById(R.id.checkcatg35);
        if(c.checkSelected(35))
        {
            checkcatg[35].setChecked(true);
            checkothers.setChecked(true);
        }
        LinearLayout buttonothers = (LinearLayout) findViewById(R.id.buttonothers);
        buttonothers.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toggleViewByID(R.id.submenuothers);
                if(checkothers.getVisibility() == View.GONE)
                {
                    if (checkcatg[35].isChecked())
                        checkothers.setChecked(true);
                    else
                        checkothers.setChecked(false);
                }
                toggleViewByID(R.id.checkothers);
                setViewByID(R.id.tooltipothers, checkothers.getVisibility());
                if(checkothers.getVisibility() == View.VISIBLE)
                    setTooltip(35, 35, R.id.tooltipothers);
            }
        });
        checkothers.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(buttonView.getVisibility() == View.VISIBLE)
                {
                    if (isChecked)
                    {
                        checkcatg[35].setChecked(true);
                    }
                    else
                    {
                        checkcatg[35].setChecked(false);
                    }
                    setTooltip(35, 35, R.id.tooltipothers);
                }
            }
        });
        checkcatg[35].setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isChecked)
                    c.add(35);
                else
                    c.remove(35);
            }
        });
        LinearLayout buttoncatg35 = (LinearLayout) findViewById(R.id.buttoncatg35);
        buttoncatg35.setOnClickListener(new View.OnClickListener()
        {public void onClick(View v){checkcatg[35].toggle();}});

        checkall = (CheckBox) findViewById(R.id.checkall);
        LinearLayout buttonall = (LinearLayout) findViewById(R.id.buttonall);
        buttonall.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkall.toggle();
            }
        });
        checkall.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
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

    private void setViewByID(int id, int visibility)
    {
        View view = findViewById(id);
        view.setVisibility(visibility);
    }

    private void setTooltip(int startID, int endID, int tooltipID )
    {
        TextView tooltipView = (TextView) findViewById(tooltipID);
        String tooltip = "";
        for(int i = startID; i <= endID; i++)
        {
            tooltip += c.returnIfSelected(i, tooltip);
        }
        if(tooltip.equals(""))
            tooltipView.setVisibility(View.GONE);
        else
        {
            tooltipView.setVisibility(View.VISIBLE);
            tooltipView.setText(tooltip);
        }
    }

    private void confirmSelection()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("categories", c.getSelected());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
