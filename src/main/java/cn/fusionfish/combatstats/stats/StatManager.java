package cn.fusionfish.combatstats.stats;

import cn.fusionfish.combatstats.CombatStats;
import cn.fusionfish.combatstats.utils.FileUtil;
import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import static cn.fusionfish.combatstats.CombatStats.*;

import java.io.File;
import java.util.*;

/**
 * 数据管理类
 * @author JeremyHu
 */
public class StatManager {

    private final Map<String, Stat> statMap = Maps.newHashMap();
    //储存所有数据的Map，String类型为玩家名

    public StatManager() {
        init();  //初始化
    }

    /**
     * 初始化
     */
    private void init() {
        CombatStats.getInstance().getLogger().info("§aLoading stats from local files...");
        List<File> files = FileUtil.getFiles(STATS_FOLDER);
        //获取数据文件夹下所有json文件
        if (files != null) {
            for (File json : files) {
                Stat stat = Stat.get(json);
                assert stat != null;
                String name = stat.getName();
                CombatStats.getInstance().getLogger().info("§2 - " + name);
                //控制台中打出信息
                statMap.put(name, stat);
            }
        }else {
            CombatStats.getInstance().getLogger().info("§aThe folder is empty...");
            CombatStats.getInstance().getLogger().info("§aReady to create stats file...");
        }
        CombatStats.getInstance().getLogger().info("§aStatManager initialized.");
    }

    /**
     * 获取玩家战斗数据
     * @param name 玩家名
     * @return 数据
     */
    public Stat get(String name) {
        Stat stat = statMap.get(name);
        if (stat == null) {
            stat = new Stat(name);
            statMap.put(name, stat);
        }
        return stat;
    }

    /**
     * 获取玩家战斗数据
     * @param player 玩家
     * @return 数据
     */
    public Stat get(Player player) {
        return get(player.getName());
    }

    /**
     * 存储数据到本地
     */
    public void save() {
        CombatStats.getInstance().getLogger().info("§aSaving stats to local files...");
        statMap.values().forEach(Stat::save);
    }


    /**
     * 瞄准时间排序
     * @return 排序
     */
    public Map<String, Double> getAimRank() {
        Map<String, Double> rank = Maps.newLinkedHashMap();
        List<String> buffer = new ArrayList<>(statMap.keySet());

        buffer.sort((o1, o2) -> {
            double d1 = statMap.get(o1).getAverageAimTime();
            double d2 = statMap.get(o2).getAverageAimTime();
            return Double.compare(d1, d2);
        });

        buffer.forEach(s -> rank.put(s, statMap.get(s).getAverageAimTime()));

        return rank;
    }

    /**
     * KD排序
     * @return 排序
     */
    public Map<String, Double> getKDRank() {
        Map<String, Double> rank = Maps.newLinkedHashMap();
        List<String> buffer = new ArrayList<>(statMap.keySet());

        buffer.sort((o1, o2) -> {
            double d1 = statMap.get(o1).getKD();
            double d2 = statMap.get(o2).getKD();
            return Double.compare(d2, d1);
        });

        buffer.forEach(s -> rank.put(s, statMap.get(s).getKD()));

        return rank;
    }
}
