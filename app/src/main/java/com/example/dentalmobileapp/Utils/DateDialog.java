package com.example.dentalmobileapp.Utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.util.Calendar;

public class DateDialog {

    public static void openDateDialog(EditText editText, Context context) {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 1);

        DatePickerDialog dialog = new DatePickerDialog(context,
                (datePicker, year, month, day) -> {
                    String sYear = String.valueOf(year);
                    String sMonth = String.valueOf(month + 1);
                    String sDay = String.valueOf(day);

                    editText.setText(sMonth + "/" + sDay + "/" + sYear);

                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH)
        );

        // Set the minimum date to the current date
        dialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

        dialog.show();
    }
}
