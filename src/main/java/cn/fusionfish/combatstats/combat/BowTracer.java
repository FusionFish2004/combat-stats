package cn.fusionfish.combatstats.combat;

import cn.fusionfish.combatstats.CombatStats;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.Map;

/**
 * 检测玩家瞄准时长
 * @author JeremyHu
 */
public class BowTracer {

    private final Map<Player, Date> lastInteract = Maps.newHashMap();
    //储存玩家上一次触发PlayerInteractEvent的时间
    private final Map<Projectile, AimEvent> aimEventMap = Maps.newHashMap();
    //用于缓存，等待对应的EntityShootBowEvent
    private final Map<Projectile, Player> dodgeMap = Maps.newHashMap();
    //用于缓存
    private final BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.getNearbyEntities(3,3,3).stream()
                        .filter(entity -> entity instanceof Arrow)
                        .map(entity -> (Arrow) entity)
                        .filter(arrow -> arrow.getShooter() instanceof Player)
                        .filter(arrow -> !arrow.getShooter().equals(player))
                        .filter(arrow -> !arrow.isOnGround())
                        .forEach(arrow -> {
                            if (!dodgeMap.containsKey(arrow)) {
                                CombatStats.getStatManager().get(player).dodgeTry();
                            }
                            dodgeMap.put(arrow, player);
                        });
            }
        }
    };
    //检测所有玩家周围的投掷物

    public void start() {
        runnable.runTaskTimer(CombatStats.getInstance(), 0L, 1L);

    }

    public void interact(Player player) {
        lastInteract.put(player, new Date());
    }

    public void shoot(Player player, Projectile projectile) {
        double last = lastInteract.get(player).getTime();
        double now = new Date().getTime();
        AimEvent aimEvent = new AimEvent(last, now, player);
        aimEventMap.put(projectile, aimEvent);
    }

    public void hitGround(Projectile projectile) {
        //当箭击中地面时
        aimEventMap.remove(projectile);
        //从缓存中移除该投掷物
        if (dodgeMap.containsKey(projectile)) {
            Player player = dodgeMap.get(projectile);
            CombatStats.getStatManager().get(player).dodge();
            dodgeMap.remove(projectile);
        }
    }

    public void hitPlayer(Projectile projectile) {
        AimEvent aimEvent = aimEventMap.get(projectile);
        Player shooter = aimEvent.getShooter();
        double time = aimEvent.getTime();
        shooter.sendActionBar("§c§l" + time + "s");
        //Stat.get(shooter).shoot(time);
        CombatStats.getStatManager().get(shooter).shoot(time);
        aimEventMap.remove(projectile);

        dodgeMap.remove(projectile);
    }

    /**
     * 玩家瞄准事件
     * @author JeremyHu
     */
    public static class AimEvent {

        private final double time;

        public double getTime() {
            return time;
        }

        public Player getShooter() {
            return shooter;
        }

        private final Player shooter;

        public AimEvent(double last, double now, Player shooter) {
            this.shooter = shooter;
            this.time = (now - last) / 1000;
        }
    }
}
