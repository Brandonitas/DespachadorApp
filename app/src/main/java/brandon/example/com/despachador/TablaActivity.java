package brandon.example.com.despachador;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import static brandon.example.com.despachador.MainActivity.micros;

public class TablaActivity extends AppCompatActivity {

    int micros,cuantum,tcc,tb;
    boolean modificado;
    String texto;



    ArrayList<Microprocesador> microsP = new ArrayList<Microprocesador>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla);

        Bundle bundle = getIntent().getExtras();
        micros = bundle.getInt("micros");
        cuantum = bundle.getInt("cuantum");
        tcc = bundle.getInt("tcc");
        tb = bundle.getInt("tb");

        modificado = bundle.getBoolean("modificado");

    if(modificado){

        texto = bundle.getString("texto");
    }
        maketable();
    }

    private void maketable() {

        for (int i = 0; i < MainActivity.micros; i++) {
            microsP.add(new Microprocesador(cuantum));
        }

        Queue<Proceso> despachador = new LinkedList<Proceso>();
        ArrayList<Proceso> listprocesos = new ArrayList<Proceso>();

        if(!modificado) {
            InputStream is = this.getResources().openRawResource(R.raw.demo);
            Scanner s = new Scanner(is);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] proceso = line.split(",");
                Proceso p = new Proceso();
                p.setNombre(proceso[0]);
                p.setTiempoEjecucion(Integer.parseInt(proceso[1]));
                p.setNumeroBloqueo(Integer.parseInt(proceso[2]));
                p.setPrioridad(Integer.parseInt(proceso[3]));
                p.setTiempoListo(Integer.parseInt(proceso[4]));
                listprocesos.add(p);
            }
            s.close();

        }else{
            Log.e("MYLOG",texto);
            String[]procesos = texto.split(System.lineSeparator());
            boolean valido = true;
            for(String s:procesos){
                try {
                    String[] proceso = s.split(",");
                    Proceso p = new Proceso();
                    p.setNombre(proceso[0]);
                    p.setTiempoEjecucion(Integer.parseInt(proceso[1]));
                    p.setNumeroBloqueo(Integer.parseInt(proceso[2]));
                    p.setPrioridad(Integer.parseInt(proceso[3]));
                    p.setTiempoListo(Integer.parseInt(proceso[4]));
                    if(!(p.getTiempoEjecucion()>0&&p.getNumeroBloqueo()>=0&&p.getPrioridad()>=0&&p.getTiempoListo()>=0)){
                        valido = false;
                        break;
                    }
                    listprocesos.add(p);
                }catch (Exception e){
                    valido = false;
                    break;

                }

            }
            if(!valido) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Valores no aceptados, solo se mostrarán los aceptados ")
                        .setTitle("Error");
                builder.setPositiveButton("Regresar a editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(TablaActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();

                //Toast.makeText(this, "Valores no aceptados", Toast.LENGTH_LONG).show();

            }
        }

        Collections.sort(listprocesos);

        for(Proceso p : listprocesos){
            despachador.add(p);
        }

        while(!despachador.isEmpty()){
            Proceso p = despachador.poll();

            Microprocesador temp = microsP.get(0);

            boolean bandera=false;
            for(Microprocesador o:microsP){
                if(o.getTiempoTotal()==0){
                    bandera=true;
                    temp=o;
                    break;
                }
            }
            //Calcular tiempo muerto
            if(!bandera){
                for(Microprocesador m : microsP){
                    if(m.getTiempoTotal()<p.getTiempoListo()){
                        temp=m;
                        break;

                    }else if(m.getTiempoTotal()<temp.getTiempoTotal()){
                        temp=m;
                    }



                }


            }

            temp.agregarProceso(p);
        }


        for(Microprocesador m: microsP){
            Log.e("MYLOG", String.valueOf(m));
           //textView.append(String.valueOf(m));
        }
        init();
    }


    public void init() {

        for(int i=0;i<micros;i++) {
            Microprocesador temp = microsP.get(i);

            TableLayout stk = (TableLayout) findViewById(R.id.table);
            TableRow microtr = new TableRow(this);

            TableRow.LayoutParams rowSpanLayout = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            rowSpanLayout.span = 8;
            TextView tvmicro = new TextView(this);
            tvmicro.setText("Microprocesador: "+(i+1));
            tvmicro.setTextSize(20);
            tvmicro.setTextColor(Color.WHITE);
            tvmicro.setTypeface(null, Typeface.BOLD);


            microtr.addView(tvmicro,rowSpanLayout);
            stk.addView(microtr);



            TableRow tbrow0 = new TableRow(this);

            TextView tv0 = new TextView(this);
            tv0.setText(" Proceso ");
            tv0.setTextColor(Color.WHITE);
            tv0.setGravity(Gravity.CENTER);
            tv0.setTypeface(null, Typeface.BOLD);
            tbrow0.addView(tv0);

            TextView tv1 = new TextView(this);
            tv1.setText(" TCC ");
            tv1.setTextColor(Color.WHITE);
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setGravity(Gravity.CENTER);
            tbrow0.addView(tv1);

            TextView tv2 = new TextView(this);
            tv2.setText("  TE ");
            tv2.setTextColor(Color.WHITE);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setGravity(Gravity.CENTER);
            tbrow0.addView(tv2);

            TextView tv3 = new TextView(this);
            tv3.setText(" TVC ");
            tv3.setTextColor(Color.WHITE);
            tv3.setTypeface(null, Typeface.BOLD);
            tv3.setGravity(Gravity.CENTER);
            tbrow0.addView(tv3);

            TextView tv4 = new TextView(this);
            tv4.setText(" TB ");
            tv4.setTextColor(Color.WHITE);
            tv4.setTypeface(null, Typeface.BOLD);
            tv4.setGravity(Gravity.CENTER);
            tbrow0.addView(tv4);

            TextView tv5 = new TextView(this);
            tv5.setText(" TT ");
            tv5.setTextColor(Color.WHITE);
            tv5.setTypeface(null, Typeface.BOLD);
            tv5.setGravity(Gravity.CENTER);
            tbrow0.addView(tv5);

            TextView tv6 = new TextView(this);
            tv6.setText(" TI ");
            tv6.setTextColor(Color.WHITE);
            tv6.setTypeface(null, Typeface.BOLD);
            tv6.setGravity(Gravity.CENTER);
            tbrow0.addView(tv6);

            TextView tv7 = new TextView(this);
            tv7.setText(" TF ");
            tv7.setTextColor(Color.WHITE);
            tv7.setTypeface(null, Typeface.BOLD);
            tv7.setGravity(Gravity.CENTER);
            tbrow0.addView(tv7);


            stk.addView(tbrow0);


            for (Proceso p : temp.getListaProcesos()) {

                TableRow tbrow = new TableRow(this);
                TextView t1v = new TextView(this);
                t1v.setText(p.getNombre());
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                TextView t2v = new TextView(this);
                t2v.setText(p.getTiempoCambiodeContexto()+"");
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText(p.getTiempoEjecucion()+"");
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);

                TextView t4v = new TextView(this);
                t4v.setText(p.getTiempoVencimientoCuantum()+"");
                t4v.setTextColor(Color.WHITE);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);

                TextView t5v = new TextView(this);
                t5v.setText(p.getTiempoBloqueo()+"");
                t5v.setTextColor(Color.WHITE);
                t5v.setGravity(Gravity.CENTER);
                tbrow.addView(t5v);

                TextView t6v = new TextView(this);
                t6v.setText(p.getTiempoTotalProceso()+"");
                t6v.setTextColor(Color.WHITE);
                t6v.setGravity(Gravity.CENTER);
                tbrow.addView(t6v);

                TextView t7v = new TextView(this);
                t7v.setText(p.getTiempoInicial()+"");
                t7v.setTextColor(Color.WHITE);
                t7v.setGravity(Gravity.CENTER);
                tbrow.addView(t7v);

                TextView t8v = new TextView(this);
                t8v.setText(p.getTiempoFinal()+"");
                t8v.setTextColor(Color.WHITE);
                t8v.setGravity(Gravity.CENTER);
                tbrow.addView(t8v);

                stk.addView(tbrow);


            }

            TableRow espacio = new TableRow(this);
            TextView espacioview = new TextView(this);
            espacioview.setText(" ");
            espacio.addView(espacioview);
            stk.addView(espacio);
        }


    }






}
