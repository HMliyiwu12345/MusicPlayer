package com.wuye.musicplayer.bean

class Music {
    //id
     var id: Long = 0
    // 歌名
     var title:String=""
    // 专辑
     var album:String=""
    // 歌手
     var artist:String=""
    // 时长
     var duration:Long=0
    // 文件大小
     var size: Long = 0
    // 路径
     var path:String=""

    override fun toString(): String {
        return "Music(id=$id, title='$title', album='$album', artist='$artist', duration=$duration, size=$size, path='$path')"
    }

}