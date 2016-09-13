package com.example.delevere.cbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Delevere on 13-Jun-16.
 */

public class UserLoginTask extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    ProgressDialog pd;

    UserLoginTask(Context ctx) {
        context = ctx;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
       // pd.show();
        pd.setMessage("Loading...");
        pd.setCancelable(false);


    }



    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        // TODO: attempt authentication against a network service.
        String login_url = "http://aoucr.com/otp/applogin.php";
        String reg_url = "http://aoucr.com/otp/app_register.php";
        String data_url = "http://aoucr.com/wsup/wsup_data.php";
        String profile_url = "http://aoucr.com/otp/app_update_profile.php";
        String otp_url = "http://aoucr.com/otp/app_otp.php";
        String otp_forgot_url = "http://aoucr.com/otp/app_otp_forgot.php";
        String contact_url = "http://aoucr.com/otp/app_add_contacts.php";
        String event_url = "http://aoucr.com/otp/app_add_event.php";
        String forgot_url = "http://aoucr.com/otp/app_forgot_password.php";
        String update_url = "http://aoucr.com/otp/app_update_password.php";
        String event_edit_url = "http://aoucr.com/otp/app_update_event.php";
        String greeting_url = "http://aoucr.com/otp/app_greeting.php";
        String report_url = "http://aoucr.com/otp/app_error_report.php";
        String noty_url = "http://aoucr.com/otp/app_send.php";




        if (type.equals("login")) {
            try {

                String username = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (type.equals("otp")) {
            try {

                String otp = params[1];
                String phone = params[2];
                URL url = new URL(otp_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (type.equals("otpforgot")) {
            try {

                String otp = params[1];
                String phone = params[2];
                URL url = new URL(otp_forgot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("otp", "UTF-8") + "=" + URLEncoder.encode(otp, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (type.equals("contacts")) {
            try {

                String phone = params[1];
                String contact = params[2];
                URL url = new URL(contact_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("contacts", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(type.equals("register")){
            try {
                String username = params[1];
                String email = params[2];
                String password = params[3];
                String key = params[4];

                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("appkey","UTF-8")+"="+URLEncoder.encode(key,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }if (type.equals("forgot")) {
            try {

                String phone1 = params[1];
                String phone2 = params[2];
                URL url = new URL(forgot_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone1", "UTF-8") + "=" + URLEncoder.encode(phone1, "UTF-8") + "&"
                        + URLEncoder.encode("phone2", "UTF-8") + "=" + URLEncoder.encode(phone2, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("updatepassword")) {
            try {

                String phone = params[1];
                String password = params[2];
                URL url = new URL(update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(type.equals("event")){
            try {
                String phonelogin = params[1];
                String eventnumber = params[2];
                String eventtype = params[3];
                String eventdate = params[4];
                String eventimage = params[5];
                String eventpermission = params[6];



                URL url = new URL(event_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phonelogin,"UTF-8")+"&"
                        +URLEncoder.encode("event_number","UTF-8")+"="+URLEncoder.encode(eventnumber,"UTF-8")+"&"
                        +URLEncoder.encode("event_type","UTF-8")+"="+URLEncoder.encode(eventtype,"UTF-8")+"&"
                        +URLEncoder.encode("event_date","UTF-8")+"="+URLEncoder.encode(eventdate,"UTF-8")+"&"
                        +URLEncoder.encode("event_permission","UTF-8")+"="+URLEncoder.encode(eventpermission,"UTF-8")+"&"

                        +URLEncoder.encode("event_image","UTF-8")+"="+URLEncoder.encode(eventimage,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("note")){
            try {

                String token = "AIzaSyCWZ64wahBFpbGxwtG0wv1pMNL2CmaABjM";
                String personkey = "chDS0UmO3sU:APA91bHBV6BeCDOhkfr2nqUX0UfJJ2MuFRyUInhVEW1dUekrbeAVfxtsYkLreedPgf6EkhUi6YId6aH2vk0hAWoVAtuq80l1EzwqbB14A_8B4TACYhAtbUeFaSL3gWsCx9x9lwor2g_O";
                String personkey2 ="fWKuylm4d1g:APA91bHAdKIq5YOO8yC_Rl3XhhMaItYdMPzuNOaxIJNk-MBF0aox7CmlPMCAqHSiaDZlDw1RA3hdmwTJiWY6Y1sdYrnH5YmcRctE5SsdcTbyTKfwwLuKtHprrkfJ1wuRbNDDKkYFQKn-";
                String msgsend = "hiiiii";



                URL url = new URL(noty_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("apikey","UTF-8")+"="+URLEncoder.encode(token,"UTF-8")+"&"
                        +URLEncoder.encode("regtoken","UTF-8")+"="+URLEncoder.encode(personkey,"UTF-8")+"&"
                        +URLEncoder.encode("regtoken","UTF-8")+"="+URLEncoder.encode(personkey2,"UTF-8")+"&"
                        +URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(msgsend,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("eventedit")){
            try {
                String eventname = params[1];
                String eventdate = params[2];
                String eventimage = params[3];
                String olddate = params[4];
                String eventperson = params[5];


                URL url = new URL(event_edit_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("eventname","UTF-8")+"="+URLEncoder.encode(eventname,"UTF-8")+"&"
                        +URLEncoder.encode("eventdate","UTF-8")+"="+URLEncoder.encode(eventdate,"UTF-8")+"&"
                        +URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(eventimage,"UTF-8")+"&"
                        +URLEncoder.encode("olddate","UTF-8")+"="+URLEncoder.encode(olddate,"UTF-8")+"&"
                        +URLEncoder.encode("eventperson","UTF-8")+"="+URLEncoder.encode(eventperson,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("data")){
            try {
                String Problem1 = params[1];
                String Problem2 = params[2];
                String Problem3 = params[3];
                String Problem4 = params[4];
                String Problem5 = params[5];
                String Problem6 = params[6];
                String Problem7 = params[7];
                String Problem8 = params[8];
                String Problem9 = params[9];
                String Problem10 = params[10];
                String log = params[11];
                String lat = params[12];
                URL url = new URL(data_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("Question1","UTF-8")+"="+URLEncoder.encode(Problem1,"UTF-8")+"&"
                        +URLEncoder.encode("Question2","UTF-8")+"="+URLEncoder.encode(Problem2,"UTF-8")+"&"
                        +URLEncoder.encode("Question3","UTF-8")+"="+URLEncoder.encode(Problem3,"UTF-8")+"&"
                        +URLEncoder.encode("Question4","UTF-8")+"="+URLEncoder.encode(Problem4,"UTF-8")+"&"
                        +URLEncoder.encode("Question5","UTF-8")+"="+URLEncoder.encode(Problem5,"UTF-8")+"&"
                        +URLEncoder.encode("Question6","UTF-8")+"="+URLEncoder.encode(Problem6,"UTF-8")+"&"
                        +URLEncoder.encode("Question7","UTF-8")+"="+URLEncoder.encode(Problem7,"UTF-8")+"&"
                        +URLEncoder.encode("Question8","UTF-8")+"="+URLEncoder.encode(Problem8,"UTF-8")+"&"
                        +URLEncoder.encode("Question9","UTF-8")+"="+URLEncoder.encode(Problem9,"UTF-8")+"&"
                        +URLEncoder.encode("Question10","UTF-8")+"="+URLEncoder.encode(Problem10,"UTF-8")+"&"
                        +URLEncoder.encode("lati","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8")+"&"
                        +URLEncoder.encode("long","UTF-8")+"="+URLEncoder.encode(log,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("greeting")){
            try {
                String phone = params[1];
                String msg = params[2];
                String from = params[3];

                URL url = new URL(greeting_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                        +URLEncoder.encode("from","UTF-8")+"="+URLEncoder.encode(from,"UTF-8")+"&"
                        +URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(msg,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("error")){
            try {
                String person = params[1];
                String  errorname= params[2];
                String errordetails = params[3];



                URL url = new URL(report_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("person","UTF-8")+"="+URLEncoder.encode(person,"UTF-8")+"&"
                        +URLEncoder.encode("error_name","UTF-8")+"="+URLEncoder.encode(errorname,"UTF-8")+"&"
                        +URLEncoder.encode("error_details","UTF-8")+"="+URLEncoder.encode(errordetails,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(type.equals("profile")){
            try {
                String phone = params[1];
                String birthday = params[2];


                URL url = new URL(profile_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                                    +URLEncoder.encode("birthday","UTF-8")+"="+URLEncoder.encode(birthday,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while ((line = bufferedReader.readLine())!=null){
                    result +=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }



    protected void onPostExecute(String result) {
        try {
            //pd.dismiss();
            if (result.equals("login")) {

                Toast.makeText(context, "Login Successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Home2Activicy.class);
                context.startActivity(intent);

            } else if (result.equals("logout")) {
                Toast.makeText(context, "Invalid Id / Password", Toast.LENGTH_LONG).show();
            } else if (result.equals("Success")) {
                Toast.makeText(context, "Ok", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, Home2Activicy.class);
                context.startActivity(intent);
            } else if (result.equals("Registered")) {
                Toast.makeText(context, "Registerd", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OtpActivity.class);
                context.startActivity(intent);
            } else if (result.equals("conform")) {
                Toast.makeText(context, "Otp Conform", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Home2Activicy.class);
                context.startActivity(intent);
            } else if (result.equals("Sorry")) {
                Toast.makeText(context, "Phone number already exists", Toast.LENGTH_SHORT).show();
            }else if (result.equals("eventadded")) {
                Toast.makeText(context, "Event Created Succesfully", Toast.LENGTH_SHORT).show();
            }else if (result.equals("eventnotadded")) {
                Toast.makeText(context, "Unable to add event in server", Toast.LENGTH_SHORT).show();
            }else if (result.equals("contactsadded")) {
                Toast.makeText(context, "Refreshed", Toast.LENGTH_LONG).show();
            }else if (result.equals("contactsfailed")) {
                Toast.makeText(context, "Sorry Contacts not added", Toast.LENGTH_LONG).show();
            }else if (result.equals("forgototpsend")) {

                Intent intent = new Intent(context, OtpForForgot.class);
                context.startActivity(intent);

                //Toast.makeText(context, "Otp", Toast.LENGTH_LONG).show();
            }else if (result.equals("forgototpnotsend")) {

                //context.startActivity(new Intent(context, OtpForForgot.class));
                Toast.makeText(context, "Unable to process", Toast.LENGTH_LONG).show();
            } else if (result.equals("conform2")) {
                Toast.makeText(context, "Otp Conform", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UpdatePasswordActivity.class);
                context.startActivity(intent);
            } else if (result.equals("Sorry2")) {
                Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
            }else if (result.equals("passwordupdated")) {
                Toast.makeText(context, "Updated successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            } else if (result.equals("passwordnotupdated")) {
                Toast.makeText(context, "Unable to Update Password.", Toast.LENGTH_SHORT).show();
            }else if (result.equals("unknownumber")) {
                Toast.makeText(context, "Invalid phone Number", Toast.LENGTH_SHORT).show();
            }else if (result.equals("eventupdated")) {
                Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show();
            }else if (result.equals("eventnotupdated")) {
                Toast.makeText(context, "Event Not Update", Toast.LENGTH_SHORT).show();
            }else if (result.equals("profileupdated")) {
                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
            }else if (result.equals("profilenotupdated")) {
                Toast.makeText(context, "Profile Not Update", Toast.LENGTH_SHORT).show();
            }else if (result.equals("greetingsend")) {
                Toast.makeText(context, "Greeting Send Successfully", Toast.LENGTH_SHORT).show();
            }else if (result.equals("greetingnotsend")) {
                Toast.makeText(context, "Greeting Not Send", Toast.LENGTH_SHORT).show();
            }else if (result.equals("errorsend")) {
                Toast.makeText(context, "Error Submitted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Home2Activicy.class);
                context.startActivity(intent);

            }else if (result.equals("errornotsend")) {
                Toast.makeText(context, "Error Not Submitted", Toast.LENGTH_SHORT).show();

            }
            else {
                return;
            }
            return;
        }catch (Exception e){
            return;
        }

    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}