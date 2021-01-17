package com.zanaga.mediamanager.util.file.parser;

import com.zanaga.mediamanager.util.file.structure.FileStructure;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class ParserExifORDateTime extends ParserExif {

    private final int beginIndex;
    private final int length;
    private final SimpleDateFormat dateFormat;

    ParserExifORDateTime(String regex, int beginIndex, int length, String pattern) {
        super(regex);
        this.beginIndex = beginIndex;
        this.length = length;
        this.dateFormat = (SimpleDateFormat)SimpleDateFormat.getInstance();
        this.dateFormat.applyPattern(pattern);
    }

    @Override
    protected FileStructure parse(File file) {
        FileStructure fileStructure = super.parse(file);
        try {
            fileStructure.setParsedTimestamp(this.dateFormat.parse(file.getName().substring(this.beginIndex, beginIndex+this.length)));
        }
        catch(ParseException exception) {

        }
        return fileStructure;
    }
}
