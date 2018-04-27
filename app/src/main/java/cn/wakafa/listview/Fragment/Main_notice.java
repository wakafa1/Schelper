package cn.wakafa.listview.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.wakafa.listview.Activity.WebViewActivity;
import cn.wakafa.listview.Others.Note;
import cn.wakafa.listview.Others.NoteAdapter;
import cn.wakafa.listview.R;
import cn.wakafa.listview.Service.CheckService;

/**
 * Created by wakafa on 2017/6/11.
 */

public class Main_notice extends android.support.v4.app.Fragment {

    private List<Note> noteList = new ArrayList<Note>();
    ArrayList<String> urlString = new ArrayList<String>();
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    public Socket socket;
    public int port = 1020;
    String seraddress = "118.89.169.100";
    public boolean serviceOn = false;
    public int firstopenON = 1;
    public int firstrefresh = 1;
    public View view;
    public static final int UPDATE_TEXT = 1;
    public int ret;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Configure UI
        view = inflater.inflate(R.layout.main_notes, container, false);
        textView = (TextView)view.findViewById(R.id.uptext);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int c = refresh(2);
                Log.d("hello", String.valueOf(c));
                if (firstopenON == 1) {
                    textView.setVisibility(View.GONE);
                }
            }
        });

        int b = refresh(1);
        firstrefresh = 2;
        Log.d("hello2", String.valueOf(b));

        // Other works to be done
        getPredata();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            firstopenON = sharedPref.getInt("firstopen", 1);
        } catch (Exception e) {}
        if (firstopenON == 1) {
            textView.setText(R.string.first_use);
            SharedPreferences.Editor mEditor = sharedPref.edit();
            mEditor.putInt("firstopen",0);
            mEditor.apply();
        } else {
            textView.setVisibility(View.GONE);
        }

        // Start service
        if (serviceOn) {
            Intent startIntent = new Intent(getActivity(), CheckService.class);
            getActivity().startService(startIntent);
        }
        return view;
    }

    private int refresh(int index){
        urlString.clear();
        noteList.clear();
        //textView.setText(R.string.welcome_text);
        //ret = initLists();
        if (index == 1) {
            swipeRefreshLayout.setRefreshing(true);
            new getOnlineMsg().execute();
            Log.d("helloret", String.valueOf(ret));
            return ret;
        } else  {
            new getOnlineMsg().execute();
            Log.d("helloret", String.valueOf(ret));
            return ret;
        }
    }

    // Collect data stored in Android perminently (by key)
    private void getPredata() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            port = Integer.parseInt(sharedPref.getString("port_text", ""));
            serviceOn = sharedPref.getBoolean("remind_switch",false);
        } catch (Exception e) {}
    }

    private void changeUI(){
        NoteAdapter adapter = new NoteAdapter(getActivity(), R.xml.note_item, noteList);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Note note = noteList.get(position);
                //Toast.makeText(MainActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
                int index = note.getIndex();
                String data = urlString.get(index);
                Intent intent = new Intent(getActivity(), WebViewActivity.class); // Call WebView
                intent.putExtra("url", data);
                startActivity(intent);
            }
        });
    }

    private class getOnlineMsg extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            return initLists();
        }

        // Do the following code after the synctask
        @Override
        protected void onPostExecute(Integer integer) {
            ret = integer;
            swipeRefreshLayout.setRefreshing(false);
            changeUI();
            switch (integer) {
                case 8:
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(R.string.net_unstable);
                    break;
                case 9:
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(R.string.net_disconnect);
                    break;
                case 0:
                    textView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private int initLists() {
        try {
            socket = new Socket();
            getPredata();
            InetSocketAddress address = new InetSocketAddress(seraddress, port);
            socket.connect(address);
            socket.setSoTimeout(3000);
        } catch (Exception e){
            try {
                socket.close();
            } catch (Exception e2) {
//                Log.d("helloerr", e2.getMessage());
                return 9;}
//            Log.d("helloerrout", e.getMessage());
            return 9;
        }
        try{
            OutputStream ops = socket.getOutputStream();
            String b = "2\n";
            ops.write(b.getBytes());
            ops.flush();
            InputStream ips = socket.getInputStream();
            byte[] bytes2 = new byte[1024];
            ips.read(bytes2);
            String str = new String(bytes2);
            String[] ss = str.split("\n");
            int index = 0;
            for(String item:ss) {
                if (!item.trim().isEmpty()) {
                    if (item.charAt(0) == '/') {
                        urlString.add(item);
                    } else {
                        Note apple = new Note(index, item, R.drawable.notice_pic);
                        noteList.add(apple);
                        index++;
                    }
                }
            }
            socket.close();
        } catch (Exception e){
//            Log.d("helloerrout22", e.getMessage());
            if (e.getMessage().equals("Read timed out")) {
                return 8;
            }
            return 8; }
        //textView.setVisibility(View.GONE);
        return 0;
    }
}


