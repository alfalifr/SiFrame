package com.sigudang.android.models

// ini kelas tagihan
class Billing(val idWarehouse : String, val warehouse : String, var totalBilling: Long, var totalVolume: Double, val products: ArrayList<BillingProduct>)