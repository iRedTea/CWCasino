package ru.cactusworld.cwcasino.casinomachine;

import org.bukkit.entity.Player;

public interface WinningGiveManager {
    void winning(Player player, int winning, CasinoMachine casinoMachine);
}
