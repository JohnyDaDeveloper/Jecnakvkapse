package cz.johnyapps.jecnakvkapse.Rozvrh;

import com.crashlytics.android.Crashlytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Slouží k uchovávání rozvrhu
 */
public class Rozvrh {
    private static final String TAG = "Rozvrh: ";
    private String datum;

    private ArrayList<Den> dny;
    private ArrayList<Period> periods;
    private Locale locale;
    private Calendar calendar;

    /**
     * Inicializace
     * @param datum Datum stažení
     */
    public Rozvrh(String datum) {
        this.datum = datum;

        this.dny = new ArrayList<>();
        this.periods = new ArrayList<>();
        this.locale = new Locale("cs", "CZ");

        Setup_calendar();
    }

    /**
     * Vrací datum
     * @return  Datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Přidává den do rozvrhu
     * @param den   {@link Den}
     */
    public void addDen(Den den) {
        dny.add(den);
    }

    /**
     * Vrací dny
     * @return  ArrayList dnů
     * @see Den
     */
    public ArrayList<Den> getDny() {
        return dny;
    }

    /**
     * Nastaví časy hoidn
     * @param periods  ArrayList časů hodin
     * @see Period
     */
    public void setPeriods(ArrayList<Period> periods) {
        this.periods = periods;
    }

    /**
     * Vrací časty hoidn
     * @return ArrayList časů hodin
     * @see Period
     */
    public ArrayList<Period> getPeriods() {
        return periods;
    }

    /**
     * Vrátí id aktuálního dne (pozice v ArrayListu {@link #dny})
     * @return  id
     */
    public int getCurrentDay() {
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY: {
                return 0;
            }

            case Calendar.TUESDAY: {
                return 1;

            }

            case Calendar.WEDNESDAY: {
                return 2;
            }

            case Calendar.THURSDAY: {
                return 3;
            }

            case Calendar.FRIDAY: {
                return 4;
            }

            case Calendar.SATURDAY: {
                return -1;
            }

            case Calendar.SUNDAY: {
                return -1;
            }

            default: return -1;
        }
    }

    /**
     * Vrátí id aktuální hodiny
     * @return  id
     */
    public int getCurrentClass() {
        int hodin = calendar.get(Calendar.HOUR_OF_DAY);
        int minuty = calendar.get(Calendar.MINUTE);

        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            String[] zacatek = period.getZacatek().split(":");
            String[] konec = period.getKonec().split(":");

            int zh = Integer.parseInt(zacatek[0]);
            int zm = Integer.parseInt(zacatek[1]);
            int kh = Integer.parseInt(konec[0]);
            int km = Integer.parseInt(konec[1]);

            if (hodin > zh) {
                if (hodin <= kh) {
                    if (minuty <= km) {
                        return i;
                    }
                }
            } else if (hodin == zh) {
                if (minuty >= zm && (hodin != kh || minuty <= km)) {
                    return i;
                }
            } else if (hodin == kh) {
                if (minuty <= km) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void Setup_calendar() {
        calendar = Calendar.getInstance();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
            calendar.setTime(format.parse(format.format(calendar.getTime())));
        } catch (ParseException e) {
            Crashlytics.log(TAG + "Date parse failed");
        }
    }
}
