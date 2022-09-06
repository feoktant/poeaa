package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.compound_key;

public class MapperRegistry {

    private static final OrderMapper orderMapper = new OrderMapper();
    private static final LineItemMapper lineItemMapper = new LineItemMapper();

    public static OrderMapper order() {
        return orderMapper;
    }

    public static LineItemMapper lineItem() {
        return lineItemMapper;
    }
}
