package ru.cactusworld.cwcasino.tools;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Message {
    reload,

    errors_machineIsEmployed, errors_noPermissions,

    takeMoney_success, takeMoney_failed,

    winning_title, winning_subtitle, winning_message,

    signLines,

    signAnimation_lines, signAnimation_linePrefix, signAnimation_currencySymbol;


    private List<String> msg;

    private static boolean PAPI;

    @SuppressWarnings("unchecked")
    public static void load(FileConfiguration c, boolean PAPIEnabled) {
        for(Message message : Message.values()) {
            PAPI = PAPIEnabled;
            try {
                Object obj = c.get("messages." + message.name().replace("_", "."));
                if(obj instanceof List) {
                    message.msg = (((List<String>) obj)).stream().map(m -> ChatColor.translateAlternateColorCodes('&', m)).collect(Collectors.toList());
                } else {
                    message.msg = Lists.newArrayList(obj == null ? "" : ChatColor.translateAlternateColorCodes('&', obj.toString()));
                }
            } catch (NullPointerException e) {
            }
        }
    }



    public Sender replace(String from, String to) {
        Sender sender = new Sender();
        return sender.replace(from, to);
    }

    public void send(CommandSender player) {
        new Sender().send(player);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(String s : Message.this.msg) {
            str.append(" ").append(s);
        }
        return ChatColor.translateAlternateColorCodes('&', str.toString());
    }

    public List<String> toList() {
        ArrayList<String> result = new ArrayList<>();
        for(String m : msg) {
            result.add(ChatColor.translateAlternateColorCodes('&',m));
        }
        return result;
    }

    public class Sender {
        private final Map<String, String> placeholders = new HashMap<>();

        public void send(CommandSender player) {
            for(String message : Message.this.msg) {
                sendMessage(player, replacePlaceholders(message));
            }

        }

        public Sender replace(String from, String to) {
            placeholders.put(from, ChatColor.translateAlternateColorCodes('&', to));
            return this;
        }

        private void sendMessage(CommandSender player, String message) {

            player.sendMessage(message);

        }

        private String replacePlaceholders(String message) {
            for(Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for(String s : Message.this.msg) {
                str.append(" ").append(replacePlaceholders(s));
            }
            return ChatColor.translateAlternateColorCodes('&', str.toString());
        }

        public List<String> toList() {
            ArrayList<String> list = new ArrayList<>();
            for(String s : Message.this.msg) {
                list.add(ChatColor.translateAlternateColorCodes('&', replacePlaceholders(s)));
            }
            return list;
        }

    }
}
