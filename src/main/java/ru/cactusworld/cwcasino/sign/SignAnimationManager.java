package ru.cactusworld.cwcasino.sign;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachine;

public interface SignAnimationManager {
    void setDefault(Sign sign, int bet);
    void startSignAnimation(int max, CasinoMachine casinoMachine, int winning, Block block, Player player);
    void endTact(Sign sign, int winning, Player player, CasinoMachine casinoMachine);

}
