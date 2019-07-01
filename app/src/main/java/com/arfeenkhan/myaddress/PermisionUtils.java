package com.arfeenkhan.myaddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermisionUtils {
    Context context;
    Activity current_activity;

    PermissionResultCallback permissionResultCallback;

    ArrayList<String> permission_list = new ArrayList<>();
    ArrayList<String> listPermissionsNeede = new ArrayList<>();

    String dialog_content = "";
    int req_code;

    public PermisionUtils(Context context) {
        this.context = context;
        this.current_activity = (Activity) context;
        permissionResultCallback = (PermissionResultCallback) context;
    }

    public PermisionUtils(Context context, PermissionResultCallback callback) {
        this.context = context;
        this.current_activity = (Activity) context;
        permissionResultCallback = callback;
    }

    public void check_permission(ArrayList<String> permission, String dialog_content, int request_code) {
        this.permission_list = permission;
        this.dialog_content = dialog_content;
        this.req_code = request_code;

        if (Build.VERSION.SDK_INT <= 28) {
            if (checkAndRequestPermissions(permission, request_code)) {
                permissionResultCallback.PermissionGranted(request_code);
            }
        } else {
            permissionResultCallback.PermissionGranted(request_code);
        }
    }

    private boolean checkAndRequestPermissions(ArrayList<String> permission, int request_code) {
        if (permission.size() > 0) {
            listPermissionsNeede = new ArrayList<>();
            for (int i = 0; i < permission.size(); i++) {
                int hasPermission = ContextCompat.checkSelfPermission(current_activity, permission.get(i));
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeede.add(permission.get(i));
                }
            }

            if (!listPermissionsNeede.isEmpty()) {
                ActivityCompat.requestPermissions(current_activity, listPermissionsNeede.toArray(new String[listPermissionsNeede.size()]), request_code);
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionResult(int requestCode, String permission[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    Map<String, Integer> perms = new HashMap<>();
                    for (int i = 0; i < permission.length; i++) {
                        perms.put(permission[i], grantResults[i]);
                    }
                    final ArrayList<String> pending_permission = new ArrayList<>();
                    for (int i = 0; i < listPermissionsNeede.size(); i++) {
                        if (perms.get(listPermissionsNeede.get(i)) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity, listPermissionsNeede.get(i)))
                                pending_permission.add(listPermissionsNeede.get(i));
                            else {
                                permissionResultCallback.NeverAskAgain(req_code);
                                Toast.makeText(context, "Go to settings and enable permission", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    }

                    if (pending_permission.size()>0)
                    {
                        showMessageOKCancel(dialog_content, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        check_permission(permission_list,dialog_content,req_code);
                                        break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            if (permission_list.size()==pending_permission.size())
                                                permissionResultCallback.PermissionDenied(req_code);
                                            else
                                                permissionResultCallback.PartialPermissionGranted(req_code,pending_permission);
                                            break;
                                }
                            }
                        });
                    }
                    else
                    {
                        permissionResultCallback.PermissionGranted(req_code);
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(current_activity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public interface PermissionResultCallback {
        void PermissionGranted(int request_code);

        void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);

        void PermissionDenied(int request_code);

        void NeverAskAgain(int request_code);
    }

}
