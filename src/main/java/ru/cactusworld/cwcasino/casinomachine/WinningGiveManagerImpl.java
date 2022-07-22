package ru.cactusworld.cwcasino.casinomachine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import ru.cactusworld.cwcasino.economy.EconomyManager;
import ru.cactusworld.cwcasino.tools.Message;

import java.util.Locale;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WinningGiveManagerImpl implements WinningGiveManager {
    private final EconomyManager economyManager;

    private final CasinoMachineManager casinoMachineManager;

    public void winning(Player player, int winning, CasinoMachine casinoMachine) {
        economyManager.giveMoney(player, winning);
        player.sendTitle(Message.winning_title.replace("%bet%", String.format(Locale.US, "%,d", winning)).toString(), Message.winning_subtitle.toString());
        Message.winning_message.replace("%bet%", String.format(Locale.US, "%,d", winning)).send(player);
        casinoMachineManager.removeActiveMachine(casinoMachine);
    }
}
