package nyc.c4q.ramonaharrison.scientificcalculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private boolean deg;        // boolean for radian/degree mode
    private String equation;    // text entered into calculator bar
    private String cache;       // text above calculator bar
    private String result;      // result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setDeg(false);
            setEquation("");
            setCache("");
            setResult("");
        }

    }

    public void type(View view) {
        // input button pressed

        Button button = (Button) view;
        String input = button.getText().toString();
        String equation = getEquation();

        TextView calculatorBar = (TextView) findViewById(R.id.calculator_bar);
        calculatorBar.setText(equation += input);
    }

    public void ac(View view) {
        // AC button pressed
        setEquation("");
        setCache("");
        setResult("");

        TextView calculatorBar = (TextView) findViewById(R.id.calculator_bar);
        calculatorBar.setText(getEquation());

        TextView cacheBar = (TextView) findViewById(R.id.cache);
        cacheBar.setText(getCache());
    }

    public void ce(View view) {
        // CE button pressed
        String equation = getEquation();
        if (equation.length() == 0) {
            setEquation("");
            setCache("");
            setResult("");
        } else {
            setEquation(equation.substring(0, equation.length() - 2));
        }

        TextView calculatorBar = (TextView) findViewById(R.id.calculator_bar);
        calculatorBar.setText(getEquation());

        TextView cacheBar = (TextView) findViewById(R.id.cache);
        cacheBar.setText(getCache());
    }

    public void equals(View view) {
        // equals button pressed
        cache = equation + " = ";
        equation = "";
        result = ""; // TODO: result = calculate(equation);

        TextView calculatorBar = (TextView) findViewById(R.id.calculator_bar);
        calculatorBar.setText(pad(result));

        TextView cacheBar = (TextView) findViewById(R.id.cache);
        cacheBar.setText(cache);

    }

    // TODO: pad equation for calculator bar, eliminate spaces in button text
    public String pad(String equation) {
        return equation;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isDeg() {
        return deg;
    }

    public void setDeg(boolean deg) {
        this.deg = deg;
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
}
