package com.example.myfirstapp.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jHelper {
    private final static LogConfigurator mLogConfigrator = new LogConfigurator();

    public Log4jHelper(Context ctx) {
        configureLog4j(ctx);
    }

    private void configureLog4j(Context ctx) {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "log4j.log";
        String filePattern = "%d - [%c] - %p : %m%n";
        int maxBackupSize = 10;
        long maxFileSize = 1024 * 1024;

        configure( fileName, filePattern, maxBackupSize, maxFileSize );
    }

    private void configure( String fileName, String filePattern, int maxBackupSize, long maxFileSize ) {
        mLogConfigrator.setFileName( fileName );
        mLogConfigrator.setMaxFileSize( maxFileSize );
        mLogConfigrator.setFilePattern(filePattern);
        mLogConfigrator.setMaxBackupSize(maxBackupSize);
        mLogConfigrator.setUseLogCatAppender(true);
        mLogConfigrator.setUseFileAppender(true);
        mLogConfigrator.configure();

    }

    public org.apache.log4j.Logger getLogger( String name ) {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( name );
        return logger;
    }
}