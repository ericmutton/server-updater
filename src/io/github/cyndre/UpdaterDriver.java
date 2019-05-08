package io.github.cyndre;

import java.time.LocalTime;
import java.util.Scanner;

public class UpdaterDriver
{
    public static void main(String[] args)
    {
        String version = null, release = "release", argument = null, value = null;
        for (int path = 0; path < args.length; path++)
        {
            argument = args[path];
            if (argument.substring(0,2).equals("--"))
            {
                argument = argument.substring(2);
                int pos = argument.indexOf("=");
                if (pos > -1)
                {
                    value = argument.substring(pos+1);
                    argument = argument.substring(0,pos);
                }
                switch (argument)
                {
                    case "release": case "snapshot":
                    {
                        release = argument;
                        break;
                    }
                    case "version":
                    {
                        version = value;
                        break;
                    }

                    default:
                    {
                        System.out.println(timestamp()+" [updater/WARN]: Ignoring unknown switch: "+argument);
                    }
                }
            }
            else
            {
                break;
            }
        }
        Updater mc = new Updater(release, argument);

        String current = mc.localVersion();
        if (current.equals(version))
        {
            System.out.println(timestamp()+" [updater/WARN]: Already running version "+version+"!");
            return;
        }
        Scanner input = new Scanner(System.in);
        if (release != "version")
        {
            System.out.print(timestamp()+" [updater/INFO]: Would you like to check for updates? ");
        }
        if (input.next().toLowerCase().equals("y") || release == "version")
        {
            version = mc.fetchVersion(version);
            if (current.equals(version))
            {
                System.out.println(timestamp()+" [updater/WARN]: Already running version "+version+"!");
                return;
            }
            System.out.print(timestamp()+" [updater/INFO]: Would you like to update the server from "+current+" to "+version+"? ");
            if (input.next().toLowerCase().equals("y"))
            {
                mc.downloadVersion();
            }
        }
        System.out.println(timestamp()+" [updater/INFO]: Starting server. . .");
    }

    public static String timestamp()
    {
        String time = LocalTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS).toString();
        while (time.length() < 8)
        {
            time += ":00";
        }
        return "["+time+"]";
    }
}
