package io.feoktant.ch12_Object_Relational_Structural_Patterns._Dependent_Mapping;

import io.feoktant.ch12_Object_Relational_Structural_Patterns._3_Association_Table_Mapping.AbstractMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AlbumMapper extends AbstractMapper<Album> {

    protected String findStatement() {
        return """
                SELECT ID, a.title, t.title as trackTitle
                  FROM albums a, tracks t
                 WHERE a.ID = ? AND t.albumID = a.ID
                 ORDER BY t.seq""";
    }

    protected Album doLoad(Long id, ResultSet rs) throws SQLException {
        String title = rs.getString(2);
        Album result = new Album(id, title);
        loadTracks(result, rs);
        return result;
    }

    public void loadTracks(Album arg, ResultSet rs) throws SQLException {
        arg.addTrack(newTrack(rs));
        while (rs.next()) {
            arg.addTrack(newTrack(rs));
        }
    }

    private Track newTrack(ResultSet rs) throws SQLException {
        String title = rs.getString(3);
        return new Track(title);
    }

    public void update(Album album) {
        try (var updateStatement = DB.prepareStatement("UPDATE albums SET title = ? WHERE id = ?")) {
            updateStatement.setLong(2, album.getId());
            updateStatement.setString(1, album.getTitle());
            updateStatement.execute();
            updateTracks(album);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTracks(Album arg) throws SQLException {
        try (var deleteTracksStatement = DB.prepareStatement("DELETE from tracks WHERE albumID = ?")) {
            deleteTracksStatement.setLong(1, arg.getId());
            deleteTracksStatement.execute();
            for (int i = 0; i < arg.getTracks().size(); i++) {
                Track track = arg.getTracks().get(i);
                insertTrack(track, i + 1, arg);
            }
        }
    }

    public void insertTrack(Track track, int seq, Album album) throws SQLException {
        try (var insertTracksStatement =
                     DB.prepareStatement("INSERT INTO tracks (seq, albumID, title) VALUES (?, ?, ?)")) {
            insertTracksStatement.setInt(1, seq);
            insertTracksStatement.setLong(2, album.getId());
            insertTracksStatement.setString(3, track.getTitle());
            insertTracksStatement.execute();
        }
    }

}
