package com.tv9news.details.podcast.general;

import androidx.annotation.Nullable;

;import com.tv9news.models.home.Lists;

public class PodcastStatus {
    @Nullable
    private Lists jcAudio;
    private long duration;
    private long currentPosition;
    private PlayState playState;

    public PodcastStatus() {
        this(null, 0, 0, PlayState.PREPARING);
    }

    public PodcastStatus(Lists jcAudio, long duration, long currentPosition, PlayState playState) {
        this.jcAudio = jcAudio;
        this.duration = duration;
        this.currentPosition = currentPosition;
        this.playState = playState;
    }

    public Lists getJcAudio() {
        return jcAudio;
    }

    public void setJcAudio(Lists jcAudio) {
        this.jcAudio = jcAudio;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public PlayState getPlayState() {
        return playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public enum PlayState {
        PLAY, PAUSE, STOP, CONTINUE, PREPARING, PLAYING
    }
}
