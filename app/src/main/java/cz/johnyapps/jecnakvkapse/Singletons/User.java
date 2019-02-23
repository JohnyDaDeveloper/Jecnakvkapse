package cz.johnyapps.jecnakvkapse.Singletons;

import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.Rozvrh.Rozvrh;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchHolder;

/**
 * Singleton který drží data uživatele načtená v mezipaměti
 */
public class User {
    private static final User user = new User();

    /**
     * Vrací instanci
     * @return  Instance
     */
    public static User getUser() {
        return user;
    }

    private String sessionId;

    private Score score;
    private Rozvrh rozvrh;
    private Omluvnak omluvnak;
    private Prichody prichody;

    private SuplarchHolder suplarchHolder;

    private LoggedListener loggedListener;
    private boolean logged;

    private boolean premium;

    /**
     * Inicializace
     */
    private User() {
        sessionId = "";

        loggedListener = null;
        logged = false;

        premium = false;
    }

    /**
     * Nastaví sessionID
     * @param sessionId ID
     * @see cz.johnyapps.jecnakvkapse.Actions.Prihlaseni
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Vrací sessionID
     * @return  ID
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Nastavuje skóre (známky)
     * @param score {@link Score}
     */
    public void setScore(Score score) {
        this.score = score;
    }

    /**
     * Vrací skóre (známky)
     * @return  Vrací známky ({@link Score})
     */
    public Score getScore() {
        return score;
    }

    /**
     * Nastavujee rozvrh
     * @param rozvrh    {@link Rozvrh}
     */
    public void setRozvrh(Rozvrh rozvrh) {
        this.rozvrh = rozvrh;
    }

    /**
     * Vrací rozvrh
     * @return  {@link Rozvrh}
     */
    public Rozvrh getRozvrh() {
        return rozvrh;
    }

    /**
     * Nastavuje omluvňák
     * @param omluvnak  {@link Omluvnak}
     */
    public void setOmluvnak(Omluvnak omluvnak) {
        this.omluvnak = omluvnak;
    }

    /**
     * Vrací omluvňák
     * @return  {@link Omluvnak}
     */
    public Omluvnak getOmluvnak() {
        return omluvnak;
    }

    /**
     * Nastavuje pŕíchody
     * @param prichody  {@link Prichody}
     */
    public void setPrichody(Prichody prichody) {
        this.prichody = prichody;
    }

    /**
     * Vrací příchody
     * @return {@link Prichody}
     */
    public Prichody getPrichody() {
        return prichody;
    }

    /**
     * Nastavuje SuplarchHoldery
     * @param suplarchHolder  Linky/odkazy ({@link SuplarchHolder})
     */
    public void setSuplarchHolder(SuplarchHolder suplarchHolder) {
        this.suplarchHolder = suplarchHolder;
    }

    /**
     * Vrací SuplarchHoldery
     * @return {@link Prichody}
     */
    public SuplarchHolder getSuplarchHolder() {
        return suplarchHolder;
    }

    /**
     * Nastaví jestli je uživatel přihášen
     * @param logged    Stav
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
        loggedListener.onLoggedChange(logged);
    }

    /**
     * Nastaví listener přihlášení
     * @param loggedListener    Listener
     */
    public void setLoggedListener(LoggedListener loggedListener) {
        this.loggedListener = loggedListener;
    }

    /**
     * Logged listener
     */
    public interface LoggedListener {
        void onLoggedChange(boolean logged);
    }

    /**
     * Nastaví premium status
     * @param status Status
     */
    public void setPremium(boolean status) {
        premium = status;
    }

    /**
     * Vrátí premium status
     * @return  True - premium, Flase - není premium
     */
    public boolean isPremium() {
        return premium;
    }
}
