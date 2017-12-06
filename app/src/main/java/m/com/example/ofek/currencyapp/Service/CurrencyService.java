package m.com.example.ofek.currencyapp.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import m.com.example.ofek.currencyapp.MainActivity;


public class CurrencyService extends IntentService {

    private static final String ACTION_UPDATE_USD = "m.com.example.ofek.currencyapp.action.USD";
    private static final String ACTION_UPDATE_EURO = "m.com.example.ofek.currencyapp.action.EURO";
    public static final String CURRENCY_TAG = "currency_pref";
    private SharedPreferences sharedPreferences;
    private static final String USD_REQUEST_URL="https://free.currencyconverterapi.com/api/v5/convert?q=USD_ILS&compact=y";
    private static final String EUR_REQUEST_URL="https://free.currencyconverterapi.com/api/v5/convert?q=EUR_ILS&compact=y";
    RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences(CURRENCY_TAG,MODE_PRIVATE);
        requestQueue= Volley.newRequestQueue(this);
    }

    public CurrencyService() {
        super("CurrencyUpdateService");
    }

    /**
     * Starts this service to perform action currency update. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startUpdateUSD(Context context) {
        Intent intent = new Intent(context, CurrencyService.class);
        intent.setAction(ACTION_UPDATE_USD);
        context.startService(intent);
    }



    public static void startUpdateEURO(Context context) {
        Intent intent = new Intent(context, CurrencyService.class);
        intent.setAction(ACTION_UPDATE_EURO);
        context.startService(intent);
    }
    public static void startUpdateBoth(Context context){
        Intent intent = new Intent(context, CurrencyService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action!=null&&ACTION_UPDATE_USD.equals(action)) {
                updateUSD();
            } else if (action!=null&&ACTION_UPDATE_EURO.equals(action)) {
                updateEURO();
            }
            else {
                updateEURO();
                updateUSD();
            }
        }
    }
    Response.Listener<String> responseUSD=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.v("Requst Response:", response);
            try {
                JSONObject responseJson=new JSONObject(response);
                Double current=responseJson.getJSONObject("USD_ILS").getDouble("val");
                sharedPreferences.edit().putString("USD",String.valueOf(current)).commit();
                LocalBroadcastManager.getInstance(CurrencyService.this).sendBroadcast(new Intent(MainActivity.ACTION_CURRENCY_UPDATED));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void updateUSD() {
        StringRequest request=new StringRequest(Request.Method.GET, USD_REQUEST_URL, responseUSD, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Requst Response:", error.getMessage());
            }
        });
        requestQueue.add(request);
    }
    Response.Listener<String> responseEUR=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.v("Requst Response:", response);
            try {
                JSONObject responseJson=new JSONObject(response);
                Double current=responseJson.getJSONObject("EUR_ILS").getDouble("val");
                sharedPreferences.edit().putString("EUR",String.valueOf(current)).commit();
                LocalBroadcastManager.getInstance(CurrencyService.this).sendBroadcast(new Intent(MainActivity.ACTION_CURRENCY_UPDATED));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void updateEURO() {
        StringRequest request=new StringRequest(Request.Method.GET, EUR_REQUEST_URL, responseEUR, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Requst Response:", error.getMessage());
            }
        });
        requestQueue.add(request);
    }
}
