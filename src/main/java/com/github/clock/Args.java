package com.github.clock;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.TimeZone;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Args {
    @Option(name="-tz", aliases="--timezone", usage="Specifies time zone.  Default is local time.", metaVar="<TIMEZONE>")
    private String timeZone;

    @Option(name="-h", aliases="--help", usage="print this message.")
    private boolean showHelp = false;

    @Option(name="-hh", aliases="--more-help", usage="print detail help message.")
    private boolean showMoreHelp = false;

    @Option(name="-v", aliases="--version", usage="show version and quit.")
    private boolean showVersion = false;

    @Option(name="-d", aliases="--debug", usage="debug mode.")
    private boolean debugMode = false;

    @Option(name="-lc", aliases="--long-hand-color", usage="長針の色を指定する。")
    private String longHandColor = "#ff0000";

    public String getLongHandColor(){
        return longHandColor;
    }

    public boolean isRunningMode(){
        return !isShowVersion() && !isShowHelp();
    }

    public boolean isShowHelp(){
        return showHelp || showMoreHelp;
    }

    public boolean isShowVersion(){
        return showVersion;
    }

    public boolean isDebugMode(){
        return debugMode;
    }

    public TimeZone getTimeZone(){
        TimeZone tz = null;
        if(timeZone != null){
            tz = TimeZone.getTimeZone(timeZone);
        }
        return tz;
    }

    public void showHelp(CmdLineParser parser){
        PrintWriter out = new PrintWriter(System.out);
        out.printf("java -jar clock.jar [OPTIONS]%n%n");
        parser.printUsage(out, null);

        if(showMoreHelp){
            System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));
        }
    }
}
