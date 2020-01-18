package com.github.clock;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.github.clock.swing.ClockViewer;

public class Main{
    public Main(String[] arguments) throws CmdLineException{
        Args args = parseOptions(arguments);

        if(args.isRunningMode()){
            Clock clock = new Clock(args.getTimeZone());
            ClockViewer viewer = new ClockViewer(clock, args);
            viewer.setDebugMode(args.isDebugMode());
            viewer.showClock();
        }
    }

    private Args parseOptions(String[] arguments) throws CmdLineException{
        Args args = new Args();
        CmdLineParser parser = new CmdLineParser(args);
        parser.parseArgument(arguments);

        if(args.isShowVersion()){
            System.out.printf("Simple Analog Clock version %s%n", "1.0");
        }
        if(args.isShowHelp()){
            args.showHelp(parser);
        }
        return args;
    }

    public static void main(String[] args) throws Exception{
        new Main(args);
    }
}
