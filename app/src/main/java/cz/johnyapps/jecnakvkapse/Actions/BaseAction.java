package cz.johnyapps.jecnakvkapse.Actions;

public abstract class BaseAction {
    protected void completed() {
        if (onCompleteListener != null) {
            onCompleteListener.onComplete();
        }
    }

    protected void error() {
        if (onCompleteListener != null) {
            onCompleteListener.onError();
        }
    }

    private OnCompleteListener onCompleteListener;
    public interface OnCompleteListener {
        void onComplete();
        void onError();
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
}
