package sidev.lib.android.siframe.tool.util.log;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import sidev.lib.android.siframe.customizable._init._Config;
import sidev.lib.android.siframe.tool.util._EnvUtil;
import sidev.lib.universal.tool.util.TimeUtil;

public class LogHP {
    public final String FOLDER_LOG_APP;
    private Application app;
    private String alamatFile;
    private Activity aktifitas;
    private String namaApp;
    private String namaAktifitas= null;
    private String namaAktifitas_sebelumnya= "";
    private int perubahanNama= 0;
    private int kaliPrintError= 0;

    private File fileOutput;
    private String namaFileOutput;
    private int barisKe= 0;
    private boolean fileYgSama= false;

    public LogHP(Activity aktifitas){
        gantiAktifitas(aktifitas);
        FOLDER_LOG_APP= Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/Develop/LogHP/" +namaApp;
        alamatFile= FOLDER_LOG_APP;
        cekIjinPrint();
        cekFolder();
    }

    public LogHP(Application app){
        this.app= app;
        namaApp= app.getResources().getString(_Config.INSTANCE.getSTRING_APP_NAME()); //R.string.app_name);
        FOLDER_LOG_APP= _EnvUtil.INSTANCE.projectLogDir(app); //Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/Develop/LogHP/" +namaApp;
        alamatFile= FOLDER_LOG_APP;
//        cekIjinPrint();
        cekFolder();
    }

    public void gantiAktifitas(Activity akt){
        aktifitas= akt;
        app= aktifitas.getApplication();
        namaApp= aktifitas.getResources().getString(_Config.INSTANCE.getSTRING_APP_NAME()); //R.string.app_name
        namaAktifitas= aktifitas.getLocalClassName();
        if(namaAktifitas.contains(".")){
            String arrayStr[]= namaAktifitas.split("\\.");
            namaAktifitas= arrayStr[arrayStr.length-1];
        }
    }

    public void letakFolder(String subPath){
        if(!subPath.startsWith("/"))
            subPath= "/" +subPath;
        alamatFile= FOLDER_LOG_APP +subPath;
        cekFolder();
    }

    public void fileBaru(){
        barisKe= 0;
        fileYgSama= false;
    }

    private void cekFolder(){
        File dirFolder= new File(alamatFile);
        if(!dirFolder.exists())
            dirFolder.mkdirs();
    }
    private void cekFile(){
        if(!fileYgSama){
            String tglSkrg= TimeUtil.INSTANCE.simpleTimestamp("dd-MM-yyyy"); //.Companion.waktuSkrg("dd-MM-yyyy");//strTimestamp.split(" ")[0];
            int noKeluaran= 1;
            namaFileOutput= alamatFile +"/Log_" +namaApp /*+":" +namaAktifitas*/ +"_" +tglSkrg +"_" +noKeluaran +".txt";

            fileOutput= new File(namaFileOutput);
            while(fileOutput.exists()){
                fileOutput= new File(namaFileOutput= alamatFile +"/Log_" +namaApp /*+":" +namaAktifitas*/ +"_" +tglSkrg +"_" +(++noKeluaran) +".txt");
            }
            fileYgSama= true;
        }
    }
    //untuk cek ijin membuat log
    public boolean cekIjinLog(){
        return LogApp.log;
    }
    //untuk cek ijin menulis di penyimpanan
    private void cekIjinPrint() {
        boolean diijinkan=
                ActivityCompat.checkSelfPermission(app, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(app, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
        if (diijinkan) {
            ActivityCompat.requestPermissions(aktifitas,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        }
    }

    public void printError(Thread t, Throwable e){
        printError(t, e, 20);
    }
    public void printError(Thread t, Throwable e, int batasStack){
        if(!cekIjinLog()) return;
        cekFile();
        StackTraceElement stack[]= e.getStackTrace();

        try{
            FileWriter fw= new FileWriter(namaFileOutput, fileYgSama);
            PrintWriter penulisFile= new PrintWriter(fw);

            String namaThread= t.getName();
            String namaKelasPertama= stack[0].getClassName();
//PESAN==========
            String jenisErr= e.getClass().getSimpleName();
            String pesan= e.getMessage();
            String pesanLokal= e.getLocalizedMessage();
            int jmlStack= stack.length;
            penulisFile.println("----------" +namaThread +" - " +namaKelasPertama +" (" +(++kaliPrintError) +")" +"----------");
            penulisFile.println("----- Jenis= " +jenisErr);
            penulisFile.println("----- Pesan= " +pesan);
            penulisFile.println("----- PesanLokal= " +pesanLokal);
            penulisFile.println("----- Jml Stack= " +jmlStack);

            int batas= jmlStack < batasStack ? jmlStack : batasStack;
            for(int i= 0; i< batas; i++){
                StackTraceElement perStack= stack[i];
                Timestamp ts= new Timestamp(new Date().getTime());
                String strTimestamp= ts.toString();
                String strDiprint= (barisKe++) +" > " +strTimestamp +" ==> " +"\"" +"ERROR_" +namaThread +"\": " +"\"" +perStack.toString() +"\"";

                penulisFile.println(strDiprint);
            }
            if(batas < jmlStack)
                penulisFile.println("----- dan " +(jmlStack -batas) +" stack lainnya -----");


//KARENA==========
            Throwable eKarena= e.getCause();
            if(eKarena != null){
                StackTraceElement stackKarena[]= eKarena.getStackTrace();
                String karena= e.getCause().getMessage();
                penulisFile.println("----- Karena= " +karena);

                for(int i= 0; i< stackKarena.length; i++){
                    StackTraceElement perStack= stackKarena[i];
                    Timestamp ts= new Timestamp(new Date().getTime());
                    String strTimestamp= ts.toString();
                    String strDiprint= (barisKe++) +" > " +strTimestamp +" ==> " +"\"" +"ERROR_" +namaThread +"\": " +"\"" +perStack.toString() +"\"";

                    penulisFile.println(strDiprint);
                }
            }
            penulisFile.close();
            fileYgSama= true;
        } catch (Exception err){
            LogApp.e("LOG_HP", "ERROR", err);
        }
    }
    public void printLog(String tag, String pesan){
//        String namaApp= aktifitas.getResources().getString(R.string.app_name);
        if(!cekIjinLog()) return;
        cekFile();

        try{
            FileWriter fw= new FileWriter(namaFileOutput, fileYgSama);
            PrintWriter penulisFile= new PrintWriter(fw);

            Timestamp ts= new Timestamp(new Date().getTime());
            String strTimestamp= ts.toString();
            String strDiprint= (barisKe++) +" > " +strTimestamp +" ==> " +"\"" +tag.toUpperCase() +"\": " +"\"" +pesan +"\"";
            if(namaAktifitas != null)
                if(!namaAktifitas_sebelumnya.equals(namaAktifitas)){
                    penulisFile.println("----------" +namaAktifitas +" (" +(++perubahanNama) +")" +"----------");
                    namaAktifitas_sebelumnya= namaAktifitas;
                }
            penulisFile.println(strDiprint);
            penulisFile.close();
            fileYgSama= true;
        } catch (Exception e){}
    }
}
