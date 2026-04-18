package com.axys.redeflexmobile.ui.redeflex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.util.Alerta;

public class ImagemActivity extends AppCompatActivity {
    GestureImageView gestureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            gestureImageView = (GestureImageView) findViewById(R.id.imageView);
            Bundle bundle = getIntent().getExtras();
            if (bundle == null)
                throw new Exception("Arquivo não encontrado");

            String local = bundle.getString(Config.LocalImagem);
            String nome = bundle.getString(Config.NomeImagem);
            getSupportActionBar().setTitle(nome);
            Bitmap bImagem = BitmapFactory.decodeFile(local);
            gestureImageView.setImageBitmap(bImagem);
        } catch (OutOfMemoryError ex) {
            new Alerta(ImagemActivity.this, "Erro", "Memória do celular está cheia").showError();
        } catch (Exception ex) {
            new Alerta(ImagemActivity.this, "Erro", ex.getMessage()).showError();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}