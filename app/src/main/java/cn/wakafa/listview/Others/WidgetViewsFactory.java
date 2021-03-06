package cn.wakafa.listview.Others;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.wakafa.listview.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public Socket socket;
    public int port = 1020;
    String seraddress = "118.89.169.100";
    private List<String> noteList = new ArrayList<String>();
    ArrayList<String> urlString = new ArrayList<String>();

    private int initLists() {
        mList.clear();
        try {
            socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(seraddress,port);
            socket.connect(address);
            socket.setSoTimeout(3000);
        } catch (Exception e){
            try {
                socket.close();
            } catch (Exception e2) {
                return 9;}
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
                        mList.add(item);
                        index++;
                    }
                }
            }
            socket.close();
        } catch (Exception e){
            if (e.getMessage().equals("Read timed out")) {
                return 8;
            }
            return 8; }
        //textView.setVisibility(View.GONE);
        return 0;
    }



    private final Context mContext;
//    public static String ButtonTitle = new String();
    public static List<String> mList = new ArrayList<String>();

    /*
     * 构造函数
     */
    public WidgetViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    /*
     * MyRemoteViewsFactory调用时执行，这个方法执行时间超过20秒回报错。
     * 如果耗时长的任务应该在onDataSetChanged或者getViewAt中处理
     */
    @Override
    public void onCreate() {
        mList.add("");
        initLists();
    }

    /*
     * 当调用notifyAppWidgetViewDataChanged方法时，触发这个方法
     * 例如：WidgetViewsFactory.notifyAppWidgetViewDataChanged();
     */
    @Override
    public void onDataSetChanged() {
        initLists();
    }

    /*
     * 这个方法不用多说了把，这里写清理资源，释放内存的操作
     */
    @Override
    public void onDestroy() {
        mList.clear();
    }

    /*
     * 返回集合数量
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /*
     * 创建并且填充，在指定索引位置显示的View，这个和BaseAdapter的getView类似
     */
    @Override
    public RemoteViews getViewAt(int position) {
        // Notice: position starts from 0
        if (position < 0 || position >= mList.size())
            return null;
        if (position < 0 || position >= urlString.size())
            return null;
        String content = mList.get(position);

        // 创建在当前索引位置要显示的View
        final RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_list_item);

        // 设置要显示的内容
        rv.setTextViewText(R.id.widget_list_item_tv, content);

        // 填充Intent，填充在AppWdigetProvider中创建的PendingIntent
        Intent intent = new Intent();

        // 传入点击行的数据
        intent.putExtra("content", content);
        intent.putExtra("position", Integer.toString(position));
        intent.putExtra("url", urlString.get(position));
        rv.setOnClickFillInIntent(R.id.widget_list_item_tv, intent);

        return rv;
    }

    /*
     * 显示一个"加载"View。返回null的时候将使用默认的View
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /*
     * 不同View定义的数量。默认为1（本人一直在使用默认值）
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /*
     * 返回当前索引的。
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 如果每个项提供的ID是稳定的，即她们不会在运行时改变，就返回true（没用过。。。）
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
}
