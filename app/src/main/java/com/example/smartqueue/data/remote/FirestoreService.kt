package com.example.smartqueue.data.remote

import com.example.smartqueue.data.model.Service
import com.example.smartqueue.data.model.Token
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /** Observe all services in real time. */
    fun observeServices(): Flow<List<Service>> = callbackFlow {
        val listener = db.collection("services")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error ?: Exception("Snapshot error"))
                    return@addSnapshotListener
                }
                val services = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Service::class.java)?.copy(serviceId = doc.id)
                }
                trySend(services)
            }
        awaitClose { listener.remove() }
    }

    /** Observe the active token for the current user. */
    fun observeMyToken(): Flow<Token?> = callbackFlow {
        val userId = auth.currentUser?.uid ?: run {
            close(Exception("Not authenticated"))
            return@callbackFlow
        }
        val listener = db.collection("tokens")
            .whereEqualTo("userId", userId)
            .whereNotIn("status", listOf("completed", "cancelled", "skipped"))
            .orderBy("issueTime", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error ?: Exception("Snapshot error"))
                    return@addSnapshotListener
                }
                val token = snapshot.documents.firstOrNull()?.let { doc ->
                    doc.toObject(Token::class.java)?.copy(tokenId = doc.id)
                }
                trySend(token)
            }
        awaitClose { listener.remove() }
    }

    /** Observe all waiting/active tokens for a service queue (admin). */
    fun observeQueueTokens(serviceId: String): Flow<List<Token>> = callbackFlow {
        val listener = db.collection("tokens")
            .whereEqualTo("serviceId", serviceId)
            .whereIn("status", listOf("waiting", "called", "serving"))
            .orderBy("issueTime", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error ?: Exception("Snapshot error"))
                    return@addSnapshotListener
                }
                val tokens = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Token::class.java)?.copy(tokenId = doc.id)
                }
                trySend(tokens)
            }
        awaitClose { listener.remove() }
    }

    /** Request a token for a service (idempotent — backend enforces uniqueness). */
    suspend fun requestToken(serviceId: String, serviceName: String): Result<Token> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Not authenticated"))
            // In production this would call a Cloud Function for transactional token issuance.
            // Placeholder: returns failure until Phase 2 backend is wired.
            Result.failure(Exception("Backend not yet connected — Phase 2"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Cancel own token. */
    suspend fun cancelToken(tokenId: String): Result<Unit> {
        return try {
            db.collection("tokens").document(tokenId)
                .update("status", "cancelled")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Admin: call next eligible token for a service. */
    suspend fun callNextToken(serviceId: String): Result<String> {
        return try {
            // Should call a Cloud Function in production.
            Result.failure(Exception("Backend not yet connected — Phase 2"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Admin: update token status (complete/skip/cancel). */
    suspend fun updateTokenStatus(tokenId: String, newStatus: String): Result<Unit> {
        return try {
            db.collection("tokens").document(tokenId)
                .update("status", newStatus)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Get user profile from Firestore. */
    suspend fun getUserProfile(userId: String): Result<com.example.smartqueue.data.model.User> {
        return try {
            val doc = db.collection("users").document(userId).get().await()
            val user = doc.toObject(com.example.smartqueue.data.model.User::class.java)
                ?: return Result.failure(Exception("User not found"))
            Result.success(user.copy(userId = doc.id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
