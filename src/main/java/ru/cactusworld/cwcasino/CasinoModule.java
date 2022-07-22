package ru.cactusworld.cwcasino;

import com.google.inject.AbstractModule;
import lombok.AllArgsConstructor;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachineManagerImpl;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachineManager;
import ru.cactusworld.cwcasino.casinomachine.WinningGiveManager;
import ru.cactusworld.cwcasino.casinomachine.WinningGiveManagerImpl;
import ru.cactusworld.cwcasino.sign.SignAnimationManager;
import ru.cactusworld.cwcasino.sign.SignAnimationManagerImpl;

@AllArgsConstructor
public class CasinoModule extends AbstractModule {

    private final CWCasino cwCasino;

    @Override
    protected void configure() {
        bind(CasinoMachineManager.class).to(CasinoMachineManagerImpl.class);
        bind(SignAnimationManager.class).to(SignAnimationManagerImpl.class);
        bind(WinningGiveManager.class).to(WinningGiveManagerImpl.class);
        bind(CWCasino.class).toInstance(cwCasino);
    }
}
