package com.ctb.mapping;

import java.io.*;
import java.util.*;

import com.ctb.mapping.ObjectiveComparisonActivity;

/**
 * @author djrice
 */
public class ObjectiveComparisonActivityReporter {

    private PrintWriter writer;
    private List activities;
    private String header;

    public ObjectiveComparisonActivityReporter(
        String header,
        List activities,
        PrintWriter writer) {
        this.activities = activities;
        this.writer = writer;
        this.header = header;
    }

    public void run() {

        writer.println(header);

        for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
            ObjectiveComparisonActivity activity =
                (ObjectiveComparisonActivity) iterator.next();

            writer.println(activityToString(activity));
        }

        writer.println("\n==== warning summary ====");
        writer.println("total:  " + countWarnings(activities));
        for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
            ObjectiveComparisonActivity activity =
                (ObjectiveComparisonActivity) iterator.next();

            if (activity.warning) {
                writer.println(activity);
            }
        }

        writer.println("\n==== correction summary ====");
        writer.println("total:  " + countCorrections(activities));
        for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
            ObjectiveComparisonActivity activity =
                (ObjectiveComparisonActivity) iterator.next();

            if (activity.corrected) {
                writer.println(activity);
            }
        }
        writer.flush();
    }

    private String activityToString(ObjectiveComparisonActivity activity) {
        if (activity == null) {
            return null;
        } else {
            String indention = "";
            for (int i = 0; i < (activity.level); i++) {
                indention += "    ";
            }
            return indention + activity;
        }
    }

    private int countWarnings(List warnings) {
        int count = 0;

        for (Iterator iterator = warnings.iterator(); iterator.hasNext();) {
            ObjectiveComparisonActivity objectiveWarning =
                (ObjectiveComparisonActivity) iterator.next();

            if (objectiveWarning.warning) {
                count++;
            }
        }
        return count;
    }

    private int countCorrections(List warnings) {
        int count = 0;

        for (Iterator iterator = warnings.iterator(); iterator.hasNext();) {
            ObjectiveComparisonActivity objectiveWarning =
                (ObjectiveComparisonActivity) iterator.next();

            if (objectiveWarning.corrected) {
                count++;
            }
        }
        return count;
    }

}
