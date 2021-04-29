package org.inurl.dsql.util;

/**
 * Visitor for all column mappings. Various column mappings are used by insert and update
 * statements. Only the null and constant mappings are supported by all statements. Other mappings
 * may or may not be supported. For example, it makes no sense to map a column to another column in
 * an insert - so the ColumnToColumnMapping is only supported on update statements.
 *
 * <p>Rather than implement this interface directly, we recommend implementing one of the derived
 * classes. The derived classes encapsulate the rules about which mappings are applicable to the
 * different types of statements.
 *
 * @author Jeff Butler
 *
 * @param <R> The type of object created by the visitor
 */
public interface ColumnMappingVisitor<R> {
    R visit(NullMapping mapping);

    R visit(ConstantMapping mapping);

    R visit(StringConstantMapping mapping);

    <T> R visit(ValueMapping<T> mapping);

    <T> R visit(ValueOrNullMapping<T> mapping);

    <T> R visit(ValueWhenPresentMapping<T> mapping);

    R visit(SelectMapping mapping);

    R visit(PropertyMapping mapping);

    R visit(PropertyWhenPresentMapping mapping);

    R visit(ColumnToColumnMapping columnMapping);
}
