/*
 * Copyright 2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Kelas ini diekstrak dari kelas aslinya disertai dg sedikit modifikasi dg tujuan untuk mempermudah penggunaan.
 * SI Dev, SI ITS 2017
 */

package sidev.lib.android.std._external

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
//import org.jetbrains.anko.AnkoException
//import org.jetbrains.anko.internals.AnkoInternals
import java.io.Serializable
import java.lang.Exception


object _AnkoInternals{

    fun Context.runOnUiThread(f: Context.() -> Unit) {
        if (Looper.getMainLooper() === Looper.myLooper()) f() else ContextHelper.handler.post { f() }
    }

    @JvmStatic
    fun <T> createIntent(ctx: Context?= null, clazz: Class<out T>?= null, params: Array<out Pair<String, Any?>>): Intent {
        val intent =
            if(ctx != null) Intent(ctx, clazz!!)
            else Intent()
        if (params.isNotEmpty()) fillIntentArguments(
            intent,
            params
        )
        return intent
    }

    @JvmStatic
    fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val value = it.second
            when (value) {
                null -> intent.putExtra(it.first, null as Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }
}