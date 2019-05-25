package io.github.cyndre;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipFile;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.io.FileUtils;

public class Updater
{
    private static String release, filepath, versionId;
    private static JSONObject versionObj;

    public Updater(String type)
    {
        setRelease(type);
    }

    public String fetchVersion(String version)
    {
        JSONObject manifest = JSONFromWeb("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        JSONArray versions = (JSONArray) manifest.get("versions");
        if (!getRelease().equals("version"))
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

    public void downloadVersion()
    {
        try
        {
            String url = getVersionObj().get("url").toString();
            setVersionObj((JSONObject) (JSONFromWeb(url)).get("downloads"));
            setVersionObj((JSONObject) getVersionObj().get("server"));
            URL ejf = new URL(getVersionObj().get("url").toString());
            print("WARN","Downloading "+getVersionId()+" . . .\n");
            FileUtils.copyURLToFile(ejf, new File(getFilepath() +"/server.jar"));
            print("INFO","Downloaded "+getVersionId()+" from "+ejf+"\n");
        }
        catch (IOException e)
        {
            print("WARN","Could not finish updating. "+e.getMessage());
            e.printStackTrace();
        }
    }

    private static JSONObject JSONFromWeb(String url)
    {
        JSONObject json = new JSONObject();
        JSONParser parser = new JSONParser();
        try
        {
            InputStream is = new URL(url).openConnection().getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            json = (JSONObject) parser.parse(br.readLine());
            br.close();
        }
        catch (ParseException | IOException e)
        {
            print("WARN","No updates could be retrieved. "+e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    public String localVersion()
    {
        String version;
        JSONParser parser = new JSONParser();
        try (ZipFile zip = new ZipFile(getFilepath()+"/server.jar"))
        {
            InputStream is = zip.getInputStream(zip.getEntry("version.json"));
            version = new String(readFully(is));
            JSONObject json = (JSONObject) parser.parse(version);
            version = json.get("name").toString();
        }
        catch (IOException | ParseException | NullPointerException e)
        {
            print("WARN","ENTERING PRE-1.14 COMPATIBILITY MODE\n");
            String line = "[00:00:00] [Server thread/INFO]: Starting minecraft server version unknown";
            try (BufferedReader br = new BufferedReader(new FileReader(new File(getFilepath()+"/logs/latest.log"))))
            {
                while (!(line = br.readLine()).contains("[Server thread/INFO]: Starting minecraft server version"));
            }
            catch (IOException ex)
            {
                print("WARN","No local version could be found. "+ex.getMessage());
                ex.printStackTrace();
            }
            return line.substring(67);
        }
        return version;
    }

    byte[] readFully(InputStream in) throws IOException //Java 8 compatibility for Java 9+ usage of readAllBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[300];
        int r;
        while((r = in.read(buffer)) != -1)
        {
            baos.write(buffer, 0, r);
        }
        return baos.toByteArray();
    }

    public static String timestamp()
    {
        StringBuffer sb = new StringBuffer(LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        while (sb.length() < 8)
        {
            sb.append(":00");
        }
        return "["+sb+"]";
    }
    public static void print(String type, String content)
    {
        System.out.print(timestamp()+" [updater/"+type+"]: "+content);
    }

    public static String getRelease()
    {
        return release;
    }

    public static void setRelease(String type)
    {
        release = type;
    }

    private static String getFilepath()
    {
        return filepath;
    }

    public static void setFilepath(String path)
    {
        filepath = path;
    }

    private static String getVersionId()
    {
        return versionId;
    }

    private static void setVersionId(String id)
    {
        versionId = id;
    }

    private static JSONObject getVersionObj()
    {
        return versionObj;
    }

    private static void setVersionObj(JSONObject obj)
    {
        versionObj = obj;
    }
}