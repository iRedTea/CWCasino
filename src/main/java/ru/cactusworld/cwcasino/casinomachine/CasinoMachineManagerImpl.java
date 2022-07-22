package ru.cactusworld.cwcasino.casinomachine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.cactusworld.cwcasino.economy.EconomyManager;
import ru.cactusworld.cwcasino.sign.SignAnimationManager;
import ru.cactusworld.cwcasino.sign.SignFactory;
import ru.cactusworld.cwcasino.tools.Message;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class CasinoMachineManagerImpl implements CasinoMachineManager {
    private final SignAnimationManager signAnimationManager;

    private final EconomyManager economyManager;

    private final SignFactory signFactory;

    private List<CasinoMachine> machines;

    private List<CasinoMachine> activeMachines;

    public List<CasinoMachine> getAllMachines() {
        return machines;
    }

    public void init(FileConfiguration machines) {
        activeMachines = new ArrayList<>();
        this.machines = new ArrayList<>();
        for(String key : machines.getConfigurationSection("machines").getKeys(false)) {
            int bet = machines.getInt("machines." + key + ".bet");
            int max = machines.getInt("machines." + key + ".max");
            World world = Bukkit.getWorld(machines.getString("machines." + key + ".world"));
            Block sign = world.getBlockAt(
                    machines.getInt("machines." + key + ".sign.x"),
                    machines.getInt("machines." + key + ".sign.y"),
                    machines.getInt("machines." + key + ".sign.z")
            );
            signFactory.createSign(sign.getLocation(), bet);
            Block button = world.getBlockAt(
                    machines.getInt("machines." + key + ".button.x"),
                    machines.getInt("machines." + key + ".button.y"),
                    machines.getInt("machines." + key + ".button.z")
            );
            this.machines.add(CasinoMachine.builder().bet(bet).button(button).max(max).sign(sign).build());
        }
    }

    public void activateMachine(CasinoMachine casinoMachine, Player player) {
        if(activeMachines.contains(casinoMachine)) {
            Message.errors_machineIsEmployed.send(player);
        } else {
            int max = casinoMachine.getMax();
            if(economyManager.takeMoney(player, casinoMachine.getBet())) {
                activeMachines.add(casinoMachine);
                Message.takeMoney_success.replace("%bet%", String.valueOf(casinoMachine.getBet())).send(player);
                int winning = (int) (Math.random() * ++max);
                signAnimationManager.startSignAnimation(max, casinoMachine, winning, casinoMachine.getSign(), player);
            } else Message.takeMoney_failed.send(player);
        }
    }

    public void removeActiveMachine(CasinoMachine machine) {
        activeMachines.remove(machine);
    }
}
