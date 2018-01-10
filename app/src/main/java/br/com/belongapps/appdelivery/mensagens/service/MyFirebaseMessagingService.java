package br.com.belongapps.appdelivery.mensagens.service;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import br.com.belongapps.appdelivery.R;
import br.com.belongapps.appdelivery.cardapioOnline.activitys.CardapioMainActivity;
import br.com.belongapps.appdelivery.posPedido.activities.MeusPedidosActivity;
import br.com.belongapps.appdelivery.util.Print;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Chamado quando a mensagem é recebida.
     *
     * @param remoteMessage Objeto que representa a mensagem recebida do Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

		String notificationBody = "";
		String notificationTitle = "";
		String notificationData = "";

		try{
		   notificationData = remoteMessage.getData().toString();
		   notificationTitle = remoteMessage.getNotification().getTitle();
		   notificationBody = remoteMessage.getNotification().getBody();
		} catch (NullPointerException e){
		   Log.e(TAG, "onMessageReceived: NullPointerException: " + e.getMessage() );
		}

		Log.d(TAG, "onMessageReceived: data: " + notificationData);
		Log.d(TAG, "onMessageReceived: notification body: " + notificationBody);
		Log.d(TAG, "onMessageReceived: notification title: " + notificationTitle);

		String dataType = remoteMessage.getData().get(getString(R.string.data_type));
		if(dataType.equals(getString(R.string.direct_message))){
			Log.d(TAG, "onMessageReceived: nova mensagem recebida.");

			String titulo = remoteMessage.getData().get(getString(R.string.data_title));
			String menssagem = remoteMessage.getData().get(getString(R.string.data_message));
			String messagemId = remoteMessage.getData().get(getString(R.string.data_message_id));
			String tipoMensagem = remoteMessage.getData().get(getString(R.string.update_message));


			if (tipoMensagem != null){
				Print.logError("TIPO DA MENSAGEM" + tipoMensagem);
			}

			sendMessageNotification(titulo, menssagem, messagemId, tipoMensagem);
		}
    }

	/**
	 * Crie uma notificação push para uma mensagem de mudança de status do pedido
	 */
	private void sendMessageNotification(String titulo, String message, String messageId, String tipoMensagem){
		Log.d(TAG, "sendChatmessageNotification: Criando uma notificação");

		//get the notification id
		int notificationId = buildNotificationId(messageId);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
				getString(R.string.default_notification_channel_id));

		Intent pendingIntent;

		if (tipoMensagem.equals("update")){

			pendingIntent = new Intent(Intent.ACTION_VIEW);
			pendingIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=br.com.belongapps.appdelivery"));
			pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		} else if(tipoMensagem.equals("acompanhamento")) {

			pendingIntent = new Intent(this, MeusPedidosActivity.class);
			pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		}	else {

			pendingIntent = new Intent(this, CardapioMainActivity.class);
			pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		}

		PendingIntent notifyPendingIntent =
				PendingIntent.getActivity(
						this,
						0,
						pendingIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
				);

		//adicionar propriedades ao construtor
		builder.setSmallIcon(R.mipmap.launch_icon_kisabor)
				.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
						R.mipmap.launch_icon_kisabor))
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentTitle(titulo)
				.setContentText(message)
				.setColor(getColor(R.color.colorPrimary))
				.setAutoCancel(true)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
				.setOnlyAlertOnce(true);

		builder.setContentIntent(notifyPendingIntent);

		//Lançar Notificação
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (mNotificationManager != null)
			mNotificationManager.notify(notificationId, builder.build());

	}


	private int buildNotificationId(String id){
		Log.d(TAG, "buildNotificationId: criando um id para a notificação.");

		int notificationId = 0;
		for(int i = 0; i < 9; i++){
			notificationId = notificationId + id.charAt(0);
		}
		Log.d(TAG, "buildNotificationId: id: " + id);
		Log.d(TAG, "buildNotificationId: notification id:" + notificationId);

		return notificationId;
	}

}













