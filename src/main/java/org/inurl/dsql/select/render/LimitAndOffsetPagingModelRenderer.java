package org.inurl.dsql.select.render;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.select.PagingModel;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.render.RenderingStrategy;

public class LimitAndOffsetPagingModelRenderer {
    private final RenderingStrategy renderingStrategy;
    private final Long limit;
    private final PagingModel pagingModel;
    private final AtomicInteger sequence;

    public LimitAndOffsetPagingModelRenderer(RenderingStrategy renderingStrategy,
            Long limit, PagingModel pagingModel, AtomicInteger sequence) {
        this.renderingStrategy = renderingStrategy;
        this.limit = limit;
        this.pagingModel = pagingModel;
        this.sequence = sequence;
    }

    public Optional<FragmentAndParameters> render() {
        return pagingModel.offset().map(this::renderLimitAndOffset)
                .orElseGet(this::renderLimitOnly);
    }

    private Optional<FragmentAndParameters> renderLimitOnly() {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);
        return FragmentAndParameters.withFragment("limit " + renderPlaceholder(mapKey))
                .withParameter(mapKey, limit)
                .buildOptional();
    }

    private Optional<FragmentAndParameters> renderLimitAndOffset(Long offset) {
        String mapKey1 = RenderingStrategy.formatParameterMapKey(sequence);
        String mapKey2 = RenderingStrategy.formatParameterMapKey(sequence);
        return FragmentAndParameters.withFragment("limit " + renderPlaceholder(mapKey1)
                    + " offset " + renderPlaceholder(mapKey2))
                .withParameter(mapKey1, limit)
                .withParameter(mapKey2, offset)
                .buildOptional();
    }

    private String renderPlaceholder(String parameterName) {
        return renderingStrategy.getFormattedJdbcPlaceholder(RenderingStrategy.DEFAULT_PARAMETER_PREFIX,
                parameterName);
    }
}
