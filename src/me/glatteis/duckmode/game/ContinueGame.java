package me.glatteis.duckmode.game;

import me.glatteis.duckmode.Duck;
import me.glatteis.duckmode.DuckMain;
import me.glatteis.duckmode.generation.LevelGenerator;
import me.glatteis.duckmode.generation.SchematicLoader;
import me.glatteis.duckmode.generation.SpawnWeapons;
import me.glatteis.duckmode.messages.Messages;
import me.glatteis.duckmode.reflection.DuckReflectionMethods;
import me.glatteis.duckmode.setting.SettingDatabase;
import me.glatteis.duckmode.setting.SettingType;
import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
        DuckMain.setState(GameState.INGAME);
        SpawnWeapons.enable();
        for (int i = 0; i < DuckMain.getPlugin().getDucks().size(); i++) {
            DuckMain.getPlugin().getDuckCount().put(i, DuckMain.getPlugin().getDucks().get(i));
            DuckMain.getPlugin().getDucks().get(i).getPlayer().playSound(DuckMain.getPlugin().getDucks().get(i).getPlayer().getLocation(),
                    Sound.ENTITY_CHICKEN_EGG, 10, 1);
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
                    SchematicLoader.clear();
                    roundHasEnded = false;
                    if (roundCounter == SettingDatabase.settingSwitches.get(SettingType.ROUNDS.toString()).get(SettingDatabase.intSetting.get(SettingType.ROUNDS.toString()))) {
                        roundCounter = 0;
                        Intermission.getIntermission().intermission();
                        return;
                    }
                    DuckMain.setState(GameState.PREGAME);
                    roundCounter++;
                    where = !where;

                    LevelGenerator.getLevelGenerator().buildPlace(where);
                    final List<Location> spawnPoints = SchematicLoader.getPlayerSpawnPoints();
                    SchematicLoader.loadAllSchematics(new SchematicLoader.ThenTask() {
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
                    for (Duck d : DuckMain.getPlugin().getDucks()) {
                        d.getPlayer().setFireTicks(0);
                    }
                    round(spawnPoints);
                    canNotMove = true;
                    LevelGenerator.getLevelGenerator().initLightingForLastGeneration();
                } else if (counter < 3) {
                    for (Duck d : DuckMain.getPlugin().getDucks()) {
                        d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.BLOCK_PISTON_EXTEND, 10, 1);
                        DuckReflectionMethods.title(d.getPlayer(), " ", 0, 10, 0);
                        DuckReflectionMethods.subtitle(d.getPlayer(), sentences[counter - 1], 0, 10, 0);
                    }
                } else if (counter == 3) {
                    for (Duck d : DuckMain.getPlugin().getDucks()) {
                        d.setDead(false);
                        d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.BLOCK_PISTON_CONTRACT, 10, 1);
                        d.enableJumping();
                        DuckReflectionMethods.title(d.getPlayer(), " ", 0, 10, 0);
                        DuckReflectionMethods.subtitle(d.getPlayer(), sentences[counter - 1], 0, 10, 0);
                    }
                    newGameRunnableAlreadyExecuted = false;
                    DuckMain.setState(GameState.INGAME);
                    canNotMove = false;
                    this.cancel();
                    return;
                }
                counter++;
            }
        }.runTaskTimer(DuckMain.getPlugin(), 50L, 6L);
    }

    private void round(final List<Location> spawnPoints) {
        Location spectatorSpawn = new Location(DuckMain.getWorld(), 0, 0, 0);
        for (int i = 0; i < DuckMain.getPlugin().getDucks().size(); i++) {
            DuckMain.getPlugin().getDucks().get(i).prepareInventory();
            DuckMain.getPlugin().getDucks().get(i).getPlayer().updateInventory();
            DuckMain.getPlugin().getDucks().get(i).getPlayer().teleport(spawnPoints.get(i));
            DuckMain.getPlugin().getDucks().get(i).getPlayer().setGameMode(GameMode.ADVENTURE);
            spectatorSpawn = spectatorSpawn.add(spawnPoints.get(i));
        }
        spectatorSpawn = spectatorSpawn.toVector().divide(
                new Vector(spawnPoints.size(), spawnPoints.size(), spawnPoints.size())
        ).toLocation(DuckMain.getWorld());
        for (Player p : DuckMain.getPlugin().getSpectators()) {
            p.teleport(spectatorSpawn);
        }
        spawnPoints.clear();
    }


    public void checkForWin() {
        if (DuckMain.getState().equals(GameState.PREGAME)) return;
        int aliveDucks = 0;
        for (Duck d : DuckMain.getPlugin().getDucks()) {
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
                        for (Duck d : DuckMain.getPlugin().getDucks()) {
                            if (!d.isDead()) {
                                aliveDucks++;
                            }
                        }
                        if (newGameRunnableAlreadyExecuted && aliveDucks == 1 && !DuckMain.getState().equals(GameState.PREGAME)) {
                            for (Duck d : DuckMain.getPlugin().getDucks()) {
                                if (!d.isDead()) {
                                    d.getPlayer().getInventory().setHelmet(new ItemStack(Material.SMOOTH_BRICK));
                                    for (Duck d2 : DuckMain.getPlugin().getDucks()) {
                                        DuckReflectionMethods.title(d2.getPlayer(), ChatColor.WHITE + d.getPlayer().getName(), 5, 20, 5);
                                        DuckReflectionMethods.subtitle(d2.getPlayer(), ChatColor.WHITE + Messages.getString("survived"), 5, 20, 5);
                                        d2.getPlayer().playSound(d.getPlayer().getLocation(), Sound.BLOCK_NOTE_HARP, 20, 5);
                                    }
                                    WinTracker.addWin(d);
                                    DuckMain.getPlugin().getContinueGame().setRoundHasEnded(true);
                                }
                            }
                        } else {
                            DuckMain.getPlugin().getContinueGame().setRoundHasEnded(true);
                        }
                    }
                }.runTaskLater(DuckMain.getPlugin(), 30L);
            }
        }
    }


}
