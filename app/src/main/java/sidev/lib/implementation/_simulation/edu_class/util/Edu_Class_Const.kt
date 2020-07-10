package sidev.lib.implementation._simulation.edu_class.util

import android.app.Activity

object Edu_Class_Const {
    val URL_ROOT= "https://tekber-app.herokuapp.com/api"
    val URL_PROFILE= "$URL_ROOT/profil"

    val KEY_UNAME= "uname"
    val KEY_PSWD= "pswd"
    val KEY_PROFILE_ROLE= "profile_role"

    val ROLE_STUDENT= 1
    val ROLE_TEACHER= 2

    val FORMAT_DATE= "yyyy-MM-dd"
    val FORMAT_TIME= "HH:mm:ss"
    val FORMAT_TIMESTAMP= "$FORMAT_DATE $FORMAT_TIME"

    val REQ_LOGIN= "login"
    val REQ_LOGOUT= "logout"
    val REQ_GET_PROFILE= "get_profile"
    val REQ_GET_CLASS_SEMESTER= "get_class_smt"
    val REQ_GET_MODULE= "get_module"
    val REQ_GET_PAGE= "get_page"
    val REQ_GET_CONTENT= "get_content"
    val REQ_GET_NOTIF= "get_news"
    val REQ_GET_PRESENCE_CLASS_IN_SMT= "get_presence_class"
    val REQ_GET_PRESENCE_TIME_NOW= "get_presence_time_now"
    val REQ_GET_PRESENCE_UPCOMING_CLASS= "get_presence_upcoming_class"
    val REQ_GET_PRESENCE_DETAIL= "get_presence_detail"
    val REQ_SEND_PRESENCE_CODE= "send_presence_code"
    val REQ_SEND_PRESENCE_NEWS= "send_presence_news"
    val REQ_SEND_PRESENCE_IJIN= "send_presence_ijin"
    val REQ_SEND_QUESTION_ANSWER= "send_question_answer"
    val REQ_PICK_FILE= 2

    val RES_OK= Activity.RESULT_OK
    val RES_NOT_OK= -10

    val RES_NO_CLASS_IN_SMT= -2
    val RES_NO_CLASS= -3
    val RES_NO_MODULE= -4
    val RES_NO_PAGE= -5
    val RES_NO_CONTENT= -6

    val DATA_CLASS_ID= "class_id"
    val DATA_CLASS_IN_SMT= "class_in_smt"
    val DATA_CLASS_IN_SMT_ID= "class_in_smt_id"
    val DATA_MODULE= "module"
    val DATA_MODULE_ID= "module_id"
    val DATA_PAGE= "page"
    val DATA_PAGE_ID= "page_id"
    val DATA_CONTENT= "content"
    val DATA_CONTENT_ID= "content_id"
    val DATA_QUESTION_ANSWER= "question_answer"
    val DATA_UNAME= "uname"
    val DATA_PSWD= "pswd"
    val DATA_PROFILE= "profile"
    val DATA_PROFILE_ROLE= "profile_role"
    val DATA_NOTIF= "news"
    val DATA_PRESENCE_CLASS= "presence_class"
    val DATA_PRESENCE_TIME_NOW= "presence_time_now"
    val DATA_PRESENCE_UPCOMING_CLASS= "presence_upcaoming_class"
    val DATA_PRESENCE_CLASS_SMT= "presence_class_smt"
    val DATA_PRESENCE_SCHEDULE= "presence_schedule"
    val DATA_PRESENCE= "presence"
    val DATA_PRESENCE_ID= "presence_id"
    val DATA_PRESENCE_CODE= "presence_code"
    val DATA_PRESENCE_NEWS= "presence_news"
    val DATA_PRESENCE_IJIN_REASON= "presence_ijin_reason"
    val DATA_PRESENCE_IJIN_FILE= "presence_ijin_file"


    val STATUS_PRESENCE_NEW= 0
    val STATUS_PRESENCE_PRESENT= 1 //Hadir
    val STATUS_PRESENCE_IJIN= 2
    val STATUS_PRESENCE_ALPHA= 3

    val QUESTION_KIND_MULTIPLE= 1
    val QUESTION_KIND_PILGAN= 2
    val QUESTION_KIND_FILL= 3

    val MSG_NO_DATA= "Tidak ada data"
    val MSG_LOAD_ERROR_PAGE= "Terjadi kesalahan saat download data.\nHarap reload."
}