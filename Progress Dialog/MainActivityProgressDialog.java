
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

public class MainActivityProgressDialog extends AppCompatActivity {

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_progress_dialog);
    }

    public void circle(View view) {
        ProgressDialog dialog;
        dialog = new ProgressDialog(MainActivityProgressDialog.this);
        dialog = ProgressDialog.show(MainActivityProgressDialog.this, "Loading...", "Loading, please wait...", false, false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        //dialog.dismiss(); to dismiss he dialog
    }

    public void horizontal(View view) {
        final ProgressDialog pdialog = new ProgressDialog(MainActivityProgressDialog.this);
        pdialog.setTitle("Upgrading Courses");
        pdialog.setMessage("Please wait...");
        pdialog.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
        pdialog.setMax(100);
        pdialog.setProgress(0);
        pdialog.getWindow().setGravity(Gravity.BOTTOM);
        pdialog.setCancelable(true);
        pdialog.setCanceledOnTouchOutside(true);
        pdialog.show();

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        public void run() {
                            pdialog.setProgress(i);
                        }
                    });

                    if (i == 100) {
                        pdialog.dismiss();
                    }
                }
            }
        }).start();
    }
}
