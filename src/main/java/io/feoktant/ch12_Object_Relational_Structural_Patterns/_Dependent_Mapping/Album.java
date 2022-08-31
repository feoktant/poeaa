package io.feoktant.ch12_Object_Relational_Structural_Patterns._Dependent_Mapping;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Album {
    private final Long id;
    private final String title;
    private final List<Track> tracks = new ArrayList<>();

    public Album(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public void addTrack(Track arg) {
        tracks.add(arg);
    }

    public void removeTrack(Track arg) {
        tracks.remove(arg);
    }

    public void removeTrack(int i) {
        tracks.remove(i);
    }

}
