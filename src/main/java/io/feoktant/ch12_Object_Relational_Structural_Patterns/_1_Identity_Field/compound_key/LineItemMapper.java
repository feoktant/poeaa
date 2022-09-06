package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

import io.feoktant.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LineItemMapper extends AbstractMapper {

    protected DomainObjectWithKey doLoad(Key key, ResultSet rs) throws SQLException {
        Order theOrder = MapperRegistry.order().find(orderID(key));
        return doLoad(key, rs, theOrder);
    }

    protected DomainObjectWithKey doLoad(Key key, ResultSet rs, Order order)
            throws SQLException {
        LineItem result;
        int amount = rs.getInt("amount");
        var product = rs.getString("product");
        result = new LineItem(key, amount, product);
        order.addLineItem(result); //links to the order
        return result;
    }

    //overrides the default case
    protected Key createKey(ResultSet rs) throws SQLException {
        return new Key(
                rs.getLong("orderID"),
                rs.getLong("seq")
        );
    }

    public void loadAllLineItemsFor(Order arg) {
        try (var stmt = DB.prepare(findForOrderString)) {
            stmt.setLong(1, arg.getKey().longValue());
            var rs = stmt.executeQuery();
            while (rs.next()) load(rs, arg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final static String findForOrderString =
            "SELECT orderID, seq, amount, product " +
                    "FROM line_items " + "WHERE orderID = ?";

    protected DomainObjectWithKey load(ResultSet rs, Order order) throws SQLException {
        Key key = createKey(rs);
        if (loadedMap.containsKey(key)) return loadedMap.get(key);

        DomainObjectWithKey result = doLoad(key, rs, order);
        loadedMap.put(key, result);
        return result;
    }

    public LineItem find(long orderID, long seq) {
        Key key = new Key(orderID, seq);
        return (LineItem) abstractFind(key);
    }

    public LineItem find(Key key) {
        return (LineItem) abstractFind(key);
    }

    protected String findStatementString() {
        return
                "SELECT orderID, seq, amount, product " +
                        "  FROM line_items " +
                        "  WHERE  (orderID = ?) AND (seq = ?)";
    }

    // hook methods overridden for the composite key
    protected void loadFindStatement(Key key, PreparedStatement finder) throws SQLException {
        finder.setLong(1, orderID(key));
        finder.setLong(2, sequenceNumber(key));
    }

    //helpers to extract appropriate values from line item's key
    private static long orderID(Key key) {
        return key.longValue(0);
    }

    private static long sequenceNumber(Key key) {
        return key.longValue(1);
    }
}
