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

public class MusicLibrary {
    //feild
    private static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
    private static final HashMap<String, Integer> albumRes = new HashMap<>();     //音楽画像ファイル（int型）をキー（ID）と紐づけ
    private static final HashMap<String, String> musicFileName = new HashMap<>(); //音楽ファイル名前（mp.3）をキー（ID）と紐づけ

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
//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------


}
