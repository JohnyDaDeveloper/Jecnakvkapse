package cz.johnyapps.jecnakvkapse.Dialogs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cz.johnyapps.jecnakvkapse.R;

public class DialogMarkBuilder extends AlertDialog.Builder {
    private View header;
    private TextView title;

    public DialogMarkBuilder(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        header = inflater.inflate(R.layout.dialog_mark_header, new LinearLayout(context), false);
        title = header.findViewById(R.id.DialogMark_title);
        setCustomTitle(header);
    }

    public DialogMarkBuilder setHeaderColor(int color) {
        header.setBackgroundColor(color);
        return this;
    }

    @Override
    public DialogMarkBuilder setTitle(int titleId) {
        this.title.setText(titleId);
        return this;
    }

    @Override
    public DialogMarkBuilder setTitle(CharSequence title) {
        this.title.setText(title);
        return this;
    }

    @Override
    public DialogMarkBuilder setMessage(int messageId) {
        return (DialogMarkBuilder) super.setMessage(messageId);
    }

    @Override
    public DialogMarkBuilder setMessage(@Nullable CharSequence message) {
        return (DialogMarkBuilder) super.setMessage(message);
    }
}
