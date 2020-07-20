package sidev.lib.universal._cob.model

import id.go.surabaya.ediscont.models.CommKind
import id.go.surabaya.ediscont.models.CommUnit
import id.go.surabaya.ediscont.models.Commodity
import id.go.surabaya.ediscont.models.UserProfile
import sidev.lib.implementation._simulation.edc.model.Transaction


val dum_oprPasarComm= arrayOf(
    /* OprPasarCommModel("asaf", "Cabe", "Lombok", 10000, "Kg"),
     OprPasarCommModel("okas", "Lombok", "Cabe", 10000, "Mg")*/
    Commodity(
        "1",
        "Lombok",
        null,
        CommKind(
            "1",
            "Cabe"
        ),
        1000,
        CommUnit(
            "1",
            "Kg"
        ),
        10.0,
        null,
        null
    ),
    Commodity(
        "1",
        "Lombok",
        null,
        CommKind(
            "1",
            "Cabe"
        ),
        1000,
        CommUnit(
            "1",
            "Mg"
        ),
        10.0,
        null,
        null
    )
)

val dum_commKind= arrayOf(
    CommKind("1","Cabe"),
    CommKind("2","Beras"),
    CommKind("3","Minyak"),
    CommKind("4","Martabak")
)

val dum_commUnit= arrayOf(
    CommUnit("1", "kg"),
    CommUnit("2", "liter"),
    CommUnit("3", "pcs"),
    CommUnit("5", "gram"),
    CommUnit("6", "ml")
)

val dum_userProf= arrayOf(
    UserProfile("1", "Pak Banjo", "Tokel 1", "Jl in aja", "0812341234", "10-12", "Karangpilar", "Suci", null, "10019", "10012393"),
    UserProfile("2", "Bu Benowo", "Tokel 2", "Jl jalan kuy", "091312718491", "09-14", "Keputih", "Keputih", null, "231839190", "213819827496"),
    UserProfile("3", "Pak Marsinah", "Tokel C", "Jl uhuy", "08746648513", "10-20", "Gebang", "Keputih", null, "231839190", "213819827496"),
    UserProfile("4", "Pak Ijoh", "Koperasi A", "Jl erlangga", "083213181876", "11-12", "Manyar", "Tambakan", null, "231839190", "213819827496"),
    UserProfile("5", "Pak Royo", "Koperasi 2", "Jl Pegangsaan", "07128941742", "08-18", "Gundi", "Bulak", null, "231839190", "213819827496"),
    UserProfile("6", "Pakjito", "Distributor 1", "Jl Alon asal slamat", "08312413789", "07-17", "Burgundi", "Mambu", null, "231839190", "213819827496"),
    UserProfile("7", "Cece Bucin", "Distributor U", "Jl bareng kuy", "071123123123", null, "Burgundi", "Mambu", null, "231839190", "213819827496")
)


val dum_commod= arrayOf(
    Commodity("1", "Beras Tawon", dum_userProf[0], dum_commKind[1], 12000, dum_commUnit[0], 12.0, null, null),
    Commodity("2", "Beras Minyak", dum_userProf[3], dum_commKind[1], 12500, dum_commUnit[0], 21.0, null, null),
    Commodity("3", "Beras Topi Koki", dum_userProf[4], dum_commKind[1], 13570, dum_commUnit[0], 25.0, null, null),
    Commodity("4", "Beras Maknyus", dum_userProf[3], dum_commKind[1], 11500, dum_commUnit[0], 31.0, null, null),
    Commodity("5", "Beras Lele", dum_userProf[6], dum_commKind[1], 10000, dum_commUnit[0], 5.0, null, null),
    Commodity("6", "Beras Berapi", dum_userProf[6], dum_commKind[1], 9000, dum_commUnit[0], 5.0, null, null),
    Commodity("7", "Beras Beras", dum_userProf[5], dum_commKind[1], 19000, dum_commUnit[0], 51.0, null, null),
    Commodity("8", "Minyak Shania", dum_userProf[5], dum_commKind[2], 14000, dum_commUnit[1], 12.0, null, null),
    Commodity("9", "Minyak Trubus", dum_userProf[4], dum_commKind[2], 14560, dum_commUnit[1], 21.0, null, null),
    Commodity("10", "Minyak Banati", dum_userProf[3], dum_commKind[2], 14560, dum_commUnit[1], 21.0, null, null),
    Commodity("11", "Martabak kecut", dum_userProf[3], dum_commKind[3], 2670, dum_commUnit[4], 24.0, null, null),
    Commodity("12", "Martabak bom bali", dum_userProf[3], dum_commKind[3], 5670, dum_commUnit[4], 12.0, null, null)
)

val dum_transaction= arrayOf(
    Transaction("1", dum_commod[0], dum_commod[0].profile?.businessName, "10-09-2020", 3, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("2", dum_commod[2], dum_commod[2].profile?.businessName, "11-09-2020", 12, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("3", dum_commod[5], dum_commod[5].profile?.businessName, "11-09-2020", 7, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("4", dum_commod[3], dum_commod[3].profile?.businessName, "11-09-2020", 3, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_CANCEL_REQUESTED),
    Transaction("5", dum_commod[4], dum_commod[4].profile?.businessName, "12-09-2020", 5, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_CANCELED),
    Transaction("6", dum_commod[6], dum_commod[6].profile?.businessName, "13-09-2020", 2, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("7", dum_commod[7], dum_commod[7].profile?.businessName, "13-09-2020", 4, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("8", dum_commod[2], dum_commod[2].profile?.businessName, "14-09-2020", 6, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_CANCELED),
    Transaction("9", dum_commod[9], dum_commod[9].profile?.businessName, "15-09-2020", 3, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_REJECTED),
    Transaction("10", dum_commod[3], dum_commod[3].profile?.businessName, "15-09-2020", 6, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_UNCONFIRMED),
    Transaction("11", dum_commod[2], dum_commod[2].profile?.businessName, "16-09-2020", 3, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_CANCEL_REQUESTED),
    Transaction("12", dum_commod[11], dum_commod[11].profile?.businessName, "17-09-2020", 2, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_UNCONFIRMED),
    Transaction("13", dum_commod[9], dum_commod[9].profile?.businessName, "18-09-2020", 2, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_CANCELED),
    Transaction("14", dum_commod[1], dum_commod[1].profile?.businessName, "18-09-2020", 12, Edc_Const.TYPE_TRANSACTION_SELL, Edc_Const.STATUS_TRANSACTION_APPROVED),
    Transaction("15", dum_commod[8], dum_commod[8].profile?.businessName, "19-09-2020", 16, Edc_Const.TYPE_TRANSACTION_BUY, Edc_Const.STATUS_TRANSACTION_UNCONFIRMED)
)

/*
val dum_oprPasarEvent= arrayOf(
    OprPasarEventModel("a", ""),
    OprPasarEventModel("b", ""),
    OprPasarEventModel("c", "")
)

val KG= "kg"
val LT= "lt"

val dum_commUnit= arrayOf(
    CommUnit__tahap2("unit1", "3 kg", KG, 3.0),
    CommUnit__tahap2("unit2", "5 kg", KG, 5.0, isDeleted = DeletionModel(fromAdmin = true)),
    CommUnit__tahap2("unit3", "10 kg", KG, 10.0),
    CommUnit__tahap2("unit4", "2 lt", LT, 2.0),
    CommUnit__tahap2("unit5", "5 lt", LT, 5.0),
    CommUnit__tahap2("unit6", "7 lt", LT, 7.0),
    CommUnit__tahap2("unit7", "15 kg", KG, 15.0),
    CommUnit__tahap2("unit8", "20 kg", KG, 20.0)
)

val dum_commKind= arrayOf(
    CommKind__tahap2("kind1", "Beras", FK_M(arrayOf("unit1", "unit2", "unit3", "unit7", "unit8"))),
    CommKind__tahap2("kind2", "Gula", FK_M(arrayOf("unit1", "unit2"))),
    CommKind__tahap2("kind3", "Tepung", FK_M(arrayOf("unit1"))),
    CommKind__tahap2("kind4", "Minyak", FK_M(arrayOf("unit4", "unit5"))),
    CommKind__tahap2("kind5", "Air", FK_M(arrayOf("unit4", "unit5", "unit6")))
)

val dum_comm= arrayOf(
    Commodity__tahap2("com1", "Beras Lele", FK_M(arrayOf("kind1"))),
    Commodity__tahap2("com2", "Beras Tawon", FK_M(arrayOf("kind1")), isDeleted = DeletionModel(fromAdmin = true)),
    Commodity__tahap2("com3", "Beras Pandan Wangi", FK_M(arrayOf("kind1"))),
    Commodity__tahap2("com4", "Beras Raja", FK_M(arrayOf("kind1"))),
    Commodity__tahap2("com5", "Beras Pinpin", FK_M(arrayOf("kind1"))),
    Commodity__tahap2("com6", "Gulaku", FK_M(arrayOf("kind2"))),
    Commodity__tahap2("com7", "Tropicana Slim", FK_M(arrayOf("kind2"))),
    Commodity__tahap2("com8", "Gula Li", FK_M(arrayOf("kind2"))),
    Commodity__tahap2("com9", "Gula Gulana", FK_M(arrayOf("kind2"))),
    Commodity__tahap2("com10", "Tepung Beras Rosebrand", FK_M(arrayOf("kind3"))),
    Commodity__tahap2("com11", "Tepung Segitiga", FK_M(arrayOf("kind3"))),
    Commodity__tahap2("com12", "Tepung Bogasari", FK_M(arrayOf("kind3"))),
    Commodity__tahap2("com13", "Tepung Cakra", FK_M(arrayOf("kind3"))),
    Commodity__tahap2("com14", "Tepung Tanpa Merk", FK_M(arrayOf("kind3"))),
    Commodity__tahap2("com15", "Bimoli", FK_M(arrayOf("kind4"))),
    Commodity__tahap2("com16", "Shania", FK_M(arrayOf("kind4"))),
    Commodity__tahap2("com17", "Minyak Ketengan", FK_M(arrayOf("kind4"))),
    Commodity__tahap2("com18", "Sanmol", FK_M(arrayOf("kind4"))),
    Commodity__tahap2("com20", "Aqua", FK_M(arrayOf("kind5"))),
    Commodity__tahap2("com21", "Club", FK_M(arrayOf("kind5"))),
    Commodity__tahap2("com22", "SWA", FK_M(arrayOf("kind5"))),
    Commodity__tahap2("com23", "Santri", FK_M(arrayOf("kind5")))
)

val dum_commUser= arrayOf(
    CommodityUser__tahap2("cu1", FK_M(arrayOf("com1")), FK_M(arrayOf("unit1")), null, 12, 12000),
    CommodityUser__tahap2("cu2", FK_M(arrayOf("com1")), FK_M(arrayOf("unit2")), null, 21, 15000),
    CommodityUser__tahap2("cu3", FK_M(arrayOf("com1")), FK_M(arrayOf("unit3")), null, 0, 10000),
//    CommodityUser__tahap2("cu4", FK_M(arrayOf("com1")), FK_M(arrayOf("unit7")), null, 14, 10000),
    CommodityUser__tahap2("cu5", FK_M(arrayOf("com1")), FK_M(arrayOf("unit8")), null, 23, 30000),
    CommodityUser__tahap2("cu9", FK_M(arrayOf("com2")), FK_M(arrayOf("unit2")), null, 14, 35000),
    CommodityUser__tahap2("cu10", FK_M(arrayOf("com3")), FK_M(arrayOf("unit7")), null, 33, 14000),
    CommodityUser__tahap2("cu12", FK_M(arrayOf("com7")), FK_M(arrayOf("unit2")), null, 11, 4000),
    CommodityUser__tahap2("cu11", FK_M(arrayOf("com2")), FK_M(arrayOf("unit1")), null, 15, 16000),
    CommodityUser__tahap2("cu13", FK_M(arrayOf("com2")), FK_M(arrayOf("unit8")), null, 10, 12000),
    CommodityUser__tahap2("cu14", FK_M(arrayOf("com20")), FK_M(arrayOf("unit5")), null, 8, 7000)
)



val dum_id1= "{'${ModelTransf.COMMUSER_ID}' : 'cu1'," +
        "'${ModelTransf.COMMUSER_COMM}' : {'${ModelTransf.COMM_ID}' : 'com1', '${ModelTransf.COMM_NAME}' : 'Beras Lele'," +
        "'${ModelTransf.COMM_KIND}' : {'${ModelTransf.KIND_ID}' : 'kind1, '${ModelTransf.KIND_NAME}' : 'Beras'," +
        "'${ModelTransf.KIND_UNIT_LIST}' : [{'${ModelTransf.UNIT_ID}' : 'unit1', '${ModelTransf.UNIT_NAME}' : '3 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 3}," +
        "{'${ModelTransf.UNIT_ID}' : 'unit2', '${ModelTransf.UNIT_NAME}' : '5 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 5}," +
        "{'${ModelTransf.UNIT_ID}' : 'unit3', '${ModelTransf.UNIT_NAME}' : '10 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 10}," +
        "{'${ModelTransf.UNIT_ID}' : 'unit7', '${ModelTransf.UNIT_NAME}' : '15 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 15}," +
        "{'${ModelTransf.UNIT_ID}' : 'unit8', '${ModelTransf.UNIT_NAME}' : '20 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 20}]" +
        "}" +
        "}," +
        "'${ModelTransf.COMMUSER_UNIT}' : {'${ModelTransf.UNIT_ID}' : 'unit1', '${ModelTransf.UNIT_NAME}' : '3 kg', '${ModelTransf.UNIT_NAME_BASE}' : '$KG', '${ModelTransf.UNIT_CONVERSION}' : 3}" +
        ", null, " +
        "'${ModelTransf.COMMUSER_PRICE}' : 12," +
        "'${ModelTransf.COMMUSER_STOCK}' : 12000}"


 */