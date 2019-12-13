package com.nagycsongor.moviedatabase.Fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nagycsongor.moviedatabase.R
import kotlinx.android.synthetic.main.fragment_profile.*
import java.security.MessageDigest
import java.util.jar.Manifest


class ProfileFragment : Fragment {
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private var userId: String? = null
    var bottomNavigationView: BottomNavigationView? = null
    var imageView: ImageView? = null
        private set
    private var sharedPreferences: SharedPreferences? = null

    constructor(bottomNavigationView: BottomNavigationView, sharedPreferences: SharedPreferences?) {
        this.sharedPreferences = sharedPreferences
        this.bottomNavigationView = bottomNavigationView

    }

    private var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
        userId = sharedPreferences?.getString("userId", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutBotton = view.findViewById<Button>(R.id.logoutButton)
        logoutBotton.setOnClickListener {
            logout()
        }

        val saveBotton = view.findViewById<Button>(R.id.saveChangesButton)
        saveBotton.setOnClickListener {
            save()
        }
        database!!.getReference("users").child(userId.toString()).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val name = p0.getValue(String::class.java)
                    if (name != null) {
                        nameEditText.setText(name.toString())
                    }
                }

            })



        imageView = view.findViewById(R.id.imageView)

        database!!.getReference("users").child(userId.toString()).child("photo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    val photo = p0.getValue(String::class.java)
                    if (photo != null) {
                        imageView?.setImageURI(Uri.parse(photo))
                    }
                }

            })

        imageView?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        activity!!,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_CODE
                    )
                } else {
                    pickImage()
                }

            } else {
                pickImage()
            }
        }


        return view
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }

    private fun save() {
        if (!TextUtils.isEmpty(nameEditText.text)) {
            database!!.getReference("users").child(userId.toString()).child("name")
                .setValue(nameEditText.text.toString())
        }
        if (!TextUtils.isEmpty(newPasswordEditText.text)) {
            database!!.getReference("users").child(userId.toString()).child("password")
                .setValue(toHash(newPasswordEditText.text.toString()))
            newPasswordEditText.setText("")
        }
    }

    private fun logout() {
        sharedPreferences?.edit()?.clear()?.commit()
        val fragmentTransaction =
            fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_frameLayout,
            LoginFragment(bottomNavigationView!!, sharedPreferences)
        )
        fragmentTransaction.commit()
        bottomNavigationView?.setTransitionVisibility(View.INVISIBLE)
    }

    private fun byteArrayToHexString(array: Array<Byte>): String {

        var result = StringBuilder(array.size * 2)

        for (byte in array) {

            val toAppend =
                String.format("%2X", byte).replace(" ", "0") // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'

        return result.toString()
    }

    private fun toHash(text: String): String {
        var result = ""

        val md5 = MessageDigest.getInstance("MD5")
        val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()
        result = byteArrayToHexString(md5HashBytes)
        return result
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                } else {
                    Toast.makeText(context, "Don't have permission to open the gallery", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView?.setImageURI(data?.data)
            database!!.getReference("users").child(userId.toString()).child("photo").setValue(data?.data.toString())
            Toast.makeText(context, data?.data.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}
