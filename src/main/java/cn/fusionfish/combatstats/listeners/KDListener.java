package cn.fusionfish.combatstats.listeners;

import cn.fusionfish.combatstats.CombatStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * 监听玩家击杀/死亡事件
 * @author JeremyHu
 */
public class KDListener implements Listener {
    @EventHandler
    public void onDie(PlayerDeathEvent event) {
        Player player = event.getEntity();
        //Stat.get(player).die();
        CombatStats.getStatManager().get(player).die();
        String msg = event.getDeathMessage();
        //获取击杀信息
        for (Player p : Bukkit.getOnlinePlayers()) {
            String name = p.getName();
            assert msg != null;
            if (msg.contains(name) && p != player) {
                //如果击杀信息中包含其他玩家名字
                //Stat.get(p).kill();
                CombatStats.getStatManager().get(p).kill();
            }
        }
    }
}
