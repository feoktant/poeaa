package io.feoktant.ch12_Object_Relational_Structural_Patterns._1_Identity_Field.integral_key;

abstract public class Mapper<E extends DomainObject<E>> {

    protected E abstractFind(long id) {
//        DataRow row = FindRow(id);
//        return (row == null)  ?  null : Find(row);
        return null;
    }
//
//    protected DataRow findRow(long id) {
//        String filter = "id = " + id;
//        DataRow[] results = table.Select(filter);
//        return (results.Length == 0)  ?  null : results[0];
//    }
//
//    public E Find(DataRow row) {
//        var result = createDomainObject();
//        Load(result, row);
//        return result;
//    }

//    public virtual long insert (DomainObject arg) {
//        DataRow row = table.NewRow();
//        arg.Id = GetNextID();
//        row["id"] = arg.Id;
//        Save (arg, row);
//        table.Rows.Add(row);
//        return arg.Id;
//    }

    abstract protected E createDomainObject();
}
