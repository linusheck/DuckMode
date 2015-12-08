package me.glatteis.duckmode;

public enum SettingTypes {

    POINTS_TO_WIN(Messages.getString("points_to_win")), //$NON-NLS-1$
    ROUNDS(Messages.getString("rounds")), //$NON-NLS-1$
    START_GAME(Messages.getString("start_game")), //$NON-NLS-1$
    HATS(Messages.getString("hats")), //$NON-NLS-1$
    OTHER(Messages.getString("other")); //$NON-NLS-1$

    String s;

    private SettingTypes(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}
