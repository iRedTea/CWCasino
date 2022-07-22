package ru.cactusworld.cwcasino;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

public final class CWCasino extends JavaPlugin {
    private CasinoCore core;

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(new CasinoModule(this));
        CasinoCore core = injector.getInstance(CasinoCore.class);
        core.init();
        this.core = core;
    }

    @Override
    public void onDisable() {
    }

    public void restart () {
        core.reload();
    }
}
