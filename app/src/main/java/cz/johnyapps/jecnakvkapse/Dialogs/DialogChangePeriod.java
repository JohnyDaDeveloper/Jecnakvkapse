package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Dialog sloužící ke změně zobrazeného období
 */
public class DialogChangePeriod {
    private static final String TAG = "DialogChangePeriod";
    private static final String TYP_POLOLETI = "pololeti";
    private static final String TYP_MESICE = "mesice";
    private static final int FIRST_HALF_YEAR = 21;
    private static final int SECOND_HALF_YEAR = 22;
    private static final int START_YEAR = 2008;

    private Context context;
    private LayoutInflater inflater;

    private String typ;

    /**
     * Inicializace
     * @param context   Context
     */
    public DialogChangePeriod(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        typ = TYP_POLOLETI;
    }

    /**
     * Vrátí dialog pro změnu roku a pololetí
     * @param obdobi    Období na které se má dialog přednastavit
     * @return  Dialog
     */
    public AlertDialog getPololeti(String obdobi) {
        typ = TYP_POLOLETI;
        return get(obdobi);
    }

    /**
     * Vrátí dialog pro změnu roku a měsíce
     * @param obdobi    Období na které se má dialog přednastavit
     * @return  Dialog
     */
    public AlertDialog getMesice(String obdobi) {
        typ = TYP_MESICE;
        return get(obdobi);
    }

    /**
     * Vytvoří dialog podle specifikací z {@link #getPololeti(String)} nebo {@link #getMesice(String)}
     * @param obdobi    Období na které se má dialog přednastavit
     * @return  Dialog
     */
    private AlertDialog get(String obdobi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_change_period_title)
                .setView(createDialogView(obdobi))
                .setNegativeButton(R.string.zrusit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.zobrazit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processResult((AlertDialog) dialog);
                    }
                })
                .setNeutralButton(R.string.dialog_change_period_aktualni, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aktualniObdobi();
                    }
                });

        return builder.create();
    }

    /**
     * Zprocesuje výsledek vybírání a vrátí data která stačí přidat do requestu pomocí {@link cz.johnyapps.jecnakvkapse.HttpConnection.Request#addObdobi(String)}
     * @param dialog    Dialog ze kterého bere data
     */
    private void processResult(AlertDialog dialog) {
        Spinner rok = dialog.findViewById(R.id.DialogPeriodChange_spinnerRok);
        Spinner pololeti = dialog.findViewById(R.id.DialogPeriodChange_spinnerPololeti);

        assert rok != null;
        assert pololeti != null;

        String strRok = rok.getSelectedItem().toString();
        int intRok = Integer.parseInt(strRok.split("/")[0]);
        intRok -= START_YEAR;

        if (typ.equals(TYP_POLOLETI)) {
            int halfId = SECOND_HALF_YEAR;
            if (pololeti.getSelectedItemPosition() != 1) {
                halfId = FIRST_HALF_YEAR;
            }

            String obdobi = "schoolYearId=" + intRok + "&schoolYearHalfId=" + halfId;
            zobrazitObdobi(obdobi);
        } else {
            int selected = pololeti.getSelectedItemPosition();
            if (selected < 4) selected += 9;
            else selected -= 3;

            String obdobi = "schoolYearId=" + intRok + "&schoolYearPartMonthId=" + selected;
            zobrazitObdobi(obdobi);
        }
    }

    /**
     * Uživatel chce zobrazit dané období
     * @param obdobi    Zformátovaná data přes {@link #processResult(AlertDialog)}
     */
    private void zobrazitObdobi(String obdobi) {
        Logger.i(TAG, "zobrazitObdobi: " + obdobi);

        if (onZmenObdobiListener != null) {
            onZmenObdobiListener.zobrazitObdobi(obdobi);
        }
    }

    /**
     * Uživatel chce zobrazit aktuální období
     */
    private void aktualniObdobi() {
        if (onZmenObdobiListener != null) {
            onZmenObdobiListener.zobrazitAktualni();
        }
    }

    /**
     * Vytvoří dialog view
     * @param obdobi    Období na které se má dialog přednastavit
     * @return  View
     */
    private View createDialogView(String obdobi) {
        View view = inflater.inflate(R.layout.dialog_period_change, null, false);
        Spinner rok = view.findViewById(R.id.DialogPeriodChange_spinnerRok);
        Spinner pololeti = view.findViewById(R.id.DialogPeriodChange_spinnerPololeti);

        ArrayAdapter<CharSequence> rokAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, getRoky());
        rokAdapter.setDropDownViewResource(R.layout.item_spinner);
        rok.setAdapter(rokAdapter);

        if (typ.equals(TYP_POLOLETI)) {
            ArrayAdapter<CharSequence> pololetiAdapter = ArrayAdapter.createFromResource(context, R.array.pololeti, R.layout.item_spinner);
            pololetiAdapter.setDropDownViewResource(R.layout.item_spinner);
            pololeti.setAdapter(pololetiAdapter);
            pololeti.setSelection(obdobi == null ? getPololeti() : getPololetiFromString(obdobi));
        } else {
            ArrayAdapter<CharSequence> pololetiAdapter = ArrayAdapter.createFromResource(context, R.array.mesice, R.layout.item_spinner);
            pololetiAdapter.setDropDownViewResource(R.layout.item_spinner);
            pololeti.setAdapter(pololetiAdapter);
            pololeti.setSelection(obdobi == null ? getMesic() : getMesicFromString(obdobi));
        }

        return view;
    }

    private int getPololetiFromString(String obdobi) {
        try {
            String[] data = obdobi.split("&");
            data = data[1].split("=");

            int value = Integer.parseInt(data[1]);

            if (value == SECOND_HALF_YEAR) {
                return 1;
            }
        } catch (Exception e) {
            Logger.w(TAG, "getPololetiFromString: chyba při parsování " + obdobi);
        }

        return 0;
    }

    private int getMesicFromString(String obdobi) {
        try {
            String[] data = obdobi.split("&");
            data = data[1].split("=");
            int value = Integer.parseInt(data[1]);

            if (value < 9) {
                return value + 3;
            } else {
                return value - 9;
            }
        } catch (Exception e) {
            Logger.w(TAG, "getPololetiFromString: chyba při parsování " + obdobi);
        }

        return 0;
    }

    /**
     * Rozhodne zda je první pololetí (=0) nebo druhé (=1)
     * @return  Číslo pololetí
     */
    private int getPololeti() {
        Locale locale = new Locale("cs", "CZ");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat month = new SimpleDateFormat("MM", locale);

        int intMonth = Integer.parseInt(month.format(calendar.getTime()));

        if (intMonth > 1 && intMonth < 9) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Vrátí 4 školní roky. Od aktuálního zpět do historie
     * @return  Roky
     */
    private CharSequence[] getRoky() {
        Locale locale = new Locale("cs", "CZ");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat year = new SimpleDateFormat("yyyy", locale);

        int intYear = Integer.parseInt(year.format(calendar.getTime()));

        String[] roky = new String[4];
        if (getPololeti() == 1) {
            for (int i = 0; i < 4; i++) {
                roky[i] = (intYear - i - 1) + "/" + (intYear - i);
            }
        } else {
            roky[0] = intYear + "/" + (intYear + 1);

            for (int i = 1; i < 4; i++) {
                roky[i] = (intYear - i) + "/" + (intYear - i + 1);
            }
        }

        return roky;
    }

    /**
     * Vrátí číslo aktuálního měsíce
     * @return  Číslo měsíce
     */
    private int getMesic() {
        Locale locale = new Locale("cs", "CZ");
        Calendar calendar = Calendar.getInstance(locale);

        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY: {
                return 4;
            }

            case Calendar.FEBRUARY: {
                return 5;
            }

            case Calendar.MARCH: {
                return 6;
            }

            case Calendar.APRIL: {
                return 7;
            }

            case Calendar.MAY: {
                return 8;
            }

            case Calendar.JUNE: {
                return 9;
            }

            case Calendar.JULY: {
                return 10;
            }

            case Calendar.AUGUST: {
                return 11;
            }

            case Calendar.SEPTEMBER: {
                return 0;
            }

            case Calendar.OCTOBER: {
                return 1;
            }

            case Calendar.NOVEMBER: {
                return 2;
            }

            case Calendar.DECEMBER: {
                return 3;
            }

            default:
                return 0;
        }
    }

    private OnZmenObdobiListener onZmenObdobiListener;
    public interface OnZmenObdobiListener {
        void zobrazitObdobi(String obdobi);
        void zobrazitAktualni();
    }

    public void setOnZmenObdobiListener(OnZmenObdobiListener onZmenObdobiListener) {
        this.onZmenObdobiListener = onZmenObdobiListener;
    }
}
