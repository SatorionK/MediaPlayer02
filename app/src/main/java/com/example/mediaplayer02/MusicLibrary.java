package com.example.mediaplayer02;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MusicLibrary {
    //feild
    private static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
    private static final HashMap<String, Integer> albumRes = new HashMap<>();     //音楽画像ファイル（int型）をキー（ID）と紐づけ
    private static final HashMap<String, String> musicFileName = new HashMap<>(); //音楽ファイル名前（mp.3）をキー（ID）と紐づけ

    //上記メソッドに値を入れる（createMediaMetadataCompatメソッドにて）

    static{MusicLibrary.createMediaMetadataCompat(
                "Jazz_In_Paris",
                "Jazz in Paris",
                "Media Right Productions",
                "Jazz & Blues",
                "Jazz",
                103,
                TimeUnit.SECONDS,
                "album_jazz_blues",
                R.drawable.album_jazz_blues,
                "jazz_in_paris.mp3");}



//--------------------------------------------------------------------------------------------------
    //①　曲画像URI取得メソッド
    //albumArtResName:drawableフォルダ内にあるデータの名前（アルバムジャケット）
    private static String getAlbumArtUri(String albumArtResName){

        return ContentResolver.SCHEME_ANDROID_RESOURCE +"://" +BuildConfig.APPLICATION_ID +"/drawable/" +albumArtResName;
    }
//--------------------------------------------------------------------------------------------------
    //②　browerserviceクラスにて使用。Idと値が紐づけられているためそれを利用
    public static String getMusicFileName(String mediaId){

        return musicFileName.containsKey(mediaId) ? musicFileName.get(mediaId) : null;
    }

//--------------------------------------------------------------------------------------------------
    //③　画像ファイルの値（int型）の取得メソッド
    private static int getAlbumRes(String mediaId){

        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : null;
    }
//--------------------------------------------------------------------------------------------------
    //④　bitmapのインスタンス生成のメソッド
    //publicではなくprivateではだめなのか？（コンテキスト影響？）
    //アプリ内のRクラスの画像の値を取得することが目的
    public static Bitmap getAlbumBitmap(Context context, String mediaId){

        return BitmapFactory.decodeResource(context.getResources(), MusicLibrary.getAlbumRes(mediaId));
    }

//--------------------------------------------------------------------------------------------------
    //⑤　MediaBrowserCompat.MediaItem（MediaBrowserCompatの内部クラス）のList内にデータを入れるメソッド
    //borowerServiceクラスにて使用
    //演奏可能な曲（今回は2曲が対象）を判別し、取得

    public static List<MediaBrowserCompat.MediaItem> getMediaItems(){
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
    //music内のデータが尽きるまで繰り返す
        for(MediaMetadataCompat metadata : music.values()){
            result.add(new MediaBrowserCompat.MediaItem(metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        return result;
    }
//--------------------------------------------------------------------------------------------------
    //⑥　metadetaの取得メソッド(通知と再生時にサービスにて使用)
    //ビルダーをつかってメタデータをここでセットする

    public static MediaMetadataCompat getMetadata(Context context, String mediaId){
        MediaMetadataCompat metadetawithoutBitmap = music.get(mediaId);
        Bitmap albumArt = getAlbumBitmap(context, mediaId);

        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();

        //1行1行入れても良いが、拡張for文で対応
        //Stringデータとintデータを分けて処理
        for(String key :
                new String[]{
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        MediaMetadataCompat.METADATA_KEY_TITLE
                }){
                   builder.putString(key, metadetawithoutBitmap.getString(key));
        }

        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, metadetawithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);

        return builder.build();
    }
//--------------------------------------------------------------------------------------------------
    //⑦　metadataの作成（データをこのメソッドにてセットする）
    //フィールド3種に入れるデータ
    private static void createMediaMetadataCompat(
            String mediaId,
            String title,
            String artist,
            String alubum,
            String genre,
            long duration,
            TimeUnit durationUnit,
            String alubumAretResName,

            int albumArtResId,
            String musicFilename){
        //mediaIdをキーとし、値を代入していく
        music.put(mediaId, new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, alubum)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, TimeUnit.MILLISECONDS.convert(duration, durationUnit))
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, MusicLibrary.getAlbumArtUri(alubumAretResName))
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, MusicLibrary.getAlbumArtUri(alubumAretResName))
                .build());

        albumRes.put(mediaId, albumArtResId);
        musicFileName.put(mediaId, musicFilename);
    }
//--------------------------------------------------------------------------------------------------


}
