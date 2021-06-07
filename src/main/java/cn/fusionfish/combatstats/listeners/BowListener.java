package cn.fusionfish.combatstats.listeners;

import cn.fusionfish.combatstats.CombatStats;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 监听玩家射箭/交互事件
 * @author JeremyHu
 */
public class BowListener implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;  //若不是玩家，返回
        Player player = (Player) event.getEntity();
        CombatStats.getBowTracer().shoot(player, (Projectile) event.getProjectile());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        //若不是右键，返回
        ItemStack mainHand = player.getInventory().getItemInMainHand();  //获取主手物品
        ItemStack offHand = player.getInventory().getItemInOffHand();  //获取副手物品
        if (!(mainHand.getType() == Material.BOW || offHand.getType() == Material.BOW)) return;
        //若玩家手持物品不是弓，返回
        CombatStats.getBowTracer().interact(player);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getType() != EntityType.ARROW) return;
        //如果投掷物不是箭，返回
        if (event.getHitBlock() != null) {
            //如果击中方块
            CombatStats.getBowTracer().hitGround(projectile);
        }

        if (event.getHitEntity() != null) {
            if (event.getHitEntity().getType() == EntityType.PLAYER) {
                CombatStats.getBowTracer().hitPlayer(projectile);
            }
        }
    }
}
