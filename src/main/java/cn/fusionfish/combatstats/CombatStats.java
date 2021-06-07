package cn.fusionfish.combatstats;

import cn.fusionfish.combatstats.combat.BowTracer;
import cn.fusionfish.combatstats.listeners.BowListener;
import cn.fusionfish.combatstats.listeners.CommandListener;
import cn.fusionfish.combatstats.listeners.KDListener;
import cn.fusionfish.combatstats.stats.StatManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CombatStats extends JavaPlugin {

    private static CombatStats instance;
    private final static BowTracer bowTracer = new BowTracer();
    private static StatManager statManager;

    public static File STATS_FOLDER;

    public static CombatStats getInstance() {
        return instance;
    }

    public static BowTracer getBowTracer() {
        return bowTracer;
    }

    @Override
    public void onEnable() {

        STATS_FOLDER = new File(this.getDataFolder(), "stats");

        instance = this;
        statManager = new StatManager();
        //初始化数据管理器

        bowTracer.start();

        Bukkit.getPluginManager().registerEvents(new BowListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new KDListener(), this);
        //注册监听器

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        if (!STATS_FOLDER.exists()) {
            STATS_FOLDER.mkdir();
        }
    }

    @Override
    public void onDisable() {
        statManager.save();
        //插件关闭时储存数据
    }

    public static StatManager getStatManager() {
        return statManager;
    }
}
