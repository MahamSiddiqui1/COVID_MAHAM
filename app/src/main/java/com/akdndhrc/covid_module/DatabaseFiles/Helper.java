package com.akdndhrc.covid_module.DatabaseFiles;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.akdndhrc.covid_module.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteOpenHelper;


public class Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "HayatPKDB";
    private static final String DATABASE_NAMEFORDATA = ".HayatPKDB";

    Context context;

    public Helper(final Context context) {

        super(context, Environment.getExternalStorageDirectory()
                + File.separator + "HayatPK"
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;

    }

    public static String CREATE_TABLE_COUNTRY = "CREATE TABLE IF NOT EXISTS  COUNTRY (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50)  UNIQUE NOT NULL," +

            " name varchar(150) NOT NULL" +
            ")";

    public static String CREATE_TABLE_PROVINCE = "CREATE TABLE IF NOT EXISTS PROVINCE (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +

            " country_id varchar(50) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";

    public static String CREATE_TABLE_DISTRICT = "CREATE TABLE IF NOT EXISTS DISTRICT (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +

            " country_id varchar(50) NOT NULL," +
            " province_id varchar(50) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";
    public static String CREATE_TABLE_TEHSIL = "CREATE TABLE IF NOT EXISTS TEHSIL (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +
            " country_id varchar(50) NOT NULL," +
            " province_id varchar(50) NOT NULL," +
            " district_id varchar(150) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";
    public static String CREATE_TABLE_UNIONCOUNCIL = "CREATE TABLE IF NOT EXISTS UNIONCOUNCIL (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +
            " district_id varchar(150) NOT NULL," +
            " tehsil_id varchar(150) NOT NULL," +
            " country_id varchar(50) NOT NULL," +
            " province_id varchar(50) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";

    public static String CREATE_TABLE_VILLAGES = "CREATE TABLE IF NOT EXISTS VILLAGES (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +

            " district_id varchar(150) NOT NULL," +
            " tehsil_id varchar(150) NOT NULL," +
            " country_id varchar(50) NOT NULL," +
            " province_id varchar(50) NOT NULL," +
            " uc_id varchar(50) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";

    public static String CREATE_TABLE_FACILITY = "CREATE TABLE IF NOT EXISTS FACILITY (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +
            " district_id varchar(150) NOT NULL," +
            " tehsil_id varchar(150) NOT NULL," +
            " country_id varchar(50) NOT NULL," +
            " province_id varchar(50) NOT NULL," +
            " uc_id varchar(50) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";
    public static String CREATE_TABLE_VACCINES = "CREATE TABLE IF NOT EXISTS VACCINES (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +
            " defaulter_date varchar(150) NOT NULL," +
            " due_date varchar(150) NOT NULL," +
            " name varchar(150) NOT NULL" +

            ")";

    public static String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS USERS( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "uid varchar(50) UNIQUE NOT NULL," +
            "privilege varchar(50) NOT NULL," +
            "username varchar(50) NOT NULL," +
            "password varchar(150) NOT NULL," +
            "salt varchar(50) NOT NULL," +
            "district_id varchar(150) NOT NULL," +
            "country_id varchar(50) NOT NULL," +
            "province_id varchar(50) NOT NULL," +
            "uc_id varchar(50) NOT NULL," +
            "metadata text DEFAULT NULL" +
            ")";
    ////for record


    //khandan main bhi data wala scene kardo

    public static String CREATE_TABLE_KHANDAN = "CREATE TABLE KHANDAN( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "manual_id varchar(128) NOT NULL," +
            "uid varchar(128) UNIQUE NOT NULL," +
            "province_id varchar(128) NOT NULL," +
            "district_id varchar(128) NOT NULL," +
            "subdistrict_id varchar(128) NOT NULL," +
            "uc_id varchar(128) NOT NULL," +
            "village_id varchar(128) NOT NULL," +
            "family_head_name varchar(128) NOT NULL," +
            "family_address varchar(128) NOT NULL," +
            "water_source varchar(128) NOT NULL," +
            "toilet_facility varchar(128) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_KMEMBER = "CREATE TABLE MEMBER( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "manual_id varchar(128) NOT NULL," +
            "uid varchar(128) UNIQUE NOT NULL," +
            "khandan_id varchar(128) NOT NULL," +
            "full_name varchar(128) NOT NULL," +
            "nicnumber varchar(128) NOT NULL," +
            "phone_number varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "gender INTEGER NOT NULL," +
            "age INT NOT NULL," +
            "dob TEXT NOT NULL," +
            "bio_code TEXT NOT NULL," +
            "qr_code varchar(128) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced INTEGER NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_CHILD_GROWTH = "CREATE TABLE CGROWTH( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_CHILD_BEMARI = "CREATE TABLE CBEMARI( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_CHILD_VACINATION = "CREATE TABLE CVACCINATION( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "vaccine_id varchar(128) NOT NULL," +
            "record_data varchar(100) NOT NULL," +
            "type varchar(30) NOT NULL," +
            "due_date varchar(100) NOT NULL," +
            "vaccinated_on varchar(100) NOT NULL," +
            "image_location varchar(100) NOT NULL," +
            "metadata text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "UNIQUE (member_uid,vaccine_id)" +
            ")";
//data vacine id communicate


    public static String CREATE_TABLE_CHILD_MAMLOOM = "CREATE TABLE CMALOOM( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MOTHER_MAMLOOM = "CREATE TABLE MMALOOM( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_CO_MORBIDITY = "CREATE TABLE COVID_CO_MORBIDITY( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_SIDE_EFFECTS = "CREATE TABLE COVID_SIDE_EFFECTS( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";




  /*  public static String CREATE_TABLE_MOTHER_VACINATION = "CREATE TABLE COVID_IMMUNIZATION( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "vaccine_id int(5) NOT NULL," +
            "type int(5) NOT NULL," +
            "vaccinated_on varchar(15) NOT NULL," +
            "image_location text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "UNIQUE (member_uid,vaccine_id)"+
            ")";*/

    public static String CREATE_TABLE_COVID_IMMUNIZATION = "CREATE TABLE COVID_IMMUNIZATION( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "vaccine_id int(5) NOT NULL," +
            "type int(5) NOT NULL," +
            "vaccinated_on varchar(15) NOT NULL," +
            "image_location text NOT NULL," +
            "UNIQUE (member_uid,vaccine_id)" +
            ")";


    public static String CREATE_TABLE_MOTHER_VACINATION = "CREATE TABLE MVACINE( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "vaccine_id int(5) NOT NULL," +
            "type int(5) NOT NULL," +
            "vaccinated_on varchar(15) NOT NULL," +
            "image_location text NOT NULL," +
            "UNIQUE (member_uid,vaccine_id)" +
            ")";

    public static String CREATE_TABLE_MOTHER_PREGNANCY = "CREATE TABLE MPREGNANCY( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "pregnancy_id INTEGER NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "lmp DATE NOT NULL," +
            "edd DATE NOT NULL," +
            "metadata text DEFAULT NULL," +
            "status INTEGER NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced INTEGER NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MOTHER_ANC = "CREATE TABLE MANC( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "pregnancy_id INTEGER NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "type varchar(50) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MOTHER_PNC = "CREATE TABLE MPNC( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "pregnancy_id INTEGER NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "type varchar(50) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MOTHER_DELIV = "CREATE TABLE MDELIV( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "pregnancy_id INTEGER NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "type varchar(50) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_REFERAL = "CREATE TABLE REFERAL( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_VIDEOS = "CREATE TABLE VIDEOS( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "video_name varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_LOGINS = "CREATE TABLE IF NOT EXISTS LOGINS( " +
            "uid varchar(128) NOT NULL," +
            "latitude varchar(20) NOT NULL," +
            "longitude varchar(20) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_FEEDBACK = "CREATE TABLE IF NOT EXISTS FEEDBACK ( " +
            "rating varchar(1) NOT NULL," +
            "record_data text NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_CVACCINE_VIALS = "CREATE TABLE IF NOT EXISTS CVACCINE_VIALS ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "vaccine_id varchar(128) NOT NULL," +
            "vaccine_name varchar(128) NOT NULL," +
            "type varchar(5) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_VACCINE_STOCKS = "CREATE TABLE IF NOT EXISTS VACCINE_STOCK ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "vaccine_id varchar(128) NOT NULL," +
            "vaccine_name varchar(128) NOT NULL," +
            "balance varchar(128) NOT NULL," +
            "utilized varchar(128) NOT NULL," +
            "received varchar(128) NOT NULL," +
            "wastage varchar(128) NOT NULL," +
            "return varchar(128) NOT NULL," +
            "record_data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MEETING_MEMBER = "CREATE TABLE IF NOT EXISTS MEETING_MEMBER( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "meeting_uid varchar(128) NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "type int(5) NOT NULL," +
            "metadata text DEFAULT NULL," +
            "record_data varchar(128) NOT NULL," +
            "month varchar(3) NOT NULL," +
            "member_status INTEGER NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced INTEGER NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_MEETING = "CREATE TABLE IF NOT EXISTS  MEETING( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "uid varchar(128) NOT NULL," +
            "type int(5) NOT NULL," +
            "metadata text DEFAULT NULL," +
            "meeting_topic varchar(5) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "meeting_status INTEGER NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced INTEGER NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MEDICINE = "CREATE TABLE IF NOT EXISTS  MEDICINE (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " uid varchar(50) UNIQUE NOT NULL," +
            " name varchar(150) NOT NULL," +
            "type int(5) NOT NULL" +

            ")";

    public static String CREATE_TABLE_VACCINE_IMAGE = "CREATE TABLE IF NOT EXISTS  VACCINE_IMAGE  (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "vaccine_id varchar(128) NOT NULL," +
            "type int(2) NOT NULL," +
            "image_data text NOT NULL" +

            ")";

    public static String CREATE_TABLE_MEDICINE_STOCKS = "CREATE TABLE IF NOT EXISTS MEDICINE_STOCK ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "medicine_id varchar(128) NOT NULL," +
            "medicine_name varchar(128) NOT NULL," +
            "balance varchar(128) NOT NULL," +
            "utilized varchar(128) NOT NULL," +
            "received varchar(128) NOT NULL," +
            "wastage varchar(128) NOT NULL," +
            "record_data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_MEMBER_MERGERD = "CREATE TABLE IF NOT EXISTS MEMBER_MERGERD ( " +
            "member_uid_1 varchar(128) NOT NULL," +
            "member_uid_2 varchar(128) NOT NULL," +
            "metadata text DEFAULT NULL," +
            "record_data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "PRIMARY KEY (member_uid_1)" +
            ")";


    public static String CREATE_TABLE_MEDICINE_LOG = "CREATE TABLE IF NOT EXISTS MEDICINE_LOG ( " +
            "member_uid varchar(128) NOT NULL," +
            "medicine_id varchar(128) NOT NULL," +
            "record_data text NOT NULL," +
            "type varchar(10) NOT NULL," +
            "disease varchar(50) NOT NULL," +
            "metadata text DEFAULT NULL," +
            "added_by varchar(128) NOT NULL," +
            "added_on varchar(30) NOT NULL," +
            "PRIMARY KEY (member_uid,record_data,type,disease)" +
            ")";

    public static String CREATE_TABLE_DELETE_MEMBER = "CREATE TABLE IF NOT EXISTS DELETE_MEMBER( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "manual_id varchar(128) NOT NULL," +
            "uid varchar(128) UNIQUE NOT NULL," +
            "khandan_id varchar(128) NOT NULL," +
            "full_name varchar(128) NOT NULL," +
            "nicnumber varchar(128) NOT NULL," +
            "phone_number varchar(128) NOT NULL," +
            "data text NOT NULL," +
            "gender INTEGER NOT NULL," +
            "age INT NOT NULL," +
            "dob TEXT NOT NULL," +
            "bio_code TEXT NOT NULL," +
            "qr_code varchar(128) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced INTEGER NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_SMS_NUMBER = "CREATE TABLE IF NOT EXISTS SMS_NUMBER( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "user_uid varchar(128) UNIQUE NOT NULL," +
            "phone_number varchar(50) NOT NULL" +
            ")";

    public static String CREATE_TABLE_DELETE_KHANDAN = "CREATE TABLE IF NOT EXISTS DELETE_KHANDAN( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "manual_id varchar(128) NOT NULL," +
            "uid varchar(128) UNIQUE NOT NULL," +
            "province_id varchar(128) NOT NULL," +
            "district_id varchar(128) NOT NULL," +
            "subdistrict_id varchar(128) NOT NULL," +
            "uc_id varchar(128) NOT NULL," +
            "village_id varchar(128) NOT NULL," +
            "family_head_name varchar(128) NOT NULL," +
            "family_address varchar(128) NOT NULL," +
            "water_source varchar(128) NOT NULL," +
            "toilet_facility varchar(128) NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";

    public static String CREATE_TABLE_CVIRUS = "CREATE TABLE IF NOT EXISTS CVIRUS ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "member_uid varchar(128) NOT NULL," +
            "record_data varchar(128) NOT NULL," +
            "type varchar(50) NOT NULL," +
            "data text NOT NULL," +
            "added_by varchar(128) NOT NULL," +
            "is_synced varchar(1) NOT NULL," +
            "added_on varchar(30) NOT NULL" +
            ")";


    public static String CREATE_TABLE_MONTHLY_REPORT = "CREATE TABLE IF NOT EXISTS MONTHLY_REPORT (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " month varchar(10) NOT NULL," +
            " year varchar(10) NOT NULL," +
            " total_population varchar(10) NOT NULL Default 0," +
            " total_population_np varchar(10) NOT NULL Default 0," +
            " FLCF_district varchar(10) NOT NULL Default 0," +
            " reporting_FLCF varchar(10) NOT NULL Default 0," +
            " off_road_vehicles varchar(10) NOT NULL Default 0," +
            " pop_registered_lhw varchar(10) NOT NULL Default 0," +
            " lhw_submit_report varchar(10) NOT NULL Default 0," +
            //basicinfo
            " health_commettees varchar(10) NOT NULL," +
            " women_suport_group varchar(10) NOT NULL," +
            " household_registered_lhw varchar(10) NOT NULL," +
            " tap varchar(10) NOT NULL," +
            " spring varchar(10) NOT NULL," +
            " handpump varchar(10) NOT NULL," +
            " well varchar(10) NOT NULL," +
            " other varchar(10) NOT NULL," +
            " flush_system varchar(10) NOT NULL," +
            //child_health
            " new_borns_1week varchar(10) NOT NULL," +
            " low_birth_weight varchar(10) NOT NULL," +
            " breast_fed varchar(10) NOT NULL," +
            " immunized varchar(10) NOT NULL," +
            " age_1223_count varchar(10) NOT NULL," +
            " age_1223_fully_imunized varchar(10) NOT NULL," +
            " age_lt3_count varchar(10) NOT NULL," +
            " age_lt3_gm varchar(10) NOT NULL," +
            " age_lt3_malnurished varchar(10) NOT NULL," +
            //maternal_health
            " new_preg varchar(10) NOT NULL," +
            " total_preg varchar(10) NOT NULL," +
            " total_vistis varchar(10) NOT NULL," +
            " iron_sup varchar(10) NOT NULL," +
            " abortions varchar(10) NOT NULL," +
            " delivey_4p varchar(10) NOT NULL," +
            " delivery_pnc varchar(10) NOT NULL," +
            " delivery_immunized varchar(10) NOT NULL," +
            //familyplanning
            " eligible varchar(10) NOT NULL," +
            " provided varchar(10) NOT NULL," +
            " followup varchar(10) NOT NULL," +
            " modern varchar(10) NOT NULL," +
            " condom_users varchar(10) NOT NULL," +
            " pill_users varchar(10) NOT NULL," +
            " injectible_users varchar(10) NOT NULL," +
            " iucd_users varchar(10) NOT NULL," +
            " surgical_users varchar(10) NOT NULL," +
            " other_users varchar(10) NOT NULL," +
            " traditional_users varchar(10) NOT NULL," +
            " referred varchar(10) NOT NULL," +
            " supplied_condoms varchar(10) NOT NULL," +
            " supplied_pills varchar(10) NOT NULL," +
            " supplied_injectibles varchar(10) NOT NULL," +
            //diseases
            " diarrhea_a5 varchar(10) NOT NULL," +
            " diarrhea_u5 varchar(10) NOT NULL," +
            " ari_a5 varchar(10) NOT NULL," +
            " ari_u5 varchar(10) NOT NULL," +
            " fever_a5 varchar(10) NOT NULL," +
            " fever_u5 varchar(10) NOT NULL," +
            " resp_a5 varchar(10) NOT NULL," +
            " resp_u5 varchar(10) NOT NULL," +
            " anaemia_a5 varchar(10) NOT NULL," +
            " anaemia_u5 varchar(10) NOT NULL," +
            " scabies_a5 varchar(10) NOT NULL," +
            " scabies_u5 varchar(10) NOT NULL," +
            " eye_infections_a5 varchar(10) NOT NULL," +
            " eye_infections_u5 varchar(10) NOT NULL," +
            " rtis_a5 varchar(10) NOT NULL," +
            " rtis_u5 varchar(10) NOT NULL," +
            " worm_a5 varchar(10) NOT NULL," +
            " worm_u5 varchar(10) NOT NULL," +
            " malaria_a5 varchar(10) NOT NULL," +
            " malaria_u5 varchar(10) NOT NULL," +
            " referral_a5 varchar(10) NOT NULL," +
            " referral_u5 varchar(10) NOT NULL," +
            " tb_suspect_a5 varchar(10) NOT NULL," +
            " tb_suspect_u5 varchar(10) NOT NULL," +
            " tb_diagnosed_a5 varchar(10) NOT NULL," +
            " tb_diagnosed_u5 varchar(10) NOT NULL," +
            " tb_followed_a5 varchar(10) NOT NULL," +
            " tb_followed_u5 varchar(10) NOT NULL," +
            //live_deaths
            " live varchar(10) NOT NULL," +
            " still varchar(10) NOT NULL," +
            " deaths_all varchar(10) NOT NULL," +
            " noenatal varchar(10) NOT NULL," +
            " infant varchar(10) NOT NULL," +
            " children varchar(10) NOT NULL," +
            " maternal varchar(10) NOT NULL," +
            //medicines
            " tab_paracetamol varchar(10) NOT NULL," +
            " syp_paracetamol varchar(10) NOT NULL," +
            " tab_choloroquin varchar(10) NOT NULL," +
            " syp_choloroquin varchar(10) NOT NULL," +
            " tab_mebendazole varchar(10) NOT NULL," +
            " syp_pipearzine varchar(10) NOT NULL," +
            " ors varchar(10) NOT NULL," +
            " eye_ontiment varchar(10) NOT NULL," +
            " syp_contrimexazole varchar(10) NOT NULL," +
            " iron_tab varchar(10) NOT NULL," +
            " antiseptic_lotion varchar(10) NOT NULL," +
            " benzyle_benzoate_lotion varchar(10) NOT NULL," +
            " sticking_plaster varchar(10) NOT NULL," +
            " b_complex_syp varchar(10) NOT NULL," +
            " cotton_bandages varchar(10) NOT NULL," +
            " cotton_wool varchar(10) NOT NULL," +
            " condoms varchar(10) NOT NULL," +
            " oral_pills varchar(10) NOT NULL," +
            " contraceptive_inj varchar(10) NOT NULL," +
            " med_others varchar(10) NOT NULL," +
            //miscellous
            " lhw_kit_bag varchar(10) NOT NULL," +
            " weighing_machine varchar(10) NOT NULL," +
            " thermometer varchar(10) NOT NULL," +
            " torch_with_cell varchar(10) NOT NULL," +
            " scissors varchar(10) NOT NULL," +
            " syringe_cutter varchar(10) NOT NULL," +
            " mis_others varchar(10) NOT NULL," +
            //supervison
            " lhs varchar(10) NOT NULL," +
            " dco varchar(10) NOT NULL," +
            " adc varchar(10) NOT NULL," +
            " fpo varchar(10) NOT NULL," +
            " ppiu varchar(10) NOT NULL" +
            ")";


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_KMEMBER);
        db.execSQL(CREATE_TABLE_CHILD_GROWTH);
        db.execSQL(CREATE_TABLE_CHILD_BEMARI);
        db.execSQL(CREATE_TABLE_CHILD_VACINATION);
        db.execSQL(CREATE_TABLE_CHILD_MAMLOOM);
        db.execSQL(CREATE_TABLE_CO_MORBIDITY);
        db.execSQL(CREATE_TABLE_SIDE_EFFECTS);
        db.execSQL(CREATE_TABLE_MOTHER_PREGNANCY);
        db.execSQL(CREATE_TABLE_MOTHER_MAMLOOM);
        db.execSQL(CREATE_TABLE_MOTHER_ANC);
        db.execSQL(CREATE_TABLE_MOTHER_PNC);
        db.execSQL(CREATE_TABLE_MOTHER_DELIV);
        db.execSQL(CREATE_TABLE_MOTHER_VACINATION);
        db.execSQL(CREATE_TABLE_COVID_IMMUNIZATION);
        db.execSQL(CREATE_TABLE_FACILITY);
        db.execSQL(CREATE_TABLE_KHANDAN);

        db.execSQL(CREATE_TABLE_COUNTRY);
        db.execSQL(CREATE_TABLE_PROVINCE);
        db.execSQL(CREATE_TABLE_UNIONCOUNCIL);
        db.execSQL(CREATE_TABLE_VACCINES);
        db.execSQL(CREATE_TABLE_VILLAGES);
        db.execSQL(CREATE_TABLE_TEHSIL);
        db.execSQL(CREATE_TABLE_DISTRICT);
        db.execSQL(CREATE_TABLE_REFERAL);
        db.execSQL(CREATE_TABLE_VIDEOS);
        db.execSQL(CREATE_TABLE_LOGINS);
        db.execSQL(CREATE_TABLE_FEEDBACK);

        db.execSQL(CREATE_TABLE_CVACCINE_VIALS);
        db.execSQL(CREATE_TABLE_VACCINE_STOCKS);

        db.execSQL(CREATE_TABLE_MEETING_MEMBER);
        db.execSQL(CREATE_TABLE_MEETING);
        db.execSQL(CREATE_TABLE_MEDICINE);
        db.execSQL(CREATE_TABLE_VACCINE_IMAGE);
        db.execSQL(CREATE_TABLE_MEDICINE_STOCKS);
        db.execSQL(CREATE_TABLE_MEMBER_MERGERD);
        db.execSQL(CREATE_TABLE_MEDICINE_LOG);
        db.execSQL(CREATE_TABLE_MONTHLY_REPORT);
        db.execSQL(CREATE_TABLE_DELETE_MEMBER);
        db.execSQL(CREATE_TABLE_SMS_NUMBER);
        db.execSQL(CREATE_TABLE_DELETE_KHANDAN);

        //db.execSQL(CREATE_TABLE_CVIRUS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*db.execSQL("UPDATE KHANDAN SET metadata = added_on" );
        db.execSQL("ADD TABLE NEW ADD head_nicnumber varchar(50)" );*/
        // this.onCreate(db);

        try {
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            String timeStamp = dates.format(cal.getTime());
            Log.d("000222", "Today Date : " + timeStamp);

            OutputStream myOutput = null;
            InputStream myInput = null;
           /* String databasePath = ctx.getDatabasePath(DATABASE_NAME).getPath();
            File f = new File(databasePath);

            Log.d("000222", " testing db path " + databasePath);
            Log.d("000222", " testing db exist " + f.exists());*/

            try {
                File directory = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "HayatPK"
                        + File.separator + "Backup DB Files"
                        + File.separator);
                if (!directory.exists()) {
                    directory.mkdir();
                }
                Log.d("000222", " testing db path: " + directory);

              /*  myOutput = new FileOutputStream(directory.getAbsolutePath()
                        + "/" + DATABASE_NAME+"-"+user_name+"-"+String.valueOf(System.currentTimeMillis()));*/

                myOutput = new FileOutputStream(Environment.getExternalStorageDirectory()
                        + File.separator + "HayatPK"
                        + File.separator + "Backup DB Files"
                        + File.separator + DATABASE_NAME + "_" + "Ver_" + oldVersion + "-" + timeStamp + ".db");

                Log.d("000222", "Copy File: " + myOutput);

                myInput = new FileInputStream(Environment.getExternalStorageDirectory()
                        + File.separator + "HayatPK"
                        + File.separator + DATABASE_NAME);

                Log.d("000222", "PUTPUT: " + myInput);

                Log.d("000222", " testing db path 1" + String.valueOf(myInput));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
            } catch (Exception e) {
                Log.d("000222", " testing db path 2 " + e.getMessage());
            } finally {
                try {
                    Log.d("000222", " testing 1");
                    if (myOutput != null) {
                        myOutput.close();
                        myOutput = null;
                    }
                    if (myInput != null) {
                        myInput.close();
                        myInput = null;
                    }
                } catch (Exception e) {
                    Log.d("000222", " testing 2");
                }
            }

        /*    try {
                File shm_file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "HayatPK"
                        + File.separator + "HayatPKDB-shm");
                if (shm_file.exists()) {
                    shm_file.delete();
                    Log.d("000222", "IF SHM FILE DELETE SUCCESSFULL");
                } else {
                    Log.d("000222", "ELSE SHM FILE NOT EXITS");
                }
            } catch (Exception e) {
                Log.d("000222", "Error SHM FILE DELETE :  " + e.getMessage());
            }

            try {
                File wal_file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "HayatPK"
                        + File.separator + "HayatPKDB-wal");
                if (wal_file.exists()) {
                    wal_file.delete();
                    Log.d("000222", "IF WAL FILE DELETE SUCCESSFULL");
                } else {
                    Log.d("000222", "ELSE WAL FILE NOT EXITS");
                }
            } catch (Exception e) {
                Log.d("000222", "Error WAL FILE DELETE :  " + e.getMessage());
            }*/


//            if (oldVersion == 1) {
//                db.execSQL(CREATE_TABLE_LOGINS);
//                db.execSQL(CREATE_TABLE_FEEDBACK);
//                Log.d("000222", "onUpgrade IF OLD VERSON 1");
//            }
//            else{
//                Log.d("000222", "onUpgrade ELSE");
//
//            }


            try {

                db.rawQuery("PRAGMA journal_mode='WAL'", null);

                switch (oldVersion) {

                    case 1:
                        db.execSQL(CREATE_TABLE_LOGINS);
                        db.execSQL(CREATE_TABLE_FEEDBACK);
                        Log.d("000222", " CASE 1 onUpgraded OLD VER:" + oldVersion);

                    case 2:
                        db.execSQL("ALTER TABLE COVID_IMMUNIZATION ADD vaccine_id int(5) NOT NULL Default -1");
                        db.execSQL("ALTER TABLE COVID_IMMUNIZATION ADD type int(5) NOT  NULL Default -1");
                        db.execSQL("ALTER TABLE COVID_IMMUNIZATION ADD vaccinated_on varchar(15) NOT NULL Default  -1 ");
                        db.execSQL("ALTER TABLE COVID_IMMUNIZATION ADD image_location text NOT NULL Default  -1 ");

                        Log.d("000222", "CASE 2 onUpgraded OLD VER:" + oldVersion);


                    case 3:
                        db.execSQL(CREATE_TABLE_CVACCINE_VIALS);
                        db.execSQL(CREATE_TABLE_VACCINE_STOCKS);
                        db.execSQL(CREATE_TABLE_MEETING_MEMBER);
                        db.execSQL(CREATE_TABLE_MEETING);
                        db.execSQL(CREATE_TABLE_MEDICINE);
                        db.execSQL(CREATE_TABLE_VACCINE_IMAGE);
                        db.execSQL(CREATE_TABLE_MEDICINE_STOCKS);
                        db.execSQL(CREATE_TABLE_MEMBER_MERGERD);
                        db.execSQL(CREATE_TABLE_MEDICINE_LOG);
                        db.execSQL(CREATE_TABLE_MONTHLY_REPORT);
                        db.execSQL(CREATE_TABLE_DELETE_MEMBER);
                        db.execSQL(CREATE_TABLE_SMS_NUMBER);

                        try {
                            db.execSQL("ALTER TABLE KHANDAN RENAME TO _KHANDAN");
                            db.execSQL(CREATE_TABLE_KHANDAN);
                            db.execSQL("INSERT OR IGNORE INTO KHANDAN SELECT * FROM _KHANDAN");

                            Log.d("000222", "Case 3 (_KHANDAN TABLE) Successfully Rename !!!!!!");


                        } catch (Exception e) {
                            Log.d("000222", "Case 3 ALTER _KHANDAN TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE MEMBER RENAME TO _MEMBER");
                            db.execSQL(CREATE_TABLE_KMEMBER);
                            db.execSQL("INSERT OR IGNORE INTO MEMBER SELECT * FROM _MEMBER");

                            Log.d("000222", "Case 3 (_MEMBER TABLE) Successfully Rename !!!!!!");


                        } catch (Exception e) {
                            Log.d("000222", "Case 3 ALTER _MEMBER TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE CVACCINATION RENAME TO _CVACCINATION");
                            db.execSQL(CREATE_TABLE_CHILD_VACINATION);
                            db.execSQL("INSERT OR IGNORE INTO CVACCINATION SELECT * FROM _CVACCINATION");

                            Log.d("000222", "Case 3 (_CVACCINATION TABLE) Successfully Rename !!!!!!");


                        } catch (Exception e) {
                            Log.d("000222", " Case 3 ALTER _CVACCINATION TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE COVID_IMMUNIZATION RENAME TO _COVID_IMMUNIZATION");
                            db.execSQL(CREATE_TABLE_MOTHER_VACINATION);
                            db.execSQL("INSERT OR IGNORE INTO COVID_IMMUNIZATION SELECT * FROM _COVID_IMMUNIZATION");

                            Log.d("000222", "Case 3 (_MVACCIENE TABLE) Successfully Rename !!!!!!");

                        } catch (Exception e) {
                            Log.d("000222", "Case 3 ALTER _MVACCINE TABLE ERROR: " + e.getMessage());
                        }

                        /*    db.execSQL("ALTER TABLE VACCINE_STOCK ADD is_synced varchar(1)  NOT NULL Default '0'");
                       db.execSQL("ALTER TABLE MEDICINE_STOCK ADD is_synced varchar(1)  NOT NULL Default '0'");
                       db.execSQL("ALTER TABLE MEMBER_MERGED ADD is_synced varchar(1)  NOT NULL Default '0'");*/

                        Log.d("000222", "CASE 3 onUpgraded OLD VER:" + oldVersion);

                    case 4:
                        db.execSQL(CREATE_TABLE_CVACCINE_VIALS);
                        db.execSQL(CREATE_TABLE_VACCINE_STOCKS);
                        db.execSQL(CREATE_TABLE_MEETING_MEMBER);
                        db.execSQL(CREATE_TABLE_MEETING);
                        db.execSQL(CREATE_TABLE_MEDICINE);
                        db.execSQL(CREATE_TABLE_VACCINE_IMAGE);
                        db.execSQL(CREATE_TABLE_MEDICINE_STOCKS);
                        db.execSQL(CREATE_TABLE_MEMBER_MERGERD);
                        db.execSQL(CREATE_TABLE_MEDICINE_LOG);
                        db.execSQL(CREATE_TABLE_MONTHLY_REPORT);
                        db.execSQL(CREATE_TABLE_DELETE_MEMBER);
                        db.execSQL(CREATE_TABLE_SMS_NUMBER);
                        db.execSQL(CREATE_TABLE_DELETE_KHANDAN);

                        try {
                            db.execSQL("ALTER TABLE KHANDAN RENAME TO _KHANDAN");
                            db.execSQL(CREATE_TABLE_KHANDAN);
                            db.execSQL("INSERT OR IGNORE INTO KHANDAN SELECT * FROM _KHANDAN");

                            Log.d("000222", "Case 4 (_KHANDAN TABLE) Successfully Rename !!!!!!");

                        } catch (Exception e) {
                            Log.d("000222", "Case 4 ALTER _KHANDAN TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE MEMBER RENAME TO _MEMBER");
                            db.execSQL(CREATE_TABLE_KMEMBER);
                            db.execSQL("INSERT OR IGNORE INTO MEMBER SELECT * FROM _MEMBER");

                            Log.d("000222", "Case 4 (_KHANDAN TABLE) Successfully Rename !!!!!!");

                        } catch (Exception e) {
                            Log.d("000222", "Case 4 ALTER _MEMBER TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE CVACCINATION RENAME TO _CVACCINATION");
                            db.execSQL(CREATE_TABLE_CHILD_VACINATION);
                            db.execSQL("INSERT OR IGNORE INTO CVACCINATION SELECT * FROM _CVACCINATION");

                            Log.d("000222", "Case 4 (_CVACCINATION TABLE) Successfully Rename !!!!!!");


                        } catch (Exception e) {
                            Log.d("000222", "Case 4 ALTER _CVACCINATION TABLE ERROR: " + e.getMessage());
                        }

                        try {
                            db.execSQL("ALTER TABLE COVID_IMMUNIZATION RENAME TO _MVACINE2");
                            db.execSQL(CREATE_TABLE_MOTHER_VACINATION);
                            db.execSQL("INSERT OR IGNORE INTO MVACINE(member_uid,record_data,data,added_by,is_synced,added_on,vaccine_id,type,vaccinated_on,image_location) SELECT member_uid,record_data,data,added_by,is_synced,added_on,vaccine_id,type,vaccinated_on,image_location FROM _MVACINE");

                            Log.d("000222", "Case 4 (_MVACCINE TABLE) Successfully Rename !!!!!!");

                        } catch (Exception e) {
                            Log.d("000222", "Case 4 ALTER _MVACCINE TABLE ERROR: " + e.getMessage());
                        }


                        Log.d("000222", "CASE 4 onUpgraded OLD VER:" + oldVersion);


                    /*case 5:

                        try {
                            db.execSQL(CREATE_TABLE_CVIRUS);
                        }catch (Exception e)
                        {
                            Log.d("000222", "CVirus TABLE ERROR: " + e.getMessage());
                        }

                        Log.d("000222", "CASE 5 onUpgraded OLD VER:" + oldVersion);*/

                        break;

                    default:
                        break;
                }

                Toast.makeText(context, context.getString(R.string.dbChangedMessage) + oldVersion + " to " + newVersion, Toast.LENGTH_LONG).show();
                Log.d("000222", "SQL onUpgrade OldVersion: " + oldVersion + "   NewVersion: " + newVersion);

            } catch (Exception e) {
                Log.d("000222", "Error: " + e.getMessage());
            }


        } catch (Exception e) {
            Log.d("000222", "Error onUpgrade :  " + e);
        }


        //this.onCreate(db);
    }


   /* public void getSqliteVersion(){
        Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", "abc123", null).rawQuery("select sqlite_version() AS sqlite_version", null);
        String sqliteVersion = "";
        while(cursor.moveToNext()){
            sqliteVersion += cursor.getString(0);
        }
        Log.d("SQLITE VERSION: ", sqliteVersion);
    }*/

}