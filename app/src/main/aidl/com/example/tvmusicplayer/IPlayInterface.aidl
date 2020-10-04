// IPlayInterface.aidl
package com.example.tvmusicplayer;

// Declare any non-default types here with import statements
import com.example.tvmusicplayer.bean.Song;
import com.example.tvmusicplayer.IPlayObserver;

interface IPlayInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void playSongs(in List<Song> songs,int position);
    
    void playOrPause();
    
    void playNext();
    
    void playPre();
    
    void seekTo(int seek);
    
    void setPlayMode(int mode);
    
    List<Song> getQueueSongs();
    
    int getCurrentPosition();
    
    void setCurrentPosition(int currentPosition);
    
    int getPlayMode();
    
    int getPlayState();
    
    Song getCurrentSong();
    
    int getDuration();
    
    int getCurrenPoint();
    
    void registerObserver(in IPlayObserver observer);
    
    void unregisterObserver(in IPlayObserver observer);
    
    void addNext(in Song song);
}
