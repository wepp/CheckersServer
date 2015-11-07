package com.checkers.domain.vo;

import java.io.IOException;

/**
 * Created by Eugene on 07.11.2015.
 */
public class Player {
    private InOutObjectStreams whiteStreams;
    private String whiteName;
    public Player(InOutObjectStreams whiteStreams, String whiteName) {
        this.whiteName = whiteName;
        this.whiteStreams = whiteStreams;
    }

    public void writeObject(Field currentField) throws IOException {
        whiteStreams.writeObject(currentField);
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return whiteStreams.readObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (whiteName != null ? !whiteName.equals(player.whiteName) : player.whiteName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result =  31 * (whiteName != null ? whiteName.hashCode() : 0);
        return result;
    }

    public void close() throws IOException {
        whiteStreams.close();
    }
}
