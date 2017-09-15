package com.example.baccalculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private double weight;
    private boolean gender; // 0 == f, 1 == m
    private int seekStep = 5;
    private double bac;
    private SeekBar seekBar;
    private RadioGroup radioGroup;
    private TextView lblStatus;
    private Button btnSave,btnAddDrinks,btnReset;
    private ProgressBar pb;
    private EditText weightTxt;
    private Switch genderSwitch;
    private TextView lblBacLevel;

    private void SetTheAppIcon()
    {
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    getSupportActionBar().setDisplayUseLogoEnabled(true);
    }


    private void SetTextOfSeek(int progress)
    {
    TextView seekBarValue = (TextView)findViewById(R.id.textViewSeekVal);
    seekBarValue.setText(String.valueOf(progress) + "%");
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void SetDefaults()
    {
        SetStatusLabel(R.string.lblSafe,R.drawable.greenrounded);
        pb.setProgress(0);
        seekBar.setProgress(seekStep);
        SetTextOfSeek(seekStep);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default values set up
        SetTheAppIcon();  // set the application Icon

        lblStatus = (TextView) findViewById(R.id.textViewStatus);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAddDrinks = (Button) findViewById(R.id.btnAddDrink);
        btnReset = (Button) findViewById(R.id.btnReset);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        btnSave.setOnClickListener(this);
        btnAddDrinks.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        SetDefaults();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/seekStep ))*seekStep;
                seekBar.setProgress(progress);
                SetTextOfSeek(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        weightTxt = (EditText) findViewById(R.id.editTextWeight);
        genderSwitch = (Switch) findViewById(R.id.switchGender);
        lblBacLevel = (TextView) findViewById(R.id.textViewBACLevel);

        switch (v.getId()) {
            case R.id.btnSave:

                if(weightTxt.getText().toString().matches("") ) {
                    weightTxt.setError(getString(R.string.lblErrorForWeight));
                }else{
                    weight = Double.parseDouble(weightTxt.getText().toString());
                    gender = genderSwitch.isChecked();
                    if(bac != 0.0)
                    CalculateBAC();
                }
                break;
            case R.id.btnAddDrink:

                if (weight != 0.0) {
                    CalculateBAC();
                }
                else
                {
                    weightTxt.setError(getString(R.string.lblErrorForWeight));
                }
                break;

                    case R.id.btnReset:

                        btnAddDrinks.setEnabled(true);
                        btnSave.setEnabled(true);
                        weightTxt.setText("");
                        genderSwitch.setChecked(false);
                        RadioButton rb = (RadioButton) findViewById(R.id.radioButton1oz);
                        rb.setChecked(true);
                        lblBacLevel.setText("0.00");
                        bac = 0.0;
                        weight = 0.0;

                        SetDefaults();
                        break;
                    default:
                        break;
                }

    }


    private void ShowToast()
    {
        Context context = getApplicationContext();
        String text = "No more drinks for you.";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void CalculateBAC()
    {

        Gender g = new Gender(gender);
        double alcohol = Double.parseDouble(String.valueOf(this.seekBar.getProgress())) / 100;
        double a = GetDrinkSize() * (alcohol);
        double r = g.GetRValue(g);

        Log.d("demo",String.valueOf(r));

        bac = bac + (a * 6.24 / (weight * r));

        DecimalFormat df = new DecimalFormat("0.00");
        double roundedValue =round(bac,2);
        lblBacLevel.setText(String.valueOf(roundedValue));

        pb.setProgress((int) (roundedValue * 100));

        if (roundedValue < 0.08) {
            SetStatusLabel(R.string.lblSafe,R.drawable.greenrounded);
        } else if (roundedValue > 0.08 & roundedValue < 0.20) {
            SetStatusLabel(R.string.lblCarefull,R.drawable.yellowrounded);

        } else if (roundedValue > 0.20 && roundedValue < 0.25) {
            SetStatusLabel(R.string.lblOfflimit,R.drawable.redrounded);
        }
        else if(roundedValue >= 0.25)
        {
            SetStatusLabel(R.string.lblOfflimit,R.drawable.redrounded);

            btnAddDrinks.setEnabled(false);
            btnSave.setEnabled(false);

            ShowToast();
        }
    }

    private void SetStatusLabel(int id1, int id2)
    {
        lblStatus.setText(id1);
        lblStatus.setBackgroundResource(id2);
    }

    private int GetDrinkSize()
    {
        radioGroup = (RadioGroup)findViewById(R.id.rGroup);

        switch (radioGroup.getCheckedRadioButtonId())
        {
            case R.id.radioButton5oz:
                return 5;
            case R.id.radioButton12Oz:
                return 12;
            default:
                return 1;
        }
    }

}
