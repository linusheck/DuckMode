package me.glatteis.duckmode;

public enum SettingTypes {

    POINTS_TO_WIN(Messages.getString("points_to_win")),
    ROUNDS(Messages.getString("rounds")),
    START_GAME(Messages.getString("start_game")),
    HATS(Messages.getString("hats")),
    OTHER(Messages.getString("other"));

    String s;

    SettingTypes(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}
