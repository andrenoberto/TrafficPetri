package com.connection;

public class Packet {
    public int id;
    public int value;

    public Packet(int id, int value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        return ((Packet) o).id == this.id;
    }
}
