// IPlayInterface.aidl
package com.example.tvmusicplayer;

// Declare any non-default types here with import statements
import com.example.tvmusicplayer.bean.Song;

interface IPlayInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void playSongs(in List<Song> songs,int position);
    
    void playOrPause();
    
    void performSong(String dataSource);
    
    void playNext();
    
    void playPre();
    
    void seekTo(int seek);
    
    void setPlayMode(int mode);
    
    List<Song> getQueueSongs();
    
    int getCurrentPosition();
    
    void setCurrentPosition();
    
    int getPlayMode();
    
    int getPlayState();
}
