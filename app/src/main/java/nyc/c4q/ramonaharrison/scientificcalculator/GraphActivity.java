package nyc.c4q.ramonaharrison.scientificcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class GraphActivity extends ActionBarActivity {

    private GraphView graph;
    private EditText inputEquation;
    private EditText inputMin;
    private EditText inputMax;
    private Button graphIt;
    private HashMap<Float, Float> coordinates;
    private String equation;
    private Float min;
    private Float max;
    private static Display display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO save instance state

        super.onCreate(savedInstanceState);
        display = getWindowManager().getDefaultDisplay();
        setContentView(R.layout.activity_graph);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.show();
        }

        graph = (GraphView) findViewById(R.id.graph);
        inputEquation = (EditText) findViewById(R.id.input_equation);
        inputMin = (EditText) findViewById(R.id.input_min);
        inputMax = (EditText) findViewById(R.id.input_max);
        graphIt = (Button) findViewById(R.id.graph_it);


        graphIt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                equation = inputEquation.getText().toString();
                min = Float.valueOf(inputMin.getText().toString());
                max = Float.valueOf(inputMax.getText().toString());
                coordinates = new HashMap<>();

                for (float x = min; x <= max; x++) {
                    float y = findY(equation, x);
                    coordinates.put(x, y);
                }

                graph.setValues(coordinates, min, max);
                graph.drawGraph();

            }
        });

    }

    public float findY(String equation, float x) {
        Float y = Float.valueOf(0);
        String xEquation = equation.replaceAll("[x]", "" + x);;
        String yString= Calculate.calculate(xEquation, false);
        if (isValue(yString)) {
            y = Float.valueOf(yString);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid equation!", Toast.LENGTH_SHORT).show();
            // TODO: get toast out of loop
        }
        return y;
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

    public static int getWidth() {
        return display.getWidth();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_calculator:
                Intent calcIntent = new Intent(this, MainActivity.class);
                this.startActivity(calcIntent);
                return true;
            case R.id.action_graph:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
