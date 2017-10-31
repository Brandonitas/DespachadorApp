package brandon.example.com.despachador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.util.Scanner;


public class EditartxtActivity extends AppCompatActivity {
    EditText editText;
    Button button;

    int micros,cuantum,tcc,tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editartxt);
        editText = (EditText) findViewById(R.id.editText4);

        Bundle bundle = getIntent().getExtras();
        micros = bundle.getInt("micros");
        cuantum = bundle.getInt("cuantum");
        tcc = bundle.getInt("tcc");
        tb = bundle.getInt("tb");


        InputStream is = this.getResources().openRawResource(R.raw.demo);
        Scanner s = new Scanner(is);
        while (s.hasNextLine()) {
            String line = s.nextLine();

            editText.append(line+System.lineSeparator());




        }
        s.close();

        button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditartxtActivity.this,TablaActivity.class);
                intent.putExtra("texto", editText.getText().toString());
                intent.putExtra("modificado",true);
                intent.putExtra("micros", micros);
                intent.putExtra("cuantum", cuantum);
                intent.putExtra("tcc", tcc);
                intent.putExtra("tb", tb);
                startActivity(intent);

            }
        });
    }
}
