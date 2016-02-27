package me.glatteis.duckmode.game;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.StaticMethods;
import me.glatteis.duckmode.generation.LevelGenerator;
import me.glatteis.duckmode.generation.SchematicLoad;
import me.glatteis.duckmode.generation.SpawnWeapons;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.setting.SettingDatabase;
import me.glatteis.duckmode.setting.SettingTypes;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ContinueGame {

    public int roundCounter = 0;
    public boolean canNotMove = false;
    private boolean roundHasEnded = true;
    private String[] sentences = null;
    private boolean newGameRunnableAlreadyExecuted = false;

    public boolean hasRoundEnded() {
        return roundHasEnded;
    }

    public void setRoundHasEnded(boolean has) {
        roundHasEnded = has;
    }

    public void startRound() {
        if (sentences == null)
            sentences = new String[]{Messages.getString("ready"), Messages.getString("set"), Messages.getString("go")};
        DuckMain.state = GameState.INGAME;
        SpawnWeapons.enable();
        for (int i = 0; i < DuckMain.ducks.size(); i++) {
            DuckMain.duckCount.put(i, DuckMain.ducks.get(i));
            DuckMain.ducks.get(i).getPlayer().playSound(DuckMain.ducks.get(i).getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 10, 1);
        }
        new BukkitRunnable() {
            boolean where = true;

            @Override
            public void run() {
                if (roundHasEnded) {
                    for (org.bukkit.entity.Entity e : DuckMain.getWorld().getEntities()) {
                        if (e instanceof Item || e instanceof ArmorStand) {
                            e.remove();
                        }
                    }
                    WeaponWatch.durability.clear();
                    SpawnWeapons.locations.clear();
                    SchematicLoad.clear();
                    roundHasEnded = false;
                    if (roundCounter == SettingDatabase.settingSwitches.get(SettingTypes.ROUNDS.toString()).get(SettingDatabase.intSetting.get(SettingTypes.ROUNDS.toString()))) {
                        DuckMain.state = GameState.INTERMISSION;
                        roundCounter = 0;
                        Intermission.intermission();
                        return;
                    }
                    DuckMain.state = GameState.PREGAME;
                    roundCounter++;
                    where = !where;

                    LevelGenerator.buildPlace(where);
                    final List<Location> spawnPoints = PlayerSpawnPoints.spawnPoints;
                    SchematicLoad.loadAllSchematics(new SchematicLoad.ThenTask() {
                        @Override
                        public void finished() {
                            countdownToRound(spawnPoints);
                        }
                    });
                }
            }
        }.runTaskTimer(DuckMain.getPlugin(), 20L, 20L);
    }

    public void countdownToRound(final List<Location> spawnPoints) {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter == 0) {
                    for (Duck d : DuckMain.ducks) {
                        d.getPlayer().setFireTicks(0);
                    }
                    round(spawnPoints);
                    canNotMove = true;
                } else if (counter < 3) {
                    for (Duck d : DuckMain.ducks) {
                        d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.PISTON_EXTEND, 10, 1);
                        DuckReflectionMethods.title(d.getPlayer(), " ", 0, 10, 0);
                        DuckReflectionMethods.subtitle(d.getPlayer(), sentences[counter - 1], 0, 10, 0);
                    }
                } else if (counter == 3) {
                    for (Duck d : DuckMain.ducks) {
                        d.setDead(false);
                        d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.PISTON_RETRACT, 10, 1);
                        StaticMethods.enableJumping(d.getPlayer());
                        DuckReflectionMethods.title(d.getPlayer(), " ", 0, 10, 0);
                        DuckReflectionMethods.subtitle(d.getPlayer(), sentences[counter - 1], 0, 10, 0);
                    }
                    newGameRunnableAlreadyExecuted = false;
                    DuckMain.state = GameState.INGAME;
                    canNotMove = false;
                    this.cancel();
                    return;
                }
                counter++;
            }
        }.runTaskTimer(DuckMain.getPlugin(), 50L, 6L);
    }

    public void round(final List<Location> spawnPoints) {
        for (int i = 0; i < DuckMain.ducks.size(); i++) {
            DuckMain.ducks.get(i).prepareInventory();
            DuckMain.ducks.get(i).getPlayer().updateInventory();
            DuckMain.ducks.get(i).getPlayer().teleport(spawnPoints.get(i));
            DuckMain.ducks.get(i).getPlayer().setGameMode(GameMode.ADVENTURE);
        }
        spawnPoints.clear();
    }


    public void checkForWin() {
        if (DuckMain.state.equals(GameState.PREGAME)) return;
        int aliveDucks = 0;
        for (Duck d : DuckMain.ducks) {
            if (!d.isDead()) {
                aliveDucks++;
            }
        }
        if (aliveDucks <= 1) {
            if (!newGameRunnableAlreadyExecuted) {
                newGameRunnableAlreadyExecuted = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int aliveDucks = 0;
                        for (Duck d : DuckMain.ducks) {
                            if (!d.isDead()) {
                                aliveDucks++;
                            }
                        }
                        if (newGameRunnableAlreadyExecuted && aliveDucks == 1 && !DuckMain.state.equals(GameState.PREGAME)) {
                            for (Duck d : DuckMain.ducks) {
                                if (!d.isDead()) {
                                    d.getPlayer().getInventory().setHelmet(new ItemStack(Material.SMOOTH_BRICK));
                                    for (Duck d2 : DuckMain.ducks) {
                                        DuckReflectionMethods.title(d2.getPlayer(), ChatColor.WHITE + d.getPlayer().getName(), 5, 20, 5);
                                        DuckReflectionMethods.subtitle(d2.getPlayer(), ChatColor.WHITE + Messages.getString("survived"), 5, 20, 5);
                                        d2.getPlayer().playSound(d.getPlayer().getLocation(), Sound.NOTE_PIANO, 20, 5);
                                    }
                                    WinTracker.addWin(d);
                                    DuckMain.continueGame.setRoundHasEnded(true);
                                }
                            }
                        } else {
                            DuckMain.continueGame.setRoundHasEnded(true);
                        }
                    }
                }.runTaskLater(DuckMain.getPlugin(), 30L);
            }
        }
    }


}
