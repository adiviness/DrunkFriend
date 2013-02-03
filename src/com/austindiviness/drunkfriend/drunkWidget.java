package com.austindiviness.drunkfriend;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class drunkWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; ++i) {
			int appWidgetId = appWidgetIds[0];

			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);

			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider_layout);
			views.setOnClickPendingIntent(R.id.button, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}
