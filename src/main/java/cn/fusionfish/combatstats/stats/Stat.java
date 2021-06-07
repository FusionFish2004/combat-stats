package cn.fusionfish.combatstats.stats;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.DecimalFormat;

import static cn.fusionfish.combatstats.CombatStats.*;

/**
 * 战斗数据类
 * @author JeremyHu
 */
public class Stat {

    private final String name;  //玩家名

    private double aimTotal;  //瞄准总时长
    private long aimTimes;  //瞄准总次数

    private long kills;  //总击杀数
    private long deaths;  //总死亡数

    private long dodgeTries;  //走位实验次数
    private long dodgeTimes;  //走位成功次数

    public Stat(String name) {
        this.name = name;
        init(name);
    }

    public Stat(Player player) {
        this(player.getName());
    }

    public String getName() {
        return name;
    }

    public void die() {
        deaths++;
    }

    public void kill() {
        kills++;
    }

    public double getKD() {
        double k = kills;
        double d = deaths;
        if (d == 0) return 0;
        return k / d;
    }

    public double getAverageAimTime() {
        if (aimTimes == 0) return 0;
        return aimTotal / aimTimes;
    }

    public void shoot(double aimTime) {
        aimTotal += aimTime;
        aimTimes ++;
    }

    public void dodgeTry() {
        dodgeTries++;
    }

    public void dodge() {
        dodgeTimes++;
    }

    public double getDodgeRate() {
        if (dodgeTries == 0) return 0;
        double tries = dodgeTries;
        double times = dodgeTimes;
        return times / tries;
    }

    public double getAimScore() {
        double average = getAverageAimTime();
        if (average == 0) return 0;
        return Math.pow(0.45, average - 0.15);
    }

    /**
     * 将数据存储到文件中
     */
    public void save() {
        File playerJson = new File(STATS_FOLDER,name + ".json");
        String json = new Gson().toJson(this);
        try {
            save(json,playerJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(Player player) {
        DecimalFormat df = new DecimalFormat("#.##");
        player.sendMessage("KD - " + df.format(getKD()));
        player.sendMessage("平均瞄准时间 - " + df.format(getAverageAimTime()));
        player.sendMessage("走位成功率 - " + df.format(getDodgeRate()));
    }

    public void score(Player player) {
        DecimalFormat df = new DecimalFormat("#.##");
        player.sendMessage("§6§l评分：");
        player.sendMessage("瞄准 - " + df.format(getAimScore()));
        player.sendMessage("走位 - " + df.format(getDodgeRate()));
    }

    private static void init(String name) {
        File playerJson = new File(STATS_FOLDER,name + ".json");
        if (!playerJson.exists()) {
            try {
                playerJson.createNewFile();
                String json = new Gson().toJson(new Stat(name));
                save(json, playerJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //如果不存在，创建文件
    }

    private static void save(String json, File playerJson) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(playerJson));
        writer.write(json);
        writer.close();
    }

    public static Stat get(File file) {
        try {
            return new Gson().fromJson(new JsonReader(new FileReader(file)), Stat.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
