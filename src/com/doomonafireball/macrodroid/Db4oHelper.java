package com.doomonafireball.macrodroid;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;

public class Db4oHelper {
    private static ObjectContainer oc = null;
    private Context mContext;

    public Db4oHelper(Context ctx) {
        this.mContext = ctx;
    }

    public ObjectContainer db() {
        // Create, open, and close the database
        try {
            if (oc == null || oc.ext().isClosed()) {
                oc = Db4oEmbedded
                        .openFile(dbConfig(), db4oDBFullPath(mContext));
            }
            return oc;
        } catch (Exception e) {
            Log.e(Tags.LOG_TAG, e.toString());
            return null;
        }
    }

    private EmbeddedConfiguration dbConfig() throws IOException {
        // Configure the behavior of the database
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().objectClass(ADay.class).objectField("id")
                .indexed(true);
        return configuration;
    }

    private String db4oDBFullPath(Context ctx) {
        // Returns the path for the database location
        return ctx.getDir("data", 0) + "/" + "macrodroid.db4o";
    }

    public void close() {
        // Closes the database
        if (oc != null) {
            oc.close();
        }
    }

    public void deleteDatabase() {
        close();
        new File(db4oDBFullPath(mContext)).delete();
    }

    public void saveDay(ADay day) {
        if (day.id == 0) {
            day.id = getNextId();
        }
        db().store(day);
        db().commit();
    }

    public void deleteDay(long id) {
        ObjectSet<ADay> result = fetchDaysById(id);
        while (result.hasNext()) {
            db().delete((ADay) result.next());
        }
        db().commit();
    }

    public ADay fetchDayById(long id) {
        ObjectSet<ADay> result = fetchDaysById(id);
        if (result.hasNext()) {
            return (ADay) result.next();
        } else {
            return null;
        }
    }

    public int countDays(long id) {
        ObjectSet<ADay> days = fetchDaysById(id);
        return days == null ? 0 : days.size();
    }

    public List<ADay> fetchAllDayRows() {
        return db().query(ADay.class);
    }

    public List<ADay> fetchExercisesForDate(final Calendar calendar) {
        final int checkCalMonth = calendar.get(Calendar.MONTH);
        final int checkCalDate = calendar.get(Calendar.DATE);
        final int checkCalYear = calendar.get(Calendar.YEAR);
        List<ADay> result = db().query(new Predicate<ADay>() {
            public boolean match(ADay day) {
                Log.d(Tags.LOG_TAG, "Passed calendar: " + calendar.get(Calendar.MONTH) + "/"
                        + calendar.get(Calendar.DATE) + "/"
                        + calendar.get(Calendar.YEAR));
                Calendar dayCal = day.getDate();
                int dayCalMonth = dayCal.get(Calendar.MONTH);
                int dayCalDate = dayCal.get(Calendar.DATE);
                int dayCalYear = dayCal.get(Calendar.YEAR);
                if ((dayCalMonth == checkCalMonth) && (dayCalDate == checkCalDate)
                        && (dayCalYear == checkCalYear)) {
                    Log.d(Tags.LOG_TAG, "Found match, adding item!");
                    return true;
                }
                return false;
            }
        });
        return result;
    }

    private ObjectSet<ADay> fetchDaysById(long id) {
        ADay day = new ADay();
        day.id = id;
        return db().queryByExample(day);
    }

    public long getNextId() {
        ObjectSet<Db4oIncrementedId> result = db().queryByExample(
                Db4oIncrementedId.class);
        Db4oIncrementedId ii = null;
        long nextId;
        if (result.hasNext()) {
            ii = (Db4oIncrementedId) result.next();
        } else {
            ii = new Db4oIncrementedId();
        }
        nextId = ii.getNextID();
        db().store(ii);
        return nextId;
    }
}
