package ru.kggm.presentation.ui.shared

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.kggm.presentation.R

@AndroidEntryPoint
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