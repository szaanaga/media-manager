package com.zanaga.mediamanager.util.file.parser;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.mp4.Mp4MetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import com.zanaga.mediamanager.util.file.structure.FileStructure;
import com.zanaga.mediamanager.util.Util;

import java.io.File;
import java.util.Date;

class ParserExif extends Parser {

    ParserExif(String regex) {
        super(regex);
    }

    @Override
    protected FileStructure parse(File file) {
        FileStructure fileStructure = new FileStructure(file);
        Date date;
        switch(Util.getSuffix(file)) {
            case "MOV":
            case "mov":
            case "MP4":
            case "mp4":
                date = getDateMP4(file);
                break;
            case "3gp":
                date = getDateFS(file);
                break;
            default:
                date = getDateDefault(file);
                break;
        }
        if(date != null) {
            fileStructure.setExifCreatedTime(date);
        }
        return fileStructure;
    }

    private Date getDateFS(File file) {
        try {
            for(Directory directory : Mp4MetadataReader.readMetadata(file).getDirectoriesOfType(FileSystemDirectory.class)) {
                Date date = directory.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
                if(date != null) {
                    return date;
                }
            }
        }
        catch(Exception exception) {

        }
        return null;
    }

    private Date getDateMP4(File file) {
        try {
            for(Directory directory : Mp4MetadataReader.readMetadata(file).getDirectoriesOfType(Mp4Directory.class)) {
                Date date = directory.getDate(Mp4Directory.TAG_CREATION_TIME);
                if(date != null) {
                    return date;
                }
            }
        }
        catch(Exception exception) {

        }
        return null;
    }

    private Date getDateDefault(File file) {
        try {
            for(Directory directory : ImageMetadataReader.readMetadata(file).getDirectoriesOfType(ExifSubIFDDirectory.class)) {
                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if(date != null) {
                    return date;
                }
            }
        }
        catch(Exception exception) {

        }
        return null;
    }
}
