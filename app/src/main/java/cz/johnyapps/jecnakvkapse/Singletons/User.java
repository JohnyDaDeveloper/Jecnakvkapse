package cz.johnyapps.jecnakvkapse.Singletons;

import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.Profil.Profil;
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
    private String login;
    private boolean dummy;

    private Score score;
    private Rozvrh rozvrh;
    private Omluvnak omluvnak;
    private Prichody prichody;
    private Profil profil;

    private SuplarchHolder suplarchHolder;

    private LoggedListener loggedListener;

    /**
     * Inicializace
     */
    private User() {
        sessionId = "";
        login = "";
        dummy = false;

        loggedListener = null;
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
     * Nastaví login
     * @param login Login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Vrátí login
     * @return  Login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Nastaví zda je uživatel "Dummy" (pro demo)
     * @param dummy True - je; False - není
     */
    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    /**
     * Vrátí zda je uživatel "Dummy" (pro demo)
     * @return dummy    True - je; False - není
     */
    public boolean isDummy() {
        return dummy;
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
     * Nastavuje profil
     * @param profil    {@link Profil}
     */
    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    /**
     * Vrátí profil
     * @return  {@link Profil}
     */
    public Profil getProfil() {
        return profil;
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
        if (!logged) {
            setSessionId("");
        }

        if (loggedListener != null) {
            loggedListener.onLoggedChange(logged);
        }
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
}
