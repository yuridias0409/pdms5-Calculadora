package pdms5.tfinal.calculadora;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import pdms5.tfinal.calculadora.databinding.ActivityCalculadoraAvancadaBinding;

public class CalculadoraAvancadaActivity extends AppCompatActivity {
    private ActivityCalculadoraAvancadaBinding activityCalculadoraAvancadaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#343434")));
        actionBar.setTitle("Calculadora");
        actionBar.setSubtitle("Avançada");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_simples);

        activityCalculadoraAvancadaBinding = ActivityCalculadoraAvancadaBinding.inflate(getLayoutInflater());
        setContentView(activityCalculadoraAvancadaBinding.getRoot());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onClick(View view){}
}
