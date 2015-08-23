package com.modernart.modernartui;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

/**
 * ModernArtUI MainActivity showing five rectangles with different colors
 * @author xentres
 */
public class ModernArtActivity extends AppCompatActivity {

    // Create all fiels
    private SeekBar colorBar = null;
    private View greenView = null;
    private View blueView = null;
    private View redView = null;
    private View whiteView = null;
    private View yellowView = null;
    private int greenDef = 0;
    private int blueDef = 0;
    private int redDef = 0;
    private int yellowDef = 0;

    /**
     * Called on StartUp
     * Setup the main view and register all listeners including the color change bar (seekBar)
     *
     * @param savedInstanceState Reinitialisation of the GUI of previously shut down
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art);

        // setup all views
        greenView = findViewById(R.id.view1);
        blueView = findViewById(R.id.view2);
        redView = findViewById(R.id.view3);
        whiteView = findViewById(R.id.view4);
        yellowView = findViewById(R.id.view5);

        // retrieve the default color of all view elements
        greenDef = ((ColorDrawable) greenView.getBackground()).getColor();
        blueDef = ((ColorDrawable) blueView.getBackground()).getColor();
        redDef = ((ColorDrawable) redView.getBackground()).getColor();
        yellowDef = ((ColorDrawable) yellowView.getBackground()).getColor();

        // setup the seekBar
        colorBar = (SeekBar) findViewById(R.id.seekBar);

        // color change listener and color change methods
        colorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setNewBackgroundColor(greenView, greenDef, progress);
                setNewBackgroundColor(blueView, blueDef, progress);
                setNewBackgroundColor(redView, redDef, progress);
                setNewBackgroundColor(yellowView,yellowDef, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing TODO
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing TODO
            }

            // set hsv colors instead of RGB
            private void setNewBackgroundColor(View view, int defaultColor, int progress) {
                float[] hsvColor = new float[3];
                Color.colorToHSV(defaultColor, hsvColor);
                hsvColor[0] = hsvColor[0] + progress;
                hsvColor[0] = hsvColor[0] % 360;
                view.setBackgroundColor(Color.HSVToColor(Color.alpha(defaultColor), hsvColor));
            }
        });
    }

    /**
     * Create options menu with at least one item
     * @param menu the menu
     * @return after creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modern_art, menu);
        return true;
    }

    /**
     * Display fragment after clicking the more information menu item
     * @param item the menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.more_information) {
            DialogFragment infoFragment = new InfoFragment();
            infoFragment.show(getFragmentManager(), "info");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
