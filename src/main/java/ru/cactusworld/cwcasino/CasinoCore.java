package ru.cactusworld.cwcasino;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.cactusworld.cwcasino.casinomachine.CasinoMachineManager;
import ru.cactusworld.cwcasino.commands.CasinoCommand;
import ru.cactusworld.cwcasino.economy.EconomyManager;
import ru.cactusworld.cwcasino.listeners.PlayerHandler;
import ru.cactusworld.cwcasino.tools.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CasinoCore {
    private final CWCasino cwCasino;

    private final PlayerHandler playerHandler;

    private final CasinoMachineManager casinoMachineManager;

    private final CasinoCommand casinoCommand;

    private final EconomyManager economyManager;

    public void init() {
        cwCasino.saveDefaultConfig();
        Message.load(cwCasino.getConfig(), false);

        CommandManager commandManager = new CommandManager(cwCasino);
        commandManager.register(casinoCommand);

        cwCasino.getServer().getPluginManager().registerEvents(playerHandler, cwCasino);

        casinoMachineManager.init(YamlConfiguration.loadConfiguration(getFileFromJar("machines.yml")));
        economyManager.init();
    }

    public void reload() {
        cwCasino.reloadConfig();
        Message.load(cwCasino.getConfig(), false);
        casinoMachineManager.init(YamlConfiguration.loadConfiguration(getFileFromJar("machines.yml")));
    }

    @SneakyThrows
    public File getFileFromJar(String fileName) {
        File file = new File(cwCasino.getDataFolder() + File.separator + fileName);
        if(!file.exists()) {
            file.createNewFile();
            InputStream ddlStream = CWCasino.class.getClassLoader().getResourceAsStream(fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buf = new byte[2048];
                int r;
                while(-1 != (r = ddlStream.read(buf))) {
                    fos.write(buf, 0, r);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
