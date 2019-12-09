package com.nagycsongor.moviedatabase.Fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.nagycsongor.moviedatabase.HelpClass.User
import com.nagycsongor.moviedatabase.R
import kotlinx.android.synthetic.main.fragment_login.*
import java.security.MessageDigest

class LoginFragment : Fragment {
    var bottomNavigationView: BottomNavigationView? = null
        private set
    constructor(bottomNavigationView: BottomNavigationView){
        this.bottomNavigationView = bottomNavigationView
    }
    private var database: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        database = FirebaseDatabase.getInstance()
        reference = database!!.getReference("users")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //TODO remove this
        val fragmentTransaction =
            fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(
            R.id.fragment_frameLayout,
            MainFragment()
        )
        fragmentTransaction.commit()
        bottomNavigationView?.setTransitionVisibility(View.VISIBLE)
        return view

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (TextUtils.isEmpty(emailEditText.text) || TextUtils.isEmpty(passwordEditText.text)) {
                Toast.makeText(view.context, "Complete all box", Toast.LENGTH_SHORT).show()
            } else {
                val hashEmail = toHash(emailEditText.text.toString())
                val hashPassword = toHash(passwordEditText.text.toString())
                val ref = reference!!.child(hashEmail)
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val u: User? = dataSnapshot.getValue(
                            User::class.java)
                        if (u != null) {
                            if (u.password?.equals(hashPassword)!!) {
                                val fragmentTransaction =
                                    fragmentManager!!.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.fragment_frameLayout,
                                    MainFragment()
                                )
                                fragmentTransaction.commit()
                                bottomNavigationView?.setTransitionVisibility(View.VISIBLE)
                            } else {
                                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val user =
                                User(hashEmail, hashPassword)
                            reference!!.child(hashEmail).setValue(user).addOnSuccessListener {
                                Toast.makeText(view.context, "User added!", Toast.LENGTH_SHORT).show()
                            }
                            val fragmentTransaction =
                                fragmentManager!!.beginTransaction()
                            fragmentTransaction.replace(
                                R.id.fragment_frameLayout,
                                MainFragment()
                            )
                            fragmentTransaction.commit()
                            bottomNavigationView?.setTransitionVisibility(View.VISIBLE)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        }

        return view
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

}
