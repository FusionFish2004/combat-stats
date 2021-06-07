package cn.fusionfish.combatstats.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件实用类
 * @author JeremyHu
 */
public class FileUtil {
    public static List<File> getFiles(File dir){

        if(!dir.isDirectory() || !dir.exists()){
            return null;
        }

        List<File> files = new ArrayList<>();
        for(int i = 0; i < dir.listFiles().length; i++){
            files.add(dir.listFiles()[i]);
        }
        return files;
    }

    public static JsonObject getJson(File file){
        if (!file.exists()){
            return null;
        }
        JsonObject json = new JsonObject();
        try {
            json = new JsonParser().parse(new JsonReader(new FileReader(file))).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return json;
    }
}
