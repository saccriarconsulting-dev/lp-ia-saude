package com.axys.redeflexmobile.ui.component;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.axys.redeflexmobile.R;

/**
 * Created by Desenvolvimento on 21/04/2016.
 */
public class NumberPicker extends LinearLayout {
    private int counter;
    private ImageButton add;
    private ImageButton sub;
    private EditText display;
    LinearLayout content;
    public NumberPickerListener myNumberPickerListener;

    public NumberPicker(Context context) {
        super(context);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        counter = 1;
        String orientacao = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "orientation");

        if (orientacao.equals("1")) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.number_picker_vertical, this, true);

            content = (LinearLayout) getChildAt(0);

            add = (ImageButton) content.getChildAt(0);
            display = (EditText) content.getChildAt(1);
            sub = (ImageButton) content.getChildAt(2);

        } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.number_picker_horizontal, this, true);

            content = (LinearLayout) getChildAt(0);

            sub = (ImageButton) content.getChildAt(0);
            display = (EditText) content.getChildAt(1);
            add = (ImageButton) content.getChildAt(2);
        }
        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (counter <= 998) {
                    counter++;
                    display.setText("" + counter);

                    if (myNumberPickerListener != null) {
                        myNumberPickerListener.OnChangeNumber(counter);
                    }
                }
            }

        });


        sub.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (counter >= 2) {
                    counter--;
                    display.setText("" + counter);

                    if (myNumberPickerListener != null) {
                        myNumberPickerListener.OnChangeNumber(counter);
                    }
                }
            }
        });

        display.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int i = Integer.parseInt(s.toString());
                    counter = i;
                } catch (Exception e) {
                    s.clear();
                    s.append("" + counter);
                }
            }
        });
    }

    public int getNumber() {
        return counter;
    }

    public void setNumber(int number) {
        counter = number;
        display.setText("" + counter);
    }

    public void setOnNumberPickerListener(NumberPickerListener event) {
        this.myNumberPickerListener = event;
    }

    private String GetAttributeStringValue(Context context, AttributeSet attrs, String namespace, String name, String defaultValue) {
        //Get a reference to the Resources
        Resources res = context.getResources();
        //Obtain a  String from the attribute
        String stringValue = attrs.getAttributeValue(namespace, name);
        //If the String is null
        if (stringValue == null) {
            //set the return String to the default value, passed as a parameter
            stringValue = defaultValue;
        }
        /*//The String isn't null, so check if it starts with '@' and contains '@string/'
        else if(stringValue.length() > 1 && stringValue.charAt(0) == '@' &&  stringValue.contains("@string/") )
        {
            //Get the integer identifier to the String resource
            final int id = res.getIdentifier(context.getPackageName() + ":" + stringValue.substring(1), null, null);
            //Decode the string from the obtained resource ID
            stringValue = res.getString(id);
        }
        //Return the string value  */
        return stringValue;
    }
}