package com.onysakura.algorithm.file.music;

public enum FileType {
    FLAC(".flac"),
    WAV(".wav"),
    APE(".ape"),
    MP3(".mp3");

    private final String suffix;

    FileType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public static FileType getType(String suffix) {
        FileType[] fileTypes = FileType.values();
        for (FileType fileType : fileTypes) {
            if (fileType.suffix.equalsIgnoreCase(suffix)) {
                return fileType;
            }
        }
        return null;
    }
}
