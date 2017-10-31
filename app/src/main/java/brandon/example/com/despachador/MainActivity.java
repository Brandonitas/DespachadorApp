package brandon.example.com.despachador;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText editText, editText1, editText2, editText3;
    Button button, button2;
    CheckBox checkBox;
    TextView textView;
    public static int micros=2;
    public static int cuantum=3000;
    public static int tcc=15;
    public static int tb=15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        button=(Button) findViewById(R.id.button);
        button2=(Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty() && !editText1.getText()
                        .toString().isEmpty() && !editText2.getText()
                        .toString().isEmpty() && !editText3.getText().toString().isEmpty()) {
                    micros = Integer.parseInt(editText.getText().toString());
                    cuantum = Integer.parseInt(editText1.getText().toString());
                    tcc = Integer.parseInt(editText2.getText().toString());
                    tb = Integer.parseInt(editText3.getText().toString());

                    if (micros >= 1 && cuantum >= 1 && tcc >= 0 && tb >= 0) {
                        if (checkBox.isChecked()) {
                            Intent intent = new Intent(MainActivity.this, EditartxtActivity.class);
                            intent.putExtra("micros", micros);
                            intent.putExtra("cuantum", cuantum);
                            intent.putExtra("tcc", tcc);
                            intent.putExtra("tb", tb);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(MainActivity.this, TablaActivity.class);
                            intent.putExtra("micros", micros);
                            intent.putExtra("cuantum", cuantum);
                            intent.putExtra("tcc", tcc);
                            intent.putExtra("tb", tb);
                            intent.putExtra("modificado", false);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Ingresa numeros positivos mayores a 0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Debes ingresar todos los valores", Toast.LENGTH_SHORT).show();
                }

            }      
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("2");
                editText1.setText("3000");
                editText2.setText("15");
                editText3.setText("15");
            }
        });

    }



}
