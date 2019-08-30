package com.example.mediaplayer02;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {
    //field
    private final String TAG = MusicService.class.getSimpleName();//LOGTAG

    final String ROOT_ID = "root";
    Handler handler;

    MediaSessionCompat mSession;//アクティビティのコントローラと関連
    AudioManager am;

    int index = 0;//再生中のインデックス
    ExoPlayer exoPlayer;

    List<MediaSessionCompat.QueueItem> queueItems = new ArrayList<>();


    @Override
    public void onCreate(){
        Log.d(TAG, "onCreate");
        super.onCreate();

        //AudioManagerの取得
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //セッションのインスタンス化
        mSession = new MediaSessionCompat(getApplicationContext(), TAG);
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | //ヘッドフォン等のボタンを扱う
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS | //キュー系のコマンドの使用をサポート
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //クライアント（アクティビティから送られる指示？）からのコールバックを設定
        mSession.setCallback(callback);

        //mediasessionを呼び出す処理
        setSessionToken(mSession.getSessionToken());

        //キューアイテムの追加
        //リスト内にMediaBrowserCompat.MediaItemクラスの要素があるので、要素分繰り返す
        //Library内の要素キューに入れようとしている
        int i = 0;

        for(MediaBrowserCompat.MediaItem media : MusicLibrary.getMediaItems()){
            queueItems.add(new MediaSessionCompat.QueueItem(media.getDescription(), i));
            i++;
        }
        mSession.setQueue(queueItems);

        //exoplayerの初期化（基本的な初期化（お作法））
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), new DefaultTrackSelector());
        //プレイヤーのイベントリスナーを設定
        exoPlayer.addListener(eventListener);

        //ハンドラインスタンス
        handler = new Handler();

        //onclickの要領で別スレッドを作る
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //再生中の場合
                if(exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady())
                    //再生中アップデートメソッド
                    UpdatePlaybackState();

                //再度実行
                handler.postDelayed(this, 500);
            }
        }, 500);

        //Media Sessionのメタデータや、プレイヤーのステータスが更新されたタイミングで
        //通知の作成/更新をする
        mSession.getController().registerCallback(new MediaControllerCompat.Callback() {

            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {

                CreateNotification();
            }

            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {

                CreateNotification();
            }
        });

    }


//--------------------------------------------------------------------------------------------------
    //onGetRootメソッド
//--------------------------------------------------------------------------------------------------
    //onLoadChildrenメソッド
//--------------------------------------------------------------------------------------------------
    //onDestroyメソッド
//--------------------------------------------------------------------------------------------------
    //MediaSession用コールバック（callback変数の定義）
    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
        //メディアデータが読まれるソースデータを生成
        DataSource.Factory datasourceFactory = new DefaultDataSourceFactory(getApplicationContext(), util.getUserAgent(getApplicationContext(), "AppNmae"));

    }
}
//--------------------------------------------------------------------------------------------------
    //プレイヤーのコールバック
//--------------------------------------------------------------------------------------------------
    //UpdatePlaybackStateメソッド
//--------------------------------------------------------------------------------------------------
    //CreateNotificationメソッド
//--------------------------------------------------------------------------------------------------
    //createContentIntentソッド
//--------------------------------------------------------------------------------------------------
    //オーディオフォーカスのコールバック
//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------


}
