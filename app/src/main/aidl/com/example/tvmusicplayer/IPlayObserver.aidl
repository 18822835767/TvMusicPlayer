// IPlayObserver.aidl
package com.example.tvmusicplayer;

// Declare any non-default types here with import statements
import com.example.tvmusicplayer.bean.Song;

interface IPlayObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onPlayStateChange(int playState);
    
    void onSeekChange(int currentPosition);
    
    void onSongChange(in Song song,int position);
    
    void onSongsEmpty();
}
