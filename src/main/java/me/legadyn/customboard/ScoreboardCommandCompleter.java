package me.legadyn.customboard;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ScoreboardCommandCompleter implements TabCompleter {
    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            this.arguments.clear();
            results.clear();
            this.arguments.add("start");
            this.arguments.add("stop");
            this.arguments.add("show");
            this.arguments.add("settimer");
            this.arguments.add("limit");
            this.arguments.add("settitle");
            this.arguments.add("setline");
            this.arguments.add("countdown");
            for (String s : this.arguments) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase()))
                    results.add(s);
            }
            return results;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("settimer") || args[0].equalsIgnoreCase("limit")) {
                this.arguments.clear();
                results.clear();
                this.arguments.add("s");
                this.arguments.add("m");
                this.arguments.add("h");
            } else if (args[0].equalsIgnoreCase("setline")) {
                this.arguments.clear();
                results.clear();
                this.arguments.add("1");
                this.arguments.add("2");
                this.arguments.add("3");
                this.arguments.add("4");
                this.arguments.add("5");
                this.arguments.add("6");
            }
            else if (args[0].equalsIgnoreCase("countdown") || args[0].equalsIgnoreCase("show")) {
                this.arguments.clear();
                results.clear();
                this.arguments.add("true");
                this.arguments.add("false");
            } else {
                this.arguments.clear();
                results.clear();
                this.arguments.add("");
            }
            for (String s : this.arguments) {
                if (s.toLowerCase().startsWith(args[1].toLowerCase()))
                    results.add(s);
            }
            return results;
        }
        List<String> defaultResult = new ArrayList<>();
        defaultResult.add("");
        return defaultResult;
    }
}
