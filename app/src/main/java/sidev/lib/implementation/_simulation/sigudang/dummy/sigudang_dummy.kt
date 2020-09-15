package sidev.lib.implementation._simulation.sigudang.dummy

import com.sigudang.android.Model.Product
import com.sigudang.android.Model.Warehouse
import com.sigudang.android.models.*
import com.sigudang.android.utilities.constant.Constants
import sidev.lib.collection.toArrayList
import sidev.lib.implementation.R
import sidev.lib.implementation._simulation.sigudang.models.PictModel

//try{}catch(e: Exception){}

val ROLE_TYPE= Constants.STATE_ROLE_LESSEE

val BOUND_STATUS= arrayOf(
    "Pending", "Proses", "Sukses", "Semua"
)

enum class BoundStatus{
    Pending, Proses, Sukses, Semua
}

enum class BoundKind{
    INBOUND, OUTBOUND, CROSSDOCKING
}

enum class PackagingEnum{
    PLASTIK, BUBBLE_WRAP, KERDUS
}

enum class SendMethod{
    REGULAR, INSTANT, JOS
}

enum class CourierType{
    PERSONAL, THIRD_PARTY
}


val warehouseList_full= arrayOf(
    Warehouse("Gudang Aryatama", "Surabaya, Indonesia", 12000, 1.4, 3.9, 300.0, 200.0),
    Warehouse("Gudang Pak Bejo", "Ndarjo, Indonesia", 10000, 1.4, 4.9, 400.0, 210.0),
    Warehouse("Gudang Cak Tul", "Ngoseng, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Cak Bro", "Ok, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Si", "Boyolali, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Ndang", "Ngoseng, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang an", "Ok, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Lah", "Boyolali, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Ntabu", "Ok, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0),
    Warehouse("Gudang Ku", "Boyolali, Indonesia", 1000, 1.4, 1.9, 1000.0, 20.0)
)

val ub_warehouseList_full= arrayOf(
    UserBusiness(1, "Gudang Aryatama"),
    UserBusiness(2, "Gudang Pak Bejo"),
    UserBusiness(3, "Gudang Cak Tul"),
    UserBusiness(4, "Gudang Cak Bro"),
    UserBusiness(5, "Gudang Si"),
    UserBusiness(6, "Gudang Ndang"),
    UserBusiness(7, "Gudang an"),
    UserBusiness(8, "Gudang Lah"),
    UserBusiness(9, "Gudang Ntabu"),
    UserBusiness(10, "Gudang Ku")
)

val ub_lesseeList_full= arrayOf(
    UserBusiness(1, "Pak Jo"),
    UserBusiness(2, "Pak Lan"),
    UserBusiness(3, "Pak Gun"),
    UserBusiness(4, "Pak Wo"),
    UserBusiness(5, "Pak No"),
    UserBusiness(6, "Bu Ndar"),
    UserBusiness(7, "Bu Let"),
    UserBusiness(8, "Mas Sak"),
    UserBusiness(9, "Mas Kur"),
    UserBusiness(10, "Mbak Yu")
)

val containerModelList_1= arrayOf(
    BoundContainerModel("id1", "Gudang Garam", 1),
    BoundContainerModel("id2", "Container Kamar", 1),
    BoundContainerModel("id3", "Container Kotak", 2)
)
val containerModelList_2= arrayOf(
    BoundContainerModel("id4", "Container Bulet", 3),
    BoundContainerModel("id5", "Container Silang", 4)
)
val containerModelList_3= arrayOf(
    BoundContainerModel("id6", "Container Segitiga", 3)
)
val containerModelList_4= arrayOf(
    BoundContainerModel("id7", "Container Ku", 6)
)
val containerModelList_full= arrayOf(
    *containerModelList_1, *containerModelList_2, *containerModelList_3, *containerModelList_4
)


val sendKindModel_list_thirdParty= arrayOf(
    SendMethodModel("Gosend", PictModel(resId = R.drawable.logo_gosend)),
    SendMethodModel("SiCepat", PictModel(resId = R.drawable.logo_sicepat)),
    SendMethodModel("JNE", PictModel(resId = R.drawable.logo_jne)),
    SendMethodModel("J&T", PictModel(resId = R.drawable.logo_jnt)),
    SendMethodModel("Pos Indonesia", PictModel(resId = R.drawable.logo_pos_indonesia)),
    SendMethodModel("Anteraja", PictModel(resId = R.drawable.logo_anteraja))
)


val sendKindModel_personal=  SendMethodModel("(Pribadi)", null, null, CourierType.PERSONAL)

val sendKindModel_list_full= arrayOf(
    sendKindModel_personal,
    *sendKindModel_list_thirdParty
)

val packagingModel_list= arrayOf(
    PackagingModel("Plastik"),
    PackagingModel("Kardus"),
    PackagingModel("Bubblewrap"),
    PackagingModel("Kresek"),
    PackagingModel("Sterofoam")
)


val packagingModel_list_1= arrayOf(
    packagingModel_list[0],
    packagingModel_list[1],
    packagingModel_list[2]
)

val packagingModel_list_2= arrayOf(
    packagingModel_list[1],
    packagingModel_list[3]
)

val packagingModel_list_3= arrayOf(
    packagingModel_list[4]
)

val categoryProductList= arrayOf(
    Category("id1", "Sepatu", "Produk"),
    Category("id1", "Buku", "Produk"),
    Category("id1", "Odol", "Produk"),
    Category("id1", "Nasi", "Produk"),
    Category("id1", "Kecap", "Produk"),
    Category("id1", "Bapokting", "Produk"),
    Category("id1", "Lain", "Produk")
)

val unitProductList= arrayOf(
    Unit_("id1", "pcs"),
    Unit_("id1", "kg"),
    Unit_("id1", "ml"),
    Unit_("id1", "buah")
)


val productList_full= arrayOf(
    Product("id1", "Asidas", categoryProductList[0], unitProductList[0], 2.2, 1.3, 2.3, PictModel(resId = R.drawable.ic_arrow_thick)),
    Product("id2", "How to be a happy single-ton", categoryProductList[1], unitProductList[0], 5.2, 2.3, 1.2, PictModel(resId = R.drawable.ic_arrow_thick)),
    Product("id3", "Everything will meet their couple", categoryProductList[0], unitProductList[0], 3.1, 3.2, 1.1, PictModel(resId = R.drawable.bg_default)),
    Product("id4", "Pepsiden", categoryProductList[2], unitProductList[0], 8.1, 1.2, 3.1, PictModel(resId = R.drawable.bg_default_inverse)),
    Product("id5", "Cap Ikan Gatol", categoryProductList[3], unitProductList[1], 11.1, 6.5, 3.1, PictModel(resId = R.drawable.bg_default_inverse)),
    Product("id6", "Kecap Oreng", categoryProductList[4], unitProductList[2], 3.1, 5.1, 1.3, PictModel(resId = R.drawable.bg_default)),
    Product("id7", "Kunci", categoryProductList[6], unitProductList[3], 1.1, 3.1, 4.1, PictModel(resId = R.drawable.bg_default)),
    Product("id8", "Minyak", categoryProductList[4], unitProductList[2], 3.1, 4.1, 5.4, PictModel(resId = R.drawable.ic_arrow_thick)),
    Product("id9", "Gula", categoryProductList[5], unitProductList[2], 3.1, 3.1, 4.1, PictModel(resId = R.drawable.bg_default_inverse)),
    Product("id10", "Sayangku", categoryProductList[4], unitProductList[2], 3.1, 1.1, 3.1, PictModel(resId = R.drawable.ic_arrow_thick))
)

val boundProdSendList_1= arrayOf(
    BoundProductSendModel(sendKindModel_list_thirdParty[0], "Jl. Mantul", 1),
    BoundProductSendModel(sendKindModel_list_thirdParty[0], "Jl. Muntap", 3),
    BoundProductSendModel(sendKindModel_list_thirdParty[1], "Jl. In Aja", 4)
)

val boundProdSendList_2= arrayOf(
    BoundProductSendModel(sendKindModel_list_thirdParty[2], "Jl. Ok Aja", 3),
    BoundProductSendModel(sendKindModel_list_thirdParty[3], "Jl. Aja Jadian Kagak", 1),
    BoundProductSendModel(sendKindModel_list_thirdParty[2], "Jl. Jalan", 4)
)

val boundProdSendList_3= arrayOf(
    BoundProductSendModel(sendKindModel_list_thirdParty[1], "Jl. Ke Depan Melupakanmu", 1),
    BoundProductSendModel(sendKindModel_list_thirdParty[1], "Jl. Eko", 3),
    BoundProductSendModel(sendKindModel_list_thirdParty[4], "Jl. Budiman", 2)
)

val boundProdSendList_full= arrayOf(
    *boundProdSendList_1,
    *boundProdSendList_2,
    *boundProdSendList_3
)

val boundProductLisft_full= arrayOf(
    BoundProduct(productList_full[0]),
    BoundProduct(productList_full[1]),
    BoundProduct(productList_full[2]),
    BoundProduct(productList_full[3]),
    BoundProduct(productList_full[4]),
    BoundProduct(productList_full[5]),
    BoundProduct(productList_full[6]),
    BoundProduct(productList_full[7]),
    BoundProduct(productList_full[8]),
    BoundProduct(productList_full[9])
)


val boundProductLisft_chosen_1= arrayOf(
    BoundProduct(productList_full[4], 10, 0, packagingModel_list_1.toCollection(ArrayList()), boundProdSendList_1.toCollection(ArrayList()), containerModelList_1.toArrayList()),
    BoundProduct(productList_full[5], 11, 0, packagingModel_list_2.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList()), containerModelList_2.toArrayList()),
    BoundProduct(productList_full[6], 11, 0, packagingModel_list_1.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList()))
)

val boundProductLisft_chosen_2= arrayOf(
    BoundProduct(productList_full[1], 10, 0, packagingModel_list_3.toCollection(ArrayList()), boundProdSendList_1.toCollection(ArrayList())),
    BoundProduct(productList_full[0], 21, 0, packagingModel_list_1.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList())),
    BoundProduct(productList_full[6], 43, 0, packagingModel_list_1.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList())),
    BoundProduct(productList_full[3], 11, 0, packagingModel_list_2.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList()))
)

val boundProductLisft_chosen_3= arrayOf(
    BoundProduct(productList_full[7], 40),
    BoundProduct(productList_full[8], 101, 0, packagingModel_list_1.toCollection(ArrayList()), boundProdSendList_2.toCollection(ArrayList()))
)

val boundProductLisft_chosen_4= arrayOf(
    BoundProduct(productList_full[2], 31, 0, packagingModel_list_1.toCollection(ArrayList()))
)


/*
val INBOUND_CREATED= 1  //--> Nunggu konfirm
val INBOUND_CONFIRMED= 2 //--> Kirim bukti resi pengiriman // Opsi kirim pihak ke-3
val INBOUND_ON_SHIPMENT= 3 //--> Sudah sampe blum >> Warehouse yang nekan
val INBOUND_ARRIVED_AND_PICKED= 4 //--> Proses memasukan ke kontener, scan barang & kontener
val INBOUND_PUT= 41 //--> Proses memasukan ke kontener, scan barang & kontener
val INBOUND_OVERDUE= 5 //--> Terlambat tidak mengirim dari lessee >> Sistem

val OUTBOUND_CREATED= 6 //--> Nunggu konfirm
val OUTBOUND_MANAGED= 71 //--> Scan, Finished
val OUTBOUND_WAITING_FOR_SHIPMENT= 81 //--> Scan, Finished
val OUTBOUND_SHIPPED= 82 //--> Scan, Finished

val CROSSDOCKING_CREATED= 9 //--> Nunggu konfirm
val CROSSDOCKING_CONFIRMED= 10 //--> Kirim bukti resi pengiriman // Opsi kirim pihak ke-3
val CROSSDOCKING_ON_SHIPMENT= 11 //--> Sudah sampe blum >> Lessee yang nekan
val CROSSDOCKING_ARRIVED_AND_MANAGED= 12 //--> Tinggal mencet
val CROSSDOCKING_MANAGED= 122 //--> Tinggal mencet
val CROSSDOCKING_WAITING_FOR_SHIPMENT= 123 //--> Scan, Finished
val CROSSDOCKING_SHIPPED= 131 //--> Tinggal mencet
*/

/*
val boundInListData_pending= arrayOf(
    Bound("wkwk", "10/10/2010", "Pak Bejo", "Gudang Maknyus",
        boundProductLisft_chosen_1.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Pending),
    Bound("wkwk", "11/10/2010", "Pak Pajo", "Gudang Ok",
        boundProductLisft_chosen_3.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Pending),
    Bound("wkwk", "11/10/2010", "Pak Keok", "Gudang Ntab",
        boundProductLisft_chosen_4.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Pending)
)

val boundInListData_proses= arrayOf(
    Bound("wkwk", "11/10/2010", "Pak Keok", "Gudang Adi",
        boundProductLisft_chosen_2.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Proses),
    Bound("wkwk", "12/10/2010", "Pak Ntab", "Gudang Tama",
        boundProductLisft_chosen_3.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Proses)
)

val boundInListData_sukses= arrayOf(
    Bound("wkwk", "11/10/2010", "Pak Keok", "Gudang Graha",
        null, BoundKind.INBOUND, BoundStatus.Sukses),
    Bound("wkwk", "12/10/2010", "Pak Ntab", "Gudang Warehouse",
        boundProductLisft_chosen_1.toCollection(ArrayList()), BoundKind.INBOUND, BoundStatus.Sukses)
)

val boundOutListData= arrayOf(
    Bound("wkwk", "10/10/2010", "Pak Bejo", "Gudang Werhos",
        null, BoundKind.OUTBOUND, BoundStatus.Pending),
    Bound("wkwk", "11/10/2010", "Pak Pajo", "Gudang Stupel",
        boundProductLisft_chosen_1.toCollection(ArrayList()), BoundKind.OUTBOUND, BoundStatus.Pending),
    Bound("wkwk", "11/10/2010", "Pak Keok", "Gudang Grimus",
        null, BoundKind.OUTBOUND, BoundStatus.Pending),
    Bound("wkwk", "11/10/2010", "Pak Keok", "Gudang Maknyus",
        boundProductLisft_chosen_4.toCollection(ArrayList()), BoundKind.OUTBOUND, BoundStatus.Proses)
)
 */



val senderList= arrayOf(
    ShipperModel("Pak Jo", "08123123123"),
    ShipperModel("Pak Ji", "08123456789"),
    ShipperModel("Pak Cak", "031413124123"),
    ShipperModel("Mbak Yu", "08123123123"),
    ShipperModel("Cak Yuk", "087123141241")
)

val boundSendList= arrayOf(
    BoundSendModel(ReceiptModel("EXP1231eja"), "Jl. mantab", sendKindModel_list_thirdParty[1]), //boundProdSendList_1[0]),
    BoundSendModel(ReceiptModel("INVAas123"), "Jl. in", sendKindModel_list_thirdParty[0]), //boundProdSendList_1[0]),
    BoundSendModel(ReceiptModel("POAIN21e31"), "Jl. jalan", sendKindModel_list_thirdParty[2]), //boundProdSendList_1[0]),
    BoundSendModel(ReceiptModel("HJAoo1231"), "Jl. muter", sendKindModel_list_thirdParty[3]), //boundProdSendList_1[0]),
    BoundSendModel(ReceiptModel("ijij381"), "Jl. lurus", sendKindModel_list_thirdParty[1]), //boundProdSendList_1[0]),
    BoundSendModel(ReceiptModel("ppjj3901a"), "Jl. bergelombang", sendKindModel_list_thirdParty[3]) //boundProdSendList_1[0]),
)

val senderListMax= senderList.size -1
val warehouseListMax= warehouseList_full.size -1


val inboundList_full= arrayOf(
    Bound(1, "wkwk", "20/01/2019", ub_lesseeList_full[0], ub_warehouseList_full[1],
        boundProductLisft_full.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ON_SHIPMENT,
        senderList[0]),
    Bound(2, "wkwk", "21/02/2019", ub_lesseeList_full[1], ub_warehouseList_full[2],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ON_SHIPMENT,
        senderList[3], boundSendList[0])
)

val inboundList_created= arrayOf(
    Bound(3, "200223013118218", "20/01/2019", ub_lesseeList_full[2], ub_warehouseList_full[0],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_CREATED),
    Bound(4, "200223013118218", "21/02/2019", ub_lesseeList_full[3], ub_warehouseList_full[1],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_CREATED),
    Bound(5, "INV/20200223/XX/II/0103077525", "23/03/2019", ub_lesseeList_full[2], ub_warehouseList_full[3],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_CREATED)
)

val inboundList_confirmed= arrayOf(
    Bound(6, "wkwk", "20/01/2019", ub_lesseeList_full[1], ub_warehouseList_full[1],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_CONFIRMED),
    Bound(7, "wkwk", "21/02/2019", ub_lesseeList_full[2], ub_warehouseList_full[2],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_CONFIRMED)
)

val inboundList_onShipment= arrayOf(
    Bound(8, "wkwk", "20/01/2019", ub_lesseeList_full[1], ub_warehouseList_full[1],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ON_SHIPMENT,
        senderList[0]),
    Bound(9, "wkwk", "21/02/2019", ub_lesseeList_full[5], ub_warehouseList_full[5],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ON_SHIPMENT,
        senderList[3], boundSendList[0])
)

val inboundList_arrived= arrayOf(
    Bound(10, "wkwk", "22/02/2019", ub_lesseeList_full[6], ub_warehouseList_full[1],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ARRIVED_AND_PICKED,
        senderList[2]),
    Bound(11, "wkwk", "24/04/2019", ub_lesseeList_full[7], ub_warehouseList_full[8],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_ARRIVED_AND_PICKED,
        senderList[1], boundSendList[3])
)

val inboundList_put= arrayOf(
    Bound(12, "wkwk", "22/02/2019", ub_lesseeList_full[1], ub_warehouseList_full[1],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_PUT,
        senderList[2]),
    Bound(13, "wkwk", "24/04/2019", ub_lesseeList_full[6], ub_warehouseList_full[4],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.INBOUND_PUT,
        senderList[4], boundSendList[1])
)

val outboundList_created= arrayOf(
    Bound(14, "200223013118218", "20/01/2019", ub_lesseeList_full[1], ub_warehouseList_full[3],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_CREATED,
        send= boundSendList[0]),
    Bound(15, "200223013118218", "21/02/2019", ub_lesseeList_full[2], ub_warehouseList_full[2],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_CREATED),
    Bound(16, "200223013118218", "23/03/2019", ub_lesseeList_full[4], ub_warehouseList_full[6],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_CREATED,
        send= boundSendList[2])
)

val outboundList_managed= arrayOf(
    Bound(17, "wkwk", "20/01/2019", ub_lesseeList_full[1], ub_warehouseList_full[1],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_MANAGED),
    Bound(18, "wkwk", "21/02/2019", ub_lesseeList_full[0], ub_warehouseList_full[7],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_MANAGED)
)

val outboundList_waitingForShipment= arrayOf(
    Bound(19, "wkwk", "20/01/2019", ub_lesseeList_full[2], ub_warehouseList_full[1],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_WAITING_FOR_SHIPMENT,
        senderList[0]),
    Bound(20, "wkwk", "21/02/2019", ub_lesseeList_full[6], ub_warehouseList_full[4],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_WAITING_FOR_SHIPMENT,
        senderList[3], boundSendList[0])
)

val outboundList_shipped= arrayOf(
    Bound(21, "wkwk", "22/02/2019", ub_lesseeList_full[6], ub_warehouseList_full[5],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_SHIPPED,
        senderList[2]),
    Bound(22, "wkwk", "24/04/2019", ub_lesseeList_full[3], ub_warehouseList_full[6],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.OUTBOUND_SHIPPED,
        senderList[1], boundSendList[3])
)




val crossdocking_= arrayOf(
    CrossdockingModel(23, "wkwk", "20/01/2019", ub_lesseeList_full[1], ub_warehouseList_full[2],
        boundProductLisft_chosen_1.toCollection(ArrayList()), status= Constants.BoundStatus.CROSSDOCKING_CREATED),
    CrossdockingModel(24, "wkwk", "21/02/2019", ub_lesseeList_full[3], ub_warehouseList_full[4],
        boundProductLisft_chosen_2.toCollection(ArrayList()), status= Constants.BoundStatus.CROSSDOCKING_CREATED),
    CrossdockingModel(25, "wkwk", "23/03/2019", ub_lesseeList_full[9], ub_warehouseList_full[8],
        boundProductLisft_chosen_1.toCollection(ArrayList()), status= Constants.BoundStatus.CROSSDOCKING_CREATED)
)


val crossdocking_created= arrayOf(
    Bound(26, "wkwk", "20/01/2019", ub_lesseeList_full[3], ub_warehouseList_full[4],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_CREATED),
    Bound(27, "wkwk", "21/02/2019", ub_lesseeList_full[7], ub_warehouseList_full[9],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_CREATED),
    Bound(28, "wkwk", "23/03/2019", ub_lesseeList_full[2], ub_warehouseList_full[3],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_CREATED)
)

val crossdocking_confirmed= arrayOf(
    Bound(29, "wkwk", "20/01/2019", ub_lesseeList_full[8], ub_warehouseList_full[5],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_CONFIRMED),
    Bound(30, "wkwk", "21/02/2019", ub_lesseeList_full[5], ub_warehouseList_full[6],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_CONFIRMED)
)

val crossdocking_onShipment= arrayOf(
    Bound(31, "wkwk", "20/01/2019", ub_lesseeList_full[2], ub_warehouseList_full[7],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_ON_SHIPMENT,
        senderList[0]),
    Bound(32, "wkwk", "21/02/2019", ub_lesseeList_full[8], ub_warehouseList_full[9],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_ON_SHIPMENT,
        senderList[3], boundSendList[0])
)

val crossdocking_arrived= arrayOf(
    Bound(33, "wkwk", "22/02/2019", ub_lesseeList_full[3], ub_warehouseList_full[3],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_ARRIVED_AND_MANAGED,
        senderList[2]),
    Bound(34, "wkwk", "24/04/2019", ub_lesseeList_full[2], ub_warehouseList_full[3],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_ARRIVED_AND_MANAGED,
        senderList[1], boundSendList[3])
)

val crossdocking_waitingForShipment= arrayOf(
    Bound(35, "wkwk", "22/02/2019", ub_lesseeList_full[7], ub_warehouseList_full[3],
        boundProductLisft_chosen_1.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_WAITING_FOR_SHIPMENT,
        senderList[2]),
    Bound(36, "wkwk", "24/04/2019", ub_lesseeList_full[2], ub_warehouseList_full[4],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_WAITING_FOR_SHIPMENT,
        senderList[4], boundSendList[1])
)

val crossdocking_shipped= arrayOf(
    Bound(37, "wkwk", "22/02/2019", ub_lesseeList_full[5], ub_warehouseList_full[8],
        boundProductLisft_chosen_2.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_SHIPPED,
        senderList[2]),
    Bound(38, "wkwk", "24/04/2019", ub_lesseeList_full[3], ub_warehouseList_full[5],
        boundProductLisft_chosen_3.toCollection(ArrayList()), Constants.BoundStatus.CROSSDOCKING_SHIPPED,
        senderList[1], boundSendList[3])
)

val bound_new= Bound(0, "id", "",
    productList = boundProductLisft_full.toCollection(ArrayList())
)

//val boundList_full= HashMap<Int, Bound>()

object boundList_full{
    val list= HashMap<Int, Array<Bound>>()
    init{
        list[Constants.BoundStatus.INBOUND_CREATED]= inboundList_created
        list[Constants.BoundStatus.INBOUND_CONFIRMED]= inboundList_confirmed
        list[Constants.BoundStatus.INBOUND_ON_SHIPMENT]= inboundList_onShipment
        list[Constants.BoundStatus.INBOUND_ARRIVED_AND_PICKED]= inboundList_arrived
        list[Constants.BoundStatus.INBOUND_PUT]= inboundList_put
        list[Constants.BoundStatus.INBOUND_OVERDUE]= inboundList_put

        list[Constants.BoundStatus.OUTBOUND_CREATED]= outboundList_created
        list[Constants.BoundStatus.OUTBOUND_MANAGED]= outboundList_managed
        list[Constants.BoundStatus.OUTBOUND_WAITING_FOR_SHIPMENT]= outboundList_waitingForShipment
        list[Constants.BoundStatus.OUTBOUND_SHIPPED]= outboundList_shipped
        list[Constants.BoundStatus.OUTBOUND_OVERDUE]= outboundList_shipped

        list[Constants.BoundStatus.CROSSDOCKING_CREATED]= crossdocking_created
        list[Constants.BoundStatus.CROSSDOCKING_CONFIRMED]= crossdocking_confirmed
        list[Constants.BoundStatus.CROSSDOCKING_ON_SHIPMENT]= crossdocking_onShipment
        list[Constants.BoundStatus.CROSSDOCKING_ARRIVED_AND_MANAGED]= crossdocking_arrived
        list[Constants.BoundStatus.CROSSDOCKING_WAITING_FOR_SHIPMENT]= crossdocking_waitingForShipment
        list[Constants.BoundStatus.CROSSDOCKING_SHIPPED]= crossdocking_shipped
        list[Constants.BoundStatus.CROSSDOCKING_OVERDUE]= crossdocking_shipped
    }
}



/*
    warehouse.name = "Gudang Aryatama"
            warehouse.address = "Surabaya, Indonesia"
            warehouse.totalVolume = 300
            warehouse.usedVolume = 200
            warehouse.totalRating = 3.9
            warehouse.distance = 1.4
 */