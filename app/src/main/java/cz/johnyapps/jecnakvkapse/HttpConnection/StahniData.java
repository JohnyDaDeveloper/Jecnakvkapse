package cz.johnyapps.jecnakvkapse.HttpConnection;

public abstract class StahniData {
    protected void completed(String result) {
        if (onCompleteListener != null) {
            onCompleteListener.onComplete(result);
        }
    }

    private OnCompleteListener onCompleteListener;
    public interface OnCompleteListener {
        void onComplete(String result);
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
}
