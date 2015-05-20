package nyc.c4q.ramonaharrison.scientificcalculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends ActionBarActivity {
    private TextView calculatorBar;
    private TextView cacheBar;
    private Button sin;
    private Button cos;
    private Button tan;
    private Button ln;
    private Button log;
    private Button sqrt;
    private Button ansb;
    private Button toThe;
    private Button rad;
    private Button deg;
    private Random random;

    private boolean isDeg;      // radian/degree mode
    private boolean isInv;      // inv button mode
    private String equation;    // equation raw format
    private String cache;       // text above calculator bar
    private String result;      // result
    private String display;     // equation formatted for display to the calculator bar
    private String ans;
    private String closeParens; // helper string for matching parentheses

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        calculatorBar = (TextView) findViewById(R.id.calculator_bar);
        cacheBar = (TextView) findViewById(R.id.cache);
        sin = (Button) findViewById(R.id.sin);
        cos = (Button) findViewById(R.id.cos);
        tan = (Button) findViewById(R.id.tan);
        ln = (Button) findViewById(R.id.ln);
        log = (Button) findViewById(R.id.log);
        sqrt = (Button) findViewById(R.id.sqrt);
        ansb = (Button) findViewById(R.id.ans);
        toThe = (Button) findViewById(R.id.xy);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // code to do for Portrait Mode
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.show();
            }
        } else {
            // code to do for Landscape Mode
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }



        if (savedInstanceState == null) {
            setDeg(false);
            setInv(false);
            setEquation("");
            setCache("");
            setResult("");
            setDisplay("0");
            setAns("0");
            setCloseParens("");
        } else {
            setDeg(savedInstanceState.getBoolean("isDeg"));
            setInv(savedInstanceState.getBoolean("isInv"));
            setEquation(savedInstanceState.getString("equation"));
            setCache(savedInstanceState.getString("cache"));
            setResult(savedInstanceState.getString("result"));
            setDisplay(savedInstanceState.getString("display"));
            setAns(savedInstanceState.getString("ans"));
            setCloseParens(savedInstanceState.getString("closeParens"));
        }

        setRadDegMode();
        displayText();
        cacheBar.setText(cache);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isDeg", isDeg);
        savedInstanceState.putBoolean("isInv", isInv);
        savedInstanceState.putString("equation", equation);
        savedInstanceState.putString("cache", cache);
        savedInstanceState.putString("result", result);
        savedInstanceState.putString("display", display);
        savedInstanceState.putString("ans", ans);
        savedInstanceState.putString("closeParens", closeParens);
    }

    public void setRadDegMode() {
        rad = (Button) findViewById(R.id.rad);
        deg = (Button) findViewById(R.id.deg);
        if (rad != null && deg != null) {
            if (isDeg) {
                deg.setBackgroundColor(Color.CYAN);
                deg.setTextColor(Color.WHITE);
                rad.setBackgroundColor(Color.LTGRAY);
                rad.setTextColor(Color.GRAY);
            } else {
                deg.setBackgroundColor(Color.LTGRAY);
                deg.setTextColor(Color.GRAY);
                rad.setBackgroundColor(Color.CYAN);
                rad.setTextColor(Color.WHITE);
            }
        }
    }

    public void type(View view) {
        // input button pressed
        Button button = (Button) view;
        digitInsertMultiply();
        equation += button.getText().toString();
        setDisplay(pad(equation));
        displayText();
    }

    public void typeSpecial(String special) {
        // input button pressed
        equation += special;
        setDisplay(pad(equation));
        displayText();
    }

    public void ac(View view) {
        // AC button pressed
        setEquation("");
        setCache("");
        setResult("");
        setDisplay("0");
        setAns("0");
        setCloseParens("");

        displayText();
        cacheBar.setText(cache);
    }

    public void ce(View view) {
        // CE button pressed
        if (equation.length() == 0) {
            setEquation("");
            setCache("");
            setResult("");
            setDisplay("0");
            setAns("0");
            setCloseParens("");
        } else {
            if (equation.charAt(equation.length() - 1) == '(') {
                if (closeParens.length() > 0) {
                    closeParens = closeParens.substring(1);
                }
            } else if (equation.charAt(equation.length() - 1) == ')') {
                closeParens += ')';
            }
            setEquation(equation.substring(0, equation.length() - 1));
            setDisplay(pad(equation));
        }

        displayText();
        cacheBar.setText(cache);
    }

    public void displayText() {
        SpannableString displayText = new SpannableString(display + closeParens);
        displayText.setSpan(new ForegroundColorSpan(Color.GRAY), displayText.length() - closeParens.length(), displayText.length(), 0);
        calculatorBar.setText(displayText, TextView.BufferType.SPANNABLE);
    }

    public void equals(View view) {
        // equals button pressed
        addCloseParens();
        insertZero();
        cache = display + " = ";
        result = Calculate.calculate(equation, isDeg); // TODO: result = calculate(equation);
        if (isValue(result)) {
            ans = result;
            display = result;
        } else {
            display = "Error";
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
        equation = "";
        closeParens = "";
        displayText();
        cacheBar.setText(cache);
    }

    public boolean isValue(String result) {
        for (int i = 0; i < result.length(); i++) {
            // checks for only numeric values in result
            char c = result.charAt(i);
            if ((c < 45 || c > 57) && c != 69) {
                return false;
            }
        }
            return true;
    }

    public void addCloseParens() {
        equation += closeParens;
        display += closeParens;
    }

    public void openParen(View view) {
        closeParens += ")";
        insertMultiply();
        typeSpecial("(");
    }

    public void closeParen(View view) {
        if (equation.contains("(")) {
            if (closeParens.length() > 0) {
                closeParens = closeParens.substring(1);
            }
            insertZero();
            typeSpecial(")");
        }
    }

    public void add(View view) {
        insertZero();
        typeSpecial("+");
    }

    public void subtract(View view) {
        typeSpecial("-");
    }

    public void divide(View view) {
        insertZero();
        typeSpecial("/");
    }

    public void multiply(View view) {
        insertZero();
        typeSpecial("*");
    }

    public void point(View view) {
        insertZero();
        if (equation.charAt(equation.length() - 1) != '.') {
            typeSpecial(".");
        }
    }

    public void insertMultiply() {
        if (0 < equation.length()) {
            char last = equation.charAt(equation.length() - 1);
            if ((47 < last && last < 58) || last == ')' || last == 's' || last == 'π' || last == 'e' || last == '%') { // assume multiplication if the last item in the equation is a digit, close paren, or 'Ans'
                typeSpecial("*");
            }
        }
    }

    public void digitInsertMultiply() {
        if (0 < equation.length()) {
            char last = equation.charAt(equation.length() - 1);
            if (last == ')' || last == 's' || last == 'π' || last == 'e' || last == '%') { // assume multiplication if the last item in the equation is a digit, close paren, or 'Ans'
                typeSpecial("*");
            }
        }
    }

    public void insertZero() {
        if (equation.length() == 0) {
            typeSpecial("0");
        } else {
            char last = equation.charAt(equation.length() - 1);
            if (last == '+' || last == '-' || last == '*' || last == '/' || last == '(') {
                typeSpecial("0");
            }
        }
    }

    public void rad(View view) {
        setDeg(false);
        setRadDegMode();
    }

    public void deg(View view) {
        setDeg(true);
        setRadDegMode();
    }

    public void factorial(View view) {
        insertZero();
        typeSpecial("!");
    }

    public void inv(View view) {
        if (!isInv) {
            isInv = true;
            sin.setText("asin");
            cos.setText("acos");
            tan.setText("atan");
            ln.setText("eⁿ");
            log.setText("10ⁿ");
            sqrt.setText("x²");
            ansb.setText("Rnd");
            toThe.setText("ⁿ√x");
        } else {
            isInv = false;
            sin.setText("sin");
            cos.setText("cos");
            tan.setText("tan");
            ln.setText("ln");
            log.setText("log");
            sqrt.setText("√");
            ansb.setText("Ans");
            toThe.setText("xⁿ");
        }
    }

    public void sin(View view) {
        closeParens += ")";
        insertMultiply();
        if (!isInv) {
            typeSpecial("sin(");
        } else {
            typeSpecial("asin(");
        }
    }

    public void ln(View view) {
        closeParens += ")";
        insertMultiply();
        if (!isInv) {
            typeSpecial("ln(");
        } else {
            typeSpecial("e^(");
        }
    }

    public void pi(View view) {
        insertMultiply();
        typeSpecial("π");
    }

    public void cos(View view) {
        insertMultiply();
        closeParens += ")";
        if (!isInv) {
            typeSpecial("cos(");
        } else {
            typeSpecial("acos(");
        }
    }

    public void log(View view) {
        insertMultiply();
        closeParens += ")";
        if (!isInv) {
            typeSpecial("log(");
        } else {
            typeSpecial("10^(");
        }
    }

    public void e(View view) {
        insertMultiply();
        typeSpecial("e");
    }

    public void tan(View view) {
        insertMultiply();
        closeParens += ")";
        if (!isInv) {
            typeSpecial("tan(");
        } else {
            typeSpecial("atan(");
        }
    }

    public void sqrt(View view) {
        if (!isInv) {
            insertMultiply();
            closeParens += ")";
            typeSpecial("√(");
        } else {
            insertZero();
            closeParens += ")";
            typeSpecial("^(2");
        }
    }

    public void percent(View view) {
        insertZero();
        typeSpecial("%");
    }

    public void ans(View view) {
        insertMultiply();
        if (!isInv) {
            typeSpecial(ans);
        } else {
            typeSpecial("" + random.nextInt(100));
        }
    }

    public void exp(View view) {
        typeSpecial("E");
    }

    public void toThe(View view) {
        closeParens += ")";
        insertZero();
        if (!isInv) {
            typeSpecial("^(");
        } else {
            closeParens += ")";
            typeSpecial("(√(");
        }
    }

    public String pad(String equation) {
        String paddedEquation = "";
        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            if (c == '+' || c == '-') {
                paddedEquation += " " + c + " ";
            } else if (c == '*') {
                paddedEquation += " × ";
            } else if (c == '/') {
                paddedEquation += " ÷ ";
            } else {
                paddedEquation += "" + c;
            }
        }

        return paddedEquation;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_calculator:
                return true;
            case R.id.action_graph:
                Log.d("event", "touched graph");
                Intent graphIntent = new Intent(this, GraphActivity.class);
                this.startActivity(graphIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean isDeg() {
        return isDeg;
    }

    public void setDeg(boolean deg) {
        this.isDeg = deg;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }


    public boolean getInv() {
        return isInv;
    }

    public void setInv(boolean inv) {
        this.isInv = inv;
    }

    public String getCloseParens() {
        return closeParens;
    }

    public void setCloseParens(String closeParens) {
        this.closeParens = closeParens;
    }
}
