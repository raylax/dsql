package org.inurl.dsql.render;

public class RenderingStrategies {
    private RenderingStrategies() {}

    public static final RenderingStrategy MYBATIS3 = new MyBatis3RenderingStrategy();

    public static final RenderingStrategy SPRING_NAMED_PARAMETER = new SpringNamedParameterRenderingStrategy();
}
