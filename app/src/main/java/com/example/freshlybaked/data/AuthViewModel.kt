package com.example.freshlybaked.data

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import com.example.freshlybaked.models.User
import com.example.freshlybaked.navigation.HOME_URL
import com.example.freshlybaked.navigation.LOGIN_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(var navController: NavHostController, var context:Context) {
    private val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private val progress:ProgressDialog = ProgressDialog(context)

    init {
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }
    fun signup(name:String,email:String,password:String) {
        progress.show()
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            var userId = mAuth.currentUser!!.uid
            var userProfile = User(name,email,password,userId)
            // Create a reference table called Users inside of Firebase database
            var userRef = FirebaseDatabase.getInstance().getReference()
                .child("User/$userId")
            userRef.setValue(userProfile).addOnCompleteListener{
                progress.dismiss()
                if (it.isSuccessful){
                    Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                    navController.navigate(LOGIN_URL)
                }else{
                    Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    fun login(email: String,password: String){
        progress.show()
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            progress.dismiss()
            if (it.isSuccessful){
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                navController.navigate(HOME_URL)
            }else
                Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
        }

    }


    fun logout(){
        mAuth.signOut()
        navController.navigate(LOGIN_URL)
    }

    fun isLoggedIn():Boolean = mAuth.currentUser != null

}

