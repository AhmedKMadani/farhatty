package com.farhatty.user.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.CallbackDialog;
import com.farhatty.user.Utiliti.DialogUtils;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.Utiliti.UtilMethods;
import com.farhatty.user.adapter.OrderAdapter;
import com.farhatty.user.model.OrdersList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class OrderHallActivity extends AppCompatActivity {



    private View parent_view;
    private Spinner payment;
    private ImageButton bt_date_res;
    private TextView date_res;
    private RecyclerView recyclerView;
    private MaterialRippleLayout lyt_add_cart;
    private TextView total_order, tax, price_tax, total_fees,_number;
    private TextInputLayout buyer_name_lyt, email_lyt, phone_lyt, address_lyt, comment_lyt;
    private EditText buyer_name, email, phone, address, comment;
    private String name_order ;
    private String email_order ;
    private String phone_order ;
    private String address_order;
    Double price ;
    Double pricesum ;

    private DatePickerDialog datePickerDialog;
    private Long date_res_millis = 0L;
    private Double _total_fees = 0D;
    private String _total_fees_str;
    ProgressDialog progressDialog = null;
    String Curreny;
    String view_hall_id;
    private RecyclerView recyclerVieworder;
    private OrderAdapter adapter;
    private List<OrdersList> orderListorder;
    String view_hall_price ;
    String random_number ;
    private PropertyInfo pi1;
    private PropertyInfo pi2;
    String Date_response;
    String TAG_re = "Response";
    int hall_id;
    SoapPrimitive resultString ;

    int upperBound = 10000000 ;
    int lowerBound = 99999999 ;
    ProgressDialog progress  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        view_hall_id =  getIntent().getStringExtra("hall_id_s") ;
        view_hall_price =  getIntent ().getStringExtra ( "hall_price_s" );
        hall_id = Integer.parseInt(view_hall_id);

        recyclerVieworder = (RecyclerView) findViewById(R.id.recyclerView);

        orderListorder = new ArrayList<>();
        adapter = new OrderAdapter ( this , orderListorder );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
        recyclerVieworder.setLayoutManager(mLayoutManager);
        recyclerVieworder.setItemAnimator(new DefaultItemAnimator ());
        recyclerVieworder.addItemDecoration(new MyDividerItemDecoration (this, DividerItemDecoration.VERTICAL, 36));
        recyclerVieworder.setAdapter(adapter);

        initToolbar();
        iniComponent();
        showService();


    }

    private void showService() {

        pricesum = 0.0;

        for (int i =0 ; i < ViewHallActivity.orderh.size () ; i++) {

         OrdersList odr =  new OrdersList (  );

         odr.setTitle ( ViewHallActivity.orderh.get ( i ).getTitle () );
         odr.setPrice ( ViewHallActivity.orderh.get ( i ).getService_Price () );

          price = Double.parseDouble ( ViewHallActivity.orderh.get ( i ).getService_Price () );
          pricesum = pricesum + price;
          orderListorder.add ( odr );
          adapter.notifyDataSetChanged ();

        }
    }


    private void initToolbar() {
        ActionBar actionBar;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.title_activity_checkout);

    }


    private void iniComponent() {
        parent_view = findViewById(android.R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lyt_add_cart = (MaterialRippleLayout) findViewById(R.id.lyt_add_cart);

        // cost view
        total_order = (TextView) findViewById(R.id.total_order);
        tax = (TextView) findViewById(R.id.tax);
        price_tax = (TextView) findViewById(R.id.price_tax);
        total_fees = (TextView) findViewById(R.id.total_fees);
        _number = (TextView) findViewById ( R.id.booking_number );

        // form view
        buyer_name = (EditText) findViewById(R.id.buyer_name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        comment = (EditText) findViewById(R.id.comment);

        buyer_name.addTextChangedListener(new CheckoutTextWatcher(buyer_name));
        email.addTextChangedListener(new CheckoutTextWatcher(email));
        phone.addTextChangedListener(new CheckoutTextWatcher(phone));
        address.addTextChangedListener(new CheckoutTextWatcher(address));
        comment.addTextChangedListener(new CheckoutTextWatcher(comment));

        buyer_name_lyt = (TextInputLayout) findViewById(R.id.buyer_name_lyt);
        email_lyt = (TextInputLayout) findViewById(R.id.email_lyt);
        phone_lyt = (TextInputLayout) findViewById(R.id.phone_lyt);
        address_lyt = (TextInputLayout) findViewById(R.id.address_lyt);
        comment_lyt = (TextInputLayout) findViewById(R.id.comment_lyt);
        payment = (Spinner) findViewById(R.id.payment);
        bt_date_res = (ImageButton) findViewById(R.id.bt_date_res);
        date_res = (TextView) findViewById(R.id.date_res);

        List<String> payment_list = new ArrayList<String>();
        payment_list.add("Cash");

        ArrayAdapter adapter_payemnt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payment_list);
        adapter_payemnt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payment.setAdapter(adapter_payemnt);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.title_please_wait);
        progressDialog.setMessage(getString(R.string.content_submit_checkout));

        bt_date_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePicker();
            }
        });

        lyt_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler ().postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        submitForm();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OrderHallActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.NO, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayData();
    }




    private void displayData() {
        getBookingNumber();
        setTotalPrice();
        }



    public static int randInt(int min, int max) {

        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    private void getBookingNumber() {

           int booking = randInt (upperBound,lowerBound);
           String random_numbe = String.valueOf ( booking );
           random_number = ("B-"+random_numbe);
          _number.setText ( random_number);


    }


    private void setTotalPrice() {

        Double _total_order = Double.valueOf(view_hall_price), _price_tax = 0D;
        String _total_order_str, _price_tax_str;

        _total_fees = _total_order + pricesum;
        _price_tax_str = String.format(Locale.US, "%1$,.2f", _price_tax);
        _total_order_str = String.format(Locale.US, "%1$,.2f", _total_order);
        _total_fees_str = String.format(Locale.US, "%1$,.2f", _total_fees);

        total_order.setText(_total_order_str + " " + getResources ().getString ( R.string.currency ) );
        tax.setText(getString(R.string.services_order)  + ":" );
        price_tax.setText( pricesum + " " + getResources ().getString ( R.string.currency ) );
        total_fees.setText(_total_fees_str + " " + getResources ().getString ( R.string.currency ) );

    }

    private void dialogDatePicker() {
        Calendar cur_calender = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int _year, int _month, int _day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, _year);
                calendar.set(Calendar.MONTH, _month);
                calendar.set(Calendar.DAY_OF_MONTH, _day);
                date_res_millis = calendar.getTimeInMillis();
                date_res.setText(getFormattedDateSimple(date_res_millis));
                datePickerDialog.dismiss();
                new CheckDate().execute (  );
            }
        }, cur_calender.get(Calendar.YEAR), cur_calender.get(Calendar.MONTH), cur_calender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setCancelable(true);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

   private static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return newFormat.format(new Date(dateTime));
    }



    private static String getFormattedDateSimplePosting(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        return newFormat.format(new Date(dateTime));
    }


    private class DoBooking extends AsyncTask <Void, Void, SoapObject> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG_re, "onPreExecute");
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            Log.i ( TAG_re, "doInBackground" );

            String SOAP_ACTION = "http://farhatty.sd/Booking";
            String METHOD_NAME = "Booking";
            String NAMESPACE = "http://farhatty.sd/";
            String URL = "http://farhatty.sd/WebService.asmx";

            SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );


            Request.addProperty ( "ID", hall_id );
            Request.addProperty ( "Code", random_number );
            Request.addProperty ( "name", name_order );
            Request.addProperty ( "phone", phone_order );
            Request.addProperty ( "email", email_order );
            Request.addProperty ( "date", getFormattedDateSimplePosting ( date_res_millis ) );
            Request.addProperty ( "address", address_order );
            Request.addProperty ( "price", pricesum );
            Request.addProperty ( "total", _total_fees );
            Request.addProperty ( "Ndays", hall_id );

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject ( Request );

            MarshalFloat md = new MarshalFloat ();
            MarshalDate mda = new MarshalDate ();

            md.register ( soapEnvelope );
            mda.register ( soapEnvelope );

            HttpTransportSE transport = new HttpTransportSE ( URL );

            try {

                transport.call ( SOAP_ACTION, soapEnvelope );
            } catch (Exception e) {
                e.printStackTrace ();
                return null;
            }

            try {
                SoapPrimitive resultStringBooking = (SoapPrimitive) soapEnvelope.getResponse ();
            } catch (Exception e) {
                e.printStackTrace ();
                return null;
            }
            return Request;
        }

    @Override
    protected void onPostExecute(SoapObject result) {
        Log.i(TAG_re, "onPostExecute");

        if(result == null){

            dialogFailedRetry ();

        }else {

            dialogSuccess(random_number);

        }


    }

}


    private boolean validateEmail() {
        String str = email.getText().toString().trim();
        if (str.isEmpty() || !UtilMethods.isValidEmail(str)) {
            email_lyt.setError(getString(R.string.invalid_email));
            requestFocus(email);
            return false;
        } else {
            email_lyt.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        String str = buyer_name.getText().toString().trim();
        if (str.isEmpty()) {
            buyer_name_lyt.setError(getString(R.string.invalid_name));
            requestFocus(buyer_name);
            return false;
        } else {
            buyer_name_lyt.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone() {
        String str = phone.getText().toString().trim();
        if (str.isEmpty()) {
            phone_lyt.setError(getString(R.string.invalid_phone));
            requestFocus(phone);
            return false;
        } else {
            phone_lyt.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateAddress() {
        String str = address.getText().toString().trim();
        if (str.isEmpty()) {
            address_lyt.setError(getString(R.string.invalid_address));
            requestFocus(address);
            return false;
        } else {
            address_lyt.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePayment() {
        int pos = payment.getSelectedItemPosition();
        return pos != 0;
    }

    private boolean validateResvertion() {
        return date_res_millis != 0L;
    }





    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




    private class CheckoutTextWatcher implements TextWatcher {
        private View view;

        private CheckoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.name:
                    validateName();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
                case R.id.address:
                    validateAddress();
                    break;
            }
        }
    }


    private void submitForm() {

        if (!validateName()) {
            Snackbar.make(parent_view, R.string.invalid_name, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!validateEmail()) {
            Snackbar.make(parent_view, R.string.invalid_email, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!validatePhone()) {
            Snackbar.make(parent_view, R.string.invalid_phone, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (!validateAddress()) {
            Snackbar.make(parent_view, R.string.invalid_address, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!validateResvertion ()) {
            Snackbar.make(parent_view, R.string.invalid_date_res, Snackbar.LENGTH_SHORT).show();
            return;

        } if (Date_response.equals ("1")){
              Log.e ( TAG_re,"Check Date validation " + Date_response);
            Snackbar.make(parent_view, R.string.invalid_date, Snackbar.LENGTH_LONG).show();

        }else {

            name_order = buyer_name.getText ().toString ();
            email_order = email.getText ().toString ();
            phone_order = phone.getText ().toString ();
            address_order = address.getText ().toString ();

            hideKeyboard ();

            dialogConfirmCheckout ();
        }
    }

    private void submitOrderData() {
        new DoBooking ().execute (  );
    }


    private void delaySubmitOrderData() {
        progressDialog.show();
        new Handler ().postDelayed( new Runnable() {
            @Override
            public void run() {
                submitOrderData();
            }
        }, 2000);
    }

    public void dialogConfirmCheckout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(getString(R.string.confirm_checkout));
        builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delaySubmitOrderData();
            }
        });
        builder.setNegativeButton(R.string.NO, null);
        builder.show();
    }

    public void dialogFailedRetry() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.failed);
        builder.setMessage(getString(R.string.failed_checkout));
        builder.setPositiveButton(R.string.TRY_AGAIN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delaySubmitOrderData();
            }
        });
        builder.setNegativeButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent (getApplicationContext(), MainActivity.class));
            }
        });
        builder.show();
    }


    public void dialogSuccess(String random_number) {
        progressDialog.dismiss();
        Dialog dialog = new DialogUtils(this).buildDialogInfo(
                getString(R.string.success_checkout),
                String.format(getString(R.string.msg_success_checkout), random_number),
                getString(R.string.OK),
                R.drawable.app_icon,
                new CallbackDialog() {
                    @Override
                    public void onPositiveClick(Dialog dialog) {
                       // finish();
                        startActivity(new Intent (getApplicationContext(), MainActivity.class));
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeClick(Dialog dialog) {
                    }
                });
        dialog.show();
    }




    private class CheckDate extends AsyncTask <Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog ( OrderHallActivity.this );
            progress.setCancelable ( false );
            progress.setTitle ( R.string.title_please_wait );
            progress.setMessage ( getString ( R.string.check_data ) );
            progress.show ();

            Log.i ( TAG_re, "onPreExecute" );
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i ( TAG_re, "doInBackground" );
            DateCheck ();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Date_response.equals ( "1" )) {
                progress.dismiss ();
                Log.e ( TAG_re, "Check Date validation " + Date_response );
                Snackbar.make ( parent_view, R.string.invalid_date, Snackbar.LENGTH_LONG ).show ();
            } else {
                progress.dismiss ();
                Log.e ( TAG_re, "Check Date validation " + Date_response );
                Log.i ( TAG_re, "onPostExecute" );
                Snackbar.make ( parent_view, R.string.valid_date, Snackbar.LENGTH_LONG ).show ();
            }

        }

        public void DateCheck() {
            String SOAP_ACTION = "http://farhatty.sd/CheckDate";
            String METHOD_NAME = "CheckDate";
            String NAMESPACE = "http://farhatty.sd/";
            String URL = "http://farhatty.sd/WebService.asmx";

            try {
                SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );

                Request.addProperty ( "Date", getFormattedDateSimplePosting ( date_res_millis ) );
                Request.addProperty ( "ID", hall_id );

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject ( Request );

                MarshalFloat md = new MarshalFloat ();
                MarshalDate mda = new MarshalDate ();

                md.register ( soapEnvelope );
                mda.register ( soapEnvelope );

                HttpTransportSE transport = new HttpTransportSE ( URL );

                transport.call ( SOAP_ACTION, soapEnvelope );

                resultString = (SoapPrimitive) soapEnvelope.getResponse ();
                Date_response = resultString.toString ();
                Log.e ( TAG_re, "Result Date Check : " + resultString );
            } catch (Exception ex) {
                Log.e ( TAG_re, "Error: " + ex.getMessage () );

            }
        }

    }
}



