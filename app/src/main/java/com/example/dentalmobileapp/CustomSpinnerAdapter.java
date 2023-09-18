package com.example.dentalmobileapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String hint;

    public CustomSpinnerAdapter(Context context, int resource, String[] values, String hint) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
        this.hint = hint;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        // Customize the background color of Spinner dropdown items here (white)
        view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

        // Set padding for the dropdown items (left, top, right, bottom)
        view.setPadding(16, 16, 16, 16);

        // Customize the text color of the dropdown items (black)
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        // Customize the text color of the selected item here (black)
        ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.black));

        // Set the default item (hint) as the first item in the Spinner
        if (position == 0) {
            ((TextView) view).setText(hint);
        } else {
            // Display the selected item's text
            ((TextView) view).setText(values[position]);
        }

        return view;
    }
}
