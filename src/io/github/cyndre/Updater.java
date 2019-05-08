package io.github.cyndre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.apache.commons.io.FileUtils;

public class Updater
{
    private static String release, filepath, versionId;
    private static JSONObject versionObj;

    public Updater(String type, String path)
    {
        setRelease(type);
        setFilepath(path);
    }

    public static String fetchVersion(String version)
    {
        JSONObject manifest = JSONFromWeb("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JSONArray versions = (JSONArray) manifest.get("versions");
        if (getRelease().equals("release") || getRelease().equals("snapshot"))
        {
            version = ((JSONObject) manifest.get("latest")).get(getRelease()).toString();
        }
        for (int i = 0; i <= versions.size(); i++)
        {
            if (i == versions.size())
            {
                System.out.println(timestamp()+" Could not find version "+version+"!");
                break;
            }
            setVersionObj((JSONObject) versions.get(i));
            setVersionId(getVersionObj().get("id").toString());
            if (getVersionId().equals(version))
            {
                break;
            }
        }
        return getVersionId();
    }

    public static void downloadVersion()
    {
        try
        {
            String url = getVersionObj().get("url").toString();
            setVersionObj((JSONObject) (JSONFromWeb(url)).get("downloads"));
            setVersionObj((JSONObject) getVersionObj().get("server"));
            URL ejf = new URL(getVersionObj().get("url").toString());
            System.out.println(timestamp()+" [updater/WARN]: Downloading "+ getVersionId() +". . .");
            FileUtils.copyURLToFile(ejf, new File(getFilepath() +"/server.jar"));
            System.out.println(timestamp()+" [updater/INFO]: Downloaded "+ getVersionId() +" from "+ejf);
        }
        catch (IOException e)
        {
            System.out.println(timestamp()+"Could not finish updating. "+e.getMessage());
            e.printStackTrace();
        }
    }

    private static JSONObject JSONFromWeb(String url)
    {
        JSONObject json = new JSONObject();
        JSONParser parser = new JSONParser();
        try
        {
            java.io.InputStream is = new URL(url).openConnection().getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            json = (JSONObject) parser.parse(br.readLine());
            br.close();
        }
        catch (ParseException | IOException e)
        {
            System.out.println("No updates could be retrieved. "+e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    public String localVersion()
    {
        String line = "[00:00:00] [Server thread/INFO]: Starting minecraft server version unknown";
        try
        {
            File log = new File(getFilepath()+"/logs/latest.log");
            BufferedReader br = new BufferedReader(new FileReader(log));
            while (!(line = br.readLine()).contains("[Server thread/INFO]: Starting minecraft server version"));
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return line.substring(67);
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

    public static String getRelease()
    {
        return release;
    }

    public static void setRelease(String release)
    {
        Updater.release = release;
    }

    public static String getFilepath()
    {
        return filepath;
    }

    public static void setFilepath(String filepath)
    {
        Updater.filepath = filepath;
    }

    public static String getVersionId()
    {
        return versionId;
    }

    public static void setVersionId(String versionId)
    {
        Updater.versionId = versionId;
    }

    public static JSONObject getVersionObj()
    {
        return versionObj;
    }

    public static void setVersionObj(JSONObject versionObj)
    {
        Updater.versionObj = versionObj;
    }
}