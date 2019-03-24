package io.github.cyndre;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.apache.commons.io.FileUtils;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to check for updates? ");
        if (input.next().toLowerCase().equals("y")) {
            check:
            try
            {
                JSONObject manifest = JSONFromWeb("https://launchermeta.mojang.com/mc/game/version_manifest.json");
                JSONArray versions = (JSONArray) manifest.get("versions");
                JSONObject latest = (JSONObject) versions.get(0);
                String currentVersion = version(args[1]);
                if (
                        !(latest.get("type").equals("snapshot") && !args[0].equals("--snapshot"))
                                && !currentVersion.equals(latest.get("id"))
                )
                {
                    String version = latest.get("type")+" "+latest.get("id");
                    System.out.println("Would you like to update the server from "+currentVersion+" to the latest "+version+"? ");
                    if (input.next().toLowerCase().equals("y")) {
                        latest = JSONFromWeb(latest.get("url").toString());
                        latest = (JSONObject) latest.get("downloads");
                        latest = (JSONObject) latest.get("server");
                        URL ejf = new URL(latest.get("url").toString());
                        System.out.println("Downloading latest "+version+". . .");
                        FileUtils.copyURLToFile(ejf, new File(args[1]+"\\server.jar"));
                        System.out.println("Downloaded latest "+version+" from "+ejf);
                    }
                }
                else
                {
                    System.out.println("No updates were found.");
                }
            }
            catch (IOException e)
            {
                System.out.println("Could not finish updating. " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("Starting server. . .");
    }

    private static JSONObject JSONFromWeb(String url)
    {
        JSONObject json;
        JSONParser parser = new JSONParser();
        try
        {
            URL manifest = new URL(url);
            URLConnection urlc = manifest.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            json = (JSONObject) parser.parse(br.readLine());
            br.close();
            return json;
        }
        catch (ParseException | IOException e)
        {
            System.out.println("No updates could be retrieved. "+e.getMessage());
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private static String version(String dir)
    {
        String version = "";
        try
        {
            File log = new File(dir+"\\logs\\latest.log");
            BufferedReader br = new BufferedReader(new FileReader(log));
            String line;
            while (!(line = br.readLine()).contains("Starting minecraft server version"));
            String[] current = line.split(" ");
            version = current[current.length-1];
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return version;
    }
}