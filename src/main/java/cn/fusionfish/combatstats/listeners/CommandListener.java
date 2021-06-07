package cn.fusionfish.combatstats.listeners;

import cn.fusionfish.combatstats.CombatStats;
import cn.fusionfish.combatstats.stats.Stat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * 监听玩家聊天
 * 测试用
 * @author JeremyHu
 */
public class CommandListener implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        Player player = event.getPlayer();
        DecimalFormat df = new DecimalFormat("#.###");

        //if (!player.isOp()) return;

        if ("~stats".equalsIgnoreCase(msg)) {
            Stat stat = CombatStats.getStatManager().get(player);
            stat.show(player);
            event.setCancelled(true);
        }

        if ("~save".equalsIgnoreCase(msg)) {
            CombatStats.getStatManager().save();
            event.setCancelled(true);
        }

        if ("~score".equalsIgnoreCase(msg)) {
            Stat stat = CombatStats.getStatManager().get(player);
            stat.score(player);
            event.setCancelled(true);
        }

        if ("~test".equalsIgnoreCase(msg)) {
            Map<String, Double> rank = CombatStats.getStatManager().getAimRank();
            rank.keySet().forEach(s -> player.sendMessage(s + " - " + df.format(rank.get(s)) + "秒"));
            event.setCancelled(true);
        }

    }


}
