package cn.wakafa.listview.Activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import cn.wakafa.listview.Fragment.Main_chat;
import cn.wakafa.listview.Fragment.Main_notice;
import cn.wakafa.listview.R;
import cn.wakafa.listview.Service.CheckService;

public class MainActivity extends AppCompatActivity {

    private Main_chat chatFragment;
    private Main_notice noticeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        changeFrag(noticeFragment,1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.upmenu,menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent stopIntent = new Intent(this, CheckService.class);
        stopService(stopIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settingitem:
                //Toast.makeText(this,"hello",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.addition:
                Toast.makeText(this,R.string.intro,Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFrag(noticeFragment,1);
                    return true;
                case R.id.navigation_dashboard:
                    changeFrag(chatFragment,2);
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(MainActivity.this, R.string.anticipate, Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    private void changeFrag(Fragment fragment, int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 1:
                if (fragment == null) {
                    noticeFragment = new Main_notice();
                    transaction.add(R.id.main_fragment, noticeFragment);
                } else {
                    transaction.show(fragment);
                }
                break;
            case 2:
                if (fragment == null) {
                    chatFragment = new Main_chat();
                    transaction.add(R.id.main_fragment, chatFragment);
                } else {
                    transaction.show(fragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (chatFragment != null) {
            transaction.hide(chatFragment);
        }
        if (noticeFragment != null) {
            transaction.hide(noticeFragment);
        }
    }
}
