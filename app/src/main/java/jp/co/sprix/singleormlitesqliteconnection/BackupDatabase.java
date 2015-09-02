package jp.co.sprix.singleormlitesqliteconnection;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


public class BackupDatabase {

    public static void BackupDatabase() {
        try {
            boolean success = true;
            File file = null;
            file = new File(Environment.getExternalStorageDirectory() + "/M.O.L.S_Backup");

            if (file.exists()) {
                success = true;
            } else {
                success = file.mkdir();
            }

            if (success) {
                String inFileName = "/data/data/jp.goalstart.sns.webview/databases/MOLS_DB.s3db";
                File dbFile = new File(inFileName);
                FileInputStream fis = new FileInputStream(dbFile);

                String outFileName = Environment.getExternalStorageDirectory() + "/M.O.L.S_Backup/MOLS_DB.s3db";
                //Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);
                //transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                output.flush();
                output.close();
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(null, sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }
    public static void copyDirectory(Context context) throws IOException {
        copyDirectory(context, new File(""), new File(""));

    }

    public static void copyDirectory(Context context,File  source1, File target1) throws IOException {
        File source = new File("/data/data/jp.co.sprix.singleormlitesqliteconnection/databases");
//        File target = new File(Environment.getExternalStorageDirectory()+"/databases");

        final File target = context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS + File.separator + "databases");

        if (!target.exists()) {
            target.mkdir();
        }

        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private static void copyFile(File source, File target) throws IOException {
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        } catch (IOException w) {
        }
    }

    public static void testDB() {
        final String url = "jdbc:mysql://<server>:<port>/<database>";
         final String user = "<username>";
          final String pass = "<password>";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            /* System.out.println("Databaseection success"); */

            String result = "Database connection success\n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from table_name");
            ResultSetMetaData rsmd = rs.getMetaData();

            while(rs.next()) {
                result += rsmd.getColumnName(1) + ": " + rs.getInt(1) + "\n";
                result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
            }

            Log.d("SQLSQL",result);
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d("SQLSQL",e.toString());
        }

    }

//    private void copyFolder(String name) {
//        // "Name" is the name of your folder!
//        AssetManager assetManager = getAssets();
//        String[] files = null;
//
//        String state = Environment.getExternalStorageState();
//
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            // We can read and write the media
//            // Checking file on assets subfolder
//            try {
//                files = assetManager.list(name);
//            } catch (IOException e) {
//                Log.e("ERROR", "Failed to get asset file list.", e);
//            }
//            // Analyzing all file on assets subfolder
//            for(String filename : files) {
//                InputStream in = null;
//                OutputStream out = null;
//                // First: checking if there is already a target folder
//                File folder = new File(Environment.getExternalStorageDirectory() + "/yourTargetFolder/" + name);
//                boolean success = true;
//                if (!folder.exists()) {
//                    success = folder.mkdir();
//                }
//                if (success) {
//                    // Moving all the files on external SD
//                    try {
//                        in = assetManager.open(name + "/" +filename);
//                        out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/yourTargetFolder/" + name + "/" + filename);
//                        Log.i("WEBVIEW", Environment.getExternalStorageDirectory() + "/yourTargetFolder/" + name + "/" + filename);
//                        copyFile(in, out);
//                        in.close();
//                        in = null;
//                        out.flush();
//                        out.close();
//                        out = null;
//                    } catch(IOException e) {
//                        Log.e("ERROR", "Failed to copy asset file: " + filename, e);
//                    } finally {
//                        // Edit 3 (after MMs comment)
//                        in.close();
//                        in = null;
//                        out.flush();
//                        out.close();
//                        out = null;
//                    }
//                }
//                else {
//                    // Do something else on failure
//                }
//            }
//        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            // We can only read the media
//        } else {
//            // Something else is wrong. It may be one of many other states, but all we need
//            // is to know is we can neither read nor write
//        }
//    }

    // Method used by copyAssets() on purpose to copy a file.
    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
