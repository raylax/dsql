package org.inurl.dsql;

@FunctionalInterface
public interface VisitableCondition<T> {
    <R> R accept(ConditionVisitor<T, R> visitor);

    /**
     * Subclasses can override this to inform the renderer if the condition should not be included
     * in the rendered SQL.  For example, IsEqualWhenPresent will not render if the value is null.
     *
     * @return true if the condition should render.
     */
    default boolean shouldRender() {
        return true;
    }

    /**
     * This method will be called during rendering when {@link VisitableCondition#shouldRender()}
     * returns false.
     */
    default void renderingSkipped() {}
}
