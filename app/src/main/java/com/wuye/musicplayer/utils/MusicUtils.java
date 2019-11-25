package com.wuye.musicplayer.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wuye.musicplayer.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    /**
     * 从内容提供者获取歌曲信息
     * @return
     */
    public static List<Music> getMusicInfo(Context context){

        //TODO 创建一个集合存储读取的歌曲信息
        List<Music> musicList = new ArrayList<>();
        //TODO 读取数据库中歌曲信息
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
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
