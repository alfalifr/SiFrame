package sidev.lib.implementation._cob

import sidev.kuliah.tekber.edu_class.model.*
//import sidev.kuliah.tekber.edu_class.util.Const
import sidev.lib.implementation._simulation.edu_class.util.Edu_Class_Const as Const
import sidev.lib.android.siframe.tool.util.`fun`.fkmFrom

val dumm_profile= arrayOf(
    Profil("1", Const.ROLE_STUDENT, "stud1", "Barjo Ko", "08123321456", "barjo@mail.com"),
    Profil("2", Const.ROLE_STUDENT, "stud2", "Pak Ijo", "088877123", "ijoroyo@mail.com"),
    Profil("3", Const.ROLE_STUDENT, "stud3", "Eko Koko", "0123321432", "kokobelok@mail.com"),
    Profil("4", Const.ROLE_STUDENT, "stud4", "JesMunee", "0521134567", "jesmun@mail.com"),
    Profil("5", Const.ROLE_TEACHER, "teach1", "Bambang Seger", "123321", "bambang@mail.com"),
    Profil("6", Const.ROLE_TEACHER, "teach2", "Ratna Tresmuni", "331273", "tresmuni@mail.com")
)


val dumm_content_read= arrayOf(
    ContentRead("0", "Pengantar", "Dalam modul ini, kalian akan belajar ttg sejarah perubahan prinsip teori gravitasi dari pandangan Newton hingga Newton."),
    ContentRead("1", "Gaya Sentripetal", "Ini adalah sebuah gaya alami, bkn gaya2an."),
    ContentRead("2", null, "Lanjutan dari gaya sentripetal"),
    ContentRead("3", "Gaya Listrik", "Ini adalah sebuah gaya alami, bkn gaya2an. (2)"),
    ContentRead("4", "Ekonom Terkini", "Ekonom adalah orang yg memiliki keahlian di bidang ekonomi"),
    ContentRead("5", "Pandemonium", "Berasal dari kata 'Pandemi', namun kata ini hanya lah fiksi belaka"),
    ContentRead("6", null, "Akhir dari kuis. Teliti lagi sebelum mengumpulkan")
)
val dumm_content_question2= arrayOf(
    ContentQuestion("10", "Tuliskan feedback anda ttg kuis ini", Const.QUESTION_KIND_FILL,
        null, null, null)
)

val dumm_content_question= arrayOf(
    ContentQuestion("1", "Apa itu gaya sentripetal?", Const.QUESTION_KIND_PILGAN,
        arrayListOf("Gaya saling menarik", "Gaya yg menjauhi titik pusat", "Gaya yg dibuat oleh Alam"), arrayListOf("0"), null),
    ContentQuestion("2", "Siapa bapak koperasi Indonesia?", Const.QUESTION_KIND_PILGAN,
        arrayListOf("Jusuf Kalla", "Soekarno", "Hatta Rajasa", "Tidak ada yg benar"), arrayListOf("3"), null),
    ContentQuestion("3", "Syarat sebuah negara, kecuali?", Const.QUESTION_KIND_MULTIPLE,
        arrayListOf("Punya rakyat", "Punya pemerintahan", "Punya sumber daya", "Diakui yg lain"), arrayListOf("0", "1","3"), null),
    ContentQuestion("4", "Jelaskan scr singkat penyebab terjadinya WW 2!", Const.QUESTION_KIND_FILL,
        null, null, null),
    ContentQuestion("5", "Jelaskan scr singkat apa itu evolusi!", Const.QUESTION_KIND_FILL,
        null, null, null),
    ContentQuestion("6", "Tuliskan pendapatmu tentang perbedaan teori gravitasi Newton dan Ensten!", Const.QUESTION_KIND_FILL,
        null, null, null),
    ContentQuestion("7", "Apa yg kamu ketahui tentang gaya gravitasi?", Const.QUESTION_KIND_FILL,
        null, null, null),
    ContentQuestion("8", "Jelaskan scr singkat asal mula munculnya gagasan gaya gravitasi!", Const.QUESTION_KIND_FILL,
        null, null, null),
    ContentQuestion("9", "Berikut merupakan gaya...", Const.QUESTION_KIND_MULTIPLE,
        arrayListOf("Gaya gravitasi", "Gaya pegas", "Gaya tarik", "Gaya magnet", "Gaya kesinambungan", "Gaya sentropegal"), arrayListOf("0", "1","3"), null)
)

val dumm_content_video= arrayOf(
    ContentVideo("1", "https://cobtemp.000webhostapp.com/eksploho/mike_shinoda_post_traumatic.mp4", "Inspirasi pagi hari"),
    ContentVideo("2", "https://cobtemp.000webhostapp.com/eksploho/iotbs_creature.mp4", null),
    ContentVideo("3", "https://cobtemp.000webhostapp.com/eksploho/coding.mp4", "Ttg koding"),
    ContentVideo("4", "https://cobtemp.000webhostapp.com/eksploho/iotbs_ensten.mp4", "Tentang om ensten"),
    ContentVideo("5", "https://cobtemp.000webhostapp.com/eksploho/iotbs_darwin.mp4", "Om darwin")
)



val dumm_page= arrayOf(
    Page("1", "Pengantar", 1, fkmFrom(dumm_content_read[0])),
    Page("2", "Physic of Gravity", 2, fkmFrom(dumm_content_video[3], dumm_content_read[1], dumm_content_read[2], dumm_content_read[3])),
    Page("3", "Latihan soal", 3, fkmFrom(*dumm_content_question, dumm_content_read.last(), *dumm_content_question2), true),
    Page("4", "All is bout change", 2, fkmFrom(dumm_content_video[4], dumm_content_question[3], dumm_content_read[1]))
)


val dumm_module= arrayOf(
    Module("1", "Teori Gravitasi", "Menunjukan perubahan teori gravitasi dari Newton sampai Ensten", null, Duration("W 1", "W 8"),
    fkmFrom(dumm_page[0], dumm_page[1], dumm_page[2])),
    Module("2", "UTS", "Kerjakan dg jujur", null, Duration("W 9"),
    fkmFrom(dumm_page[2])),
    Module("3", "Teori Evolusi", "\"Perubahan membutuhkan waktu yg lama\" -anon", null, Duration("W 10", "W 16"),
    fkmFrom(dumm_page[0], dumm_page[3], dumm_page[2])),
    Module("3", "Teori Terakhir", "Ini yg trahir", null, Duration("W 17"),
    fkmFrom(dumm_page[0], dumm_page[3], dumm_page[2]))
)

val dumm_class= arrayOf(
    ClassModel("1", "Analisis Sains", "A", "Maryati S.Pd", 3, fkmFrom(*dumm_module), null),
    ClassModel("2", "Analisis Ekonom", "B", "Spradiyono Suratmajadimeja", 3, fkmFrom(*dumm_module), null)
)

val dumm_smt_class= arrayOf(
    SemesterClass("1", fkmFrom(*dumm_class), 6)
)

val dumm_time_now= WeekTime("1", "Rabu, 17 Juni 2020", 5)
val dumm_upcoming_class= dumm_class[0]

val dumm_schedule= arrayOf(
    ScheduleModel("1", fkmFrom(dumm_class[0]), "Senin", Duration("09.00", "10.50"), "Gedung A"),
    ScheduleModel("2", fkmFrom(dumm_class[0]), "Rabu", Duration("11.00", "12.50"), "Gedung AB"),
    ScheduleModel("3", fkmFrom(dumm_class[1]), "Kami", Duration("10.00", "11.50"), "Graha Di")
)

val dumm_presense= arrayOf(
    Presence_("1", "17 Juni 2020", Const.STATUS_PRESENCE_PRESENT, "Belajar tentang teori gravitasi", null, null),
    Presence_("2", "21 Juni 2020", Const.STATUS_PRESENCE_IJIN, null, null, null),
    Presence_("3", "30 Juni 2020", Const.STATUS_PRESENCE_ALPHA, null, null, null),
    Presence_("4", "10 Juli 2020", Const.STATUS_PRESENCE_PRESENT, "Teori evolusinya asik", null, null),
    Presence_("5", "17 Juli 2020", Const.STATUS_PRESENCE_NEW, null, null, null),
    Presence_("6", "17 Juli 2020", Const.STATUS_PRESENCE_NEW, null, null, null),
    Presence_("7", "17 Juli 2020", Const.STATUS_PRESENCE_NEW, null, null, null),
    Presence_("8", "17 Juli 2020", Const.STATUS_PRESENCE_NEW, null, null, null)
)

val dumm_presence_class= arrayOf(
    PresenceClass("1", fkmFrom(dumm_class[0]), fkmFrom(dumm_schedule[0], dumm_schedule[1]), fkmFrom(*dumm_presense), 2, 1, 1),
    PresenceClass("2", fkmFrom(dumm_class[1]), fkmFrom(dumm_schedule[2]), fkmFrom(*dumm_presense), 2, 1, 1)
)

val dumm_presence_class_smt= arrayOf(
    PresenceClassSmt("1", "6", fkmFrom(*dumm_presence_class))
)

val dumm_news= arrayOf(
    Notif("1", null, "Tugas baru Analisis Sains", "2020-06-15 10:01:00"),
    Notif("2", null, "DL tugas 2 Analisis Sains", "2020-06-15 11:01:00"),
    Notif("3", null, "Tugas baru Analisis Ekonom", "2020-06-16 12:01:00"),
    Notif("4", null, "Tugas baru Analisis Sains!", "2020-06-17 12:10:00"),
    Notif("5", "AS Pindah kelas", "Pindah ke gedung A", "2020-06-18 12:11:10"),
    Notif("6", "Kuis mendadak", "Besok AS ada kuis", "2020-06-18 13:01:00"),
    Notif("7", null, "Tugas baru Analisis Sains", "2020-06-18 15:07:00"),
    Notif("8", null, "Tugas baru Analisis Ekonom", "2020-06-18 20:04:00"),
    Notif("8", null, "Tugas baru Analisis Ekonom", "2020-06-19 21:04:00"),
    Notif("8", null, "Tugas baru Analisis Ekonom", "2020-06-19 23:04:00"),
    Notif("8", null, "Tugas baru Analisis Ekonom", "2020-20-20 20:04:00")
)