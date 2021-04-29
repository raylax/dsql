package org.inurl.dsql.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class FragmentCollector {
    final List<FragmentAndParameters> fragments = new ArrayList<>();

    FragmentCollector() {
        super();
    }

    private FragmentCollector(FragmentAndParameters initialFragment) {
        add(initialFragment);
    }

    public void add(FragmentAndParameters fragmentAndParameters) {
        fragments.add(fragmentAndParameters);
    }

    public FragmentCollector merge(FragmentCollector other) {
        fragments.addAll(other.fragments);
        return this;
    }

    public Stream<String> fragments() {
        return fragments.stream()
                .map(FragmentAndParameters::fragment);
    }

    public Map<String, Object> parameters() {
        return fragments.stream()
                .map(FragmentAndParameters::parameters)
                .collect(HashMap::new, HashMap::putAll, HashMap::putAll);
    }

    public boolean hasMultipleFragments() {
        return fragments.size() > 1;
    }

    public static Collector<FragmentAndParameters, FragmentCollector, FragmentCollector> collect() {
        return Collector.of(FragmentCollector::new,
                FragmentCollector::add,
                FragmentCollector::merge);
    }

    public static Collector<FragmentAndParameters, FragmentCollector, FragmentCollector> collect(
            FragmentAndParameters initialFragment) {
        return Collector.of(() -> new FragmentCollector(initialFragment),
                FragmentCollector::add,
                FragmentCollector::merge);
    }
}
