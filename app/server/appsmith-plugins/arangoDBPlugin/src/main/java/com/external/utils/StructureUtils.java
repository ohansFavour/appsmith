package com.external.utils;

import com.appsmith.external.models.DatasourceStructure;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureUtils {

    public static void generateTemplatesAndStructureForACollection(String collectionName,
                                                            Map document,
                                                            ArrayList<DatasourceStructure.Column> columns,
                                                            ArrayList<DatasourceStructure.Template> templates) {

        for (Object entry : document.entrySet()) {
            final String name = ((Map.Entry<String, Object>) entry).getKey();
            final Object value = ((Map.Entry<String, Object>) entry).getValue();
            String type;
            boolean autogenerate = false;

            if (value instanceof Integer) {
                type = "Integer";
            } else if (value instanceof Long) {
                type = "Long";
            } else if (value instanceof Double) {
                type = "Double";
            } else if (value instanceof Decimal128) {
                type = "BigDecimal";
            } else if (value instanceof String) {
                type = "String";
            } else if (value instanceof ObjectId) {
                autogenerate = true;
                type = "ObjectId";
            } else if (value instanceof Collection) {
                type = "Array";
            } else if (value instanceof Date) {
                type = "Date";
            } else {
                type = "Object";
            }

            columns.add(new DatasourceStructure.Column(name, type, null, autogenerate));
        }

        columns.sort(Comparator.naturalOrder());

        Map<String, Object> templateConfiguration = new HashMap<>();
        templateConfiguration.put("collectionName", collectionName);
        templateConfiguration.put("sampleValues", document);
        templates.addAll(generateTemplate(templateConfiguration));
    }

    private static List<DatasourceStructure.Template> generateTemplate(Map<String, Object> templateConfiguration) {
        List templates = new ArrayList();
        templates.add(generateSelectTemplate(templateConfiguration));
        templates.add(generateCreateTemplate(templateConfiguration));
        templates.add(generateUpdateTemplate(templateConfiguration));
        templates.add(generateRemoveTemplate(templateConfiguration));

        return templates;
    }

    private static DatasourceStructure.Template generateSelectTemplate(Map<String, Object> templateConfiguration) {
        String collectionName = (String) templateConfiguration.get("collectionName");
        Map<String, String> sampleValues = (Map<String, String>) templateConfiguration.get("sampleValues");
        String filterKey = "_key";
        String filterValue = sampleValues.get("_key");

        String rawQuery = "FOR document IN " + collectionName + "\n" +
                "FILTER " + "document." + filterKey + " == \"" + filterValue + "\"\n" +
                "RETURN document";

        return new DatasourceStructure.Template(
                "Select",
                rawQuery,
                null
        );
    }

    private static DatasourceStructure.Template generateCreateTemplate(Map<String, Object> templateConfiguration) {
        String collectionName = (String) templateConfiguration.get("collectionName");

        String rawQuery = "INSERT \n" +
                "{\n" +
                "    insertKey: \"insertValue\"\n" +
                "}\n" +
                "INTO " + collectionName;

        return new DatasourceStructure.Template(
                "Create",
                rawQuery,
                null
        );
    }

    private static DatasourceStructure.Template generateUpdateTemplate(Map<String, Object> templateConfiguration) {
        String collectionName = (String) templateConfiguration.get("collectionName");
        Map<String, String> sampleValues = (Map<String, String>) templateConfiguration.get("sampleValues");
        String filterKey = "_key";
        String filterValue = sampleValues.get("_key");

        String rawQuery = "UPDATE\n" +
                "{\n" +
                "    " + filterKey + ": \"" + filterValue + "\"\n" +
                "}\n" +
                "WITH\n" +
                "{\n" +
                "    updateKey: \"updateVal\"\n" +
                "}\n" +
                "IN " + collectionName;

        return new DatasourceStructure.Template(
                "Update",
                rawQuery,
                null
        );
    }

    private static DatasourceStructure.Template generateRemoveTemplate(Map<String, Object> templateConfiguration) {
        String collectionName = (String) templateConfiguration.get("collectionName");
        Map<String, String> sampleValues = (Map<String, String>) templateConfiguration.get("sampleValues");
        String filterValue = sampleValues.get("_key");

        String rawQuery = "REMOVE \"" + filterValue + "\" IN " + collectionName;

        return new DatasourceStructure.Template(
                "Delete",
                rawQuery,
                null
        );
    }

    public static String getOneDocumentQuery(String collectionName) {
        return "for doc in " + collectionName + " limit 1 return doc";
    }

}
