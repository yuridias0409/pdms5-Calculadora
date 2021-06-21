package pdms5.tfinal.calculadora;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import pdms5.tfinal.calculadora.databinding.ActivityCalculadoraAvancadaBinding;

public class CalculadoraAvancadaActivity extends AppCompatActivity {
    ActivityCalculadoraAvancadaBinding activityCalculadoraAvancadaBinding;
    String numeroAtual = "";
    String conta = "";
    ArrayList<Double> numeros = new ArrayList<>();
    ArrayList<String> operadores = new ArrayList<>();

    //Formatar o resultado
    DecimalFormat df = new DecimalFormat(".00");

    //Script que possibilita utilizar funções javascript
    ScriptEngineManager m = new ScriptEngineManager();
    ScriptEngine e = m.getEngineByName("js");

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.calcNormal){
            Intent calculadoraSimplesIntent = new Intent(this, CalculadoraSimplesActivity.class);
            startActivity(calculadoraSimplesIntent);
            return true;
        }
        return false;
    }

    public void onClick(View view) throws ScriptException {
        Button botaoClicado = (Button) view;
        String botaoClicadoText = botaoClicado.getText().toString();

        //Verifica se o botão clicado foi um operador
        Pattern pattern = Pattern.compile("=|\\+|-|÷|X|%", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(botaoClicadoText);
        boolean isOperador = matcher.find();

        if(isOperador){ //É um número
            if(botaoClicadoText.equals("=") || botaoClicadoText.equals("%")){
                if(numeroAtual.contains("√")){
                    //Calcula a Raiz
                    numeroAtual = Double.toString(Math.sqrt(Double.parseDouble(numeroAtual.replaceAll("√",""))));
                }

                if(numeroAtual.contains("^")){
                    String[] numeros = numeroAtual.split("\\^");
                    //Calcula a Raiz
                    numeroAtual = Double.toString(Math.pow(Double.parseDouble(numeros[0]), Double.parseDouble(numeros[1])));
                }

                if(botaoClicadoText.equals("%")){
                    //é garantido que sempre existira uma conta, pois caso não exista, o caracter nem poderá ser adicionado
                    //remove o último operador colocado
                    String contaTemporaria = conta.substring(0, conta.length() - 1);
                    Object resultTemporario = e.eval(contaTemporaria);
                    Double numeroTemporario = Double.parseDouble(resultTemporario.toString());

                    numeroTemporario = numeroTemporario * Double.parseDouble(numeroAtual)/100;

                    numeroAtual = Double.toString(numeroTemporario);
                }

                conta = conta + numeroAtual;
                activityCalculadoraAvancadaBinding.calcContaTv.setText(conta);

                //Recupera uma funça existente no JavaScript que transforma String em Expressões
                Object result = e.eval(conta);

                //Formata para conter apenas duas casas decimais
                result = df.format(result);

                activityCalculadoraAvancadaBinding.calcResultadoTv.setText(result.toString());

                //Limpa os dados
                activityCalculadoraAvancadaBinding.calcContaTv.setText("");
                conta = "";
                numeros = new ArrayList<>();
                numeroAtual = "";
            }   else{
                if(!numeroAtual.equals("")){
                    if(numeroAtual.contains("√")){
                        //Calcula a Raiz
                        numeroAtual = Double.toString(Math.sqrt(Double.parseDouble(numeroAtual.replaceAll("√",""))));
                    }

                    if(numeroAtual.contains("^")){
                        String[] numeros = numeroAtual.split("\\^");
                        //Calcula a Raiz
                        numeroAtual = Double.toString(Math.pow(Double.parseDouble(numeros[0]), Double.parseDouble(numeros[1])));
                    }

                    numeros.add(Double.parseDouble(numeroAtual));

                    if (botaoClicadoText.equals("X")) {
                        botaoClicadoText = "*";
                    } else if (botaoClicadoText.equals("÷")) {
                        botaoClicadoText = "/";
                    }

                    if (conta.equals("")) {
                        conta = numeroAtual + botaoClicadoText;
                    } else {
                        conta = conta + numeroAtual + botaoClicadoText;
                    }

                    operadores.add(botaoClicadoText);

                    //Mostra conta inteira
                    activityCalculadoraAvancadaBinding.calcContaTv.setText(conta);

                    //Limpa para o imput do próximo número
                    activityCalculadoraAvancadaBinding.calcResultadoTv.setText("0");
                    numeroAtual = "";
                }
            }
        }   else{ //Não é um operador
            if(botaoClicadoText.equals("C")){
                activityCalculadoraAvancadaBinding.calcResultadoTv.setText("0");
                numeroAtual = "";
            }   else{
                if(numeros.isEmpty()){
                    activityCalculadoraAvancadaBinding.calcResultadoTv.setText("");
                }

                //Não permite inserir o símbolo de Raiz no meio de um número
                if(botaoClicadoText.equals("√")){
                    if(!numeroAtual.equals("")){
                        activityCalculadoraAvancadaBinding.calcResultadoTv.setText(numeroAtual);
                    }   else{
                        numeroAtual = numeroAtual + botaoClicadoText;
                        activityCalculadoraAvancadaBinding.calcResultadoTv.setText(numeroAtual);
                    }
                } else{
                    numeroAtual = numeroAtual + botaoClicadoText;
                    activityCalculadoraAvancadaBinding.calcResultadoTv.setText(numeroAtual);
                }
            }
        }
    }
}
