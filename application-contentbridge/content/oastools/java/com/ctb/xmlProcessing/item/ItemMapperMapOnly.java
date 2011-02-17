package com.ctb.xmlProcessing.item;

import java.io.File;
import java.util.List;


import com.ctb.mapping.*;

import org.apache.log4j.*;

public class ItemMapperMapOnly implements ItemMapper {
    private static Logger logger = Logger.getLogger(ItemMapperMapOnly.class);
    private final ItemBuilder builder;
    private final ItemValidator validator;
    private final Mapper mapper;

    public ItemMapperMapOnly(
        int validationMode,
        File frameworkDefinitonFile,
        File objectivesFile,
        File mappingFile,
        String objectivesFileFormat) {
        this.builder = ItemBuilderFactory.getItemBuilder(validationMode);
        this.validator = ItemValidatorFactory.getItemValidator(validationMode);
        mapper =
            MapperFactory.newMapper(
                frameworkDefinitonFile,
                objectivesFile,
                mappingFile,
                objectivesFileFormat);
    }
    public ItemMapperMapOnly(
        Integer validationMode,
        Objectives objectives,
        ItemMap itemMap) {
        this(validationMode.intValue(), objectives, itemMap);
    }

    public ItemMapperMapOnly(
        int validationMode,
        Objectives objectives,
        ItemMap itemMap) {
        this.builder = ItemBuilderFactory.getItemBuilder(validationMode);
        this.validator = ItemValidatorFactory.getItemValidator(validationMode);
        mapper = MapperFactory.newMapper(objectives, itemMap);
    }

    public Item mapItem(Item originalItem) throws IdentityMappingException {
        String itemId = originalItem.getId();
        String frameworkCode = getFrameworkCode();
        if (mapper.curriculumId(itemId) == null) {
            logger.info("Item [" + itemId + "] not listed in item map file");
            throw new UnmappedItemIDException(itemId);
        }

        Item newItem = this.builder.build(this.mapper.mapItemXML(originalItem));
        newItem.setObjectiveId( mapper.curriculumId(itemId) );
        this.validator.validate(newItem);
        newItem.setFrameworkCode( getFrameworkCode());
        return newItem;
    }

    public String getFrameworkCode() {
        return this.mapper.getObjectives().getFrameworkCode();
    }
    public List getAncestorObjectiveList(String objId,Item item) {
        Objectives objectives = mapper.getObjectives();
        return objectives.getHierarchyObjectiveList(objId);
    }
    
    public String getObjectiveCode( Item originalItem )
    {
        String itemId = originalItem.getId();
        return mapper.curriculumId(itemId);
    }

}
