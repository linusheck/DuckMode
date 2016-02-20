package me.glatteis.duckmode.game;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GameEnd {

    public static void win() {
        Queue<Duck> ducks = new LinkedList<Duck>();
        Collection<Integer> duckScores = new ArrayList<Integer>(WinTracker.wins.values());
        for (int i = 0; i < DuckMain.ducks.size(); i++) {
            Integer maxValue = Collections.max(duckScores);
            if (maxValue == null) {
                break;
            }
            Duck thisDuck = null;
            for (Duck d : WinTracker.wins.keySet()) {
                if (WinTracker.wins.get(d).equals(maxValue) && !ducks.contains(d)) {
                    thisDuck = d;
                    break;
                }
            }
            if (thisDuck == null) {
                break;
            }
            ducks.add(thisDuck);
        }

        List<Duck> winnerDucks = new ArrayList<Duck>();
        Bukkit.broadcastMessage(ChatColor.GREEN + "====================");
        Bukkit.broadcastMessage(Messages.getString("scores_in_game"));
        int counter = 0;
        while (!ducks.isEmpty()) {
            counter++;
            Duck d = ducks.poll();
            winnerDucks.add(d);
            Bukkit.broadcastMessage(ChatColor.BOLD.toString() + counter + ". " + ChatColor.RESET + d.getPlayer().getName() + " - " + WinTracker.wins.get(d) + Messages.getString("games_won"));
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "====================");
        Bukkit.broadcastMessage(Messages.getString("server_restarts"));
        try {
            runCommands(winnerDucks.get(0), winnerDucks.get(1), winnerDucks.get(2));
        } catch (Exception e) {
            //
        }
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.kickPlayer(ChatColor.DARK_GREEN + "DuckMode " + ChatColor.RESET + Messages.getString("ended"));
                }
                Bukkit.shutdown();
            }
        }.runTaskLater(DuckMain.getPlugin(), 400);
    }

    private static void runCommands(Duck winner, Duck second, Duck third) throws IOException {
        String path = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString() + "/plugins/DuckMode/end_commands.txt";
        FileInputStream stream = new FileInputStream(path);
        Scanner s = new Scanner(stream);
        List<String> commands = new ArrayList<String>();
        while (s.hasNext()) {
            commands.add(s.next());
        }
        for (String thisCommand : commands) {
            if (thisCommand.contains("@second") && second == null || thisCommand.contains("@third") && third == null)
                continue;
            thisCommand = thisCommand.replaceAll("@winner", winner.getPlayer().getName())
                    .replaceAll("@second", second != null ? second.getPlayer().getName() : "")
                    .replaceAll("@third", third != null ? third.getPlayer().getName() : "");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), thisCommand);
        }
    }

}
