package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;


public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener)getActivity());
        }

    }

    public static final String KEY_PREF_PLAYER_COUNT = "pref_player_count";

    private SettingsFragment fragment;
    private int resultCode = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch(key) {
            case KEY_PREF_PLAYER_COUNT:
                resultCode = RESULT_OK;
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(resultCode, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(resultCode, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragment.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fragment.getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
