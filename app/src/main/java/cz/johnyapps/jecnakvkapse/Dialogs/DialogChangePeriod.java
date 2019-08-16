package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.johnyapps.jecnakvkapse.R;

/**
 * Dialog sloužící ke změně zobrazeného období
 */
public class DialogChangePeriod {
    private Context context;
    private LayoutInflater inflater;

    private String typ;

    /**
     * Inicializace
     * @param context   Context
     */
    protected DialogChangePeriod(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        typ = "pololeti";
    }

    /**
     * Vrátí dialog pro změnu roku a pololetí
     * @return  Dialog
     */
    public AlertDialog getPololeti() {
        typ = "pololeti";
        return get();
    }

    /**
     * Vrátí dialog pro změnu roku a měsíce
     * @return  Dialog
     */
    public AlertDialog getMesice() {
        typ = "mesice";
        return get();
    }

    /**
     * Vytvoří dialog podle specifikací z {@link #getPololeti()} nebo {@link #getMesice()}
     * @return  Dialog
     */
    private AlertDialog get() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Výběr období")
                .setView(Setup_dialogView())
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Zobrazit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processResult((AlertDialog) dialog);
                    }
                })
                .setNeutralButton("Aktuální", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aktualni();
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

        String strRok = rok.getSelectedItem().toString();
        int intRok = Integer.parseInt(strRok.split("/")[0]);
        intRok -= 2008;

        if (typ.equals("pololeti")) {
            int halfId = 22;
            if (pololeti.getSelectedItemPosition() != 1) {
                halfId = 21;
            }

            String obdobi = "schoolYearId=" + intRok + "&schoolYearHalfId=" + halfId;
            zobrazit(obdobi);
        } else {
            int selected = pololeti.getSelectedItemPosition();
            if (selected < 4) selected += 9;
            else selected -= 3;

            String obdobi = "schoolYearId=" + intRok + "&schoolYearPartMonthId=" + selected;
            zobrazit(obdobi);
        }
    }

    /**
     * Spustí akci po zmáčknutí tlačítka "Zobrazit"
     * @param obdobi    Zformátovaná data přes {@link #processResult(AlertDialog)}
     */
    public void zobrazit(String obdobi) {
        Log.i("DialogChangePeriod", obdobi);
    }

    /**
     * Spustí akci po zmáčknutí tlačítka "Aktuální"
     */
    public void aktualni() {

    }

    /**
     * Nastaví dialog view
     * @return  View
     */
    private View Setup_dialogView() {
        View view = inflater.inflate(R.layout.dialog_period_change, null, false);
        Spinner rok = view.findViewById(R.id.DialogPeriodChange_spinnerRok);
        Spinner pololeti = view.findViewById(R.id.DialogPeriodChange_spinnerPololeti);

        ArrayAdapter<CharSequence> rokAdapter = new ArrayAdapter<>(context, R.layout.item_spinner, getRoky());
        rokAdapter.setDropDownViewResource(R.layout.item_spinner);
        rok.setAdapter(rokAdapter);

        if (typ.equals("pololeti")) {
            ArrayAdapter<CharSequence> pololetiAdapter = ArrayAdapter.createFromResource(context, R.array.pololeti, R.layout.item_spinner);
            pololetiAdapter.setDropDownViewResource(R.layout.item_spinner);
            pololeti.setAdapter(pololetiAdapter);
            pololeti.setSelection(pololeti());
        } else {
            ArrayAdapter<CharSequence> pololetiAdapter = ArrayAdapter.createFromResource(context, R.array.mesice, R.layout.item_spinner);
            pololetiAdapter.setDropDownViewResource(R.layout.item_spinner);
            pololeti.setAdapter(pololetiAdapter);
            pololeti.setSelection(getMesic());
        }

        return view;
    }

    /**
     * Rozhodne zda je první pololetí (=0) nebo druhé (=1)
     * @return  Číslo pololetí
     */
    private int pololeti() {
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
        if (pololeti() == 1) {
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
}
