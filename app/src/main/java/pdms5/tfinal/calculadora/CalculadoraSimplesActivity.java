package pdms5.tfinal.calculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import pdms5.tfinal.calculadora.databinding.ActivityCalculadoraSimplesBinding;

public class CalculadoraSimplesActivity extends AppCompatActivity {
    ActivityCalculadoraSimplesBinding activityCalculadoraSimplesBinding;
    String numeroAtual = "";
    String conta = "";
    ArrayList<Double> numeros = new ArrayList<>();
    //ArrayList<String> operadores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#343434")));
        actionBar.setTitle("Calculadora");
        actionBar.setSubtitle("Simples");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_simples);

        activityCalculadoraSimplesBinding = ActivityCalculadoraSimplesBinding.inflate(getLayoutInflater());
        setContentView(activityCalculadoraSimplesBinding.getRoot());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.calcAvancada){
            Intent calculadoraAvancadaIntent = new Intent(this, CalculadoraAvancadaActivity.class);
            startActivity(calculadoraAvancadaIntent);
            return true;
        }
        return false;
    }

    public void onClick(View view) throws ScriptException {
        Button botaoClicado = (Button) view;
        String botaoClicadoText = botaoClicado.getText().toString();


        //Verifica se o botão clicado foi um operador
        Pattern pattern = Pattern.compile("√|=|\\+|-|÷|X|%|\\^", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(botaoClicadoText);
        boolean isOperador = matcher.find();

        if(isOperador){ //É um número
            if(botaoClicadoText.equals("=")){
                conta = conta + numeroAtual;
                activityCalculadoraSimplesBinding.calcContaTv.setText(conta);

                //Recupera uma funça existente no JavaScript que transforma String em Expressões
                ScriptEngineManager m = new ScriptEngineManager();
                ScriptEngine e = m.getEngineByName("js");
                Object result = e.eval(conta);

                activityCalculadoraSimplesBinding.calcResultadoTv.setText(result.toString());

                //Limpa os dados
                activityCalculadoraSimplesBinding.calcContaTv.setText("");
                conta = "";
                numeros = new ArrayList<>();
                numeroAtual = "";
            }   else{
                if(!numeroAtual.equals("")){
                    numeros.add(Double.parseDouble(numeroAtual));

                    if(botaoClicadoText.equals("X")){
                        botaoClicadoText = "*";
                    }   else if(botaoClicadoText.equals("÷")){
                        botaoClicadoText = "/";
                    }

                    if(conta.equals("")){
                        conta = numeroAtual + botaoClicadoText;
                    }   else{
                        conta = conta + numeroAtual + botaoClicadoText;
                    }

                    //operadores.add(botaoClicadoText);

                    //Mostra conta inteira
                    activityCalculadoraSimplesBinding.calcContaTv.setText(conta);

                    //Limpa para o imput do próximo número
                    activityCalculadoraSimplesBinding.calcResultadoTv.setText("0");
                    numeroAtual = "";
                }
            }
        }   else{ //Não é um operador
            if(botaoClicadoText.equals("C")){
                activityCalculadoraSimplesBinding.calcResultadoTv.setText("0");
                numeroAtual = "";
            }   else{
                if(numeros.isEmpty()){
                    activityCalculadoraSimplesBinding.calcResultadoTv.setText("");
                }
                numeroAtual = numeroAtual + botaoClicadoText;
                activityCalculadoraSimplesBinding.calcResultadoTv.setText(numeroAtual);
            }
        }
    }

}