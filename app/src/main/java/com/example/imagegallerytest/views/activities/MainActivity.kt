package com.example.imagegallerytest.views.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.imagegallerytest.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions(listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        )
    }

    private fun setupPermissions(permissions: List<String>) {
        permissions
            .filter {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED
            }
            .partition {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }
            .also { permissionsPartition ->
//                AlertDialog.Builder(this).apply {
//                    setTitle(R.string.permission_read_storage_title)
//                    setMessage(R.string.permission_read_storage_message)
//                    setPositiveButton("OK") {
//                            _, _ -> makePermissionRequest(permissionsPartition.first)
//                    }
//                    create()
//                    show()
//                }
                makePermissionRequest(permissionsPartition.second)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun makePermissionRequest(permissions: List<String>) {
        if (permissions.isEmpty()) return
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1234)
    }
}