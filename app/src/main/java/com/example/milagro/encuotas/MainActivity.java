package com.example.milagro.encuotas;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
  private final static String LOG_KEY = MainActivity.class.getSimpleName();
  private final static String CURRENCY_SERVICE_URL = "https://openexchangerates.org/api/latest.json?app_id=adfb8bedc38e49428a97b6838e8f9f56";
  public final static Double DEFAULT_INFLATION = 0.3;
  public final static Integer DEFAULT_SHARE_NUMBER = 3;
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Fill with the predefined values
    EditText inflationInput = (EditText) findViewById(R.id.inflation_input);
    inflationInput.setText(DEFAULT_INFLATION.toString());

    EditText shareNumberInput = (EditText) findViewById(
        R.id.share_number_input);
    shareNumberInput.setText(DEFAULT_SHARE_NUMBER.toString());

    fillUsdValue((EditText) findViewById(R.id.usd_value_input));

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  // Creates the new fragments for the cash and share final prices.
  private void setPrices(Purchase cashPurchase, Purchase sharePurchase) {

    // Create a new Fragment to be placed in the activity layout
    PurchaseFragment leftFragment = new PurchaseFragment().newInstance(
        cashPurchase);
    // Create a new Fragment to be placed in the activity layout
    PurchaseFragment rightFragment = new PurchaseFragment().newInstance(
        sharePurchase);

    // Add the fragment to the 'fragment_container' FrameLayout
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container_left, leftFragment).commit();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container_right, rightFragment).commit();
  }

  // Request the USD value to the external service and then call setPrices.
  private void fillUsdValue(final EditText usdValueInput) {
    // TODO(milit) Add a cache.
    RequestQueue queue = Volley.newRequestQueue(this);
    // Listeners
    Response.Listener<JSONObject> respListener = new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        // Save the response into the external variable
        try {
          Double usdValue = response.getJSONObject("rates").getDouble("ARS");
          Log.i(LOG_KEY, usdValue.toString());
          DecimalFormat format = new DecimalFormat("#.00");
          String usdValueString = format.format(usdValue);
          usdValueInput.setText(usdValueString.replace(',', '.'));
        } catch (JSONException exception) {
          Toast.makeText(getBaseContext(), R.string.error_warning,
              Toast.LENGTH_SHORT).show();
          Log.e(LOG_KEY, exception.toString());
          return;
        }
      }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), R.string.no_connection_warning,
            Toast.LENGTH_LONG).show();
      }
    };
    // Request a string response from the provided URL.
    JsonObjectRequest stringRequest = new JsonObjectRequest(
        CURRENCY_SERVICE_URL, null, respListener, errorListener);
    queue.add(stringRequest);
  }

  public void calculate(View view) {
    Log.i(LOG_KEY, "Pressing button");

    try {
      Double price = this.getValue((EditText) findViewById(R.id.price_input),
          R.string.no_price_warning, 0.0);
      if (price == 0.0) {
        Toast.makeText(getBaseContext(), R.string.zero_price_warning,
            Toast.LENGTH_SHORT).show();
        return;
      }

      Double inflation = this.getValue(
          (EditText) findViewById(R.id.inflation_input), 0, 0.0);

      Integer shareNumber = this.getValue(
          (EditText) findViewById(R.id.share_number_input), 0, 0.0).intValue();

      Double usdValue = this.getValue(
          (EditText) findViewById(R.id.usd_value_input), 0, 1.0);
      setPrices(new Purchase(inflation, price, usdValue, 0,
              getResources().getString(R.string.in_cash)),
          new Purchase(inflation, price, usdValue, shareNumber,
              getResources().getString(R.string.in_shares)));

    } catch (NumberFormatException exception) {
      Toast.makeText(getBaseContext(), R.string.invalid_number_warning,
          Toast.LENGTH_LONG).show();
    }
  }

  // Returns the value of the price_input EditText.
  // If the field is empty or is zero, shows a warning and returns 0.0
  private Double getValue(EditText input, int missingError,
                          Double defaultValue) {
    String priceStr = input.getText().toString();
    Double price = defaultValue;
    if (priceStr.equals("")) {
      if (missingError != 0) {
        Toast.makeText(getBaseContext(), missingError,
            Toast.LENGTH_SHORT).show();
      }
    } else {
      price = Double.parseDouble(priceStr);
    }
    return price;
  }

  @Override
  public void onStart() {
    super.onStart();

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client.connect();
    Action viewAction = Action.newAction(
        Action.TYPE_VIEW, // TODO: choose an action type.
        "Main Page", // TODO: Define a title for the content shown.
        // TODO: If you have web page content that matches this app activity's content,
        // make sure this auto-generated web page URL is correct.
        // Otherwise, set the URL to null.
        Uri.parse("http://host/path"),
        // TODO: Make sure this auto-generated app URL is correct.
        Uri.parse("android-app://com.example.milagro.encuotas/http/host/path")
    );
    AppIndex.AppIndexApi.start(client, viewAction);
  }

  @Override
  public void onStop() {
    super.onStop();

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    Action viewAction = Action.newAction(
        Action.TYPE_VIEW, // TODO: choose an action type.
        "Main Page", // TODO: Define a title for the content shown.
        // TODO: If you have web page content that matches this app activity's content,
        // make sure this auto-generated web page URL is correct.
        // Otherwise, set the URL to null.
        Uri.parse("http://host/path"),
        // TODO: Make sure this auto-generated app URL is correct.
        Uri.parse("android-app://com.example.milagro.encuotas/http/host/path")
    );
    AppIndex.AppIndexApi.end(client, viewAction);
    client.disconnect();
  }
}
