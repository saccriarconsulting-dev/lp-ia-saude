package com.axys.redeflexmobile.shared.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.axys.redeflexmobile.BuildConfig;
import com.axys.redeflexmobile.ui.redeflex.Config;

import java.io.File;

import timber.log.Timber;

/**
 * Created by Desenvolvimento on 25/04/2016.
 */
public class MyMultiThreadSQLiteOpenHelper extends MultiThreadSQLiteOpenHelper {

    private static final String DATABASE_NAME = Config.PROJECT_NAME + ".db";

    private String comando;

    public static String getDatabasePath(Context context) {
        String dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        } else {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        Log.i("log", "dir: " + dir);
        return dir + File.separator + "RDF" + File.separator;
    }

    MyMultiThreadSQLiteOpenHelper(Context context) {
        super(context, getDatabasePath(context) + DATABASE_NAME, null, BuildConfig.DB_VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        CreateBaseVersao1(database);
        UpdateBaseVersao2(database);
        UpdateBaseVersao3(database);
        UpdateBaseVersao4(database);
        UpdateBaseVersao5(database);
        UpdateBaseVersao6(database);
        UpdateBaseVersao7(database);
        UpdateBaseVersao8(database);
        UpdateBaseVersao9(database);
        UpdateBaseVersao10(database);
        UpdateBaseVersao11(database);
        UpdateBaseVersao12(database);
        UpdateBaseVersao13(database);
        UpdateBaseVersao14(database);
        UpdateBaseVersao15(database);
        UpdateBaseVersao16(database);
        UpdateBaseVersao17(database);
        UpdateBaseVersao18(database);
        UpdateBaseVersao19(database);
        UpdateBaseVersao20(database);
        UpdateBaseVersao21(database);
        UpdateBaseVersao22(database);
        updateBaseVersao23(database);
        updateBaseVersao24(database);
        updateBaseVersao25(database);
        updateBaseVersao26(database);
        updateBaseVersao27(database);
        updateBaseVersao28(database);
        updateBaseVersao29(database);
        updateBaseVersao30(database);
        updateBaseVersao31(database);
        updateBaseVersao32(database);
        updateBaseVersao33(database);
        updateBaseVersao34(database);
        updateBaseVersao35(database);
        updateBaseVersao36(database);
        updateBaseVersao37(database);
        updateBaseVersao38(database);
        updateBaseVersao39(database);
        updateBaseVersao40(database);
        updateBaseVersao41(database);
        updateBaseVersao42(database);
        updateBaseVersao43(database);
        updateBaseVersao44(database);
        updateBaseVersao45(database);
        updateBaseVersao46(database);
        updateBaseVersao47(database);
        updateBaseVersao48(database);
        updateBaseVersao49(database);
        updateBaseVersao50(database);
        updateBaseVersao51(database);
        updateBaseVersao52(database);
        updateBaseVersao53(database);
        updateBaseVersao54(database);
        updateBaseVersao55(database);
        updateBaseVersao56(database);
        updateBaseVersao57(database);
        updateBaseVersao58(database);
        updateBaseVersao59(database);
        updateBaseVersao60(database);
        updateBaseVersao61(database);
        updateBaseVersao62(database);
        updateBaseVersao63(database);
        updateBaseVersao64(database);
        updateBaseVersao65(database);
        updateBaseVersao66(database);
        updateBaseVersao67(database);
        updateBaseVersao68(database);
        updateBaseVersao69(database);
        updateBaseVersao70(database);
        updateBaseVersao71(database);
        updateBaseVersao72(database);
        updateBaseVersao73(database);
        updateBaseVersao74(database);
        updateBaseVersao75(database);
        updateBaseVersao76(database);
        updateBaseVersao77(database);
        updateBaseVersao78(database);
        updateBaseVersao79(database);
        updateBaseVersao80(database);
        updateBaseVersao81(database);
        updateBaseVersao82(database);
        updateBaseVersao83(database);
        updateBaseVersao84(database);
        updateBaseVersao85(database);
        updateBaseVersao86(database);
        updateBaseVersao87(database);
        updateBaseVersao88(database);
        updateBaseVersao89(database);
        updateBaseVersao90(database);
        updateBaseVersao91(database);
        updateBaseVersao92(database);
        updateBaseVersao93(database);
        updateBaseVersao94(database);
        updateBaseVersao95(database);
        updateBaseVersao96(database);
        updateBaseVersao97(database);
        updateBaseVersao98(database);
        updateBaseVersao99(database);
        updateBaseVersao100(database);
        updateBaseVersao101(database);
        updateBaseVersao102(database);
        updateBaseVersao103(database);
        updateBaseVersao104(database);
        updateBaseVersao105(database);
        updateBaseVersao106(database);
        updateBaseVersao107(database);
        updateBaseVersao108(database);
        updateBaseVersao109(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                UpdateBaseVersao2(database);
                UpdateBaseVersao3(database);
                UpdateBaseVersao4(database);
                UpdateBaseVersao5(database);
                UpdateBaseVersao6(database);
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 2:
                UpdateBaseVersao3(database);
                UpdateBaseVersao4(database);
                UpdateBaseVersao5(database);
                UpdateBaseVersao6(database);
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 3:
                UpdateBaseVersao4(database);
                UpdateBaseVersao5(database);
                UpdateBaseVersao6(database);
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);

                break;
            case 4:
                UpdateBaseVersao5(database);
                UpdateBaseVersao6(database);
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 5:
                UpdateBaseVersao6(database);
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 6:
                UpdateBaseVersao7(database);
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 7:
                UpdateBaseVersao8(database);
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 8:
                UpdateBaseVersao9(database);
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 9:
                UpdateBaseVersao10(database);
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 10:
                UpdateBaseVersao11(database);
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                break;
            case 11:
                UpdateBaseVersao12(database);
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 12:
                UpdateBaseVersao13(database);
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 13:
                UpdateBaseVersao14(database);
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 14:
                UpdateBaseVersao15(database);
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 15:
                UpdateBaseVersao16(database);
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 16:
                UpdateBaseVersao17(database);
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 17:
                UpdateBaseVersao18(database);
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 18:
                UpdateBaseVersao19(database);
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 19:
                UpdateBaseVersao20(database);
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 20:
                UpdateBaseVersao21(database);
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 21:
                UpdateBaseVersao22(database);
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 22:
                updateBaseVersao23(database);
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 23:
                updateBaseVersao24(database);
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 24:
                updateBaseVersao25(database);
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 25:
                updateBaseVersao26(database);
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 26:
                updateBaseVersao27(database);
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 27:
                updateBaseVersao28(database);
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 28:
                updateBaseVersao29(database);
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 29:
                updateBaseVersao30(database);
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 30:
                updateBaseVersao31(database);
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 31:
                updateBaseVersao32(database);
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 32:
                updateBaseVersao33(database);
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 33:
                updateBaseVersao34(database);
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 34:
                updateBaseVersao35(database);
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 35:
                updateBaseVersao36(database);
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 36:
                updateBaseVersao37(database);
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 37:
                updateBaseVersao38(database);
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 38:
                updateBaseVersao39(database);
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 39:
                updateBaseVersao40(database);
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 40:
                updateBaseVersao41(database);
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 41:
                updateBaseVersao42(database);
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 42:
                updateBaseVersao43(database);
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 43:
                updateBaseVersao44(database);
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 44:
                updateBaseVersao45(database);
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 45:
                updateBaseVersao46(database);
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 46:
                updateBaseVersao47(database);
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 47:
                updateBaseVersao48(database);
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 48:
                updateBaseVersao49(database);
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 49:
                updateBaseVersao50(database);
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 50:
                updateBaseVersao51(database);
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 51:
                updateBaseVersao52(database);
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 52:
                updateBaseVersao53(database);
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 53:
                updateBaseVersao54(database);
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 54:
                updateBaseVersao55(database);
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 55:
                updateBaseVersao56(database);
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 56:
                updateBaseVersao57(database);
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 57:
                updateBaseVersao58(database);
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 58:
                updateBaseVersao59(database);
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 59:
                updateBaseVersao60(database);
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 60:
                updateBaseVersao61(database);
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 61:
                updateBaseVersao62(database);
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 62:
                updateBaseVersao63(database);
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 63:
                updateBaseVersao64(database);
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 64:
                updateBaseVersao65(database);
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 65:
                updateBaseVersao66(database);
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 66:
                updateBaseVersao67(database);
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 67:
                updateBaseVersao68(database);
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 68:
                updateBaseVersao69(database);
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 69:
                updateBaseVersao70(database);
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 70:
                updateBaseVersao71(database);
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 71:
                updateBaseVersao72(database);
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 72:
                updateBaseVersao73(database);
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 73:
                updateBaseVersao74(database);
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 74:
                updateBaseVersao75(database);
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 75:
                updateBaseVersao76(database);
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 76:
                updateBaseVersao77(database);
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 77:
                updateBaseVersao78(database);
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 78:
                updateBaseVersao79(database);
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 79:
                updateBaseVersao80(database);
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                break;
            case 80:
                updateBaseVersao81(database);
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                break;
            case 81:
                updateBaseVersao82(database);
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                break;
            case 82:
                updateBaseVersao83(database);
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                break;
            case 83:
                updateBaseVersao84(database);
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                break;
            case 84:
                updateBaseVersao85(database);
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                break;
            case 85:
                updateBaseVersao86(database);
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                break;
            case 86:
                updateBaseVersao87(database);
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                break;
            case 87:
                updateBaseVersao88(database);
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                break;
            case 88:
                updateBaseVersao89(database);
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                break;
            case 89:
                updateBaseVersao90(database);
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                updateBaseVersao95(database);
                break;
            case 90:
                updateBaseVersao91(database);
                updateBaseVersao92(database);
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                updateBaseVersao95(database);
                break;
            case 91:
                updateBaseVersao92(database);
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                updateBaseVersao95(database);
                updateBaseVersao96(database);
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 92:
                updateBaseVersao93(database);
                updateBaseVersao94(database);
                updateBaseVersao95(database);
                updateBaseVersao96(database);
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 93:
                updateBaseVersao94(database);
                updateBaseVersao95(database);
                updateBaseVersao96(database);
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 94:
                updateBaseVersao95(database);
                updateBaseVersao96(database);
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 95:
                updateBaseVersao96(database);
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 96:
                updateBaseVersao97(database);
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                break;
            case 97:
                updateBaseVersao98(database);
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                break;
            case 98:
                updateBaseVersao99(database);
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                updateBaseVersao105(database);
                break;
            case 99:
                updateBaseVersao100(database);
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                break;
            case 100:
                updateBaseVersao101(database);
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                updateBaseVersao105(database);
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 101:
                updateBaseVersao102(database);
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                updateBaseVersao105(database);
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 102:
                updateBaseVersao103(database);
                updateBaseVersao104(database);
                updateBaseVersao105(database);
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 103:
                updateBaseVersao104(database);
                updateBaseVersao105(database);
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 104:
                updateBaseVersao105(database);
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 105:
                updateBaseVersao106(database);
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
            case 106:
                updateBaseVersao107(database);
                updateBaseVersao108(database);
                updateBaseVersao109(database);
                break;
        }
        if (oldVersion < 109 && newVersion >= 109) {
            updateBaseVersao109(database);
        }
    }

    private void updateBaseVersao109(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Produto] ADD COLUMN [valorPorFormaPagto] Text;");
    }

    private void updateBaseVersao108(SQLiteDatabase database) {
        // NO-OP intencional.
        // Não adicionar colunas aqui porque a versão atual não precisa delas.
    }
    private void updateBaseVersao107(SQLiteDatabase database){
        execute(database, "alter table InformacoesGeraisPOS add valorpix_transacionado DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add valor_pix_transmesatual DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add valor_pix_transmesanterior DECIMAL(10, 2);");
    }

    private void updateBaseVersao106(SQLiteDatabase database) {
        comando = "ALTER TABLE [Colaborador] ADD COLUMN [CicloRoteirizacao] Int;";
        this.execute(database, comando);
    }

    private void updateBaseVersao105(SQLiteDatabase database) {
        comando = "ALTER TABLE [Venda] ADD COLUMN [ComprovantePagto] Text;";
        this.execute(database, comando);
    }

    private void updateBaseVersao104(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [VendaAvista] Text;";
        this.execute(database, comando);

        comando = "ALTER TABLE [ProspeccaoClienteAdquirencia] ADD COLUMN [TaxaRav] DECIMAL(10, 2);";
        this.execute(database, comando);
    }

    private void updateBaseVersao103(SQLiteDatabase database) {
        comando = "Create Table if not exists  SolicitacaoPid (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "Id_Server integer," +
                "TipoPessoa text not null," +
                "TipoCliente text not null," +
                "IdVendedor integer not null," +
                "Latitude DECIMAL(20, 16)," +
                "Longitude DECIMAL(20, 16)," +
                "Precisao DECIMAL(20, 16)," +
                "CodigoSGV text," +
                "CodigoAdquirencia integer," +
                "CpfCnpj text not null," +
                "RazaoSocial text not null," +
                "NomeFantasia text not null," +
                "Observacao text," +
                "MCCPrincipal integer," +
                "DDD integer," +
                "Rede integer," +
                "NDeLojas integer," +
                "FaturamentoPrevisto decimal," +
                "NomeConcorrente text," +
                "DistribuicaoDebito double," +
                "DistribuicaoCredito double," +
                "DistribuicaoCredito6x double," +
                "DistribuicaoCredito12x double," +
                "Origem text," +
                "Renegociacao integer," +
                "Contraproposta integer," +
                "Reprecificada integer," +
                "Sincronizado integer," +
                "DataSync Date," +
                "DataCadastro Date," +
                "DataAvaliacao Date," +
                "DataEnvioTermo Date," +
                "Status text," +
                "PropostaId integer)";
        this.execute(database, comando);

        comando = "Create Table if not exists  SolicitacaoPidRede (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "IdSolicitacaoPid integer," +
                "CpfCnpj text not null," +
                "Mcc text not null," +
                "TpvTotal decimal," +
                "TpvPorcentagem double)";
        this.execute(database, comando);

        comando = "Create Table if not exists SolicitacaoPidPos (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "IdSolicitacaoPid integer," +
                "TipoMaquinaId integer," +
                "ValorAluguel decimal," +
                "TipoConexaoId integer," +
                "IdOperadora integer," +
                "MetragemCabo integer," +
                "Quantidade integer," +
                "Situacao integer)";
        this.execute(database, comando);

        comando = "Create Table if not exists SolicitacaoPidProduto (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "IdSolicitacaoPid integer," +
                "BandeiraTipoId integer," +
                "TaxaDebito integer," +
                "TaxaCredito integer," +
                "TaxaCredito6x integer," +
                "TaxaCredito12x integer," +
                "TaxaRavAutomatica integer," +
                "TaxaRavEventual integer," +
                "Aluguel integer)";
        this.execute(database, comando);

        comando = "Create Table if not exists SolicitacaoPidAnexo (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "IdSolicitacaoPid integer," +
                "Tipo Text," +
                "Anexo Text," +
                "NomeArquivo Text," +
                "TipoArquivo Text," +
                " Latitude DECIMAL(20, 16)," +
                " Longitude DECIMAL(20, 16)," +
                " Precisao DECIMAL(20, 16)," +
                " Sync integer," +
                "Tamanhoarquivo text)";
        this.execute(database, comando);

        comando = "Create Table if not exists SolicitacaoPidTaxaMDR (" +
                "Id integer not null primary key AUTOINCREMENT," +
                "IdSolicitacaoPid integer," +
                "BandeiraTipoId integer," +
                "TaxaDebito DECIMAL(11, 2)," +
                "TaxaCredito DECIMAL(11, 2)," +
                "TaxaCredito6x DECIMAL(11, 2)," +
                "TaxaCredito12x DECIMAL(11, 2)," +
                "TaxaRavAutomatica DECIMAL(11, 2)," +
                "TaxaRavEventual DECIMAL(11, 2)," +
                "TaxaDebitoConcorrente DECIMAL(11, 2)," +
                "TaxaCreditoConcorrente DECIMAL(11, 2)," +
                "TaxaCredito6xConcorrente DECIMAL(11, 2)," +
                "TaxaCredito12xConcorrente DECIMAL(11, 2)," +
                "TaxaRavAutomaticaConcorrente DECIMAL(11, 2)," +
                "TaxaRavEventualConcorrente DECIMAL(11, 2)," +
                "TaxaDebitoContraproposta DECIMAL(11, 2)," +
                "TaxaCreditoContraproposta DECIMAL(11, 2)," +
                "TaxaCredito6xContraproposta DECIMAL(11, 2)," +
                "TaxaCredito12xContraproposta DECIMAL(11, 2)," +
                "TaxaRavAutomaticaContraproposta DECIMAL(11, 2)," +
                "TaxaRavEventualContraproposta DECIMAL(11, 2)," +
                "TaxaSimuladorTPVDebito DECIMAL(11, 2)," +
                "TaxaSimuladorTPVCredito DECIMAL(11, 2)," +
                "TaxaSimuladorTPVCredito6x DECIMAL(11, 2)," +
                "TaxaSimuladorTPVCredito12x DECIMAL(11, 2)," +
                "TaxaSimuladorIntercambioDebito DECIMAL(11, 2)," +
                "TaxaSimuladorIntercambioCredito DECIMAL(11, 2)," +
                "TaxaSimuladorIntercambioCredito6x DECIMAL(11, 2)," +
                "TaxaSimuladorIntercambioCredito12x DECIMAL(11, 2)," +
                "TaxaSimuladorNetMDRDebito DECIMAL(11, 2)," +
                "TaxaSimuladorNetMDRCredito DECIMAL(11, 2)," +
                "TaxaSimuladorNetMDRCredito6x DECIMAL(11, 2)," +
                "TaxaSimuladorNetMDRCredito12x DECIMAL(11, 2))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [QrCodeLink] Text;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [ChaveCobranca] Text;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [Pago] int;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [DataExpiracaoPix] DATETIME;";
        this.execute(database, comando);

        // Ajuste campos TaxaMDR
        this.execute(database, "ALTER TABLE [TaxaMDR] ADD COLUMN [TipoClassificacao] varchar(3);");
        this.execute(database, "ALTER TABLE [TaxaMDR] ADD COLUMN [TipoNegociacao] integer;");
        this.execute(database, "ALTER TABLE [TaxaMDR] ADD COLUMN [RAV] integer;");

        // Criar campo Cliente Cadastro
        this.execute(database, "alter table ClienteCadastro add COLUMN IdSolPid_Server integer;");
    }

    private void updateBaseVersao102(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [precoVendaMinimo] DECIMAL(10, 2);";
        this.execute(database, comando);
    }

    private void updateBaseVersao101(SQLiteDatabase database) {
        comando = "create table if not exists SolicitacaoPrecoDiferenciado (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdServerSolicitacao integer," +
                " IdVendedor Integer," +
                " NomeSolicitante text," +
                " DataSolicitacao Date," +
                " DataInicial Date," +
                " DataFinal Date," +
                " SituacaoId integer," +
                " TipoId integer," +
                " DataCriacao Date," +
                " DataAtualizacao Date," +
                " Sync integer," +
                " DataSincronizacao Date)";
        this.execute(database, comando);

        comando = "create table if not exists SolicitacaoPrecoDiferenciadoDetalhe (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdSolicitacao integer," +
                " IdServer integer," +
                " DDD integer," +
                " IdCliente integer," +
                " CodigoClienteSGV text," +
                " ItemCode text," +
                " Quantidade Integer," +
                " PrecoVenda DECIMAL(10, 2)," +
                " PrecoDiferenciado DECIMAL(10, 2)," +
                " TipoPagamentoId Integer," +
                " StatusId Integer," +
                " IdVendedor integer," +
                " MotivoRecusada text," +
                " Aplicada text," +
                " Justificativa Text)";
        this.execute(database, comando);
    }

    private void updateBaseVersao100(SQLiteDatabase database) {
        comando = "ALTER TABLE [PistolagemTemp] ADD COLUMN [AuditadoCons] varchar(1);";
        this.execute(database, comando);
    }

    private void updateBaseVersao99(SQLiteDatabase database) {
        comando = "create table if not exists Vendedor (" +
                " IdVendedor integer not null primary key," +
                " Vendedor text not null," +
                " SemanaRota integer)";
        this.execute(database, comando);

        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [idVendedor] integer");
    }

    private void updateBaseVersao98(SQLiteDatabase database) {
        comando = "create table if not exists IccidOperadora (" +
                " Id integer not null primary key," +
                " OperadoraId integer not null," +
                " InicioCodBarra text not null," +
                " Ativo int)";
        this.execute(database, comando);
    }

    private void updateBaseVersao97(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [RelatorioMeta] ADD COLUMN [tendencia] decimal;");
    }

    private void updateBaseVersao96(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteMigracaoAdq] INTEGER default 0;");

        execute(database, "Alter Table CadastroMigracaoSub add Column Sexo Varchar(1)");
        execute(database, "Alter Table CadastroMigracaoSub add Column RG Varchar(20)");
        execute(database, "Alter Table CadastroMigracaoSub add Column IdProfissao int");
        execute(database, "Alter Table CadastroMigracaoSub add Column IdRendaMensal int");
        execute(database, "Alter Table CadastroMigracaoSub add Column IdPatrimonio int");
        execute(database, "Alter Table CadastroMigracaoSub add Column FaturamentoBruto decimal");
        execute(database, "Alter Table CadastroMigracaoSub add Column PercVendaCartao int");
        execute(database, "Alter Table CadastroMigracaoSub add Column PercFaturamentoEcommerce int");
        execute(database, "Alter Table CadastroMigracaoSub add Column PercFaturamento int");
        execute(database, "Alter Table CadastroMigracaoSub add Column EntregaPosCompra text");
        execute(database, "Alter Table CadastroMigracaoSub add Column PrazoEntrega int");
        execute(database, "Alter Table CadastroMigracaoSub add Column PercEntregaImediata int");
        execute(database, "Alter Table CadastroMigracaoSub add Column PercEntregaPosterior int");
        execute(database, "Alter Table CadastroMigracaoSub add Column DataFundacaoPF date");
        execute(database, "Alter Table CadastroMigracaoSub add Column TipoMigracao Varchar(3)");

        comando = "create table if not exists CadastroMigracaoSubHorario (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdCliente integer not null," +
                " IdCadastroMigracao integer not null," +
                " DiaAtendimentoId integer not null," +
                " Aberto integer not null," +
                " HoraInicio text not null," +
                " HoraFim text not null)";
        this.execute(database, comando);

        comando = "create table if not exists FaixaSalarial (" +
                " id integer not null primary key," +
                " descricao text not null," +
                " situacao VARCHAR(1) DEFAULT 'A')";
        this.execute(database, comando);

        comando = "create table if not exists FaixaPatrimonial (" +
                " id integer not null primary key," +
                " descricao text not null," +
                " situacao VARCHAR(1) DEFAULT 'A')";
        this.execute(database, comando);

        comando = "create table if not exists FaixaDeFaturamentoMensal (" +
                " id integer not null primary key," +
                " descricao text not null," +
                " situacao VARCHAR(1) DEFAULT 'A')";
        this.execute(database, comando);

        comando = "create table if not exists Profissoes (" +
                " id integer not null primary key," +
                " descricao text not null," +
                " situacao VARCHAR(1) DEFAULT 'A')";
        this.execute(database, comando);

        comando = "create table if not exists ClienteCadastroHorarioFuncionamento (" +
                " id integer not null primary key AUTOINCREMENT," +
                " idCadastro integer not null," +
                " diaAtendimentoId integer not null," +
                " aberto intger not null," +            // 0 - Fechado 1 - Aberto
                " horaInicio text," +
                " horaFim text)";
        this.execute(database, comando);

        comando = "create table ClienteCadastroContatos (" +
                " id integer not null primary key AUTOINCREMENT," +
                " idCadastro integer not null," +
                " nome text not null," +
                " telefone text," +
                " celular text)";
        this.execute(database, comando);

        comando = "create table ClienteCadastroSocios (" +
                " id integer not null primary key AUTOINCREMENT," +
                " idCadastro integer not null," +
                " nome text not null," +
                " dataNascimento Date," +
                " cpf text," +
                " idProfissao integer," +
                " idRenda integer," +
                " idPatrimonio integer," +
                " email text," +
                " telefone text," +
                " celular text," +
                " tipoSocio integer)";              // Fixo 0
        this.execute(database, comando);

        comando = "create table ClienteCadastroContatoPrincipal (" +
                " id integer not null primary key AUTOINCREMENT," +
                " idCadastro integer not null," +
                " nome text not null," +
                " email text," +
                " telefone text," +
                " celular text)";
        this.execute(database, comando);

        this.execute(database, "alter table ClienteCadastro add COLUMN idProfissao integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN idRenda integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN idPatrimonio integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN sexo text;");
        this.execute(database, "alter table ClienteCadastro add COLUMN faturamentoBruto decimal;");
        this.execute(database, "alter table ClienteCadastro add COLUMN percVendaCartao integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN percVendaEcommerce integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN percFaturamento integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN percEntregaImediata integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN percEntregaPosterior integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN prazoEntrega integer;");
        this.execute(database, "alter table ClienteCadastro add COLUMN entregaPosCompra text;");
        this.execute(database, "alter table ClienteCadastro add COLUMN dataFundacaoPF Date;");

        comando = "CREATE TABLE IF NOT EXISTS TipoLogradouro (" +
                "  [id] integer," +
                "  [descricao] VARCHAR(100)," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";
        this.execute(database, comando);
    }

    private void updateBaseVersao95(SQLiteDatabase database) {
        comando = "create table if not exists ConsignadoLimiteCliente (" +
                " Id integer not null primary key," +
                " IdCliente integer not null," +
                " CodigoClienteSGV text not null," +
                " Ativo text)";
        this.execute(database, comando);

        comando = "create table if not exists ConsignadoLimiteProduto (" +
                " Id integer not null primary key," +
                " IdConsignadoLimiteCliente integer not null," +
                " IdProduto Varchar(20) not null," +
                " Quantidade int not null)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [VendaConsignada] integer DEFAULT 1;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [AuditagemConsignadaObriga] integer DEFAULT 1;";
        this.execute(database, comando);

        comando = "create table if not exists Consignado (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdVendedor integer not null," +
                " TipoConsignado varchar(3)," + // CON ou DEV
                " IdCliente integer not null," +
                " IdServer integer," +
                " IdVisita integer," +
                " DataEmissao Datetime," +
                " ValorTotal DECIMAL(10, 2)," +
                " DataSync Datetime," +
                " Latitude DECIMAL(20, 16)," +
                " Longitude DECIMAL(20, 16)," +
                " Precisao DECIMAL(20, 16)," +
                " VersaoApp varchar(50)," +
                " IdConsignadoRefer integer," +
                " Status varchar(20))";
        this.execute(database, comando);

        comando = "create table if not exists ConsignadoItem (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdConsignado integer not null," +
                " IdServer integer," +
                " IdProduto Varchar(20) not null," +
                " Qtd int not null," +
                " ValorUnit DECIMAL(10, 2)," +
                " ValorTotalItem DECIMAL(10, 2)," +
                " QtdCombo int null," +
                " Cancelado int null)";
        this.execute(database, comando);

        comando = "create table if not exists ConsignadoItemCodBarra (" +
                " Id integer not null primary key AUTOINCREMENT," +
                " IdConsignado integer not null," +
                " IdConsignadoItem integer not null," +
                " IdServer integer," +
                " CodigoBarraIni Varchar(20)," +
                " CodigoBarraFim Varchar(20)," +
                " Qtd int not null," +
                " Cancelado int null)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ComboTemp] ADD COLUMN [IdConsignado] integer;";
        this.execute(database, comando);

        comando = "ALTER TABLE [PistolagemTemp] ADD COLUMN [IdConsignado] integer;";
        this.execute(database, comando);

        comando = "ALTER TABLE [PistolagemComboTemp] ADD COLUMN [IdConsignado] integer;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [IdConsignadoRefer] integer;";
        this.execute(database, comando);

    }

    private void updateBaseVersao94(SQLiteDatabase database) {
        comando = "ALTER TABLE [LocalizacaoCliente] ADD COLUMN [tipoId] integer";
        this.execute(database, comando);
    }

    private void updateBaseVersao93(SQLiteDatabase database) {
        execute(database, "alter table InformacoesGeraisPOS add ValorTransMesAtual DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add ValorTransMesAnterior DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add TransacionadoRecarga varchar(100);");
        execute(database, "alter table InformacoesGeraisPOS add ValorRecarga_Transacionado DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add ValorRecarga_TransMesAtual DECIMAL(10, 2);");
        execute(database, "alter table InformacoesGeraisPOS add ValorRecarga_TransMesAnterior DECIMAL(10, 2);");
    }

    private void updateBaseVersao92(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [Premium] INTEGER DEFAULT 0;");
    }

    private void updateBaseVersao91(SQLiteDatabase database) {
        comando = "create table if not exists InformacaoCorban (" +
                " idcliente integer not null primary key," +
                " codigocorban integer not null," +
                " loja text," +
                " curva text," +
                " lod decimal," +
                " situacao text," +
                " valor decimal," +
                " dias integer," +
                " dataativacao date," +
                " dataatualizacao date," +
                " dataultimatransacao date)";
        this.execute(database, comando);

        comando = "";
        comando = "create table if not exists InformacaoCorbanTransacao (" +
                " idcliente integer not null," +
                " anomes integer not null," +
                " qtdtransacao integer," +
                " CONSTRAINT [PK_InformacaoCorbanTransacao] PRIMARY KEY(idcliente, anomes))";
        this.execute(database, comando);
    }

    private void updateBaseVersao90(SQLiteDatabase database) {
        //execute(database, "ALTER TABLE [SugestaoVenda] ADD COLUMN [SituacaoCliente] TEXT;");
        //execute(database, "ALTER TABLE [SugestaoVenda] ADD COLUMN [DescricaoGrupo] TEXT;");
    }

    private void updateBaseVersao89(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS CampanhaMerchanClaroMaterial (" +
                "  [idMaterial] INTEGER NOT NULL PRIMARY KEY," +
                "  [descricao] VARCHAR(100)," +
                "  [idCampanha] INTEGER," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);

        execute(database, "ALTER TABLE [ComprovanteSkyTa] ADD COLUMN [idMaterial] INTEGER;");
        execute(database, "ALTER TABLE [ComprovanteSkyTa] ADD COLUMN [interno] INTEGER;");
    }

    private void updateBaseVersao88(SQLiteDatabase database) {
        this.execute(database, "DROP TABLE [ComprovanteSkyTa]");
        this.execute(database, "CREATE TABLE [ComprovanteSkyTa] (" +
                "[id] INTEGER PRIMARY KEY AUTOINCREMENT," +
                "[idColaborador] INTEGER," +
                "[idCliente] INTEGER," +
                "[tipo] INTEGER," +
                "[anexo] TEXT," +
                "[sync] TINYINT DEFAULT 0," +
                "[idCampanha] INTEGER," +
                "[data] DATETIME DEFAULT (datetime('now','localtime'))" +
                ")");
    }

    private void updateBaseVersao87(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [clienteCorban] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao86(SQLiteDatabase database) {
        comando = "ALTER TABLE [Colaborador] ADD COLUMN [email] VARCHAR(500);";
        this.execute(database, comando);
    }

    private void updateBaseVersao85(SQLiteDatabase database) {
        comando = DBCredencial.GERAR_TABELA;
        execute(database, comando);
    }

    private void updateBaseVersao84(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS TipoConta (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [descricao] VARCHAR(500)," +
                "  [ativo] INTEGER DEFAULT 1)";

        this.execute(database, comando);
    }

    private void updateBaseVersao83(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteTipoAdq] INTEGER;");
    }

    private void updateBaseVersao82(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS ChamadoMotivo (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [descricao] VARCHAR(500)," +
                "  [ativo] INTEGER DEFAULT 1)";

        this.execute(database, comando);

        execute(database, "ALTER TABLE [ChamadosInteracoes] ADD COLUMN [idChamadoMotivo] INTEGER;");
    }

    private void updateBaseVersao81(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS ClienteTaxaMdr (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idCliente] INTEGER," +
                "  [bandeiraTipoId] INTEGER," +
                "  [taxaDebito] DECIMAL(20, 16) DEFAULT NULL," +
                "  [taxaCredito] DECIMAL(20, 16) DEFAULT NULL," +
                "  [taxaCredito6x] DECIMAL(20, 16) DEFAULT NULL," +
                "  [taxaCredito12x] DECIMAL(20, 16) DEFAULT NULL," +
                "  [taxaAntecipacao] DECIMAL(20, 16) DEFAULT NULL," +
                "  [ativo] INTEGER DEFAULT 1)";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ClienteDomicilioBancario (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idCliente] INTEGER," +
                "  [nomeBanco] VARCHAR(500)," +
                "  [tipoConta] VARCHAR(500)," +
                "  [agencia] VARCHAR(500)," +
                "  [agenciaDigito] VARCHAR(500)," +
                "  [conta] VARCHAR(500)," +
                "  [contaDigito] VARCHAR(500)," +
                "  [idBandeira] INTEGER," +
                "  [ativo] INTEGER DEFAULT 1)";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Bandeiras (" +
                "  [idBandeira] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [nomeBandeira] VARCHAR(500)," +
                "  [imagem] BLOB," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);
    }

    private void updateBaseVersao80(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [telefoneSub] VARCHAR(500);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [emailSub] VARCHAR(500);");
    }

    private void updateBaseVersao79(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [PendenciaCliente] ADD COLUMN [exibeExplicacao] INTEGER DEFAULT 0;");
        execute(database, "ALTER TABLE [PendenciaCliente] ADD COLUMN [explicacao] VARCHAR(500);");
    }

    private void updateBaseVersao78(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS ProspeccaoClienteAdquirencia (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idVendedor] INTEGER," +
                "  [tipoPessoa] VARCHAR(100)," +
                "  [cpfCnpj] VARCHAR(100)," +
                "  [mcc] INTEGER," +
                "  [faturamentoMedioPrevisto] DECIMAL(20, 16)," +
                "  [idPrazoNegociacao] INTEGER," +
                "  [antecipacao] INTEGER DEFAULT 1," +
                "  [nomeCompleto] VARCHAR(100)," +
                "  [nomeFantasia] VARCHAR(100)," +
                "  [telefone] VARCHAR(100)," +
                "  [email] VARCHAR(100)," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [dataCadastro] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS CadastroSimulacaoTaxa (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idSimulacaoTaxa] INTEGER," +
                "  [bandeiraTipoId] INTEGER," +
                "  [debito] DECIMAL(20, 16)," +
                "  [creditoAVista] DECIMAL(20, 16)," +
                "  [creditoAte6] DECIMAL(20, 16)," +
                "  [creditoMaior6] DECIMAL(20, 16)," +
                "  [antecipacaoAutomatica] DECIMAL(20, 16))";
        this.execute(database, comando);
    }

    private void updateBaseVersao77(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteMigracaoSub] INTEGER;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [antecipacaoAutomatica] INTEGER;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [prazoDeNegociacao] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [idDomicilio] INTEGER;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [nomeBanco] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [tipoConta] INTEGER;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [agencia] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [digitoAgencia] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [conta] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [digitoConta] VARCHAR(100);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [negociadoMigracaoSub] INTEGER DEFAULT 0;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [idBanco] INTEGER;");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [idMcc] INTEGER;");
    }

    private void updateBaseVersao76(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS CadastroMigracaoSub (" +
                "  [idAppMobile] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idVendedor] INTEGER," +
                "  [idCliente] INTEGER," +
                "  [email] VARCHAR(100)," +
                "  [telefoneCelular] VARCHAR(100)," +
                "  [tipoConta] VARCHAR(100)," +
                "  [idBanco] INTEGER," +
                "  [agencia] VARCHAR(100)," +
                "  [digitoAgencia] VARCHAR(100)," +
                "  [conta] VARCHAR(100)," +
                "  [digitoConta] VARCHAR(100)," +
                "  [versaoApp] VARCHAR(100)," +
                "  [idMcc] INTEGER," +
                "  [faturamentoMedioPrevisto] DECIMAL(20, 16)," +
                "  [idPrazoNegociacao] INTEGER," +
                "  [antecipacao] INTEGER DEFAULT 0," +
                "  [idMotivoRecusa] INTEGER," +
                "  [observacaoRecusa] VARCHAR(100)," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [sync] INTEGER DEFAULT 0," +
                "  [dataCadastro] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [token] VARCHAR(100)," +
                "  [confirmado] INTEGER," +
                "  [criado_em] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [situacao] INTEGER DEFAULT 0, " +
                "  [retorno] VARCHAR(100), " +
                "  [idServer] INTEGER)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS CadastroMigracaoSubTaxa (" +
                "  [idTaxa] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idCadastroMigracaoSub] INTEGER," +
                "  [idCliente] INTEGER," +
                "  [bandeiraTipoId] INTEGER," +
                "  [debito] DECIMAL(20, 16)," +
                "  [creditoAVista] DECIMAL(20, 16)," +
                "  [creditoAte6] DECIMAL(20, 16)," +
                "  [creditoMaior6] DECIMAL(20, 16)," +
                "  [antecipacaoAutomatica] DECIMAL(20, 16)," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS MotivoMigracaoSub (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [descricao] VARCHAR(100)," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);
    }

    private void updateBaseVersao75(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Pendencia (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [pendenciaId] INTEGER DEFAULT 0," +
                "  [descricao] VARCHAR(100)," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PendenciaMotivo (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [pendenciaId] INTEGER DEFAULT 0," +
                "  [pendenciaMotivoId] INTEGER DEFAULT 0," +
                "  [descricao] VARCHAR(100)," +
                "  [ativo] INTEGER DEFAULT 1)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PendenciaCliente (" +
                "  [pendenciaClienteId] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [clienteId] INTEGER DEFAULT 0," +
                "  [pendenciaId] INTEGER DEFAULT 0," +
                "  [pendenciaMotivoId] INTEGER DEFAULT 0," +
                "  [observacao] VARCHAR(100)," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [dataVisualizacao] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [dataResposta] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [ativo] INTEGER DEFAULT 1," +
                "  [ordem] INTEGER)";
        this.execute(database, comando);

        execute(database, "ALTER TABLE [Colaborador] ADD COLUMN [verificaClientePendencia] INTEGER DEFAULT 0;");
    }

    private void updateBaseVersao74(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [limitePrimeiraVenda] REAL;");
    }

    private void updateBaseVersao73(SQLiteDatabase database) {
        execute(database, DBPOS.UPDATE_INFORMACOES_GERAIS_POS_73);
    }

    private void updateBaseVersao72(SQLiteDatabase database) {
        comando = "ALTER TABLE [Visita] ADD COLUMN [origem] INTEGER DEFAULT 0;";
        this.execute(database, comando);
    }

    private void updateBaseVersao71(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [dddTelefone2] VARCHAR(2);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [telefone2] VARCHAR(10);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [dddCelular2] VARCHAR(2);");
        execute(database, "ALTER TABLE [Cliente] ADD COLUMN [celular2] VARCHAR(10);");
        execute(database, "ALTER TABLE [cliente] ADD COLUMN [vencimentoFatura] VARCHAR(20);");
        execute(database, "ALTER TABLE [InformacoesGeraisPOS] ADD COLUMN [DataCadastro] DATE;");
        execute(database, "ALTER TABLE [InformacoesGeraisPOS] ADD COLUMN [VendedorInstalacao] VARCHAR(255);");
    }

    private void updateBaseVersao70(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [ClienteCadastroPOS] ADD COLUMN [idOperadora] INTEGER;");
        execute(database, "ALTER TABLE [ClienteCadastroPOS] ADD COLUMN [metragemCabo] INTEGER;");
        execute(database, DBOperadora.UPDATE_70_SQL_CRIAR_TABELA);
        execute(database, DBModeloPOS.UPDATE_70_SQL_INSERIR_OPERADORA);
        execute(database, DBModeloPOS.UPDATE_70_SQL_INSERIR_METRAGEM);

        execute(database, DBPrazoNegociacao.SQL);
        execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [idPrazoNegociacao] INTEGER;");
        execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [confirmado] INTEGER DEFAULT 0;");
        execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [token] TEXT;");

        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_ID_PRAZO_NEGOCIACAO);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_FATURAMENTO_INICIAL);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_FATURAMENTO_FINAL);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_TAXA_DEBITO);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_TAXA_CREDITO);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_TAXA_ATE_6);
        execute(database, DBTaxaMdr.UPDATE_70_SQL_INSERIR_TAXA_MAIOR_6);
        execute(database, DBClienteCadastroTaxa.UPDATE_70_SQL_INSERIR_ANTECIPACAO);
        execute(database, DBIsencao.UPDATE_70_SQL_INSERIR_QTDE_MESES_STRING);
    }

    private void updateBaseVersao69(SQLiteDatabase database) {
        execute(database, DBAtualizarCliente.SQL_ALTER_69);
    }

    private void updateBaseVersao68(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [AuditagemEstoque] ADD COLUMN [idetificadorVenda] VARCHAR(50);");
        execute(database, "ALTER TABLE [AuditagemEstoqueCodigoBarra] ADD COLUMN [iccidIdServer] INTEGER");
        execute(database, "ALTER TABLE [Venda] ADD COLUMN [identificadorAuditagem] VARCHAR(50);");
    }

    private void updateBaseVersao67(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [TaxaMdr] ADD COLUMN [descricao] VARCHAR(200);");
    }

    private void updateBaseVersao66(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [ClienteCadastroPOS] ADD COLUMN [idTerminalString] VARCHAR(20);");
    }

    private void updateBaseVersao65(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [ItemVenda] ADD COLUMN [qtdeCombo] INTEGER;");
    }

    private void updateBaseVersao64(SQLiteDatabase database) {
        execute(database, DBClienteCadastroTaxa.SQL);
    }

    private void updateBaseVersao63(SQLiteDatabase database) {
        String[] campos = DBSugestaoVenda.UPDATE_63;
        for (String campo : campos) {
            execute(database, campo);
        }
    }

    private void updateBaseVersao62(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [tipoContaId] INTEGER;");
    }

    private void updateBaseVersao61(SQLiteDatabase database) {
        execute(database, DBHorarioNotificacao.SQL);
    }

    private void updateBaseVersao60(SQLiteDatabase database) {
        String tabelaMerchandisingProduto = "CREATE TABLE IF NOT EXISTS MerchandisingProduto(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[nome] VARCHAR(100),\n" +
                "[idOperadora] INTEGER)";

        String tabelaMerchandising = "CREATE TABLE IF NOT EXISTS Merchandising(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[tipo] INTEGER,\n" +
                "[idVisita] INTEGER)";

        String tabelaMerchandisingEnvelopamento = "CREATE TABLE IF NOT EXISTS MerchandisingEnvelopamento(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idMerchandising] INTEGER,\n" +
                "[idOperadora] INTEGER,\n" +
                "[caminhoFoto] VARCHAR(300))";

        String tabelaMerchandisingFachada = "CREATE TABLE IF NOT EXISTS MerchandisingFachada(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idMerchandising] INTEGER,\n" +
                "[merchanInterno] VARCHAR(1),\n" +
                "[merchanExterno] VARCHAR(1),\n" +
                "[caminhoFotoInterno] VARCHAR(300),\n" +
                "[caminhoFotoExterno] VARCHAR(300))";

        String tabelaMerchandisingNenhum = "CREATE TABLE IF NOT EXISTS MerchandisingNenhum(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idMerchandising] INTEGER,\n" +
                "[merchandising] VARCHAR(1),\n" +
                "[caminhoFoto] VARCHAR(300))";

        String tabelaMerchandisingPadrao = "CREATE TABLE IF NOT EXISTS MerchandisingPadrao(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idMerchandising] INTEGER,\n" +
                "[merchanInterno] VARCHAR(1),\n" +
                "[merchanExterno] VARCHAR(1),\n" +
                "[caminhoFotoInterno] VARCHAR(300),\n" +
                "[caminhoFotoExterno] VARCHAR(300))";

        String tabelaMerchadisingPadraoProduto = "CREATE TABLE IF NOT EXISTS MerchandisingProdutoPadrao(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idProduto] INTEGER,\n" +
                "[idPadrao] INTEGER,\n" +
                "[tipoMerchandising] INTEGER,\n" +
                "[data] DATE)";

        String adicionarColunaMerchandising = "ALTER TABLE [Cliente] ADD COLUMN [merchandising] VARCHAR(1) DEFAULT 'N';";
        String adicionarColunaIdSegmento = "ALTER TABLE [Cliente] ADD COLUMN [idSegmentoSGV] VARCHAR(5);";
        String adicionarColunaPontoReferencia = "ALTER TABLE [Cliente] ADD COLUMN [pontoReferencia] VARCHAR(100);";
        String adicionarColunaContato = "ALTER TABLE [Cliente] ADD COLUMN [contato] VARCHAR(300);";
        String adicionarColunaPossuiRecadastro = "ALTER TABLE [Cliente] ADD COLUMN [possuiRecadastro] VARCHAR(1) DEFAULT 'N';";
        String adicionarColunaRetornoRecadastro = "ALTER TABLE [Cliente] ADD COLUMN [retornoRecadastro] VARCHAR(200);";
        String adicionarColunaImportado = "ALTER TABLE [AuditagemCliente] ADD COLUMN [importado] INTEGER DEFAULT 0";

        try {
            database.execSQL(tabelaMerchandising);
            database.execSQL(tabelaMerchandisingProduto);
            database.execSQL(tabelaMerchandisingEnvelopamento);
            database.execSQL(tabelaMerchandisingFachada);
            database.execSQL(tabelaMerchandisingNenhum);
            database.execSQL(tabelaMerchandisingPadrao);
            database.execSQL(tabelaMerchadisingPadraoProduto);
            database.execSQL(adicionarColunaMerchandising);
            database.execSQL(adicionarColunaIdSegmento);
            database.execSQL(adicionarColunaPontoReferencia);
            database.execSQL(adicionarColunaContato);
            database.execSQL(adicionarColunaPossuiRecadastro);
            database.execSQL(adicionarColunaRetornoRecadastro);
            database.execSQL(DBAtualizarCliente.SQL);
            database.execSQL(adicionarColunaImportado);
            database.execSQL(DBSugestaoVenda.SQL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateBaseVersao59(SQLiteDatabase database) {
        this.execute(database, DBProspect.SQL);
        this.execute(database, DBMotivoAdquirencia.SQL);
        this.execute(database, DBPerguntasQualidade.SQL);
        this.execute(database, DBCatalogoAdquirencia.SQL);
        this.execute(database, DBRotaAdquirencia.SQL);
        this.execute(database, DBRotaAdquirenciaAgendada.SQL);
        this.execute(database, DBVisitaAdquirencia.SQL_VISITA);
        this.execute(database, DBVisitaAdquirencia.SQL_VISITA_ANEXO);
        this.execute(database, DBVisitaAdquirencia.SQL_VISITA_QUALIDADE);
        this.execute(database, DBIsencao.SQL);
        this.execute(database, DBClienteCadastroEndereco.SQL);
        this.execute(database, DBModeloPOS.SQL_CONEXAO);
        this.execute(database, DBTaxaMdr.SQL);

        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [digitoAgencia] VARCHAR(10);");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [faturamentoMedioPrevisto] DECIMAL(20, 2)");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [debitoAutomatico] BIT DEFAULT 0;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [txDebitoTipo] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [txCreditoAvistaTipo] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [txAte6Tipo] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [txMaior6Tipo] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [isencao] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastro] ADD COLUMN [idServer] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [tamanhoArquivo] VARCHAR(20);");
        this.execute(database, "ALTER TABLE [ClienteCadastroPOS] ADD COLUMN [id] INTEGER;");
        this.execute(database, "ALTER TABLE [ClienteCadastroPOS] ADD COLUMN [tipoConexao] INTEGER;");
    }

    private void updateBaseVersao58(SQLiteDatabase database) {
        this.execute(database, DBSolicitacaoTroca.CREATE_SOLICITACAO);
        this.execute(database, DBSolicitacaoTroca.CREATE_SOLICITACAO_DETALHES);
        this.execute(database, DBSolicitacaoTroca.CREATE_SOLICITACAO_CODIGO_BARRA);
        this.execute(database, DBSolicitacaoTroca.CREATE_SOLICITACAO_MOTIVO);
    }

    private void updateBaseVersao57(SQLiteDatabase database) {
        this.execute(database, DBComprovanteSkyTa.SQL);
    }

    private void updateBaseVersao56(SQLiteDatabase database) {
        this.execute(database, DBAdquirencia.CRIAR_TABELA);
    }

    private void updateBaseVersao55(SQLiteDatabase database) {
        this.execute(database, DBPOS.CREATE_ULTIMA_DATA_TANSACAO_POS);
        this.execute(database, DBPOS.CREATE_INFORMACOES_GERAIS_POS);
        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [curvaAdquirencia] VARCHAR(50);");
        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteFisico] INTEGER DEFAULT 0;");
        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteEletronico] INTEGER DEFAULT 0;");
        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteAdquirencia] INTEGER DEFAULT 0;");
        this.execute(database, "ALTER TABLE [Cliente] ADD COLUMN [clienteAppFlex] INTEGER DEFAULT 0;");
    }

    private void updateBaseVersao54(SQLiteDatabase database) {
        comando = "ALTER TABLE ClienteCadastro ADD antecipacao BIT DEFAULT 0;";
        this.execute(database, comando);
    }

    private void updateBaseVersao53(SQLiteDatabase database) {
        this.execute(database, DBModeloPOS.SQL);

        comando = "CREATE TABLE IF NOT EXISTS ClienteCadastroPOS (" +
                "[idAppMobile] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idClienteCadastro] INTEGER,\n" +
                "[idTipoMaquina] INTEGER,\n" +
                "[valorAluguel] DECIMAL(6, 2),\n" +
                "[idTerminal] INTEGER,\n" +
                "[situacao] INTEGER DEFAULT 1,\n" +
                "[cpfCnpjCliente] VARCHAR(20),\n" +
                "[dataSync] DATETIME)";
        this.execute(database, comando);
    }

    private void updateBaseVersao52(SQLiteDatabase database) {
        this.execute(database, "ALTER TABLE [TokenCliente] ADD COLUMN [tipoToken] INTEGER;");
    }

    private void updateBaseVersao51(SQLiteDatabase database) {
        execute(database, "ALTER TABLE [Colaborador] ADD COLUMN [cartaoPonto] INTEGER;");
    }

    private void updateBaseVersao50(SQLiteDatabase database) {
        this.execute(database, DBCartaoPonto.SQL);
        this.execute(database, "ALTER TABLE [Colaborador] ADD COLUMN [pis] VARCHAR(20);");
        this.execute(database, "ALTER TABLE [Colaborador] ADD COLUMN [cnpjFilial] VARCHAR(20);");
    }

    private void updateBaseVersao49(SQLiteDatabase database) {
        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [latitude] DECIMAL(20, 16);";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [longitude] DECIMAL(20, 16);";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [precisao] DECIMAL(20, 16);";
        this.execute(database, comando);
    }

    private void updateBaseVersao48(SQLiteDatabase database) {
        comando = "ALTER TABLE [venda] ADD COLUMN [status] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [itemVenda] ADD COLUMN [cancelado] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [itemVendaCodigoBarra] ADD COLUMN [cancelado] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [itemVendaCombo] ADD COLUMN [cancelado] INTEGER DEFAULT 0;";
        this.execute(database, comando);
    }

    private void updateBaseVersao47(SQLiteDatabase database) {
        comando = "ALTER TABLE [Colaborador] ADD COLUMN [idCliente] INTEGER;";
        this.execute(database, comando);
    }

    private void updateBaseVersao46(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS SenhaMasters(\n" +
                "[senha] VARCHAR(4),\n" +
                "[dataAtualizacao] VARCHAR(10))";
        this.execute(database, comando);
    }

    private void updateBaseVersao45(SQLiteDatabase database) {
        comando = "ALTER TABLE ClienteCadastro ADD mcc INTEGER";
        this.execute(database, comando);
    }

    private void updateBaseVersao44(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS TaxasAdquirenciaPF(\n" +
                "[id] INTEGER,\n" +
                "[ramoAtividade] VARCHAR(50),\n" +
                "[mcc] INTEGER,\n" +
                "[situacao] VARCHAR(1),\n" +
                "[minDebito] FLOAT,\n" +
                "[minCreditoAVista] FLOAT,\n" +
                "[minAte6] FLOAT,\n" +
                "[minMaior6] FLOAT,\n" +
                "[tabDebito] FLOAT,\n" +
                "[tabCreditoAVista] FLOAT,\n" +
                "[tabAte6] FLOAT,\n" +
                "[tabMaior6] FLOAT,\n" +
                "[maxDebito] FLOAT,\n" +
                "[maxCreditoAVista] FLOAT,\n" +
                "[maxAte6] FLOAT,\n" +
                "[maxMaior6] FLOAT,\n" +
                "CONSTRAINT pkTaxasAdquirenciaPF PRIMARY KEY ([id]))";

        this.execute(database, comando);
    }

    private void updateBaseVersao43(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS DataHoraServidor(\n" +
                "[dataServidor] DATETIME DEFAULT (datetime('now','localtime')))";
        this.execute(database, comando);
    }

    private void updateBaseVersao42(SQLiteDatabase database) {
        comando = "ALTER TABLE Cliente ADD bloqueiaAtendimento BIT DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE Cliente ADD qtdReprovacaoImg BIT DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE Cliente ADD recadastro BIT DEFAULT 0;";
        this.execute(database, comando);

        comando = "DROP TABLE TaxasAdquirencia;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS TaxasAdquirencia(\n" +
                "[id] INTEGER,\n" +
                "[tipo] INTEGER,\n" +
                "[situacao] VARCHAR(1),\n" +
                "[minDebito] FLOAT,\n" +
                "[minCreditoAVista] FLOAT,\n" +
                "[minAte6] FLOAT,\n" +
                "[minMaior6] FLOAT,\n" +
                "[tabDebito] FLOAT,\n" +
                "[tabCreditoAVista] FLOAT,\n" +
                "[tabAte6] FLOAT,\n" +
                "[tabMaior6] FLOAT,\n" +
                "[maxDebito] FLOAT,\n" +
                "[maxCreditoAVista] FLOAT,\n" +
                "[maxAte6] FLOAT,\n" +
                "[maxMaior6] FLOAT,\n" +
                "CONSTRAINT pkTaxasAdquirencia PRIMARY KEY ([id],[tipo]))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS CadastroVendedorPOS(\n" +
                "[Id] INTEGER,\n" +
                "[TipoMaquinaId] INTEGER,\n" +
                "[IdVendedor] INTEGER,\n" +
                "[DataSync] DATE,\n" +
                "[ValorAluguel] DECIMAL(18,2), \n" +
                "[IdLimite] INTEGER, \n" +
                "CONSTRAINT pkCadastroVendedorPOS PRIMARY KEY ([id]))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS TipoMaquina(\n" +
                "[TipoMaquinaId] INTEGER,\n" +
                "[Modelo] VARCHAR(50),\n" +
                "[ValorAluguelPadrao] DECIMAL(18,2), \n" +
                "[Descricao] VARCHAR(100),\n" +
                "CONSTRAINT pkTipoMaquina PRIMARY KEY ([TipoMaquinaId]))";
        this.execute(database, comando);

        comando = "ALTER TABLE ClienteCadastro ADD recadastro BIT DEFAULT 0";
        this.execute(database, comando);
    }

    private void updateBaseVersao41(SQLiteDatabase database) {
        comando = "DROP TABLE PistolagemComboTemp;";
        this.execute(database, comando);

        comando = "ALTER TABLE [PistolagemTemp] ADD [idCombo] INTEGER;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PistolagemComboTemp(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[qtdCombo] INTEGER,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[isCombo] INTEGER,\n" +
                "[idPreco] INTEGER,\n" +
                "[valor] DECIMAL(6, 2),\n" +
                "[quantidade] INTEGER,\n" +
                "[finalizado] INTEGER DEFAULT 0,\n" +
                "[idVenda] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ComboTemp(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idCombo] INTEGER,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[quantidade] INTEGER,\n" +
                "[finalizado] INTEGER DEFAULT 0,\n" +
                "[idVenda] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')))";
        this.execute(database, comando);

        comando = "CREATE TRIGGER valida_visita \n" +
                "BEFORE UPDATE ON Visita\n" +
                "FOR EACH ROW\n" +
                "WHEN NEW.idMotivo != OLD.idMotivo AND NEW.sync = 1 \n" +
                "BEGIN\n" +
                "     SELECT RAISE(ABORT,'Não é possivel alterar a visita');\n" +
                "END;";
        this.execute(database, comando);
    }

    private void updateBaseVersao40(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [cerca] INTEGER;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS AuditoriaOperadora(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idCliente] VARCHAR(20),\n" +
                "[operadora] INTEGER)";
        this.execute(database, comando);

        comando = "DROP TABLE ErroSync";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ErroSync(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idColaborador] INTEGER,\n" +
                "[entidade] VARCHAR(50),\n" +
                "[idEntidade] INTEGER)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ItemVendaCodigoBarra] ADD COLUMN [idProdutoSelect] VARCHAR(20);";
        this.execute(database, comando);
    }

    private void updateBaseVersao39(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS ErroSync(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idColaborador] INTEGER,\n" +
                "[entidade] VARCHAR(50),\n" +
                "[idEntidade] INTEGER)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Chamados] ADD [dataAgendamento] DATETIME;";
        this.execute(database, comando);
    }

    private void updateBaseVersao38(SQLiteDatabase database) {
        comando = "ALTER TABLE [Chamados] ADD [idCliente] VARCHAR(20);";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_chamados_001 ON Chamados (idChamado);";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_chamados_002 ON Chamados (idCliente);";
        this.execute(database, comando);

        comando = "ALTER TABLE [PistolagemTemp] ADD [idVenda] INTEGER;";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_PistolagemTemp_001 ON PistolagemTemp (idVenda)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PistolagemComboTemp (\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idPistolagem] INTEGER NOT NULL,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[qtd] INTEGER,\n" +
                "[idVenda] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')))";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_PistolagemComboTemp_001 ON PistolagemComboTemp (idPistolagem)";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_PistolagemComboTemp_002 ON PistolagemComboTemp (idVenda)";
        this.execute(database, comando);
    }

    private void updateBaseVersao37(SQLiteDatabase database) {
        comando = "CREATE INDEX idx_cliente_id_002 ON Cliente (ativo);";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_itemvenda_002 ON ItemVenda (idVenda); ";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_Visita_001 ON Visita (idCliente, dataInicio); ";
        this.execute(database, comando);
    }

    private void updateBaseVersao36(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS EstruturaProd(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[itemPai] VARCHAR(20),\n" +
                "[itemFilho] VARCHAR(20),\n" +
                "[proporcao] DECIMAL(19, 2),\n" +
                "[qtd] INTEGER)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [validaCercaFinal] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [novoAtend] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "DROP TABLE Iccid";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Iccid(\n" +
                "[idServer] INTEGER PRIMARY KEY,\n" +
                "[codigoBarra] VARCHAR(50) UNIQUE,\n" +
                "[codigoSemVerificador] VARCHAR(50),\n" +
                "[itemCode] VARCHAR(50))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD [valorTotal] DECIMAL(19, 2);";
        this.execute(database, comando);

        comando = "ALTER TABLE [ItemVenda] ADD [isCombo] INTEGER;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD [vendaBobina] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [ItemVenda] ADD [idPreco] INTEGER;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ItemVendaCombo(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idVenda] INTEGER NOT NULL,\n" +
                "[idItemVenda] INTEGER NOT NULL,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[qtd] INTEGER)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [curvaChip] VARCHAR(50);";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [adquirencia] INTEGER;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [eletronico] INTEGER;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [fisico] INTEGER;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [qtdBoletoPendente] INTEGER;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [valorBoletoPendente] DECIMAL(19, 2);";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PistolagemTemp(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[idCliente] VARCHAR(20),\n" +
                "[codigoBarra] VARCHAR(50),\n" +
                "[codigoBarraFinal] VARCHAR(50),\n" +
                "[quantidade] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[finalizado] INTEGER DEFAULT 0)";
        this.execute(database, comando);
    }

    private void updateBaseVersao35(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Devolucao(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idCliente] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[situacao] INTEGER,\n" +
                "[tipo] INTEGER,\n" +
                "[finalizado] INTEGER DEFAULT 0,\n" +
                "[idServer] VARCHAR(20),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS DevolucaoItens(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idDevolucao] INTEGER NOT NULL,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[quantidade] INTEGER,\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS DevolucaoItensICCID(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idDevolucaoItem] INTEGER NOT NULL,\n" +
                "[idDevolucao] INTEGER NOT NULL,\n" +
                "[ICCIDEntrada] VARCHAR(50),\n" +
                "[ICCIDSaida] VARCHAR(50),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD [atualizaBinario] INTEGER DEFAULT 0";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS TokenCliente(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idCliente] INTEGER NOT NULL,\n" +
                "[token] VARCHAR(50))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [validaDataGPS] INTEGER DEFAULT 1; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD [solicitaOpenSignal] INTEGER DEFAULT 0";
        this.execute(database, comando);
    }

    private void updateBaseVersao34(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [curva] VARCHAR(50); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [bloqueiaVendaVista] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao33(SQLiteDatabase database) {
        comando = "DROP TABLE [PrecoDif]";
        this.execute(database, comando);

        comando = "DROP TABLE [PrecoCliente]";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PrecoDiferenciado(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idCliente] VARCHAR(20),\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[data] DATETIME,\n" +
                "[datainicio] DATETIME,\n" +
                "[datafim] DATETIME,\n" +
                "[preco] DECIMAL(19, 2),\n" +
                "[sync] INTEGER DEFAULT 0,\n" +
                "[qtd] INTEGER DEFAULT 0,\n" +
                "[qtdVendida] INTEGER DEFAULT 0,\n" +
                "[situacao] VARCHAR(1) DEFAULT 'A')";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [semanaRota] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao32(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Suporte(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[problema] VARCHAR(500),\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[versao] VARCHAR(50),\n" +
                "[descricao] VARCHAR(1000),\n" +
                "[localArquivo] VARCHAR(400),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);
    }

    private void updateBaseVersao31(SQLiteDatabase database) {
        comando = "DROP TABLE [Estado]; ";
        this.execute(database, comando);

        comando = "DROP TABLE [TipoLogradouro]; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [obrigaAuditagem] INTEGER DEFAULT 1; ";
        this.execute(database, comando);

        comando = "DROP TABLE [ObrigaAuditoria]; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao30(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [permiteVendaPrazo] VARCHAR(1) DEFAULT 'S'; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [diasCortes] INTEGER DEFAULT 15; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [validaOrdemRota] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao29(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [grupo] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [digitoConta] VARCHAR(10);";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [operadora] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Iccid] ADD COLUMN [operadora] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Iccid] ADD COLUMN [grupo] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao28(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS LogPreco(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idPreco] INTEGER,\n" +
                "[idVenda] INTEGER,\n" +
                "[idVendaItem] INTEGER,\n" +
                "[quantidade] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [PrecoCliente] ADD COLUMN [qtdVendida] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void updateBaseVersao27(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [latitude] DECIMAL(20, 16); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [longitude] DECIMAL(20, 16); ";
        this.execute(database, comando);

        comando = "DROP TABLE Rota";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Rota(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idCliente] INTEGER,\n" +
                "[diaSemana] INTEGER,\n" +
                "[ordem] INTEGER,\n" +
                "[idTipo] INTEGER,\n" +
                "[semana] INTEGER)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS SenhaCliente(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idCliente] INTEGER NOT NULL,\n" +
                "[senha] VARCHAR(30) NOT NULL)";
        this.execute(database, comando);
    }

    private void updateBaseVersao26(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS LimiteCLiente(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY,\n" +
                "[idCliente] INTEGER,\n" +
                "[saldo] DECIMAL(8, 2),\n" +
                "[limite] DECIMAL(8, 2))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [qtdCombo] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [permiteValorZero] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [fechamento] VARCHAR(20); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [atualizarContato] VARCHAR(50); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [email] VARCHAR(100); ";
        this.execute(database, comando);
    }

    private void updateBaseVersao25(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Chamados(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[idMobile] INTEGER,\n" +
                "[idChamado] INTEGER,\n" +
                "[idFilial] INTEGER,\n" +
                "[idDepartamento] INTEGER,\n" +
                "[idSolicitante] INTEGER,\n" +
                "[solicitante] VARCHAR(300),\n" +
                "[assunto] VARCHAR(255),\n" +
                "[status] INTEGER,\n" +
                "[dataCadastro] DATETIME,\n" +
                "[idAtendente] INTEGER,\n" +
                "[atendente] VARCHAR(300),\n" +
                "[dataAlteracao] DATETIME,\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ChamadosInteracoes(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[idMobile] INTEGER,\n" +
                "[idChamadoMobile] INTEGER,\n" +
                "[idInteracao] INTEGER,\n" +
                "[idChamado] INTEGER,\n" +
                "[descricao] VARCHAR(1000),\n" +
                "[dataCadastro] DATETIME,\n" +
                "[idUsuario] INTEGER,\n" +
                "[usuario] VARCHAR(300),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Departamentos(\n" +
                "[id] INTEGER PRIMARY KEY,\n" +
                "[descricao] VARCHAR(200),\n" +
                "[ativo] INTEGER)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Filial(\n" +
                "[id] INTEGER PRIMARY KEY,\n" +
                "[descricao] VARCHAR(200),\n" +
                "[estado] VARCHAR(2),\n" +
                "[ativo] INTEGER)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ChamadosAnexos(\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[idMobile] INTEGER,\n" +
                "[idChamadoMobile] INTEGER,\n" +
                "[idInteracaoMobile] INTEGER,\n" +
                "[idInteracao] INTEGER,\n" +
                "[idChamado] INTEGER,\n" +
                "[dataCadastro] DATETIME,\n" +
                "[localArquivo] VARCHAR(400),\n" +
                "[nomeArquivo] VARCHAR(255),\n" +
                "[tipoArquivo] VARCHAR(255),\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [statusSGV] VARCHAR(50); ";
        this.execute(database, comando);
    }

    private void updateBaseVersao24(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [bipagemCliente] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [statuseFresh] VARCHAR(1); ";
        this.execute(database, comando);
    }

    private void updateBaseVersao23(SQLiteDatabase database) {
        comando = "DROP TABLE [LogErro]";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [celularSocio] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [tpPropriedade] VARCHAR(50)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [vendaFora] VARCHAR(3)";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_auditagemClienteCodBarra_001 ON [AuditagemClienteCodBarra] (idAuditagem)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Iccid (\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[codigo] VARCHAR(50))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [validaIccid] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao22(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS AuditagemCliente (\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idCliente] VARCHAR(20),\n" +
                "[idProduto] VARCHAR(20),\n" +
                "[qtdeInformada] INTEGER,\n" +
                "[data] DATETIME DEFAULT (datetime('now','localtime')),\n" +
                "[confirmado] VARCHAR(1) DEFAULT 'N',\n" +
                "[sync] INTEGER DEFAULT 0,\n" +
                "[versaoApp] VARCHAR(50))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS AuditagemClienteCodBarra (\n" +
                "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "[idAuditagem] INTEGER,\n" +
                "[codigoBarra] VARCHAR(50),\n" +
                "[codigoBarraFinal] VARCHAR(50),\n" +
                "[quantidade] INTEGER,\n" +
                "[sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD auditagem VARCHAR(1) DEFAULT 'N'";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Cnae(\n" +
                "[id] VARCHAR(100) PRIMARY KEY,\n" +
                "[ramoAtividade] VARCHAR(200),\n" +
                "[mcc] INTEGER,\n" +
                "[situacao] VARCHAR(1))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS TaxasAdquirencia(\n" +
                "[id] INTEGER PRIMARY KEY,\n" +
                "[situacao] VARCHAR(1),\n" +
                "[minDebito] VARCHAR(100),\n" +
                "[minCreditoAVista] VARCHAR(100),\n" +
                "[minAte6] VARCHAR(100),\n" +
                "[minMaior6] VARCHAR(100),\n" +
                "[tabDebito] VARCHAR(100),\n" +
                "[tabCreditoAVista] VARCHAR(100),\n" +
                "[tabAte6] VARCHAR(100),\n" +
                "[tabMaior6] VARCHAR(100),\n" +
                "[maxDebito] VARCHAR(100),\n" +
                "[maxCreditoAVista] VARCHAR(100),\n" +
                "[maxAte6] VARCHAR(100),\n" +
                "[maxMaior6] VARCHAR(100))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Bancos(\n" +
                "[id] INTEGER PRIMARY KEY,\n" +
                "[descricao] VARCHAR(100),\n" +
                "[situacao] VARCHAR(1))";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [cnae] VARCHAR(100)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [nomeSocio] VARCHAR(200)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [cpfSocio] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [dtnascimentoSocio] DATE";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [im] VARCHAR(50)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [faturamentoAnual] DECIMAL(20, 2)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [ticketMedio] DECIMAL(20, 2)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [pontoRef] VARCHAR(200)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [usuarioPortal] VARCHAR(200)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [emailPortal] VARCHAR(100)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [banco] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [agencia] VARCHAR(100)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [conta] VARCHAR(100)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [qtdParcelas] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [horaInicio1] VARCHAR(5)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [horaFinal1] VARCHAR(5)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [horaInicio2] VARCHAR(5)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [horaFinal2] VARCHAR(5)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [codigoSgv] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [vlrAluguelUnt] DECIMAL(20, 2)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [qtdAluguelMaq] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [aluguelMaqVenc] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [tpPagamento] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [qtdPOScFio] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [vlrUntPOScFio] DECIMAL(9, 2)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [qtdPOSsFio] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [vlrUntPOSsFio] DECIMAL(9, 2)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [txDebito] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [txCreditoAvista] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [txAte6] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [txMaior6] VARCHAR(20)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [dtInicioSociedade] DATE";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [percentualPart] INTEGER";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [qtdSocios] INTEGER";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao21(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [qtdCodBarra] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [iniciaCodBarra] VARCHAR(10); ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao20(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [pedeSenha] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao19(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [sync] INTEGER; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao18(SQLiteDatabase database) {
        comando = "ALTER TABLE [Colaborador] ADD COLUMN [distancia] FLOAT";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastro] ADD COLUMN [versaoApp] VARCHAR(50); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Remessa] ADD COLUMN [localArquivo] VARCHAR(400); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Remessa] ADD COLUMN [syncArquivo] INTEGER DEFAULT 0; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [senha] VARCHAR(30); ";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_itemvenda_001 ON ItemVenda (sync); ";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_AuditagemEstCodBarra_001 ON AuditagemEstoqueCodigoBarra (idAuditagem); ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao17(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [atualizarLocal] VARCHAR(1)";
        this.execute(database, comando);

        comando = "ALTER TABLE [LocalizacaoCliente] ADD COLUMN [localArquivo] VARCHAR(400)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [localArquivo] VARCHAR(400)";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao16(SQLiteDatabase database) {
        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [retorno] VARCHAR(200)";
        this.execute(database, comando);

        comando = "ALTER TABLE [LocalizacaoCliente] ADD [anexo] TEXT";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao15(SQLiteDatabase database) {
        comando = "ALTER TABLE [Visita] ADD COLUMN [distancia] FLOAT";
        this.execute(database, comando);

        comando = "ALTER TABLE [ClienteCadastroAnexo] ADD COLUMN [situacao] INTEGER DEFAULT 0";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao14(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS LocalizacaoCliente (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [idCliente] VARCHAR(20)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [datagps] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [tipo] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdCadCliPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdDocCliPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdLocCliPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdRemessaPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdItemRemessaPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [OS] ADD COLUMN obsVendedor VARCHAR(800);";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [exibirCodigo] VARCHAR(10);";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [codigoAplic] VARCHAR(20);";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ObrigaAuditoria (" +
                " [diaSemana] INTEGER NOT NULL PRIMARY KEY," +
                " [obrigatorio] VARCHAR(1))";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao13(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [bipagemAuditoria] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao12(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS ProjetoTrade (" +
                "  [id] VARCHAR(20)," +
                "  [descricao] VARCHAR(500)," +
                "  [ddd] VARCHAR(3)," +
                "  [dataInicio] DATE," +
                "  [dataFinal] DATE," +
                "  [dataLimite] DATE," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ProjetoTradeItens (" +
                "  [id] VARCHAR(20)," +
                "  [idProjeto] VARCHAR(20)," +
                "  [idProduto] VARCHAR(20)," +
                "  [descProduto] VARCHAR(50)," +
                "  [qtd] INTEGER," +
                "  [preco] DECIMAL(6, 2)," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [idProjeto] VARCHAR(20);";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [tipo] VARCHAR(4); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [versaoApp] VARCHAR(50) DEFAULT '-1'; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao11(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Segmento (" +
                "  [id] VARCHAR(20)," +
                "  [descricao] VARCHAR(500)," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS TipoLogradouro (" +
                "  [id] VARCHAR(20)," +
                "  [descricao] VARCHAR(100)," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Estado (" +
                "  [id] VARCHAR(20)," +
                "  [sigla] VARCHAR(5)," +
                "  [descricao] VARCHAR(500)," +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ClienteCadastro (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [tipo] VARCHAR(1)," +
                "  [nome] VARCHAR(200)," +
                "  [fantasia] VARCHAR(200)," +
                "  [cpfcnpj] VARCHAR(20)," +
                "  [rg_Ie] VARCHAR(20)," +
                "  [datanascimento] DATE," +
                "  [segmento] VARCHAR(20)," +
                "  [cep] VARCHAR(10)," +
                "  [tipoLogradouro] VARCHAR(100)," +
                "  [logradouro] VARCHAR(200)," +
                "  [bairro] VARCHAR(100)," +
                "  [numeroLogradouro] VARCHAR(5)," +
                "  [complementoLogradouro] VARCHAR(200)," +
                "  [cidade] VARCHAR(100)," +
                "  [uf] VARCHAR(5)," +
                "  [datacadastro] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [dataatualizacao] DATETIME," +
                "  [nomeContato] VARCHAR(200)," +
                "  [telResidencial] VARCHAR(20)," +
                "  [telCelular] VARCHAR(20)," +
                "  [email] VARCHAR(100)," +
                "  [dia_Visita] VARCHAR(20)," +
                "  [frequencia] VARCHAR(20)," +
                "  [obs] VARCHAR(800)," +
                "  [tipoCliente] VARCHAR(20)," +
                "  [retorno] VARCHAR(500)," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [sync] INTEGER DEFAULT 0) ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ClienteCadastroAnexo (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idCadastro] INTEGER," +
                "  [tipo] VARCHAR(15)," +
                "  [anexo] TEXT," +
                "  [sync] INTEGER DEFAULT 0) ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS FormaPagamento (" +
                "  [id] INTEGER," +
                "  [descricao] VARCHAR(50)," +
                "  [ativo] VARCHAR(1) DEFAULT 'S') ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [dataVencimento] DATE;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Cliente] ADD COLUMN [codigoeFresh] VARCHAR(20);";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao10(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS PrecoCliente (" +
                "  [id] VARCHAR(20)," +
                "  [idCliente] VARCHAR(20)," +
                "  [idProduto] VARCHAR(20)," +
                "  [data] DATETIME," +
                "  [preco] DECIMAL(19, 2)," +
                "  [sync] INTEGER DEFAULT 0, " +
                "  [idVenda] INTEGER, " +
                "  [qtd] INTEGER, " +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";

        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS PrecoDif (" +
                "  [id] VARCHAR(20)," +
                "  [idProduto] VARCHAR(20)," +
                "  [data] DATETIME," +
                "  [preco] DECIMAL(19, 2)," +
                "  [datainicio] DATETIME, " +
                "  [datafim] DATETIME, " +
                "  [situacao] VARCHAR(1) DEFAULT 'A') ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [bloqueiaHorario] VARCHAR(1) DEFAULT 'S'; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao9(SQLiteDatabase database) {
        comando = "ALTER TABLE [Cliente] ADD COLUMN [idclienteintraflex] VARCHAR(20); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Rota] ADD COLUMN [idCliente] VARCHAR(20); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [idCliente] VARCHAR(20); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [versaoApp] VARCHAR(50); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [latitude] DECIMAL(20, 16); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [longitude] DECIMAL(20, 16); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Visita] ADD COLUMN [precisao] DECIMAL(20, 16); ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Venda] ADD COLUMN [idCliente] VARCHAR(20); ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Localizacao (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16))";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdVisitaPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdVendaPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdVendaItensPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "ALTER TABLE [Telemetria] ADD COLUMN [qtdCodBarraPend] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS SenhaVenda (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [senha] VARCHAR(15)," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [ItemVenda] ADD COLUMN [boletoSync] INTEGER DEFAULT 0;";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Remessa(" +
                " [id] VARCHAR(20), " +
                " [numero] VARCHAR(20), " +
                " [situacao] INTEGER, " +
                " [datageracao] DATETIME, " +
                " [dataconfirmacao] DATETIME, " +
                " [sync] INTEGER DEFAULT 0) ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS RemessaItem(" +
                " [id] VARCHAR(20), " +
                " [idRemessa] VARCHAR(20), " +
                " [idProduto] VARCHAR(20), " +
                " [qtdRemessa] INTEGER, " +
                " [qtdInformada] INTEGER DEFAULT 0, " +
                " [sync] INTEGER DEFAULT 0) ";
        this.execute(database, comando);

        comando = "CREATE TABLE LogErro(" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT \n" +
                " ,[erro] TEXT)";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_produto_id_001 ON Produto (id);";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_cliente_id_001 ON Cliente (id);";
        this.execute(database, comando);

        comando = "CREATE INDEX idx_remessaitem_001 ON RemessaItem (sync);";
        this.execute(database, comando);

        comando = "ALTER TABLE [AuditagemEstoque] ADD COLUMN [confirmado] VARCHAR(1) DEFAULT 'N'";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS AuditagemEstoqueCodigoBarra (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idAuditagem] INTEGER," +
                "  [idProduto] VARCHAR(20)," +
                "  [codigoBarra] VARCHAR(50)," +
                "  [codigoBarraFinal] VARCHAR(50)," +
                "  [quantidade] INTEGER DEFAULT 0," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [desbloqueiaVenda] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [dataDesbloqueia] DATETIME; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao8(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Visita (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [idClienteSGV] VARCHAR(20)," +
                "  [dataInicio] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [dataFim] DATETIME," +
                "  [idMotivo] INTEGER," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Venda (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [idVisita] INTEGER," +
                "  [idFormaPagamento] INTEGER," +
                "  [idClienteSGV] VARCHAR(20)," +
                "  [data] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ItemVenda (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [idProduto] VARCHAR(20)," +
                "  [idVenda] INTEGER," +
                "  [dataVenda] DATETIME," +
                "  [qtde] INTEGER," +
                "  [valorUN] DECIMAL(6, 2)," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS ItemVendaCodigoBarra (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [idVenda] INTEGER," +
                "  [dataVenda] DATETIME," +
                "  [idItemVenda] INTEGER," +
                "  [idProduto] VARCHAR(20)," +
                "  [codigoBarra] VARCHAR(50)," +
                "  [codigoBarraFinal] VARCHAR(50)," +
                "  [quantidade] INTEGER DEFAULT 0," +
                "  [sync] INTEGER DEFAULT 0)";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Boleto (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [NumBoleto] INTEGER DEFAULT 0," +
                "  [LinhaDigitavel] VARCHAR(100)," +
                "  [DataVencimento] DATETIME," +
                "  [Valor] REAL," +
                "  [Situacao] INTEGER DEFAULT 0) ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [bipagem] VARCHAR(1) DEFAULT 'N'; ";
        this.execute(database, comando);

        comando = "ALTER TABLE [Produto] ADD COLUMN [precovenda] DECIMAL(19, 2) DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao7(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "  [id] VARCHAR(20)," +
                "  [nomeFantasia] VARCHAR(200)," +
                "  [razaoSocial] VARCHAR(200)," +
                "  [tipoLogradouro] VARCHAR(50)," +
                "  [nomeLogradouro] VARCHAR(200)," +
                "  [numeroLogradouro] VARCHAR(20)," +
                "  [complementoLogradouro] VARCHAR(200)," +
                "  [bairro] VARCHAR(100)," +
                "  [cidade] VARCHAR(100)," +
                "  [estado] VARCHAR(50)," +
                "  [cep] VARCHAR(10)," +
                "  [dddTelefone] VARCHAR(2)," +
                "  [telefone] VARCHAR(10)," +
                "  [dddCelular] VARCHAR(2)," +
                "  [cpf_cnpj] VARCHAR(20)," +
                "  [rg_ie] VARCHAR(20)," +
                "  [celular] VARCHAR(10)," +
                "  [codigoSGV] VARCHAR(20)," +
                "  [ativo] VARCHAR(1) DEFAULT 'S') ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Rota (" +
                "  [idClienteSGV] VARCHAR(20)," +
                "  [diaSemana] INTEGER," +
                "  [ordem] INTEGER," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [ativo] VARCHAR(1) DEFAULT 'S') ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Motivo (" +
                "  [id] INTEGER," +
                "  [tipo] INTEGER," +
                "  [descricao] VARCHAR(50)," +
                "  [ativo] VARCHAR(1) DEFAULT 'S') ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao6(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS [AuditagemEstoque] (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idProduto] VARCHAR(20)," +
                "  [qtdeInformada] INTEGER," +
                "  [qtdeReal] INTEGER," +
                "  [data] DATETIME DEFAULT (datetime('now','localtime'))," +
                "  [sync] INTEGER DEFAULT 0 );";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS [Permissao] (" +
                "  [id] VARCHAR(20)," +
                "  [idMenuApp] VARCHAR(20)," +
                "  [ativo] VARCHAR(1) DEFAULT 'S') ";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao5(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS [Mensagem](\n" +
                "[id] INTEGER NOT NULL,\n" +
                "[texto] VARCHAR,\n" +
                "[data] DATETIME,\n" +
                "[dataVisualizacao] DATETIME,\n" +
                "[visualizacaoSync] INTEGER DEFAULT 0)";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao4(SQLiteDatabase database) {
        comando = "ALTER TABLE [Produto] ADD COLUMN [ativo] VARCHAR(1) DEFAULT 'S'; ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS [RelatorioMeta] (" +
                "  [idIndicador] INTEGER," +
                "  [indicador] VARCHAR(500)," +
                "  [idOperadora] INTEGER," +
                "  [operadora] VARCHAR(50)," +
                "  [meta] INTEGER," +
                "  [realizado] INTEGER," +
                "  [data] DATETIME DEFAULT (datetime('now','localtime')));";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao3(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS [Telemetria] (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idVendedor] INTEGER," +
                "  [modeloAparelho] VARCHAR(200)," +
                "  [versaoOs] VARCHAR(50)," +
                "  [versaoApp] VARCHAR(50)," +
                "  [imei] VARCHAR(20)," +
                "  [bateria] INTEGER," +
                "  [tipoInternet] VARCHAR(300)," +
                "  [latitude] DECIMAL(20, 16)," +
                "  [longitude] DECIMAL(20, 16)," +
                "  [precisao] DECIMAL(20, 16)," +
                "  [dataGps] DATETIME DEFAULT (datetime('now','localtime')));";
        this.execute(database, comando);
    }

    private void UpdateBaseVersao2(SQLiteDatabase database) {
        comando = "CREATE TABLE [ComandoExecutar] (\n" +
                "  [id] VARCHAR, \n" +
                "  [comando] VARCHAR, \n" +
                "  [data] DATETIME DEFAULT (datetime('now','localtime')), \n" +
                "  [dataExecutado] DATETIME, \n" +
                "  [mensagem] VARCHAR, \n" +
                "  [basedados] VARCHAR);\n";
        this.execute(database, comando);

        comando = "ALTER TABLE [Colaborador] ADD COLUMN [enviarBase] INTEGER DEFAULT 0; ";
        this.execute(database, comando);
    }

    private void CreateBaseVersao1(SQLiteDatabase database) {
        comando = "CREATE TABLE IF NOT EXISTS Colaborador (" +
                "  [id] INTEGER NOT NULL, " +
                "  [nome] VARCHAR(30))";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS OS (" +
                "  [id] INTEGER," +
                "  [idTipo] INTEGER," +
                "  [DescricaoTipo] VARCHAR(200)," +
                "  [idCliente] INTEGER," +
                "  [nomeCliente] VARCHAR(200)," +
                "  [obs] VARCHAR(1000)," +
                "  [Data] DATETIME," +
                "  [idClasse] INTEGER," +
                "  [classeSla] VARCHAR(200)," +
                "  [DataVisualizacao]DATETIME," +
                "  [DataAgendamento] DATETIME," +
                "  [DataAtendimento] DATETIME," +
                "  [DataLimiteAtend] DATETIME," +
                "  [AgendamentoSync] INTEGER DEFAULT 0," +
                "  [AtendimentoSync] INTEGER DEFAULT 0," +
                "  [VisualizacaoSync] INTEGER DEFAULT 0 ) ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS Produto (" +
                "  [id] VARCHAR(20)," +
                "  [codSgv] VARCHAR(20)," +
                "  [nome] VARCHAR(200)," +
                "  [valor] DECIMAL(6, 2)," +
                "  [estoqueMax] INTEGER DEFAULT 0," +
                "  [mediaDiariaVnd]  DECIMAL(6, 2)," +
                "  [diasEstoque] INTEGER DEFAULT 0," +
                "  [estoqueSugerido] INTEGER DEFAULT 0," +
                "  [estoqueAtual] INTEGER DEFAULT 0 ) ";
        this.execute(database, comando);

        comando = "CREATE TABLE [SolicMerc] (" +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "  [idServer] INTEGER DEFAULT 0," +
                "  [dataCriacao] DATETIME DEFAULT (datetime('now','localtime')) " +
                ");";
        this.execute(database, comando);

        comando = "CREATE TABLE [ItensSolicMerc] ( " +
                "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  " +
                "  [idSolicMerc] INTEGER, " +
                "  [idProduto] VARCHAR(20), " +
                "  [qtde] INTEGER, " +
                "  [estoqueMax] INTEGER DEFAULT 0," +
                "  [mediaDiariaVnd]  DECIMAL(6, 2)," +
                "  [diasEstoque] INTEGER DEFAULT 0," +
                "  [qtdeSugerida] INTEGER DEFAULT 0," +
                "  [estoqueAtual] INTEGER DEFAULT 0 ) ";
        this.execute(database, comando);

        comando = "CREATE TABLE IF NOT EXISTS [SituacaoSolicitacao] (" +
                "  [idSolicMerc] INTEGER," +
                "  [idStatus] INTEGER," +
                "  [data] DATETIME DEFAULT (datetime('now','localtime'))" +
                ");";
        this.execute(database, comando);
    }


    private void execute(SQLiteDatabase database, String command) {
        try {
            database.execSQL(command);
        } catch (Exception ex) {
            Timber.e(ex, "%s: %s", getClass().getSimpleName(), ex.getMessage());
        }
    }
}
