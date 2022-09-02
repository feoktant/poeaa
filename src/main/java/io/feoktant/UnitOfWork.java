package io.feoktant;


//import io.feoktant.ch18_base_patterns.Layer_Supertype.DomainObject;

import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {

    private final List<DomainObject> newObjects = new ArrayList<>();
    private final List<DomainObject> dirtyObjects = new ArrayList<>();
    private final List<DomainObject> removedObjects = new ArrayList<>();

    private static final ThreadLocal<UnitOfWork> current = new ThreadLocal<>();

    public void registerNew(DomainObject obj) {
//        Assert.notNull("id not null", obj.getID());
//        Assert.isTrue("object not dirty", !dirtyObjects.contains(obj));
//        Assert.isTrue("object not removed", !removedObjects.contains(obj));
//        Assert.isTrue("object not already registered new", !newObjects.contains(obj));
        newObjects.add(obj);
    }

    public void registerDirty(DomainObject obj) {
//        Assert.notNull("id not null", obj.getID());
//        Assert.isTrue("object not removed", !removedObjects.contains(obj));
        if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
            dirtyObjects.add(obj);
        }
    }

    public void registerRemoved(DomainObject obj) {
//        Assert.notNull("id not null", obj.getID());
        if (newObjects.remove(obj)) return;
        dirtyObjects.remove(obj);
        if (!removedObjects.contains(obj)) {
            removedObjects.add(obj);
        }
    }

    public void registerClean(DomainObject obj) {
//        Assert.notNull("id not null", obj.getID());
    }

    public void commit() {
        insertNew();
//        updateDirty();
//        deleteRemoved();
    }

    private void insertNew() {
//        newObjects.forEach(obj ->
//                MapperRegistry.getMapper(obj.getClass()).insert(obj));
    }

    public static void newCurrent() {
        setCurrent(new UnitOfWork());
    }

    public static void setCurrent(UnitOfWork uow) {
        current.set(uow);
    }

    public static UnitOfWork getCurrent() {
        return current.get();
    }

}

class DomainObject {
    private Long ID;

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        this.ID = id;
    }

    protected void markNew() {
        UnitOfWork.getCurrent().registerNew(this);
    }

    protected void markClean() {
        UnitOfWork.getCurrent().registerClean(this);
    }

    protected void markDirty() {
        UnitOfWork.getCurrent().registerDirty(this);
    }

    protected void markRemoved() {
        UnitOfWork.getCurrent().registerRemoved(this);
    }

}

class EditAlbumScript {

    public static void updateTitle(Long albumId, String title) {
        UnitOfWork.newCurrent();
//        Mapper mapper = MapperRegistry.getMapper(Album.class);
//        Album album = (Album) mapper.find(albumId);
//        album.setTitle(title);
        UnitOfWork.getCurrent().commit();
    }
}