package com.ctb.common.tools;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.ctb.hibernate.HibernateUtils;

public class DBObjectivesReports {
    private Connection connection;
    private DBConnection dbConnection;
    private long userID;
    private DBObjectivesGateway ogw;

    public DBObjectivesReports(Connection connection, long userID) {
        this.connection = connection;
        this.dbConnection = new DBConnection(connection);
        this.userID = userID;
        ogw = new DBObjectivesGateway(HibernateUtils.getSession(connection));
    }

    // list all frameworks and products
    public String listAvailableFrameworks() throws SQLException {
        StringBuffer buffer = new StringBuffer(1000);

        final String GET_FRAMEWORKS_SQL =
            "select product_id, internal_display_name, product_name, "
                + "activation_status from product "
                + "where product_type = 'CF' order by product_id";
        final String GET_PRODUCTS_SQL =
            "select product_id, internal_display_name, product_name, "
                + "activation_status from product "
                + "where product_type = 'ST' and parent_product_id = {0} order by product_id";

        ResultSet result = dbConnection.executeQuery(GET_FRAMEWORKS_SQL, null);

        buffer.append(
            "\nID      CODE                NAME                                              ");
        buffer.append(
            "\n------- ------------------- --------------------------------------------------");

        while (result.next()) {
            printFramework(result, buffer);
            Long product_id = new Long(result.getLong("product_id"));

            ResultSet products =
                dbConnection.executeQuery(
                    GET_PRODUCTS_SQL,
                    new Object[] { product_id });

            while (products.next()) {
                printProduct(products, buffer);
            }
            products.close();
            products.getStatement().close();
        }
        result.close();
        result.getStatement().close();
        return buffer.toString();
    }

    private void printFramework(ResultSet result, StringBuffer buffer)
        throws SQLException {
        Long product_id = new Long(result.getLong("product_id"));
        String internal_display_name =
            result.getString("internal_display_name");
        String product_name = result.getString("product_name");
        String activation_status = result.getString("activation_status");

        buffer.append("\n" + padString(product_id.toString(), 8));
        buffer.append(padString(internal_display_name, 20));
        buffer.append(product_name);
        buffer.append(" (" + getActivationStatusLabel(activation_status) + ")");
    }

    private void printProduct(ResultSet result, StringBuffer buffer)
        throws SQLException {
        String internal_display_name =
            result.getString("internal_display_name");

        buffer.append("\n        ");
        buffer.append(padString(internal_display_name, 20));
    }

    // generate report of all objectives in a framework
    public String generateFrameworkReport(
        String frameworkCode,
        String productCode)
        throws SQLException {
        StringBuffer buffer = new StringBuffer(3000);

        writeFramework(buffer, frameworkCode, productCode);
        Map categoryLevels = getCategoryLevels(frameworkCode);
        List topLevelitemSets = getTopLevelitemSets(frameworkCode, productCode);

        Iterator topLevelitemSetsIterator = topLevelitemSets.iterator();

        while (topLevelitemSetsIterator.hasNext()) {
            ItemSet itemSet = (ItemSet) topLevelitemSetsIterator.next();

            listItemSet(buffer, categoryLevels, itemSet, 1);
        }

        return buffer.toString();
    }

    private void listItemSet(
        StringBuffer buffer,
        Map categoryLevels,
        ItemSet set,
        int level)
        throws SQLException {
        writeItemSet(buffer, categoryLevels, set, level);
        List items = getItemsForItemSet(set);
        Iterator itemIterator = items.iterator();

        while (itemIterator.hasNext()) {
            Item item = (Item) itemIterator.next();

            writeItem(buffer, categoryLevels, item, level + 1);
        }

        List itemSets = getItemSetChildren(set);
        Iterator itemSetIterator = itemSets.iterator();

        while (itemSetIterator.hasNext()) {
            ItemSet itemSet = (ItemSet) itemSetIterator.next();

            listItemSet(buffer, categoryLevels, itemSet, level + 1);
        }
    }

    private void writeFramework(
        StringBuffer buffer,
        String frameworkCode,
        String productCode) {
        buffer.append("FRAMEWORK: " + frameworkCode);
        if (productCode != null) {
            buffer.append("  PRODUCT: " + productCode);
        }
        buffer.append("\n\n");
    }

    private void writeItemSet(
        StringBuffer buffer,
        Map categoryLevels,
        ItemSet set,
        int level) {
        buffer.append(StringUtils.repeat(" ", level * 2));
        String levelName =
            ((CategoryLevel) (categoryLevels.get(set.CategoryId))).Name;

        buffer.append(levelName + ": ");
        buffer.append(
            set.CmsId
                + " - "
                + set.Name
                + " ("
                + getActivationStatusLabel(set.ActivationStatus)
                + ")\n");
    }

    private void writeItem(
        StringBuffer buffer,
        Map categoryLevels,
        Item item,
        int level) {
        buffer.append(StringUtils.repeat(" ", level * 2 + 2));
        buffer.append(
            "Item "
                + item.Id
                + " "
                + item.Name
                + " ("
                + getActivationStatusLabel(item.ActivationStatus)
                + ")\n");
    }

    private String getActivationStatusLabel(String status) {
        if (status.equals("AC")) {
            return "Active";
        } else if (status.equals("IN")) {
            return "Inactive";
        } else {
            return "<unknown>";
        }
    }

    private String padString(String text, int len) {
        int textlen = text.length();

        for (int i = textlen; i < len; i++) {
            text += " ";
        }
        return text;
    }

    private String padStringLeft(String text, int len) {
        int textlen = text.length();

        for (int i = textlen; i < len; i++) {
            text = " " + text;
        }
        return text;
    }

    public List getTopLevelitemSets(String frameworkCode, String productCode)
        throws SQLException {
        int frameworkId = ogw.getFrameWorkID(frameworkCode);
        int productId = frameworkId;

        if (productCode != null) {
            productId = ogw.getProductID(frameworkCode, productCode);
        }

        List list = new ArrayList();

        // get level at which product is linked
        String sql1;

        sql1 =
            "SELECT content_area_level from product where product.product_id = {0}";
        Object[] arguments1 = { new Integer(frameworkId)};

        int productLevel = dbConnection.executeCountQuery(sql1, arguments1);

        String sql2 =
            "SELECT item_set.item_set_id, item_set.item_set_name, item_set.ext_cms_item_set_id, item_set.item_set_category_id, item_set.activation_status FROM "
                + "item_set, item_set_category, item_set_product WHERE "
                + "item_set_category.item_set_category_id = item_set.item_set_category_id AND "
                + "item_set_product.item_set_id = item_set.item_set_id AND "
                + "item_set_category.framework_product_id = {0} AND "
                + "item_set_product.product_id = {1} AND "
                + "item_set_category.item_set_category_level = {2}"
                + "ORDER BY item_set.ext_cms_item_set_id";

        Object[] arguments2 =
            {
                new Integer(frameworkId),
                new Integer(productId),
                new Integer(productLevel)};

        ResultSet result = dbConnection.executeQuery(sql2, arguments2);

        while (result.next()) {
            list.add(new ItemSet(result));
        }
        result.close();
        result.getStatement().close();
        return list;
    }

    public Map getCategoryLevels(String frameworkCode) throws SQLException {
        int frameworkId = ogw.getFrameWorkID(frameworkCode);
        Map map = new HashMap();

        String sql =
            "select item_set_category_id, item_set_category_level, item_set_category_name from item_set_category "
                + "where framework_product_id = '"
                + frameworkId
                + "' order by item_set_category_level";

        ResultSet result = dbConnection.executeQuery(sql, null);

        while (result.next()) {
            Integer id = new Integer(result.getInt("item_set_category_id"));
            Integer level =
                new Integer(result.getInt("item_set_category_level"));
            String name = result.getString("item_set_category_name");

            map.put(id, new CategoryLevel(id, level, name));
        }
        result.close();
        result.getStatement().close();
        return map;
    }

    public List getItemSetChildren(ItemSet parent) throws SQLException {
        List list = new ArrayList();

        String sql =
            "select item_set.item_set_id, item_set_name, ext_cms_item_set_id, item_set_category_id, item_set.activation_status "
                + "from item_set, item_set_parent "
                + "where item_set.item_set_type='RE' and "
                + "item_set_parent.item_set_id = item_set.item_set_id and "
                + "item_set_parent.parent_item_set_id = {0}"
                + "order by item_set.ext_cms_item_set_id";

        Object[] arguments = { parent.Id };

        ResultSet result = dbConnection.executeQuery(sql, arguments);

        while (result.next()) {
            list.add(new ItemSet(result));
        }
        result.close();
        result.getStatement().close();
        return list;
    }

    public List getItemsForItemSet(ItemSet set) throws SQLException {
        List list = new ArrayList();

        String sql =
            "select item.item_id, item.description, item.activation_status "
                + "from item, item_set_item "
                + "where item_set_item.item_id = item.item_id and "
                + "item_set_item.item_set_id = {0}";

        Object[] arguments = { set.Id };

        ResultSet result = dbConnection.executeQuery(sql, arguments);

        while (result.next()) {
            list.add(new Item(result));
        }
        result.close();
        result.getStatement().close();
        return list;
    }

    class CategoryLevel {
        public Integer Level;
        public Integer Id;
        public String Name;

        public CategoryLevel(Integer id, Integer level, String name) {
            this.Id = id;
            this.Level = level;
            this.Name = name;
        }
    }

    class ItemSet {
        public Integer Id;
        public String Name;
        public String CmsId;
        public Integer CategoryId;
        public String ActivationStatus;

        public ItemSet(
            Integer Id,
            String Name,
            String CmsId,
            Integer CategoryId,
            String ActivationStatus) {
            this.Id = Id;
            this.Name = Name;
            this.CmsId = CmsId;
            this.CategoryId = CategoryId;
            this.ActivationStatus = ActivationStatus;
        }

        public ItemSet(ResultSet result) throws SQLException {
            Id = new Integer(result.getInt("item_set_id"));
            Name = result.getString("item_set_name");
            CmsId = result.getString("ext_cms_item_set_id");
            CategoryId = new Integer(result.getInt("item_set_category_id"));
            ActivationStatus = result.getString("activation_status");
        }
    }

    class Item {
        public String Id;
        public String Name;
        public String ActivationStatus;

        public Item(String Id, String Name, String ActivationStatus) {
            this.Id = Id;
            this.Name = Name;
            this.ActivationStatus = ActivationStatus;
        }

        public Item(ResultSet result) throws SQLException {
            Id = result.getString("item_id");
            Name = result.getString("description");
            if (Name != null && Name.length() > 100) {
                Name = Name.substring(0, 100) + "...";
            }
            ActivationStatus = result.getString("activation_status");
        }
    }
}
