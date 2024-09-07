package com.example.colour;  // Replace with your package name

import static android.widget.Toast.makeText;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    ImageButton button;
    int defaultcolour;
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle toggle;
    private TextView actinst;
    View view;
    Float factor = 0.5f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the menu button to open the drawer
         menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Access the TextView from the drawer layout
        View headerView = navigationView.getHeaderView(0); // Assuming this is where the TextView is located
        actinst = headerView.findViewById(R.id.actinst);
int count = 0;
navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_instructions) {
                // Handle instructions click
                TextView actinst = findViewById(R.id.actinst);  // Get reference to text view
                if (actinst != null) {
                    if (actinst.getVisibility() == View.GONE) {  // Check current visibility
                        actinst.setVisibility(View.VISIBLE);
                    } else {
                        actinst.setVisibility(View.GONE);
                    }
                }
            } else {
                // Hide actinst for other menu items
                TextView actinst = findViewById(R.id.actinst);
                if (actinst != null) {
                    actinst.setVisibility(View.GONE);
                }
            }
            return true;
        });
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton radioButton1 = findViewById(R.id.hex);
        RadioButton radioButton2 = findViewById(R.id.pick);

        radioGroup.setOnCheckedChangeListener((group, checkedId)->{
            if(checkedId == radioButton1.getId()){
                findViewById(R.id.editText).setEnabled(true);
                findViewById(R.id.pick_color_button).setEnabled(false);
            }
            else if(checkedId == radioButton2.getId()){
                findViewById(R.id.pick_color_button).setEnabled(true);
                findViewById(R.id.editText).setEnabled(false);
            }
            else{
                findViewById(R.id.editText).setEnabled(false);

                findViewById(R.id.pick_color_button).setEnabled(false);

            }
            });


        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                   factor = setfactor(i);
                applyTint(findViewById(R.id.preview_tint_color), defaultcolour, factor);
                applyshade(findViewById(R.id.preview_shade_color), defaultcolour, factor);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button = findViewById(R.id.pick_color_button);
        defaultcolour = ContextCompat.getColor(MainActivity.this, com.google.android.material.R.color.design_default_color_primary) ;
        view = findViewById(R.id.preview_selected_color);

        String haha = "#FFFFFF";

        view.setBackgroundColor(Color.parseColor(haha));
        TextView textView = findViewById(R.id.ogtext);
        sethex(Color.parseColor(haha),textView);
        applyTint(findViewById(R.id.preview_tint_color), Color.parseColor(haha), factor);
        applyshade(findViewById(R.id.preview_shade_color), Color.parseColor(haha), factor);
        applycomp(findViewById(R.id.preview_comp_color),Color.parseColor(haha));

        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String hexcolor = editText.getText().toString();
                if(hexcolor.length()==7||hexcolor.length()==9 ){
                       try {
                           int color;
                           color = Color.parseColor(hexcolor);
                           defaultcolour = color;
                           view.setBackgroundColor(defaultcolour);

                           TextView textView = findViewById(R.id.ogtext);
                           sethex(color,textView);
                           applyTint(findViewById(R.id.preview_tint_color), defaultcolour, factor);
                           applyshade(findViewById(R.id.preview_shade_color), defaultcolour, factor);
                           applycomp(findViewById(R.id.preview_comp_color), defaultcolour);
                       }
                       catch (Exception e){
                       Toast.makeText(MainActivity.this,"Enter valid input",Toast.LENGTH_LONG).show();
                       }
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });
    }

    public float setfactor(int i){
        factor = (float) (i/100.0f);
        return factor;

    }

    public int getTintColor(int color, float tintFactor) {
        // Ensure tintFactor is between 0 and 1
        tintFactor = Math.max(0, Math.min(tintFactor, 1));
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        // Blend the color with white
        int r = (int) (red + (255 - red) * tintFactor);
        int g = (int) (green + (255 - green) * tintFactor);
        int b = (int) (blue + (255 - blue) * tintFactor);

        return Color.argb(alpha, r, g, b);
    }


    public void applyTint(View view, int color, float tintFactor) {
        int tintedColor = getTintColor(color, tintFactor);
        view.setBackgroundColor(tintedColor);
        TextView textView = findViewById(R.id.tinttext);
        sethex(tintedColor,textView);
    }



    public int getshade(int color, float shadeFactor){
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int r = (int) (red * (1 - shadeFactor));
        int g = (int) (green * (1 - shadeFactor));
        int b = (int) (blue * (1 - shadeFactor));

        return Color.argb(alpha, r, g, b);
    }
    public void applyshade(View view, int color, float shadeFactor) {

        int shadedColor = getshade(color, shadeFactor);
            view.setBackgroundColor(shadedColor);
           TextView textView = findViewById(R.id.shadetext);
        sethex(shadedColor,textView);
    }

    public int getcomp(int color){

        int alpha = 255;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int r = (int) (255 - red );

        int g = (int) (255 - green );
        int b = (int) (255 - blue );
        Log.d("ColorDebug", "Original Color - R: " + red + " G: " + green + " B: " + blue);
        Log.d("ColorDebug", "Complementary Color - R: " + r + " G: " + g + " B: " + b);

        return Color.argb(alpha,r,g,b);
    }
    public void applycomp(View view,int color){
        int compcol = getcomp(color);
        view.setBackgroundColor(compcol);
        TextView textView = findViewById(R.id.comptext);
        sethex(compcol,textView);
    }

    public void openColorPicker(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultcolour, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {


                defaultcolour = color;
                view.setBackgroundColor(defaultcolour);
                TextView textView = findViewById(R.id.ogtext);
                sethex(defaultcolour, textView);

                applyTint(findViewById(R.id.preview_tint_color), defaultcolour, factor);
                applyshade(findViewById(R.id.preview_shade_color), defaultcolour, factor);
                applycomp(findViewById(R.id.preview_comp_color)   ,defaultcolour);
            }
        });
        ambilWarnaDialog.show();
    }
    public void sethex(int color, TextView funchaha) {
        String hexCode = String.format("#%06X", (0xFFFFFF & color));  // Convert color int to hex string
        funchaha.setText(hexCode);
    }





}
