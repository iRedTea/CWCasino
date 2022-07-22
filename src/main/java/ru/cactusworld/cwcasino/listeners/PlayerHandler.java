package ru.cactusworld.cwcasino.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachine;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachineManager;
import ru.cactusworld.cwcasino.tools.Message;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class PlayerHandler implements Listener {

    private final CasinoMachineManager casinoMachineManager;

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            boolean isMachine = false;
            CasinoMachine casinoMachine = null;
            for(CasinoMachine machine : casinoMachineManager.getAllMachines()) {
                if(machine.getButton().getLocation().equals(block.getLocation())) {
                    isMachine = true;
                    casinoMachine = machine;
                }
            }
            if(isMachine) {
                if(event.getPlayer().hasPermission("casino.use"))
                    casinoMachineManager.activateMachine(casinoMachine, event.getPlayer());
                else Message.errors_noPermissions.send(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.STONE_BUTTON || event.getBlock().getType() == Material.WALL_SIGN) {
            for(CasinoMachine casinoMachine : casinoMachineManager.getAllMachines()) {
                if(casinoMachine.getSign().getLocation().equals(event.getBlock().getLocation())
                || casinoMachine.getButton().getLocation().equals(event.getBlock().getLocation())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
