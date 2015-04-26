package com.bobcode.splitexpense.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bobcode.splitexpense.R;
import com.bobcode.splitexpense.utils.MyUtils;


public class PayoutActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private TableLayout payoutTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pay Out");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[][] dataForRow = {{"Vijayan Anjalees name", "10\n[100]", "223", "23", "42", "[200]"},
                {"Senthil", "10\n[100]", "223", "23", "42", "130"},
                {"Amitesh Dixit", "10\n[100]", "223", "23", "42", "90"},
                {"Shanmuga", "10\n[100]", "223", "23", "42", "[200]"}};

        payoutTableLayout = (TableLayout) findViewById(R.id.payoutTableLayout);
        for (int dataIndex = 0; dataIndex < 4; dataIndex++) {
            TableRow tableRow1 = new TableRow(this);
            for (int i = 0; i <= 5; i++) {
                TextView textViewName = new TextView(this);
                textViewName.setText(dataForRow[dataIndex][i]);
                textViewName.setBackgroundResource(R.drawable.cell_shape);
                textViewName.setTextAppearance(this, R.style.textViewPayout);
                textViewName.setGravity(Gravity.CENTER);
                textViewName.setPadding(3, 3, 3, 10);
                textViewName.setLayoutParams(new TableRow.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
                tableRow1.addView(textViewName);
            }
            payoutTableLayout.addView(tableRow1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                MyUtils.myPendingTransitionLeftInRightOut(this);
                break;

            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MyUtils.myPendingTransitionLeftInRightOut(this);
    }

}
