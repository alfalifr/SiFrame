package sidev.lib.implementation._cob;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import kotlin.jvm.JvmClassMappingKt;
import sidev.lib.android.siframe.adapter.RvAdp;
import sidev.lib.android.siframe.arch.intent_state.ViewIntent;
import sidev.lib.implementation.intent_state.ContentFragConf;
import sidev.lib.implementation.viewmodel.ContentVm;
import sidev.lib.reflex._DefaultValueFunKt;
//import sidev.lib.universal.fun.KReflexFunKt;
//import sidev.lib.universal.fun.KReflexFunKt;

public class Cob {
    public static void main(String[] args) {
        HashMap<String, Integer> m= new HashMap<String, Integer>();
        m.put("P", 1);
        m.put("A", 2);
        m.put("Z", 3);

        Set<String> set= new LinkedHashSet<>();
        set.add("P");
        set.add("A");
        set.add("Z");

        set.remove("P");
        set.add("P");

        m.remove("P");
//        m.put("P", 4);

        String[] keys= new String[m.size()];
        Integer[] vals= new Integer[m.size()];

        m.keySet().toArray(keys);
        m.values().toArray(vals);

        String[] aset= new String[set.size()];
        set.toArray(aset);
        ContentFragConf.Intent.DownloadData.INSTANCE.isResultTemporary();
        new ViewIntent(){

        };

        for(int i= 0; i< m.size(); i++){
            System.out.println("key= " +keys[i] +" val= " +vals[i]);
        }
        for(int i= 0; i< set.size(); i++){
            System.out.println("set= " +aset[i]);
        }

        ContentVm vm= new ContentVm(null);

        RvAdp<String, LinearLayoutManager> adp= new RvAdp(null, null){
            @Override
            public int getItemLayoutId() {
                return 0;
            }

            @Override
            public void bindVH(@NotNull SimpleViewHolder vh, int pos, Object data) {

            }

            @NotNull
            @Override
            public RecyclerView.LayoutManager setupLayoutManager(Context ctx) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }
        };

//        for(Class cls : Z.class)
//        System.out.println("");
        int defa= _DefaultValueFunKt.defaultPrimitiveValue(JvmClassMappingKt.getKotlinClass(int.class)); //KReflexFunKt.defaultPrimitiveValue(KReflexFunKt.getKClass(int.class));
        System.out.println("default int= " +defa);
    }
}
/*
class B extends A{

}
class A{
    String a;
}

interface S{}
interface Y extends S{}
interface X{}
interface Z extends S, X{}
// */

enum A{
    AA
}