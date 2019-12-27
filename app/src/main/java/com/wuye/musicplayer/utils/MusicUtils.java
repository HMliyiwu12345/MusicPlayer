package com.wuye.musicplayer.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.wuye.musicplayer.bean.Music;
import com.wuye.musicplayer.consf.Constant;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    /**
     * 选择本地音乐
     * @param context
     */
    public static void chooseMusic(Activity context){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "audio/*");
        context.startActivityForResult(intent, Constant.REQUEST_MUSIC);

    }
    /**
     * 从内容提供者获取歌曲信息
     * @return
     */
    public static List<Music> getMusicInfo(Context context, Uri uri){

        //TODO 创建一个集合存储读取的歌曲信息
        List<Music> musicList = new ArrayList<>();
        //TODO 读取数据库中歌曲信息
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for(int i=0; i<cursor.getCount(); i++){
            Music music = new Music();
            cursor.moveToNext();
            // id
            music.setId(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            // 歌曲
            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            // 歌手
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            // 时长
            music.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            // 文件大小
            music.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            // 路径
            music.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

            // 将资源为音乐的媒体文件存储到集合中
            if(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0){
                musicList.add(music);
            }
        }

        cursor.close();

        return musicList;
    }




}
