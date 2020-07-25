package com.sigudang.android.Model

import com.sigudang.android.models.AccountAddress
import java.io.Serializable

class Account (val name: String, val addresses: ArrayList<AccountAddress>, val email: String) : Serializable