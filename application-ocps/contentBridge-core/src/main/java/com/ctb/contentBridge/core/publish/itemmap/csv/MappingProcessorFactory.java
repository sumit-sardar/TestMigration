package com.ctb.contentBridge.core.publish.itemmap.csv;

import java.util.Map;

import com.ctb.contentBridge.core.publish.mapping.ItemMap;
import com.ctb.contentBridge.core.publish.mapping.Objectives;

/**
 * @author wmli
 */
public class MappingProcessorFactory {
    public static MappingProcessorGeneric createMappingProcessorForTestMapInitial(
        Objectives objectives,
        ItemMap itemMap) {
        return new MappingProcessorGeneric(
            new MappingBuilderItemObjectivePair(objectives),
            new MappingValidatorMergeValidate(objectives, itemMap));
    }

    public static MappingProcessorGeneric createMappingProcessorForTestMapObjectiveUpdate(
        Objectives objectives,
        int displayLevel) {
        return new MappingProcessorGeneric(
            new MappingBuilderCSVEntry(objectives),
            new MappingValidatorObjectiveUpdate(objectives, displayLevel));
    }

    public static MappingProcessorGeneric createMappingProcessorForTestMapMergeValidation(
        Objectives objectives,
        ItemMap itemMap) {
        return new MappingProcessorGeneric(
            new MappingBuilderCSVEntry(objectives),
            new MappingValidatorMergeValidate(objectives, itemMap));
    }

    public static MappingProcessorGeneric createMappingProcessorForTestMapMerge(
        Objectives objectives,
        ItemMap itemMap) {
        return new MappingProcessorGeneric(
            new MappingBuilderCSVEntry(objectives),
            new MappingValidatorMerge(objectives, itemMap));
    }

    public static MappingProcessorGeneric createMappingProcessorForAKUpdate(
        Map items,
        Objectives objectives,
        ItemMap itemMap) {
        return new MappingProcessorGeneric(
            new MappingBuilderAKUpdateDecorator(
                new MappingBuilderCSVEntry(objectives),
                items),
            new MappingValidatorMerge(objectives, itemMap));
    }
}
