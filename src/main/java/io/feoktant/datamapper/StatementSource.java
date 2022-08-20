package io.feoktant.datamapper;

public interface StatementSource {
    String sql();

    Object[] parameters();
}
