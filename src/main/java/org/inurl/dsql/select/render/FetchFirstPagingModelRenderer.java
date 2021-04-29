package org.inurl.dsql.select.render;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.inurl.dsql.select.PagingModel;
import org.inurl.dsql.util.FragmentAndParameters;
import org.inurl.dsql.render.RenderingStrategy;

public class FetchFirstPagingModelRenderer {
    private final RenderingStrategy renderingStrategy;
    private final PagingModel pagingModel;
    private final AtomicInteger sequence;

    public FetchFirstPagingModelRenderer(RenderingStrategy renderingStrategy,
            PagingModel pagingModel, AtomicInteger sequence) {
        this.renderingStrategy = renderingStrategy;
        this.pagingModel = pagingModel;
        this.sequence = sequence;
    }

    public Optional<FragmentAndParameters> render() {
        return pagingModel.offset()
                .map(this::renderWithOffset)
                .orElseGet(this::renderFetchFirstRowsOnly);
    }

    private Optional<FragmentAndParameters> renderWithOffset(Long offset) {
        return pagingModel.fetchFirstRows()
                .map(ffr -> renderOffsetAndFetchFirstRows(offset, ffr))
                .orElseGet(() -> renderOffsetOnly(offset));
    }

    private Optional<FragmentAndParameters> renderFetchFirstRowsOnly() {
        return pagingModel.fetchFirstRows().flatMap(this::renderFetchFirstRowsOnly);
    }

    private Optional<FragmentAndParameters> renderFetchFirstRowsOnly(Long fetchFirstRows) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);
        return FragmentAndParameters
                .withFragment("fetch first " + renderPlaceholder(mapKey)
                    + " rows only")
                .withParameter(mapKey, fetchFirstRows)
                .buildOptional();
    }

    private Optional<FragmentAndParameters> renderOffsetOnly(Long offset) {
        String mapKey = RenderingStrategy.formatParameterMapKey(sequence);
        return FragmentAndParameters.withFragment("offset " + renderPlaceholder(mapKey)
                + " rows")
                .withParameter(mapKey, offset)
                .buildOptional();
    }

    private Optional<FragmentAndParameters> renderOffsetAndFetchFirstRows(Long offset, Long fetchFirstRows) {
        String mapKey1 = RenderingStrategy.formatParameterMapKey(sequence);
        String mapKey2 = RenderingStrategy.formatParameterMapKey(sequence);
        return FragmentAndParameters.withFragment("offset " + renderPlaceholder(mapKey1)
                + " rows fetch first " + renderPlaceholder(mapKey2)
                + " rows only")
                .withParameter(mapKey1, offset)
                .withParameter(mapKey2, fetchFirstRows)
                .buildOptional();
    }

    private String renderPlaceholder(String parameterName) {
        return renderingStrategy.getFormattedJdbcPlaceholder(RenderingStrategy.DEFAULT_PARAMETER_PREFIX,
                parameterName);
    }
}
