package com.radioline.master.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.SystemService;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class DispatchActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private EditText etComments;
    private Switch swCurrency;
    private Button btSendToServer;
    private TextView tvSum;
    private DatePicker dpDeliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        etComments = (EditText) findViewById(R.id.etComments);
        swCurrency = (Switch) findViewById(R.id.swCurrency);
        swCurrency.setChecked(false);
        swCurrency.setOnCheckedChangeListener(this);
        tvSum = (TextView) findViewById(R.id.tvSum);
        btSendToServer = (Button) findViewById(R.id.btSendToServer);
        setSumBasket(true);
        SystemService ss = new SystemService(this);
        if (!ss.isNetworkAvailable()) {
            btSendToServer.setEnabled(false);
        }

        dpDeliveryDate = (DatePicker) findViewById(R.id.dpDeliveryDate);
        dpDeliveryDate.setCalendarViewShown(false);
        Time now = new Time();
        now.setToNow();
        dpDeliveryDate.setMinDate(now.toMillis(false));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 14);
        dpDeliveryDate.setMaxDate(calendar.getTimeInMillis());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dispatch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSumBasket(final Boolean USD) {
        ParseQuery<Basket> query = Basket.getQuery();
        query.fromLocalDatastore();
        //query.whereGreaterThan("quantity", 0);
        query.findInBackground(new FindCallback<Basket>() {
            public void done(List<Basket> BasketList,
                             ParseException e) {
                if (e == null) {
                    double sumInBasket = 0;
                    for (Basket ibasket : BasketList) {
                        if (USD)
                            sumInBasket = sumInBasket + (ibasket.getQuantity() * ibasket.getRequiredpriceUSD());
                        else
                            sumInBasket = sumInBasket + (ibasket.getQuantity() * ibasket.getRequiredpriceUAH());
                    }
                    DecimalFormat dec = new DecimalFormat("0.00");
                    if (USD)
                        tvSum.setText("$ " + dec.format(sumInBasket));
                    else
                        tvSum.setText("₴ " + dec.format(sumInBasket));
                }
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.swCurrency:
                setSumBasket(!b);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSendToServer:
                SentToServer();
                break;
        }
    }

    private void SentToServer() {
        //Method SetOrder
        //Input Order
        /* ***************order***********
        OrderNumber
        PartnerId
        ContractId
        NewOrder
        Description
        DeliveryDate
        Table
        *******************Table***************
        * RowOrder
        ProductId
        ProductCode
        ProductArticle
        ProductPartNumber
        Quantity
        RequiredPrice
        *
         */
        SoapObject Order = new SoapObject("http://www.rl.ua", "items");
        Order.addProperty("PartnerId", "a27889a9-4e9f-11e2-8faf-00155d040a09");
        Order.addProperty("NewOrder", true);
        Order.addProperty("Description", etComments.getText().toString());
        //Order.
        PropertyInfo pi = new PropertyInfo();
        pi.setValue(Order);
        pi.setType(Order.getClass());
    }
}
