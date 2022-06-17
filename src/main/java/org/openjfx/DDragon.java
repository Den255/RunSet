package org.openjfx;

import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class DDragon {
    String version;
    public DDragon() {
        try {
            URL url = new URL("https://ddragon.leagueoflegends.com/api/versions.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            //int status = con.getResponseCode();
            InputStreamReader streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            String versions_str = new BufferedReader(streamReader).lines().collect(Collectors.joining("\n"));

            con.disconnect();
            JsonParser parser = new JsonParser();
            Object obj = parser.parse(versions_str);
            JsonArray array = (JsonArray)obj;
            version = array.get(0).getAsString();
        } catch (IOException e) {
            System.out.println("No connection!");
        }finally {
            System.out.println("DDragon data version: "+version);
        }
    }
    public void checkVersion() throws IOException {
        FileOutputStream ddVersion = new FileOutputStream("./ddragon-version", true);
        ddVersion.flush();
        ddVersion.close();

        String local_version = Files.readString(Path.of("./ddragon-version"));
        System.out.println("Local ddragon version: "+local_version);
        if(!local_version.equals(version)){
            System.out.println("Update local version!");
            URL runes = new URL("https://static.u.gg/assets/lol/riot_static/"+version+"/data/en_US/runesReforged.json");
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/"+version+"/data/en_US/champion.json");
            if(this.syncDDragonData(url, "./champions.json") && this.syncDDragonData(runes,"./runesReforged.json")){
                PrintWriter writer = new PrintWriter("./ddragon-version");
                writer.write(version);
                writer.flush();
                writer.close();
            }
        }
    }
    public boolean syncDDragonData(URL url, String out) {
        int status = 0;
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            status = con.getResponseCode();
            System.out.println(status);
            InputStreamReader streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            String champions = new BufferedReader(streamReader).lines().collect(Collectors.joining());
            con.disconnect();

            PrintWriter writer = new PrintWriter(out);
            writer.write(champions);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println("No connection!");
            return status == 500;
        }
        return status == 200;
    }
}
