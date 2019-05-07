package io.github.cyndre;

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
                        System.out.println("Ignoring unknown switch: "+argument);
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
            System.out.println("Already running version "+version+"!");
            return;
        }
        Scanner input = new Scanner(System.in);
        if (release != "version")
        {
            System.out.print("Would you like to check for updates? ");
        }
        if (input.next().toLowerCase().equals("y") || release == "version")
        {
            version = mc.fetchVersion(version);
            if (current.equals(version))
            {
                System.out.println("Already running version "+version+"!");
                return;
            }
            System.out.print("Would you like to update the server from "+current+" to "+version+"? ");
            if (input.next().toLowerCase().equals("y"))
            {
                mc.downloadVersion();
            }
        }
        System.out.println("Starting server. . .");
    }
}
