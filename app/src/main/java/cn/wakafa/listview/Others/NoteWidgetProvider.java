package cn.wakafa.listview.Others;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import cn.wakafa.listview.Activity.WebViewActivity;
import cn.wakafa.listview.R;
import cn.wakafa.listview.Service.WidgetViewsService;

/**
 * Implementation of App Widget functionality.
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class NoteWidgetProvider extends AppWidgetProvider {

    String clickAction = "click";
    int i = 0;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // 获取Widget的组件名
        ComponentName thisWidget = new ComponentName(context,
                NoteWidgetProvider.class);

        // 创建一个RemoteView
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        // 把这个Widget绑定到RemoteViewsService
        Intent intent = new Intent(context, WidgetViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);

        // 设置widget_list适配器
        remoteViews.setRemoteAdapter(R.id.widget_list, intent);

        // 设置当显示的widget_list为空显示的View
        remoteViews.setEmptyView(R.id.widget_list, R.layout.none_data);

        // 点击列表触发事件
        /*Intent clickIntent = new Intent(context, NoteWidgetProvider.class);

        // 设置Action，方便在onReceive中区别点击事件
        clickIntent.setAction("click");
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setPendingIntentTemplate(R.id.widget_list,
                pendingIntentTemplate);*/

        Intent fullIntent = new Intent(context, WebViewActivity.class);
        PendingIntent pfullintent = PendingIntent.getActivity(context, 0, fullIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list, pfullintent);


        // 刷新按钮
        final Intent refreshIntent = new Intent(context,
                NoteWidgetProvider.class);
        refreshIntent.setAction("refresh");
        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(
                context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.button_refresh,
                refreshPendingIntent);

        // 更新Wdiget
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }


    /**
     * 接收来自WidgetViewsFactory的Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        switch (action) {
            case "refresh":
                // 刷新Widget
                final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                final ComponentName cn = new ComponentName(context, NoteWidgetProvider.class);

                // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法，即更新列表。
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                        R.id.widget_list);
                break;

            case "click":
                // 单击Wdiget中ListView的某一项会显示一个Toast提示。
                Toast.makeText(context, intent.getStringExtra("url"),
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        i++;
    }

}

