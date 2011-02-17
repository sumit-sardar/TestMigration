package com.ctb.common.jessica;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

import org.apache.commons.lang.*;

import com.ctb.common.tools.*;
import com.ctb.hibernate.HibernateUtils;
import com.ctb.util.*;
import com.ctb.xmlProcessing.item.*;

public class ItemDetailReport {

    static final String EMAIL_FROM = "bobby_magee@mcgraw-hill.com";
    static final String ITEM_DETAIL = "Item_Detail";
    private static final String CTB = "CTB";

    private String dateStr;
    private String envStr;
    private DBConnection dbconnection;
    private String additionalITEMCriteria;
    private String outputDir;
    private boolean runCounts;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println(
                "Usage: java com.ctb.common.jessica.ItemDetailReport env emailAddr OR File with Email Addresses");
            System.exit(1);
        }
        ItemDetailReport idr =
            new ItemDetailReport(StringUtils.trim(args[0]), null, null, true);
        idr.runReport(StringUtils.trim(args[1]));

    }

    public ItemDetailReport(
        String dbEnv,
        String additionalITEMCriteria,
        String outputDir,
        boolean runCounts) {
        File dbProps = new File(dbEnv + ".properties");

        envStr = StringUtils.chomp(dbProps.getName(), ".properties");
        try {
            initDB(dbProps);
        } catch (Exception e) {
            throw new SystemException("unable to init db", e);
        }
        initDateString();
        this.additionalITEMCriteria = additionalITEMCriteria;
        this.outputDir = outputDir;
        this.runCounts = runCounts;
    }

    public void runReport(String email) throws Exception {
        List files = runReport();

        sendFiles(files, email);
        for (Iterator iterator = files.iterator(); iterator.hasNext();) {
            File file = (File) iterator.next();

            file.deleteOnExit();
        }
    }

    public List runReport() throws Exception {
        FrameworkHolder[] frameworks = getItemFrameworks();
        Vector files = createReports(envStr, frameworks);
        Vector countsFiles = new Vector();

        if (runCounts) {
            countsFiles = createItemCounts(files);
        }
        files.addAll(countsFiles);
        return files;
    }

    private void initDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
        Date date = new Date();

        dateStr = formatter.format(date);
    }

    private void initDB(File dbProperties) throws Exception {
        DBConfig config = new DBConfig(dbProperties);

        dbconnection = new DBConnection(config.openConnection());
        dbconnection.setDebug(false);
    }

    private FrameworkHolder[] getItemFrameworks() throws Exception {
        String sql =
            "select distinct framework_product_id, internal_display_name from item, "
                + "product where product.product_id = item.framework_product_id and "
                + "item.activation_status = 'AC' and item_id like '%.%' and item_type in ('SR', 'CR')";
        Vector v = new Vector();
        Statement st = null;
        ResultSet rs = null;

        try {
            st = dbconnection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                v.add(new FrameworkHolder(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        FrameworkHolder[] frameworks = new FrameworkHolder[v.size()];

        v.copyInto(frameworks);
        return frameworks;
    }

    private Vector createReports(String env, FrameworkHolder[] frameworks)
        throws Exception {
        Vector files = new Vector();
        File f = null;

        for (int i = 0; i < frameworks.length; i++) {
            f = run(env, frameworks[i]);
            if (fileContainsItems(f)) {
                files.add(f);
            } else {
                f.deleteOnExit();
            }
        }
        return files;
    }

    private boolean fileContainsItems(File f) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (IOException e) {
            throw new SystemException(
                "unable to determine if report contains items",
                e);
        }
    }

    private Vector createItemCounts(Vector files) throws Exception {
        int sz = files.size();
        File f = null;
        File countsFile = null;
        Vector counts = new Vector();

        for (int i = 0; i < sz; i++) {
            f = (File) files.elementAt(i);
            countsFile = createItemCountsFile(f);
            counts.add(countsFile);
        }
        return counts;
    }

    private File run(String env, FrameworkHolder framework) throws Exception {
        ItemHolder itemHolder = null;
        Vector category = new Vector();
        Hashtable categoryHt = new Hashtable();
        File parentDir = new File(outputDir == null ? "." : outputDir);
        File exportFile =
            new File(
                parentDir,
                framework.frameworkCode
                    + "_"
                    + ITEM_DETAIL
                    + "_"
                    + env
                    + "_"
                    + dateStr
                    + ".csv");

        populateValues(framework, category, categoryHt);
        String sql = getSql(category, categoryHt, framework.frameworkId);

        BufferedWriter fileOut = new BufferedWriter(new FileWriter(exportFile));
        Statement st = null;
        ResultSet rs = null;

        try {
            st = dbconnection.createStatement();
            rs = st.executeQuery(sql);
            int count = 0;

            while (rs.next()) {
                itemHolder = new ItemHolder();
                populateItemHolder(rs, itemHolder, category);
                writeItem(category, itemHolder, fileOut, (count == 0));
                count++;
            }
            fileOut.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.safeClose(st, rs);
        }
        return exportFile;
    }

    private String getSql(
        Vector category,
        Hashtable categoryHt,
        int frameworkId)
        throws Exception {
        String itemSetNameSql =
            "(SELECT  iss.item_set_name FROM item_set iss, item_set iss1, item_set_ancestor isa, item_set_item isi "
                + "WHERE iss.item_set_id = isa.ancestor_item_set_id AND isa.item_set_id = iss1.item_set_id AND "
                + "isi.item_set_id = iss1.item_set_id AND iss1.item_set_type = 'RE'AND isi.item_id = ITEM.item_id AND "
                + " iss.item_set_category_id = ";
        String itemSetIdSql =
            "(SELECT  iss.item_set_id FROM item_set iss, item_set iss1, item_set_ancestor isa, item_set_item isi "
                + "WHERE iss.item_set_id = isa.ancestor_item_set_id AND isa.item_set_id = iss1.item_set_id AND "
                + "isi.item_set_id = iss1.item_set_id AND iss1.item_set_type = 'RE'AND isi.item_id = ITEM.item_id AND "
                + " iss.item_set_category_id = ";

        StringBuffer sb = new StringBuffer();
        int sz = category.size();
        String catName = null;
        int catId = 0;

        for (int i = 0; i < sz; i++) {
            catName = (String) category.elementAt(i);
            catId = ((Integer) categoryHt.get(catName)).intValue();
            sb.append(itemSetNameSql).append(catId).append("),");
            sb.append(itemSetIdSql).append(catId).append(")");
            if (i != sz - 1) {
                sb.append(", ");
            }
        }

        String sql =
            "SELECT ITEM_ID, ITEM_TYPE, TEMPLATE_ID, EXT_STIMULUS_TITLE, "
                + sb.toString()
                + " , CORRECT_ANSWER, ITEM.UPDATED_DATE_TIME  FROM ITEM "
                + "WHERE ACTIVATION_STATUS = 'AC' AND ITEM_ID LIKE '%.%'  AND "
                + "ITEM_TYPE IN ('SR', 'CR') AND FRAMEWORK_PRODUCT_ID = "
                + frameworkId
                + (additionalITEMCriteria == null
                    ? ""
                    : (" AND " + additionalITEMCriteria))
                + " ORDER BY 5, 6, 2 DESC, 1";

        return sql;
    }

    private void populateItemHolder(
        ResultSet rs,
        ItemHolder itemHolder,
        Vector category)
        throws Exception {
        itemHolder.itemId = rs.getString(1);
        itemHolder.itemType = rs.getString(2);
        itemHolder.template = rs.getString(3);
        itemHolder.stimulus = rs.getString(4);
        if (itemHolder.stimulus == null) {
            itemHolder.stimulus = "";
        }
        int catStartIndex = 5;
        int sz = category.size() * 2;
        String catName = null;
        String itemSetName = null;
        Long itemSetId = null;
        int ct = 0;

        while (ct < sz) {
            catName = (String) category.elementAt(ct / 2);
            itemSetName = rs.getString(catStartIndex + ct++);
            BigDecimal itemSetIdAsBigD =
                (BigDecimal) rs.getObject(catStartIndex + ct++);

            itemSetId =
                itemSetIdAsBigD == null
                    ? null
                    : new Long(itemSetIdAsBigD.longValue());
            itemHolder.putCategory(
                catName,
                new CategoryHolder(itemSetName, itemSetId));
        }
        int nextIndex = catStartIndex + sz;

        itemHolder.correctAnswer = rs.getString(nextIndex++);
        if (itemHolder.itemType.equals(Item.CONSTRUCTED_RESPONSE)) {
            DBDatapointGateway dpgw =
                new DBDatapointGateway(
                    HibernateUtils.getSession(dbconnection.getConnection()));

            try {
                Datapoint dp =
                    dpgw.getDatapoint(
                        itemHolder.itemId,
                        itemHolder.getObjectiveId(category));

                itemHolder.correctAnswer =
                    "min:" + dp.getMinPoints() + " max:" + dp.getMaxPoints();
            } catch (SystemException sysEx) {
                itemHolder.correctAnswer = "<no datapoint found>";
            }
        }
        Timestamp ts = rs.getTimestamp(nextIndex++);
        Date d = new Date(ts.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy h:mm a z");

        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        itemHolder.updDateStr = sdf.format(d);
    }

    private void writeItem(
        Vector category,
        ItemHolder itemHolder,
        BufferedWriter fileOut,
        boolean writeHeader)
        throws Exception {
        int sz = category.size();

        if (writeHeader) {
            // print header to csv file
            StringBuffer heading = new StringBuffer();

            heading.append("Item ID, Item Type, ");
            for (int i = 0; i < sz; i++) {
                heading.append((String) category.elementAt(i));
                heading.append(", ");
            }
            heading.append(
                "Template,Stimulus Title,Correct Answer,Updated Date");
            fileOut.write(heading.toString());
            fileOut.newLine();
        }
        StringBuffer sb = new StringBuffer();

        sb.append(itemHolder.itemId).append(",\"").append(
            itemHolder.itemType).append(
            "\",\"");
        String catName;

        for (int i = 0; i < sz; i++) {
            catName = (String) category.elementAt(i);
            sb.append(itemHolder.getItemSetName(catName));
            sb.append("\",\"");
        }
        sb
            .append(itemHolder.template)
            .append("\",\"")
            .append(itemHolder.stimulus)
            .append("\",\"");
        sb.append(itemHolder.correctAnswer).append("\",\"");
        sb.append(itemHolder.updDateStr).append("\"");
        fileOut.write(sb.toString());
        fileOut.newLine();
    }

    private void populateValues(
        FrameworkHolder framework,
        Vector category,
        Hashtable categoryHt)
        throws Exception {
        String sql =
            "SELECT ITEM_SET_CATEGORY_NAME, ITEM_SET_CATEGORY_ID  FROM ITEM_SET_CATEGORY "
                + "WHERE FRAMEWORK_PRODUCT_ID = "
                + framework.frameworkId
                + " ORDER BY ITEM_SET_CATEGORY_LEVEL";
        Statement st = null;
        ResultSet rs = null;

        try {
            st = dbconnection.createStatement();
            rs = st.executeQuery(sql);
            String name = null;
            int id = 0;

            while (rs.next()) {
                name = rs.getString(1);
                id = rs.getInt(2);
                populateCatHt(name, id, framework, category, categoryHt);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.safeClose(st, rs);
        }
    }

    private void populateCatHt(
        String name,
        int catId,
        FrameworkHolder framework,
        Vector category,
        Hashtable categoryHt)
        throws Exception {
        if ("Root".equalsIgnoreCase(name)) {
            return;
        }

        if (framework.frameworkCode.equals(CTB)) {
            if (catId > 8) {
                return;
            }
        }
        category.add(name);
        categoryHt.put(name, new Integer(catId));
    }

    public File createItemCountsFile(File exportFile) throws Exception {
        ItemDetailReportItemCountWriter writer =
            new ItemDetailReportItemCountWriter(exportFile);

        return writer.writeReport();
    }

    private void sendFiles(List files, String email) {
        String subject = "Item Reports for " + envStr + " as of " + dateStr;
        String msgText =
            "The attached files report on the active items in "
                + envStr
                + " as of "
                + dateStr
                + ". \n";
        String zipFileName = "ItemReports_" + envStr + "_" + dateStr + ".zip";

        try {
            Email.sendMessage(
                OCSConfig.DEFAULT_SMTP_HOST,
                EMAIL_FROM,
                email,
                subject,
                msgText,
                files,
                zipFileName);
        } catch (Exception e) {
            throw new SystemException("unable to send files via email", e);
        }
    }

    private class ItemHolder {
        String itemId = "";
        String itemType = "";
        String stimulus = "";
        String template = "";
        String correctAnswer = "";
        String updDateStr = "";
        Map categoryHoldersByName = new HashMap();

        public void putCategory(String key, CategoryHolder category) {
            categoryHoldersByName.put(key, category);
        }

        public String getItemSetName(String categoryKey) {
            CategoryHolder ch =
                (CategoryHolder) categoryHoldersByName.get(categoryKey);

            if (ch.itemSetName == null
                || ch.itemSetName.trim().length() == 0) {
                return "";
            } else {
                return ch.itemSetName;
            }
        }

        public String getItemSetIdAsString(String categoryKey) {
            CategoryHolder ch =
                (CategoryHolder) categoryHoldersByName.get(categoryKey);

            if (ch.itemSetId == null) {
                return "";
            } else {
                return String.valueOf(ch.itemSetId);
            }
        }

        public long getObjectiveId(Vector categories) {
            int ct = categories.size();

            while (ct > 0) {
                String category = (String) categories.elementAt(ct-- -1);

                if (getItemSetIdAsString(category).length() > 0) {
                    return Long.parseLong(getItemSetIdAsString(category));
                }
            }
            throw new SystemException(
                "unable to find item set id for item " + itemId);
        }
    }

    private class CategoryHolder {
        String itemSetName;
        Long itemSetId;

        public CategoryHolder(String itemSetName, Long itemSetId) {
            this.itemSetName = itemSetName;
            this.itemSetId = itemSetId;
        }
    }

    private class FrameworkHolder {
        String frameworkCode = "";
        int frameworkId = 0;

        public FrameworkHolder(int id, String code) {
            frameworkId = id;
            frameworkCode = code;
        }
    }
}
