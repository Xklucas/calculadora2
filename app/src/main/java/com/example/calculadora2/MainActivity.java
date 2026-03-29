package com.example.calculadora2;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editVisor; // visor da calculadora
    private String operadorAtual = ""; // guarda o operador (+, -, x, /)
    private boolean resultadoExibido = false; // indica se acabou de mostrar resultado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editVisor = findViewById(R.id.editVisor);

        // BOTÕES NUMÉRICOS
        findViewById(R.id.btn0).setOnClickListener(v -> digitarNumero("0"));
        findViewById(R.id.btn1).setOnClickListener(v -> digitarNumero("1"));
        findViewById(R.id.btn2).setOnClickListener(v -> digitarNumero("2"));
        findViewById(R.id.btn3).setOnClickListener(v -> digitarNumero("3"));
        findViewById(R.id.btn4).setOnClickListener(v -> digitarNumero("4"));
        findViewById(R.id.btn5).setOnClickListener(v -> digitarNumero("5"));
        findViewById(R.id.btn6).setOnClickListener(v -> digitarNumero("6"));
        findViewById(R.id.btn7).setOnClickListener(v -> digitarNumero("7"));
        findViewById(R.id.btn8).setOnClickListener(v -> digitarNumero("8"));
        findViewById(R.id.btn9).setOnClickListener(v -> digitarNumero("9"));

        // BOTÃO PONTO DECIMAL
        findViewById(R.id.btnPonto).setOnClickListener(v -> digitarPonto());

        // OPERADORES
        findViewById(R.id.btnSomar).setOnClickListener(v -> digitarOperador("+"));
        findViewById(R.id.btnSubtrair).setOnClickListener(v -> digitarOperador("-"));
        findViewById(R.id.btnMultiplicar).setOnClickListener(v -> digitarOperador("x"));
        findViewById(R.id.btnDividir).setOnClickListener(v -> digitarOperador("/"));

        // IGUAL
        findViewById(R.id.btnIgual).setOnClickListener(v -> calcular());

        // BACKSPACE E CLEAR
        findViewById(R.id.btnBackspace).setOnClickListener(v -> backspace());
        findViewById(R.id.btnClear).setOnClickListener(v -> limpar());
    }

    // Adiciona número ao visor
    private void digitarNumero(String num) {
        // Se acabou de mostrar resultado, limpa antes de digitar novo número
        if (resultadoExibido) {
            editVisor.setText("");
            resultadoExibido = false;
        }

        String atual = editVisor.getText().toString();
        editVisor.setText(atual + num);
    }

    // Adiciona ponto decimal
    private void digitarPonto() {
        String atual = editVisor.getText().toString();

        if (atual.isEmpty()) return;

        char ultimo = atual.charAt(atual.length() - 1);
        if (!Character.isDigit(ultimo)) return;

        String ultimoNumero = getUltimoNumero(atual);

        // Evita mais de um ponto no mesmo número
        if (ultimoNumero.indexOf('.') == -1) {
            editVisor.setText(atual + ".");
        }
    }

    // Adiciona operador
    private void digitarOperador(String op) {
        String atual = editVisor.getText().toString();

        if (atual.isEmpty() && op.equals("-")) {
            editVisor.setText("-");
            return;
        }

        if (atual.isEmpty()) return;

        resultadoExibido = false;

        char ultimo = atual.charAt(atual.length() - 1);

        // Substitui operador se já existir um no final
        if (ultimo == '+' || ultimo == '-' || ultimo == 'x' || ultimo == '/') {
            editVisor.setText(atual.substring(0, atual.length() - 1) + op);
        } else {
            editVisor.setText(atual + op);
        }

        operadorAtual = op;
    }

    // Realiza o cálculo
    private void calcular() {
        String expressao = editVisor.getText().toString();
        if (expressao.isEmpty() || operadorAtual.isEmpty()) return;

        int indexOp = expressao.indexOf(operadorAtual, 1);
        if (indexOp == -1) return;

        String strNum1 = expressao.substring(0, indexOp);
        String strNum2 = expressao.substring(indexOp + 1);

        double num1, num2;

        try {
            num1 = Double.parseDouble(strNum1);
            num2 = Double.parseDouble(strNum2);
        } catch (NumberFormatException e) {
            editVisor.setText("ERROR");
            resultadoExibido = true;
            return;
        }

        if (operadorAtual.equals("/") && num2 == 0) {
            editVisor.setText("ERROR");
            resultadoExibido = true;
            return;
        }

        double resultado;

        switch (operadorAtual.charAt(0)) {
            case '+': resultado = num1 + num2; break;
            case '-': resultado = num1 - num2; break;
            case 'x': resultado = num1 * num2; break;
            case '/': resultado = num1 / num2; break;
            default: return;
        }

        operadorAtual = "";

        if (resultado == (long) resultado) {
            editVisor.setText(String.valueOf((long) resultado));
        } else {
            editVisor.setText(String.valueOf(resultado));
        }

        resultadoExibido = true;
    }

    // Apaga último caractere
    private void backspace() {
        String atual = editVisor.getText().toString();
        if (atual.isEmpty()) return;

        editVisor.setText(atual.substring(0, atual.length() - 1));
    }

    // Limpa visor
    private void limpar() {
        editVisor.setText("");
        operadorAtual = "";
        resultadoExibido = false;
    }

    // Retorna o último número digitado
    private String getUltimoNumero(String expressao) {
        int ultimoOp = -1;
        for (int i = expressao.length() - 1; i >= 1; i--) {
            char c = expressao.charAt(i);
            if (c == '+' || c == '-' || c == 'x' || c == '/') {
                ultimoOp = i;
                break;
            }
        }
        if (ultimoOp == -1) return expressao;
        return expressao.substring(ultimoOp + 1);
    }
}