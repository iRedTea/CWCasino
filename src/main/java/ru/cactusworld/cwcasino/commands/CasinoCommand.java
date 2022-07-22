package ru.cactusworld.cwcasino.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import ru.cactusworld.cwcasino.CWCasino;
import ru.cactusworld.cwcasino.tools.Message;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Command("casino")
public class CasinoCommand extends CommandBase {
    private final CWCasino cwCasino;

    @Default
    public void defaultCommand(final CommandSender commandSender) {
        if(commandSender.hasPermission("casino.reload")) {
            long timeInitStart = System.currentTimeMillis();
            cwCasino.restart();
            long timeReload = (System.currentTimeMillis() - timeInitStart);
            Message.reload.replace("%time%", String.valueOf(timeReload)).send(commandSender);
        } else Message.errors_noPermissions.send(commandSender);
    }
}
