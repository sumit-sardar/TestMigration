package com.ctb.xmlProcessing.item;

import java.sql.Connection;
import java.util.ArrayList;

import net.sf.hibernate.Session;

import com.ctb.common.tools.ADSConfig;
import com.ctb.common.tools.OpenDeployConfig;
import com.ctb.common.tools.media.DBMediaGateway;
import com.ctb.hibernate.HibernateSession;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.mapping.ItemMap;
import com.ctb.mapping.Objectives;

public class ItemProcessorFactory {

    private ItemProcessorFactory() {
    }

    private static ItemProcessor decorate(ItemProcessor itemProcessor) {
        return newItemProcessorReporting(itemProcessor);
    }

    private static ItemProcessor newItemProcessorReporting(ItemProcessor itemProcessor) {
        return new ItemProcessorReportingDecorator(itemProcessor);
    }

    private static ItemProcessor createItemProcessor(
        OpenDeploy od,
        ItemBuilder builder,
        ItemValidator validator,
        ItemMapper mapper,
        ItemMediaGenerator mediaGen,
        ItemWriter writer,
        String operation,
        ArrayList unicodeList,
        ADSConfig adsConfig,
        String maxPanelWidth,
        String includeAcknowledgment) {
        return decorate(
            new ItemProcessorGeneric(
                od,
                builder,
                validator,
                mapper,
                mediaGen,
                writer,
                operation,
                unicodeList,
                adsConfig,
                maxPanelWidth,
                includeAcknowledgment));
    }

    private static ItemProcessor createItemProcessor(
            OpenDeploy od,
            ItemBuilder builder,
            ItemValidator validator,
            ItemMapper mapper,
            ItemMediaGenerator mediaGen,
            ItemWriter writer,
            String operation) {
            return decorate(
                new ItemProcessorGeneric(
                    od,
                    builder,
                    validator,
                    mapper,
                    mediaGen,
                    writer,
                    operation,
                	new ArrayList(),
                	new ADSConfig(),
                	new String(),
                	new String()));
        }

    
    public static ItemProcessor getItemProcessorDummy() {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderDummy(),
            ItemValidatorFactory.getItemValidatorDummy(),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterDummy(),
            "dummy processing");
    }

    public static ItemProcessor getItemProcessorGenerateMappingHierarchy() {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderDummy(),
            ItemValidatorFactory.getItemValidatorDummy(),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterDummy(),
            "projected map");
    }

    public static ItemProcessor getItemProcessorGenerateFlattenedMappingHierarchy() {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderDummy(),
            ItemValidatorFactory.getItemValidatorDummy(),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterDummy(),
            "projected map");
    }

    public static ItemProcessor getItemProcessorImportItems(
        String localImageArea,
        String targetImageArea,
        int validationMode,
        OpenDeployConfig config,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeploy(
                localImageArea,
                targetImageArea,
                config),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGenerator(),
            ItemWriterFactory.getItemWriterOASDatabase(connection),
            "import");
    }

    public static ItemProcessor getItemProcessorImportItemsNoMedia(
        int validationMode,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterOASDatabaseNoMedia(connection),
            "import (no media)");

    }

    public static ItemProcessor getItemProcessorImportItemsValidate(
        int validationMode,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperDummy(),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterOASDatabaseValidateOnly(connection),
            "import validate");
    }

    public static ItemProcessor getItemProcessorMapItems(
        String localImageArea,
        String targetImageArea,
        int validationMode,
        OpenDeployConfig config,
        Objectives objectives,
        ItemMap itemMap,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeploy(
                localImageArea,
                targetImageArea,
                config),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            ItemMediaGeneratorFactory.getItemMediaGenerator(),
            ItemWriterFactory.getItemWriterOASDatabase(connection),
            "map");
    }

    public static ItemProcessor getItemProcessorMapItemsValidate(
        int validationMode,
        Objectives objectives,
        ItemMap itemMap,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeployDummy(),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
            ItemWriterFactory.getItemWriterOASDatabaseValidateOnly(connection),
            "map validate");
    }

    public static ItemProcessor getItemProcessorBuildTestSofa(
        Objectives objectives,
        ItemMap itemMap,
        Session session) {
        int validationMode =
            ItemAssembler.PARSE_ALLOW_DEFAULTS
                + ItemAssembler.PARSE_SKIP_EXTRA_VALIDATION;
        return createItemProcessor(
                OpenDeployFactory.getOpenDeployDummy(),
                ItemBuilderFactory.getItemBuilder(validationMode),
                ItemValidatorFactory.getItemValidator(validationMode),
                ItemMapperFactory.getItemMapperAllowsImport(
                    validationMode,
                    objectives,
                    itemMap),
                ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
                ItemWriterFactory.getItemWriterOASDatabaseNoMedia(session),
                "add to SubTest");
    }
    
    public static ItemProcessor getItemProcessorBuildTestAssessment(
            Objectives objectives,
            ItemMap itemMap,
            Session session,
            ArrayList unicodeList,
            ADSConfig adsConfig,
            String maxPanelWidth,
            String includeAcknowledgment) {
            int validationMode =
                ItemAssembler.PARSE_ALLOW_DEFAULTS
                    + ItemAssembler.PARSE_SKIP_EXTRA_VALIDATION;
            return createItemProcessor(
                OpenDeployFactory.getOpenDeployDummy(),
                ItemBuilderFactory.getItemBuilder(validationMode),
                ItemValidatorFactory.getItemValidator(validationMode),
                ItemMapperFactory.getItemMapperIgnoreSample(
                    validationMode,
                    objectives,
                    itemMap),
                ItemMediaGeneratorFactory.getItemMediaGeneratorDummy(),
                ItemWriterFactory.getItemWriterOASDatabaseNoMedia(session),
                "add to SubTest",
                unicodeList,
                adsConfig,
                maxPanelWidth,
                includeAcknowledgment);
    }

    public static ItemProcessor getItemProcessorRoundTripMap(
        Objectives objectives,
        ItemMap itemMap,
        Connection sourceConnection,
        Connection targetConnection) {

        int validationMode = ItemAssembler.PARSE_ALLOW_DEFAULTS;
        DBMediaGateway mediaReader =
            new DBMediaGateway(
                HibernateUtils.getSession(
                    sourceConnection,
                    HibernateSession.SOURCE_CONNECTION));
        ItemMediaGenerator itemMediaCache =
            ItemMediaGeneratorFactory.getItemMediaCache();

        return decorate( new ItemProcessorRoundTrip(
            new OpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderRoundTrip(
                validationMode,
                mediaReader,
                (ItemMediaCache) itemMediaCache),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            itemMediaCache,
            ItemWriterFactory.getItemWriterOASDatabase(targetConnection),
            "round-trip map"));
    }

    public static ItemProcessor getItemProcessorRoundTripValidate(
        Objectives objectives,
        ItemMap itemMap,
        Connection sourceConnection,
        Connection targetConnection) {

        int validationMode = ItemAssembler.PARSE_ALLOW_DEFAULTS;
        DBMediaGateway mediaReader =
            new DBMediaGateway(
                HibernateUtils.getSession(
                    sourceConnection,
                    HibernateSession.SOURCE_CONNECTION));
        ItemMediaGenerator itemMediaCache =
            ItemMediaGeneratorFactory.getItemMediaCache();

        return decorate( new ItemProcessorRoundTrip(
            new OpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderRoundTrip(
                validationMode,
                mediaReader,
                (ItemMediaCache) itemMediaCache),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            itemMediaCache,
            ItemWriterFactory.getItemWriterOASDatabaseValidateOnly(
                targetConnection),
            "round-trip map validate"));
    }

    public static ItemProcessor getItemProcessorRoundTripValidateMock(
        Objectives objectives,
        ItemMap itemMap,
        Connection sourceConnection,
        ItemWriterMock itemWriterMock) {

        int validationMode = ItemAssembler.PARSE_ALLOW_DEFAULTS;
        DBMediaGateway mediaReader =
            new DBMediaGateway(
                HibernateUtils.getSession(
                    sourceConnection,
                    HibernateSession.SOURCE_CONNECTION));
        ItemMediaGenerator itemMediaCache =
            ItemMediaGeneratorFactory.getItemMediaCache();

        return createItemProcessor(
            new OpenDeployDummy(),
            ItemBuilderFactory.getItemBuilderRoundTrip(
                validationMode,
                mediaReader,
                (ItemMediaCache) itemMediaCache),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            itemMediaCache,
            itemWriterMock,
            "round-trip map (mock)");
    }

    public static ItemProcessor getItemProcessorImportItemsCacheMedia(
        String localImageArea,
        String targetImageArea,
        int validationMode,
        OpenDeployConfig config,
        ItemMediaGenerator cacheingMediaGenerator,
        Connection connection) {
        return createItemProcessor(
            OpenDeployFactory.getOpenDeploy(
                localImageArea,
                targetImageArea,
                config),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperDummy(),
            cacheingMediaGenerator,
            ItemWriterFactory.getItemWriterOASDatabase(connection),
            "import");
    }

    public static ItemProcessor getItemProcessorMapOnImport(
        String localImageArea,
        String targetImageArea,
        OpenDeployConfig config,
        Objectives objectives,
        ItemMap itemMap,
        ItemMediaGenerator mediaCache,
        int validationMode,
        Connection targetConnection) {
        return createItemProcessor(
                OpenDeployFactory.getOpenDeploy(
                        localImageArea,
                        targetImageArea,
                        config),
            ItemBuilderFactory.getItemBuilder(validationMode),
            ItemValidatorFactory.getItemValidator(validationMode),
            ItemMapperFactory.getItemMapperMapOnly(
                validationMode,
                objectives,
                itemMap),
            mediaCache,
            ItemWriterFactory.getItemWriterOASDatabase(targetConnection),
            "map");
    }

}
