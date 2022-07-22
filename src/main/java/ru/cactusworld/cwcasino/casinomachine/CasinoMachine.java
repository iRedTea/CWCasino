package ru.cactusworld.cwcasino.casinomachine;

import lombok.*;
import org.bukkit.block.Block;

@Data
@Builder
public class CasinoMachine {
    private final @NonNull Block sign;
    private final @NonNull Block button;
    private final int bet;
    private final int max;
}
