package ru.cactusworld.cwcasino.sign;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import ru.cactusworld.cwcasino.CWCasino;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class SignFactory {
    private final CWCasino cwCasino;
    private final SignAnimationManager signAnimationManager;

    public void createSign(@NonNull Location location, int bet) {
        try {
            Sign sign = (Sign) location.getBlock().getState();
            signAnimationManager.setDefault(sign, bet);
        } catch (ClassCastException e) {
            cwCasino.getLogger().severe("Block on location " + location + " is not a sign!");
        }

    }
}
