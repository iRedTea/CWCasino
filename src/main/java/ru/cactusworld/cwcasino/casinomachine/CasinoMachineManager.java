package ru.cactusworld.cwcasino.casinomachine;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public interface CasinoMachineManager {
    List<CasinoMachine> getAllMachines();
    void init(FileConfiguration machines);
    void activateMachine(CasinoMachine casinoMachine, Player player);
    void removeActiveMachine(CasinoMachine machine);
}
