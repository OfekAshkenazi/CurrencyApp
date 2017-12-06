package m.com.example.ofek.currencyapp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import m.com.example.ofek.currencyapp.Receiver.CurrencyUpdateWatcher;
import m.com.example.ofek.currencyapp.Service.CurrencyService;

import static m.com.example.ofek.currencyapp.Service.CurrencyService.CURRENCY_TAG;

public class MainActivity extends AppCompatActivity implements CurrencyUpdateWatcher.CurrencyChangeListener {
    SharedPreferences preferences;
    Button euroBtn,usdBtn,bothBtn,clearBtn;
    ImageButton refreshBtn;
    TextView usdTV,eurTV;
    public static final String ACTION_CURRENCY_UPDATED = "m.com.example.ofek.currencyapp.action.CURRENCY_UPDATED";
    private CurrencyUpdateWatcher currencyWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyWatcher=new CurrencyUpdateWatcher(this);
        setContentView(R.layout.activity_main);
        preferences=getSharedPreferences(CURRENCY_TAG,MODE_PRIVATE);
        findViews();
        setListeners();
        setValues();
        LocalBroadcastManager.getInstance(this).registerReceiver(currencyWatcher,new IntentFilter(ACTION_CURRENCY_UPDATED));
    }

    private void setValues() {
        String usdVal=preferences.getString("USD","No Currency Found");
        String eurVal=preferences.getString("EUR","No Currency Found");
        usdTV.setText("USD: "+usdVal);
        eurTV.setText("EUR: "+eurVal);
    }

    private void findViews() {
        euroBtn=findViewById(R.id.euroBtn);
        usdBtn=findViewById(R.id.usdBtn);
        bothBtn=findViewById(R.id.bothBtn);
        usdTV=findViewById(R.id.usdTV);
        eurTV=findViewById(R.id.euroTV);
        refreshBtn=findViewById(R.id.refreshBtn);
        clearBtn=findViewById(R.id.clearBtn);
    }

    private void setListeners() {
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().clear().commit();
            }
        });
        euroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrencyService.startUpdateEURO(view.getContext());
            }
        });
        usdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrencyService.startUpdateUSD(view.getContext());
            }
        });
        bothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrencyService.startUpdateBoth(view.getContext());
            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (currencyWatcher!=null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(currencyWatcher);
            currencyWatcher=null;
        }
        stopService(new Intent(this,CurrencyService.class));
        super.onDestroy();
    }

    @Override
    public void onCurrencyChanged() {
        setValues();
    }
}
