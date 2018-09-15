package ru.yandex.shtukarr.bigdigitalstesta.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ru.yandex.shtukarr.bigdigitalstesta.R;

public class TestFragment extends Fragment {
    private static final String INTENT_URL = "String";
    private static final String INTENT_STATUS = "Status";
    private static final String INTENT_ID = "Id";

    private EditText editTextImageUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        editTextImageUrl = rootView.findViewById(R.id.image_url);
        rootView.findViewById(R.id.btn_ok).setOnClickListener(v -> handleSave());
        return rootView;
    }

    private void handleSave() {
        if (!validate()) {
            return;
        }
        Log.d("TAG", "handleSave");
        try {
            Intent intent = new Intent();
            intent.putExtra(INTENT_STATUS, -1);
            intent.putExtra(INTENT_ID, -1);
            intent.putExtra(INTENT_URL, editTextImageUrl.getText().toString());
            intent.setClassName("ru.yandex.shtukarr.bigdigitalstestb", "ru.yandex.shtukarr.bigdigitalstestb.ImageActivity");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), getResources().getString(R.string.installSecondApp), Toast.LENGTH_LONG).show();
        }
    }

    protected boolean validate() {
        boolean result = true;
        if (TextUtils.isEmpty(editTextImageUrl.getText().toString().trim())) {
            editTextImageUrl.setError("Required");
            result = false;
        } else {
            editTextImageUrl.setError(null);
        }
        return result;
    }
}
