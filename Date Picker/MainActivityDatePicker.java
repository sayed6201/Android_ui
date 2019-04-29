
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivityDatePicker extends AppCompatActivity {

    Button showFragment;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_date_picker);

        showFragment = (Button) findViewById(R.id.btnDatePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        /**Check If Android Version is Greater Than or equals to Oreo Then directly register a listener
         *
         * Else If not Then Get a Calendar Instance first before getting selected date, month, year from Calendar Class**/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**Set Date Change Listener If current version is Greater or Equal to Oreo**/
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Toast.makeText(MainActivityDatePicker.this, "picked date is " + datePicker.getDayOfMonth() +
                            " / " + (datePicker.getMonth() + 1) +
                            " / " + datePicker.getYear(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            /**Set Date Change Listener If current version is Lower Than Oreo**/

            /**Get a Instance of Calendar Class**/
            Calendar calendar = Calendar.getInstance();
            /**Init DatePicker**/
            datePicker.init(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            /**Show Toast**/
                            Toast.makeText(MainActivityDatePicker.this, "picked date is " + datePicker.getDayOfMonth() +
                                    " / " + (datePicker.getMonth() + 1) +
                                    " / " + datePicker.getYear(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showFragment(View view) {
        /**Show DialogFragment By Calling DatePickerFragment Class**/
        DialogFragment newFragment = new DatePickerFragment();
        /**Call show() of DialogFragment class**/
        newFragment.show(getSupportFragmentManager(), "date picker");
    }
}
