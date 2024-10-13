package com.example.mvvmexercise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    val registrationStatus = MutableLiveData<String>()

    fun registerUser(name: String, email: String, password: String, role: String) {
        if (isEmailValid(email)) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        val uid = task.result?.user?.uid
                        if (uid != null) {
                            val userData = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "role" to role,
                                "averageRating" to 0,
                                "employeeOfTheMonthCount" to 0
                            )
                            db.collection("users").document(uid).set(userData)
                                .addOnSuccessListener {
                                    user?.sendEmailVerification()
                                        ?.addOnCompleteListener { verifyTask ->
                                            if(verifyTask.isSuccessful) {
                                                registrationStatus.value = "VerificationEmailSent"
                                            } else {
                                                registrationStatus.value = "EmailVerificationFailed"
                                            }
                                        }
                                }
                                .addOnFailureListener {
                                    registrationStatus.value = "FirestoreWriteFailed"
                                }
                        }
                    } else {
                        registrationStatus.value = "RegistrationFailed"
                    }
                }
                .addOnFailureListener {
                    registrationStatus.value = "RegistrationFailed"
                }
        } else {
            registrationStatus.value = "InvalidEmail"
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.endsWith("cesae.pt") || email.endsWith("msft.cesae.pt")
    }
}