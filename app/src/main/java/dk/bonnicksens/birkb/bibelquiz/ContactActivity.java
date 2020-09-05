package dk.bonnicksens.birkb.bibelquiz;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class ContactActivity extends Activity /*AppCompatActivity*/ {

    // Views
    Button buttonMCQ;
    Button buttonTFQ;
    Button buttonMAQ;
    Button buttonSQ;
    Button buttonContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Set views
        buttonMCQ = (Button) findViewById(R.id.buttonMCQ);
        buttonTFQ = (Button) findViewById(R.id.buttonTFQ);
        buttonMAQ = (Button) findViewById(R.id.buttonMAQ);
        buttonSQ = (Button) findViewById(R.id.buttonSQ);
        buttonContact = (Button) findViewById(R.id.buttonContact);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Klikker på MCQ
        buttonMCQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Åbn email app og forududfyld felter
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"birkbonnicksen2@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Spørgsmål til bibelquiz");
                i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                        "Jeg / vi har et forslag til et spørgsmål med flere svarmuligheder og kun et rigtigt svar.\n\n" +
                        "OBS.\n" +
                        "Angiv spørgsmål samt mellem ca. 2 - 4 svarmuligheder.\n" +
                        "Udfyld også meget gerne ekstra information til det rigtige svar. Meget gerne henvisning til f.eks. bibelen eller Indsigt.\n\n" +
                        "Spørgsmål: [skriv her]\n" +
                        "Sandt svar: [skriv her]\n" +
                        "Falsk svar: [skriv her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Information omkring svar: [skriv her]\n\n" +
                        "Venlig hilsen\n" +
                        "[skriv her]\n");
                try {
                    startActivity(Intent.createChooser(i, "Send e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Klikker på TFQ
        buttonTFQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Åbn email app og forududfyld felter
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"birkbonnicksen2@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Spørgsmål til bibelquiz");
                i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                        "Jeg / vi har et forslag til et spørgsmål med to svarmuligheder, sandt eller falsk.\n\n" +
                        "OBS.\n" +
                        "Udfyld meget gerne ekstra information til det rigtige svar. Meget gerne henvisning til f.eks. bibelen eller Indsigt.\n\n" +
                        "Spørgsmål: [skriv her]\n" +
                        "Sandt svar: [skriv her]\n" +
                        "Falsk svar: [skriv her]\n" +
                        "Information omkring svar: [skriv her]\n\n" +
                        "Venlig hilsen\n" +
                        "[skriv her]\n");
                try {
                    startActivity(Intent.createChooser(i, "Send e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sæt MAQ
        buttonMAQ.setBackgroundResource(R.drawable.selector_button2);

        // Klikker på MAQ
        buttonMAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Åbn email app og forududfyld felter
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"birkbonnicksen2@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Spørgsmål til bibelquiz");
                i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                        "Jeg / vi har et forslag til et spørgsmål med mange svarmuligheder og flere rigtige svar.\n\n" +
                        "OBS.\n" +
                        "Angiv spørgsmål samt mellem ca. 2 - 6 rigtige svarmuligheder og ca. 2 - 6 falske svarmuligheder.\n" +
                        "Udfyld også meget gerne ekstra information til det rigtige svar. Meget gerne henvisning til f.eks. bibelen eller Indsigt.\n\n" +
                        "Spørgsmål: [skriv her]\n" +
                        "Sandt svar: [skriv her]\n" +
                        "Sandt svar: [skriv her]\n" +
                        "Sandt svar: [skriv evt. her]\n" +
                        "Sandt svar: [skriv evt. her]\n" +
                        "Sandt svar: [skriv evt. her]\n" +
                        "Sandt svar: [skriv evt. her]\n" +
                        "Falsk svar: [skriv her]\n" +
                        "Falsk svar: [skriv her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Falsk svar: [skriv evt. her]\n" +
                        "Information omkring svar: [skriv her]\n\n" +
                        "Venlig hilsen\n" +
                        "[skriv her]\n");
                try {
                    startActivity(Intent.createChooser(i, "Send e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sæt SQ
        buttonSQ.setBackgroundResource(R.drawable.selector_sequencebutton);

        // Klikker på SQ
        buttonSQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Åbn email app og forududfyld felter
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"birkbonnicksen2@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Spørgsmål til bibelquiz");
                i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                        "Jeg / vi har et forslag til et rækkefølge spørgsmål.\n\n" +
                        "OBS.\n" +
                        "Angiv spørgsmål samt en rækkefølge på mellem ca. 3 - 12 svarmuligheder.\n" +
                        "Udfyld også meget gerne ekstra information til det rigtige svar. Meget gerne henvisning til f.eks. bibelen eller Indsigt.\n\n" +
                        "Spørgsmål: [skriv her]\n" +
                        "1: [skriv her]\n" +
                        "2: [skriv her]\n" +
                        "3: [skriv her]\n" +
                        "4: [skriv evt. her]\n" +
                        "5: [skriv evt. her]\n" +
                        "6: [skriv evt. her]\n" +
                        "7: [skriv evt. her]\n" +
                        "8: [skriv evt. her]\n" +
                        "9: [skriv evt. her]\n" +
                        "10: [skriv evt. her]\n" +
                        "11: [skriv evt. her]\n" +
                        "12: [skriv evt. her]\n" +
                        "Information omkring svar: [skriv her]\n\n" +
                        "Venlig hilsen\n" +
                        "[skriv her]\n");
                try {
                    startActivity(Intent.createChooser(i, "Send e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Klikker på Contact
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Åbn email app og forududfyld felter
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"birkbonnicksen2@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Spørgsmål til bibelquiz");
                i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                        "Jeg / vi har forslag til rettelser, kommentarer, ris eller ros til quizzen.\n\n" +
                        "[skriv her]\n\n" +
                        "Venlig hilsen\n" +
                        "[skriv her]\n");
                try {
                    startActivity(Intent.createChooser(i, "Send e-mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        // Åbn email app og forududfyld felter
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"birkbonnicksen2@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Ang. Bibelquiz");
        i.putExtra(Intent.EXTRA_TEXT, "Hej,\n\n" +
                "Jeg / vi har et forslag til et spørgsmål.\n" +
                "Spørgsmål: [skriv her]\n" +
                "Sandt svar: [skriv her]\n" +
                "Falsk svar: [skriv her]\n" +
                "Falsk svar: [skriv evt. her]\n" +
                "Falsk svar: [skriv evt. her]\n" +
                "Information omkring svar: [skriv her]\n\n" +
                "OBS.\n" +
                "Angiv spørgsmål samt mellem 2 - 4 svarmuligheder.\n" +
                "Udfyld også meget gerne ekstra information til det rigtige svar. Meget gerne henvisning til f.eks. bibelen eller Indsigt.\n" +
                "Du / I må meget gerne sende flere forslag på en gang.\n\n" +
                "Ris, ros, forslag til rettelser eller kommentarer kan angives her under:\n" +
                "[skriv evt. her]\n\n\n" +
                "Venlig hilsen\n" +
                "[skriv her]\n");
        try {
            startActivity(Intent.createChooser(i, "Send e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.title_button_contact_noEmailClient, Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                // Man klikker på tilbage knappen

                // Finish activity
                finish();

                // Gå til hovedmenu
                Intent intent = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent);

                // Fin transition
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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
    */
}
