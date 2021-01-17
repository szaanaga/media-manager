package com.zanaga.mediamanager.util.file.parser;

import com.zanaga.mediamanager.util.file.structure.FileStructure;

import java.io.File;
import java.util.regex.Pattern;

abstract class Parser {

    private final Pattern pattern;

    Parser(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    boolean matches(String name) {
        return this.pattern.matcher(name).matches();
    }

    protected abstract FileStructure parse(File file);
}
