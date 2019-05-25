package io.github.cyndre;

import java.util.Scanner;

public class UpdaterDriver
{
    public static void main(String[] args)
    {
        boolean update = false;
        String version = null, release = "release", value = null, filepath = null;
        Updater mc = new Updater(release);
        for (String argument: args)
        {
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
                    case "auto-update":
                    {
                        update = true;
                        break;
                    }
                    default:
                    {
                        mc.print("WARN","Ignoring unknown argument: --"+argument);
                    }
                }
            }
            else
            {
                filepath = argument;
                break;
            }
        }
        mc.setRelease(release);
        mc.setFilepath(filepath);
        String current = mc.localVersion();
        version = mc.fetchVersion(version);
        if (current.equals(version))
        {
            mc.print("WARN","Already running version "+current+"!");
        }
        else if (update)
        {
            mc.downloadVersion();
        }
        else
        {
            Scanner input = new Scanner(System.in);
            mc.print("INFO","Would you like to check for updates? ");
            if (input.next().toLowerCase().equals("y"))
            {
                mc.print("INFO","Would you like to update the server from "+current+" to "+version+"? ");
                if (input.next().toLowerCase().equals("y"))
                {
                    mc.downloadVersion();
                }
            }
        }
    }
}
