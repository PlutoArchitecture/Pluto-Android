package com.minggo.pluto.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;


import com.minggo.pluto.Pluto;
import com.minggo.pluto.R;
import com.minggo.pluto.util.FileUtils;
import com.minggo.pluto.util.LogUtils;

import org.apache.commons.httpclient.HttpException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * 应用程序异常类：用于捕获异常和提示错误信息
 *
 * @author minggo
 * @created 2012-3-21
 */
public class PlutoException extends Exception implements UncaughtExceptionHandler {

    private static final long serialVersionUID = -2802147109149812598L;
    private final static boolean Debug = true;//是否保存错误日志
    public static String filepath = Pluto.SDPATH + "/errlog/"; //保存到SD卡的目录 ,用于上传,上传后删除
    private static String crashfilepath = Pluto.SDPATH + "/crashlog/";//本地日志文件目录
    public static String filename = "err_log.txt"; //保存到SD卡的文件名
    private static PlutoException appException;

    /**
     * 定义异常类型
     */
    public final static byte TYPE_NETWORK = 0x01;
    public final static byte TYPE_SOCKET = 0x02;
    public final static byte TYPE_HTTP_CODE = 0x03;
    public final static byte TYPE_HTTP_ERROR = 0x04;
    public final static byte TYPE_XML = 0x05;
    public final static byte TYPE_IO = 0x06;
    public final static byte TYPE_RUN = 0x07;

    private byte type;
    private int code;

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    private PlutoException(Context context) {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private PlutoException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
        if (Debug) {
            this.saveErrorLog(excp);
        }
    }

    public int getCode() {
        return this.code;
    }

    public int getType() {
        return this.type;
    }

    /**
     * 提示友好的错误信息
     *
     * @param ctx
     */
    public void makeToast(Context ctx) {
        switch (this.getType()) {
            case TYPE_HTTP_CODE:
                String err = ctx.getString(R.string.http_status_code_error, this.getCode());
                Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_HTTP_ERROR:
                Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_SOCKET:
                Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_NETWORK:
                Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_XML:
                Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_IO:
                Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
                break;
            case TYPE_RUN:
                Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 保存异常日志
     *
     * @param excp
     */
    private void saveErrorLog(Exception excp) {

        String savePath = "";
        String logFilePath = "";
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            //判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                savePath = filepath;
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                logFilePath = savePath + filename;
            }
            //没有挂载SD卡，无法写文件
            if (logFilePath == "") {
                return;
            }
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("\n");
            pw.println("\n--------------------" + (new Date().toLocaleString()) + "---------------------\n");

            excp.printStackTrace(pw);
            StringBuffer exceptionStr = new StringBuffer();
            exceptionStr.append("Cause by:");
            StackTraceElement[] caseElements = excp.getCause().getStackTrace();
            for (int i = 0; i < caseElements.length; i++) {
                exceptionStr.append(caseElements[i].toString() + "\n");
            }
            pw.println(exceptionStr.toString());

            excp.printStackTrace(pw);
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static PlutoException http(int code) {
        return new PlutoException(TYPE_HTTP_CODE, code, null);
    }

    public static PlutoException http(Exception e) {
        return new PlutoException(TYPE_HTTP_ERROR, 0, e);
    }

    public static PlutoException socket(Exception e) {
        return new PlutoException(TYPE_SOCKET, 0, e);
    }

    public static PlutoException io(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new PlutoException(TYPE_NETWORK, 0, e);
        } else if (e instanceof IOException) {
            return new PlutoException(TYPE_IO, 0, e);
        }
        return run(e);
    }

    public static PlutoException xml(Exception e) {
        return new PlutoException(TYPE_XML, 0, e);
    }

    public static PlutoException network(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new PlutoException(TYPE_NETWORK, 0, e);
        } else if (e instanceof HttpException) {
            return http(e);
        } else if (e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    public static PlutoException run(Exception e) {
        return new PlutoException(TYPE_RUN, 0, e);
    }

    /**
     * 获取APP异常崩溃处理对象
     *
     * @param context
     * @return
     */
    public static PlutoException getAppExceptionHandler(Context context) {
        if (appException == null) {
            appException = new PlutoException(context);
        }
        return appException;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }

    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     *
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        LogUtils.info("plutoexception",">>>>>handle exception");
        if (ex == null) {
            return false;
        }

        final Context context = AppManager.getAppManager().currentActivity();

        if (context == null) {
            LogUtils.info("plutoexception",">>>>>context is null");
            return false;
        }

        final String crashReport = getCrashReport(ex);
        ex.printStackTrace();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileUtils.WriterTxtFile(filepath, filename, crashReport, true);//写到本地
                FileUtils.WriterTxtFile(crashfilepath, filename, crashReport, true);//写到本地
            }
        }).start();
        //FileUtils.WriterTxtFile(filepath, filename, crashReport, true);//写到本地
        //FileUtils.WriterTxtFile(crashfilepath, filename, crashReport, true);//写到本地
		//AppManager.getAppManager().App_Exit(context);
        //显示异常信息&发送报告
		System.out.println("<<<handleException:"+ex.getMessage());
//        new Thread() {
//            public void run() {
//                Looper.prepare();
//                sendAppCrashReport(context, crashReport);
//                Looper.loop();
//            }
//        }.start();
        return true;
    }

    /**
     * 发送App异常崩溃报告
     * @param cont
     * @param crashReport
     */
    public static void sendAppCrashReport(final Context cont, final String crashReport)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.app_error);
        builder.setMessage(R.string.app_error_message);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LogUtils.debug("AppException UIHelper", "有异常日志，下次打开提交");
                System.exit(0);//这个地方最好根据自己实际情况先finish所有的activity先

            }
        });
        builder.setNegativeButton("发送", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    File file = new File(PlutoException.filepath);
                    if (file.exists()) {
                        file = new File(PlutoException.filepath+PlutoException.filename);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);//这个地方最好根据自己实际情况先finish所有的activity先
            }
        });
        builder.show();
    }


    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Throwable ex) {
        PackageInfo pinfo = AppContext.getInstance().getPackageInfo();
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("\n------------------" + new Date().toLocaleString() + "-----------------------\n");
        exceptionStr.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");

        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }

        exceptionStr.append("Cause by:");
        StackTraceElement[] caseElements = ex.getCause().getStackTrace();
        for (int i = 0; i < caseElements.length; i++) {
            exceptionStr.append(caseElements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }
}