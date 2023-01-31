package com.tv9news.details.podcast.general.errors;

public class AudioFilePathInvalidException extends Exception {
    public AudioFilePathInvalidException(String url) {
        super("The file path is not a valid path: " + url +
                "\n" +
                "Have you add File Access Permission?");
    }
}
