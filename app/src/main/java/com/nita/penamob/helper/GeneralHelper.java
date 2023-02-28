package com.nita.penamob.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.nita.penamob.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class GeneralHelper {
    private Activity activity;
    private final Context context;
    private final Map<Object, Object> response = new HashMap<>();

    public interface response {
        Map<Object, Object> getData(Map<Object, Object> response);
    }

    public GeneralHelper(Activity activity) {
        this.activity   = activity;
        this.context    = activity.getApplicationContext();
    }

    public GeneralHelper(Context context) {
        this.context    = context;
    }

    // Fungsi ini digunakan untuk berpindah ke activity lain / page lain
    public void startIntent(Class destination, boolean clearIntent,
                            Map<String, String> paramList)
    {
        Intent intent   = new Intent(activity, destination);
        if (clearIntent)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NO_HISTORY);
        else
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (paramList != null) {
            for (Map.Entry<String, String> data : paramList.entrySet()) {
                String key      = data.getKey();
                String value    = data.getValue();

                intent.putExtra(key, value);
            }
        }

        activity.startActivity(intent);
    }

    // Fungsi ini digunakan untuk berpindah ke activity lain namun saat kembali ke activity sebelumnya
    // memberikan suatu nilai
    public void startIntentForResult(Class destination, Map<String, String> paramList, int code)
    {
        Intent intent = new Intent(activity, destination);

        if (paramList != null) {
            for (Map.Entry<String, String> data : paramList.entrySet()) {
                String key      = data.getKey();
                String value    = data.getValue();

                intent.putExtra(key, value);
            }
        }
        activity.startActivityForResult(intent, code);
    }

    // Fungsi ini digunakan untuk menyimpan session sesuai dengan key dan value yang diinginkan
    public void saveSession(String name, String value) {
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putString(name, value).apply();
    }

    // Fungsi ini digunakan untuk mengambil data session sesuai dengan key yang diinginkan
    public String getSession(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString(key, null);
    }

    // Fungsi ini digunakan untuk membersihkan session / proses logout aplikasi
    public void clearSession() {
        SharedPreferences sharedPreferences         = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor             = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // Fungsi ini digunakan untuk membersihkan session / proses logout aplikasi
    public void clearSessionByKey(String key) {
        SharedPreferences sharedPreferences         = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor             = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void changeColorImage(ImageView imageView, int color) {
        imageView.setColorFilter(activity.getBaseContext().getResources().getColor(color));
    }

    public void changeColorButtinImage(ImageButton button, int color) {
        button.setColorFilter(activity.getBaseContext().getResources().getColor(color));
    }

    public void setTextHtml(TextView textView, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    public void openUrlToBrowser(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            activity.startActivity(i);
        } catch (Exception er) {
            showToast("URL TIDAK VALID!", 0);
            er.printStackTrace();
        }
    }

    public void showToast(String message, int duration) {
        Toast.makeText(activity.getApplicationContext(), message, duration).show();
    }

    public void hideStatusBar() {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    public View inflateView(int layout) {
        View view;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        return view;
    }

    // Fungsi ini digunakan untuk setup progress dialog / loading sesuai dengan context
    public void setupProgressDialog(ProgressDialog pDialog, String title) {
        pDialog.setMessage(title);
        pDialog.setCancelable(false);
    }

    // Fungsi ini digunakan untuk menampilkan progress dialog / loading
    public void showProgressDialog(ProgressDialog pDialog, boolean show) {
        if (show) {
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        } else {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    // Fungsi ini digunakan untuk mengenkripsi base 64 suatu String
    public String encryptString(String text){
        byte[] data;
        String base64   = "";
        try {
            data            = text.getBytes(StandardCharsets.UTF_8);
            base64          = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
        }
        return base64;
    }

    // Fungsi ini digunakan untuk mendecrypt base 64 suatu String
    public String decryptString(String text){
        byte[] valueDecoded = new byte[0];
        valueDecoded = Base64.decode(text.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }

    //    Fungsi ini digunakan untuk memberikan gari bawah pada text
    public void setUnderline(TextView view, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        view.setText(content);
    }

    public void showEmptyState(FrameLayout parent, boolean show, int image,
                               String txtTitle, String txtMessage) {
//        View child          = inflateView(R.layout.empty_state);
//        ImageView icon      = (ImageView) child.findViewById(R.id.icon);
//        TextView title      = (TextView) child.findViewById(R.id.title);
//        TextView message    = (TextView) child.findViewById(R.id.message);
//
//        if (image != 0) {
//            icon.setImageResource(image);
//            icon.setVisibility(View.VISIBLE);
//        } else {
//            icon.setVisibility(View.GONE);
//        }
//
//        title.setText(txtTitle);
//        message.setText(txtMessage);
//
//        parent.removeAllViews();
//        if (show)
//            parent.addView(child);
    }

    // Fungsi ini digunakan untuk setup webview yang memiliki konten text
    public void formatIsText(final ProgressDialog pDialog, final WebView webView, final String urlContent, final String type)
    {
        showProgressDialog(pDialog, true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_NONE, null);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                showProgressDialog(pDialog, false);
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageFinished(final WebView view, final String url) {
                showProgressDialog(pDialog, false);
            }
        });
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        if (type.equals("url")) {
            webView.loadUrl(urlContent);
        } else {
            webView.loadData(urlContent, "text/html", "UTF-8");
        }
    }

    // Fungsi ini digunakan untuk menampilkan popup biasa yang berisi judul dan deskripsi
    public void popupDialog(String title, String message, final boolean isFinishActivity) {
        try {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialogInterface.dismiss();
                            if (isFinishActivity)
                                activity.finish();
                            return;
                        default:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setCancelable(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
            } else {
                builder.setMessage(Html.fromHtml(message));
            }

            builder.setNegativeButton("Tutup", dialogClickListener);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDateRangePicker(AppCompatActivity appCompatActivity, response response) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        long today = MaterialDatePicker.todayInUtcMilliseconds();

        calendar.setTimeInMillis(today);

        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        long january = calendar.getTimeInMillis();

        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = calendar.getTimeInMillis();

        // material date picker constraint
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setStart(january);
        constraintBuilder.setEnd(december);
//        constraintBuilder.setValidator(DateValidatorPointForward.now());

        // init material date picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("PICK A DATE");
        builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
        builder.setCalendarConstraints(constraintBuilder.build());

        final MaterialDatePicker materialDatePicker = builder.build();

        materialDatePicker.show(appCompatActivity.getSupportFragmentManager(), "DATE RANGE PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date startDate = new Date(selection.first);
                Date endDate = new Date(selection.second);

                String startDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .format(startDate);

                String endDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .format(endDate);

                Map<Object, Object> param = new HashMap<>();
                param.put("startDate", startDate);
                param.put("endDate", endDate);
                response.getData(param);
            }
        });
    }

    public void openDateDialog(response response) {
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Map<Object, Object> param = new HashMap<>();
                param.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        .format(myCalendar.getTime()));
                response.getData(param);
            }
        };

        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void openTimeDialog(response response) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Map<Object, Object> param = new HashMap<>();
                String hours = strpad(String.valueOf(selectedHour), 2, "0", "STR_PAD_LEFT");
                String minutes = strpad(String.valueOf(selectedMinute), 2, "0", "STR_PAD_LEFT");
                param.put("time", hours + ":" + minutes);
                response.getData(param);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public String strpad(String input, int length, String pad, String sense) {
        int resto_pad = length - input.length();
        String padded = "";

        if (resto_pad <= 0){ return input; }

        if(sense.equals("STR_PAD_RIGHT"))
        {
            padded  = input;
            padded += fillString(pad,resto_pad);
        }
        else if(sense.equals("STR_PAD_LEFT"))
        {
            padded  = fillString(pad, resto_pad);
            padded += input;
        }
        else // STR_PAD_BOTH
        {
            int pad_left  = (int) Math.ceil(resto_pad/2);
            int pad_right = resto_pad - pad_left;

            padded  = fillString(pad, pad_left);
            padded += input;
            padded += fillString(pad, pad_right);
        }
        return padded;
    }


    protected String fillString(String pad, int resto ) {
        boolean first = true;
        String padded = "";

        if (resto >= pad.length())
        {
            for (int i = resto; i >= 0; i = i - pad.length())
            {
                if (i  >= pad.length())
                {
                    if (first){ padded = pad; } else { padded += pad; }
                }
                else
                {
                    if (first){ padded = pad.substring(0, i); } else { padded += pad.substring(0, i); }
                }
                first = false;
            }
        }
        else
        {
            padded = pad.substring(0,resto);
        }
        return padded;
    }

    public String convertDateString(String text, String fromPattern, String toPattern) {
        String result = "";
        try {
            DateFormat dateFormat   = new SimpleDateFormat(fromPattern);
            DateFormat toFormat     = new SimpleDateFormat(toPattern);
            Date date               = dateFormat.parse(text);
            result                  = toFormat.format(date);
        } catch (Exception e){}
        return result;
    }

    public long dateToMilliSecond(String dateString) {
        long milliSecond = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);
            milliSecond = date.getTime();
        } catch (Exception e) {
            showToast("Convert date to millisecond failed", 0);
            e.printStackTrace();
        }

        return milliSecond;
    }

    public void saveSessionBatch(JSONObject data) {
        try {
            JSONArray keys = data.names();
            for (int i = 0; i < keys.length(); ++i) {
                String key = keys.getString(i); // Here's your key
                String value = data.getString(key); // Here's your value

                saveSession(key, value);
            }
        } catch (Exception e) {}
    }

    public void removeSessionBatch(JSONObject data) {
        try {
            JSONArray keys = data.names();
            for (int i = 0; i < keys.length(); ++i) {
                String key = keys.getString(i); // Here's your key
                String value = data.getString(key); // Here's your value

                clearSessionByKey(key);
            }
        } catch (Exception e) {}
    }

    public void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //    Fungsi ini digunakan untuk menampilkan popup konfirmasi
    public void popupConfirm(String title, String body, DialogInterface.OnClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(body)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void generateTokenFirebase() {
        if (getSession("token_firebase") == null) {
//            FirebaseInstanceId.getInstance().getInstanceId()
//                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                            if (!task.isSuccessful()) {
//                                return;
//                            }
//                            String token = task.getResult().getToken();
//                            saveSession("token_firebase", token);
//                        }
//                    });
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
//     * @param messageBody FCM message body received.
     */
//    public void sendNotification(String messageTitle, String messageBody, Map<String, String> data) {
//        Intent intent = new Intent(context, Dashboard.class);
//        if (data != null) {
//            for (Map.Entry<String, String> row : data.entrySet()) {
//                String key = row.getKey();
//                String value = row.getValue();
//                intent.putExtra(key, value);
//            }
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        String channelId = context.getString(R.string.channelID);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(context, channelId)
//                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle(messageTitle)
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

    public boolean arePermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return true;
    }

    public void requestPermissionsCompat(Activity activity, int code, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, code);
    }

    public void openFileChooser(Activity activity, int code, String[] permissions) {
        if (arePermissionsGranted(activity, permissions)) {
            ImagePicker.create(activity)
                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                    .folderMode(true) // folder mode (false by default)
                    .toolbarFolderTitle("Folder") // folder selection title
                    .toolbarImageTitle("Pilih gambar") // image selection title
                    .toolbarArrowColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.white)) // Toolbar 'up' arrow color
                    .includeVideo(false) // Show video on image picker
                    .single() // single mode
//                .multi() // multi mode (default mode)
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    .enableLog(true) // disabling log
                    .start(); // start image picker activity with request code
        } else {
            requestPermissionsCompat(activity, code, permissions);
        }
    }

    public Bitmap loadImageFromStorage(String path) {
        try {
            File f      = new File(path);
            Bitmap b    = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void openMaps(String latitudeFrom, String longitudeFrom, String latitudeDestination, String longitudeDestination) {
        String packageMaps = "com.google.android.apps.maps";
        if (!latitudeDestination.isEmpty() && !longitudeDestination.isEmpty()) {
            if (isPackageInstalled(packageMaps, context.getPackageManager())) {
                String uri = "http://maps.google.com/maps?saddr=" + latitudeFrom + "," + longitudeFrom + "&daddr=" + latitudeDestination + "," + longitudeDestination;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(packageMaps);
                context.startActivity(intent);
            } else {
                showToast("Silahkan install aplikasi Maps terlebih dahulu di Google Play", 0);
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageMaps)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageMaps)));
                }
            }
        }
    }

    public String getRandomString(final int sizeOfRandomString, String type)
    {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        if (type == "number") {
            ALLOWED_CHARACTERS ="0123456789";
        }

        final Random random = new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}