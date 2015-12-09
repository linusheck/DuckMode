package me.glatteis.duckmode;

import me.glatteis.duckmode.weapons.WeaponWatch;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ContinueGame {

    public static int roundCounter = 0;
    public static boolean canNotMove = false;
    private static boolean roundHasEnded = true;

    public static boolean hasRoundEnded() {
        return roundHasEnded;
    }

    public static void setRoundHasEnded(boolean has) {
        roundHasEnded = has;
    }

    public static void startRound() {
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
                    if (roundCounter == SettingDatabase.settingSwitches.get(SettingTypes.ROUNDS.toString()).get(SettingDatabase.intSetting.get(SettingTypes.ROUNDS.toString()))) { //$NON-NLS-1$ //$NON-NLS-2$
                        DuckMain.state = GameState.INTERMISSION;
                        roundCounter = 0;
                        Intermission.intermission();
                        return;
                    }
                    DuckMain.state = GameState.PREGAME;
                    roundCounter++;
                    where = !where;

                    new BukkitRunnable() {
                        public void run() {
                            LevelGenerator.buildPlace(where);
                            final List<Location> spawnPoints = PlayerSpawnPoints.spawnPoints;
                            new BukkitRunnable() {
                                public void run() {
                                    SchematicLoad.loadAllSchematics();
                                    countdownToRound(spawnPoints);
                                }
                            }.runTask(DuckMain.getPlugin());
                        }
                    }.runTaskAsynchronously(DuckMain.getPlugin());
                }
            }
        }.runTaskTimer(DuckMain.getPlugin(), 20L, 20L);
    }

    public static void countdownToRound(final List<Location> spawnPoints) {
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
                    }
                } else if (counter == 3) {
                    for (Duck d : DuckMain.ducks) {
                        d.setDead(false);
                        d.getPlayer().playSound(d.getPlayer().getLocation(), Sound.PISTON_RETRACT, 10, 1);
                        StaticMethods.enableJumping(d.getPlayer());
                    }
                    DuckMain.state = GameState.INGAME;
                    canNotMove = false;
                    this.cancel();
                    return;
                }
                counter++;
            }
        }.runTaskTimer(DuckMain.getPlugin(), 50L, 10L);
    }

    public static void round(final List<Location> spawnPoints) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (!(spawnPoints.size() < DuckMain.ducks.size())) { //Wait for generation to finish
                    for (int i = 0; i < DuckMain.ducks.size(); i++) {
                        StaticMethods.prepareInventory(DuckMain.ducks.get(i));
                        DuckMain.ducks.get(i).getPlayer().updateInventory();
                        DuckMain.ducks.get(i).getPlayer().teleport(spawnPoints.get(i).clone().add(0, 0.5, 0));
                        DuckMain.ducks.get(i).getPlayer().setGameMode(GameMode.ADVENTURE);
                    }
                    spawnPoints.clear();
                    this.cancel();
                }

            }
        }.runTaskTimer(DuckMain.getPlugin(), 0L, 10L);

    }


}
