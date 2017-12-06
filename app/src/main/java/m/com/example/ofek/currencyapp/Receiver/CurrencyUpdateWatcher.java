package m.com.example.ofek.currencyapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ofek on 06-Dec-17.
 */

public class CurrencyUpdateWatcher extends BroadcastReceiver {
    CurrencyChangeListener listener;

    public CurrencyUpdateWatcher(CurrencyChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onCurrencyChanged();
    }

    public interface CurrencyChangeListener{
        void onCurrencyChanged();
    }
}
