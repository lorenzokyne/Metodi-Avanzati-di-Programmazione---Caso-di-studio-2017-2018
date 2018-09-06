package com.example.utente.Client;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe che modella l'interfaccia per il caricamento dei dati salvati.
 * @author Lorenzo
 */
public class LoadFile extends Fragment {
    /**
     * EditText per la comunicazione con l'utente.
     */
    private EditText filename, logTab;
    /**
     * Stream con le richieste da inviare al server.
     */
    private ObjectOutputStream out;
    /**
     * Stream con le risposte ricevute dal server.
     */
    private ObjectInputStream in;
    /**
     * Socket di connessione con il server.
     */
    private static Socket s;
    /**
     * Attributo contenente i risultati dell'interrogazione al server.
      */
    private static String result="";

    /**
     * Chiamato per avere la user interface istanziata per questo fragment.
     * @param inflater Oggetto di tipo LayoutInflater che può essere usato per riempire le viste nel fragment.
     * @param container Se non-null è il contenitore padre in cui si trova il fragment.
     * @param savedInstanceState Se non-null questo fragment viene ricostruito da uno stato salvato precedente, cioè questo.
     * @return La View per l'interfaccia utente del fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.frag2_layout, container, false);
        operations(view);
        return view;
    }

    /**
     * Metodo che gestisce la connessione al server ed eventualmente mostra all'utente la risposta.
     * @param view La view da cui si gestiscono le parti grafiche (es. bottoni ecc.).
     */
    public void operations(View view) {
        /**
         * Pulsante da premere per inviare la richiesta.
         */
        Button b = (Button) view.findViewById(R.id.button2_store);

        /**
         * Campo in cui si inserisce il nome del file.
         */
        filename = (EditText) view.findViewById(R.id.load_filename);

        /**
         * Il campo in cui verrà visualizzata la risposta.
         */
        logTab = (EditText) view.findViewById(R.id.load_logTab);

        logTab.setTag(logTab.getKeyListener());
        logTab.setKeyListener(null);
        logTab.setKeyListener((KeyListener) logTab.getTag());
        b.setOnClickListener(new View.OnClickListener() {


            /**
             * Ascoltatore del pulsante, evento che si verifica quando il pulsante viene premuto.
             * @param view La view da cui si gestiscono le parti grafiche (es. bottoni ecc.).
             */
            @Override
            public void onClick(final View view) {
                Thread t=new ThrClick();
                result="";
                t.start();
                try {
                    Thread.sleep(2000);
                    if(!result.equals(""))
                        logTab.setText(result);
                    else
                        mostraPopup("File non trovato!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Metodo che gestisce il caricamento dei dati salvati all'interno di un file
     * nel server.
     *
     * @return Restituisce una stringa corrispondente all'informazione ricevuta dal server.
     * @throws IOException
     *             in presenza di errori nell'invio/ricezione dei dati al server.
     * @throws ClassNotFoundException
     *             in caso si riceva un Object di tipo sconosciuto.
     */

    private String learningFromFileAction() throws IOException, ClassNotFoundException {
        String nomeTab = filename.getText().toString();
        try {
            out.writeObject(3);
            Thread.sleep(100);
            out.writeObject(nomeTab);
            String answer = in.readObject().toString();
            if (!answer.equals("OK")) {
                return "";
            } else {
                answer = in.readObject().toString();
               result=answer;
            }

        } catch (NumberFormatException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Classe che modella il thread su cui avverrà la connessione al server e la trasmissione dei dati.
     */
    class ThrClick extends Thread {
        /**
         * Override del metodo run della classe Thread, stabilisce la connessione al server ed effettua eventualmente la richiesta.
         */
        public void run(){
            try {
                InetAddress addr = InetAddress.getByName(MainActivity.ip);
                s = new Socket(addr, MainActivity.port);
                out=new ObjectOutputStream(s.getOutputStream());
                in=new ObjectInputStream(s.getInputStream());
                result=learningFromFileAction();
                s.close();
            } catch (IOException e) {
                mostraPopup("Connessione al server fallita");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(s!=null)
                     s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Mostra un popup con il messaggio di errore specificato.
     * @param errText Il messaggio che si vuole mostrare.
     */
    public void mostraPopup(String errText) {
        PopClass p=new PopClass(errText);
        p.show(getFragmentManager(),"example");
    }
}