package ru.cactusworld.cwcasino.economy;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import ru.cactusworld.cwcasino.CWCasino;


@RequiredArgsConstructor(onConstructor_ = @Inject)
@Singleton
public class EconomyManager {
    private Economy e;

    private final CWCasino cwCasino;

    public void init() {
        RegisteredServiceProvider<Economy> reg = Bukkit.getServicesManager().getRegistration(Economy.class);
        if(reg != null) e = reg.getProvider();
        if(e == null) {
            cwCasino.getLogger().severe("Vault is not installed! CWCasino is Disabled.");
        }
    }
    public boolean takeMoney(Player p, double a) {
        if (e.getBalance(p) < a) return false;
        return e.withdrawPlayer(p, a).transactionSuccess();
    }

    public double getMoney(Player p, double a) {
        return e.getBalance(p);
    }

    public boolean setMoney(Player p, double a) {
        boolean b = true;
        if(!e.withdrawPlayer(p, e.getBalance(p)).transactionSuccess()) b = false;
        if(!e.depositPlayer(p, a).transactionSuccess()) b = false;
        return b;
    }

    public boolean giveMoney(Player p, double a) {
        boolean b = true;
        if(!e.depositPlayer(p, a).transactionSuccess()) b = false;
        return b;
    }

}
