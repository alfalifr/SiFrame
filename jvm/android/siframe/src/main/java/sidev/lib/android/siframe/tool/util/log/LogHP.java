package sidev.lib.android.siframe.tool.util.log;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Environment;
//import kotlin.Throwable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import sidev.lib.android.std.val._Config;
import sidev.lib.android.std.tool.util._EnvUtil;
import sidev.lib.jvm.tool.util.TimeUtil;

public class LogHP {
    private Application app;
    @NonNull
    private final String FOLDER_LOG_APP;
    @NonNull
    private String rootFolder;
    @NonNull
    private String subPath = "";
    @NonNull
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

    public LogHP(@NonNull Activity aktifitas){
        gantiAktifitas(aktifitas);
        FOLDER_LOG_APP= Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/Develop/LogHP/" +namaApp;
        rootFolder = FOLDER_LOG_APP;
        alamatFile= rootFolder;
        cekIjinPrint();
        cekFolder();
    }

    public LogHP(@NonNull Application app){
        this.app= app;
        namaApp= app.getResources().getString(_Config.INSTANCE.getSTRING_APP_NAME_RES()); //R.string.app_name);
        FOLDER_LOG_APP= _EnvUtil.INSTANCE.projectLogDir(app, true); //Environment.getExternalStorageDirectory().getAbsolutePath() +"/Android/Develop/LogHP/" +namaApp;
        rootFolder = FOLDER_LOG_APP;
        alamatFile= rootFolder;
//        cekIjinPrint();
        cekFolder();
    }

    public void gantiAktifitas(@NonNull Activity akt){
        aktifitas= akt;
        app= aktifitas.getApplication();
        namaApp= aktifitas.getResources().getString(_Config.INSTANCE.getSTRING_APP_NAME_RES()); //R.string.app_name
        namaAktifitas= aktifitas.getLocalClassName();
        if(namaAktifitas.contains(".")){
            String arrayStr[]= namaAktifitas.split("\\.");
            namaAktifitas= arrayStr[arrayStr.length-1];
        }
    }

    public void letakFolder(@NonNull String subPath){
        if(!subPath.startsWith("/"))
            subPath= "/" +subPath;
        alamatFile= rootFolder +subPath;
        this.subPath= subPath;
        cekFolder();
    }

    public void folderRoot(@NonNull String path){
        rootFolder = path;
        alamatFile= rootFolder +subPath;
        cekFolder();
    }

    public void fileBaru(){
        barisKe= 0;
        fileYgSama= false;
    }

    @NonNull
    public String alamatFile(){
        return alamatFile;
    }

    @NonNull
    public String getRootFolder(){
        return rootFolder;
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

    public void printError(@NonNull Thread t, @NonNull Throwable e){
        printError(t, e, 20);
    }
    public void printError(@NonNull Thread t, @NonNull Throwable e, int batasStack){
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

            int batas= Math.min(jmlStack, batasStack);
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
            ArrayList<Throwable> writtenT = new ArrayList<>();
            writtenT.add(e);
            Throwable eKarena= e.getCause();
            int causeInt= 0;

            while(eKarena != null){
                if(writtenT.contains(eKarena)) break; //Agar gak cyclic reference sehingga infinite loop.
                writtenT.add(eKarena);
                causeInt++;

                StackTraceElement stackKarena[]= eKarena.getStackTrace();
                String karena= eKarena.getMessage();

                jmlStack= stackKarena.length;
                batas= Math.min(jmlStack, batasStack);

                penulisFile.println();
                penulisFile.println("----- Karena (" +causeInt + ")");
                penulisFile.println("----- Jenis= " +eKarena.getClass().getSimpleName());
                penulisFile.println("----- Pesan= " +karena);
                penulisFile.println("----- Jml Stack= " +jmlStack);

                for(int i= 0; i< batas; i++){
                    StackTraceElement perStack= stackKarena[i];
                    Timestamp ts= new Timestamp(new Date().getTime());
                    String strTimestamp= ts.toString();
                    String strDiprint= (barisKe++) +" > " +strTimestamp +" ==> " +"\"" +"ERROR_" +namaThread +"\": " +"\"" +perStack.toString() +"\"";

                    penulisFile.println(strDiprint);
                }
                if(batas < jmlStack)
                    penulisFile.println("----- dan " +(jmlStack -batas) +" stack lainnya -----");
                eKarena= eKarena.getCause();
            }
            penulisFile.close();
            fileYgSama= true;
        } catch (Exception err){
            LogApp.e("LOG_HP", "ERROR", err);
        }
    }
    public void printLog(@NonNull String tag, @NonNull String pesan){
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
