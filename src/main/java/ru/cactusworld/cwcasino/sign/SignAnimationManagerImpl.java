package ru.cactusworld.cwcasino.sign;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Button;
import ru.cactusworld.cwcasino.CWCasino;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachine;
import ru.cactusworld.cwcasino.casinomachine.WinningGiveManager;
import ru.cactusworld.cwcasino.tools.Message;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class SignAnimationManagerImpl implements SignAnimationManager {
    private final CWCasino cwCasino;

    private final WinningGiveManager winningGiveManager;

    public void setDefault(Sign sign, int bet) {
        List<String> lines = Message.signLines.replace("%bet%", String.valueOf(bet) + Message.signAnimation_currencySymbol).toList();
        sign.setLine(0, lines.get(0));
        sign.setLine(1, lines.get(1));
        sign.setLine(2, lines.get(2));
        sign.setLine(3, lines.get(3));
        sign.update();
    }

    @SneakyThrows
    public void startSignAnimation(int max, CasinoMachine casinoMachine, int winning, Block block, Player player) {
        if(block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            List<String> lines = Message.signAnimation_lines.replace("%player%", player.getDisplayName()).toList();
            sign.setLine(0, lines.get(0));
            sign.setLine(1, lines.get(1));
            sign.setLine(2, lines.get(2));
            sign.update();
            startTact(sign, max, casinoMachine, player, winning);
        } else throw new RuntimeException("Block on " + block.getLocation().toString() + " is not a sign");
    }

    public void endTact(Sign sign, int winning, Player player, CasinoMachine casinoMachine) {
        setWinningLine(sign, winning);
        casinoMachine.getButton().setType(Material.STONE_BUTTON);
        BlockState bs = casinoMachine.getButton().getState();
        Button but = (Button) bs.getData();
        but.setFacingDirection(BlockFace.UP);
        bs.setData(but);
        bs.update(true);
        winningGiveManager.winning(player, winning, casinoMachine);
        Bukkit.getScheduler().scheduleSyncDelayedTask(cwCasino, () -> {
            setDefault(sign, casinoMachine.getBet());
        }, 17L * 2);
    }

    @SneakyThrows
    private void startTact(Sign sign, int max, CasinoMachine casinoMachine, Player player, int winning) {
        casinoMachine.getButton().setType(Material.AIR);
        for(int i = 5; i >= 0; i--) {
            if(i == 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(cwCasino, () -> {
                    endTact(sign, winning, player, casinoMachine);
                }, 20L * 5);
                break;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(cwCasino, () -> {
                setRandomLine(sign, max);
            }, 20L * i);
        }

    }

    private void setRandomLine(Sign sign, int max) {

        sign.setLine(3, Message.signAnimation_linePrefix + String.format(Locale.US, "%,d", (int) (Math.random() * ++max)) +
                Message.signAnimation_currencySymbol);
        sign.update();
    }

    private void setWinningLine(Sign sign, int winning) {
        sign.setLine(3, Message.signAnimation_linePrefix + String.format(Locale.US, "%,d", winning) +
                Message.signAnimation_currencySymbol);
        sign.update();
    }
}
