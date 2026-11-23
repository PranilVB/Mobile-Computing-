package com.example.smartqueue.data.repository

import com.example.smartqueue.data.model.MockData
import com.example.smartqueue.data.model.Service
import com.example.smartqueue.data.model.Token
import com.example.smartqueue.data.model.User
import com.example.smartqueue.data.remote.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Single repository used by all ViewModels.
 * Phase 1: Returns mock data for UI preview.
 * Phase 2+: Delegates to FirestoreService with real Firebase backend.
 */
class QueueRepository(private val firestoreService: FirestoreService) {

    // ---- Phase 1: Mock data (clearly separated) ----

    fun getMockServices(): Flow<List<Service>> = flow {
        emit(MockData.services)
    }

    fun getMockActiveToken(): Flow<Token?> = flow {
        emit(MockData.activeToken)
    }

    fun getMockAdminTokens(): Flow<List<Token>> = flow {
        emit(MockData.adminTokens)
    }

    // ---- Phase 2+: Real Firebase data ----

    fun observeServices(): Flow<List<Service>> = firestoreService.observeServices()

    fun observeMyToken(): Flow<Token?> = firestoreService.observeMyToken()

    fun observeQueueTokens(serviceId: String): Flow<List<Token>> =
        firestoreService.observeQueueTokens(serviceId)

    suspend fun requestToken(serviceId: String, serviceName: String): Result<Token> =
        firestoreService.requestToken(serviceId, serviceName)

    suspend fun cancelToken(tokenId: String): Result<Unit> =
        firestoreService.cancelToken(tokenId)

    suspend fun callNextToken(serviceId: String): Result<String> =
        firestoreService.callNextToken(serviceId)

    suspend fun updateTokenStatus(tokenId: String, newStatus: String): Result<Unit> =
        firestoreService.updateTokenStatus(tokenId, newStatus)

    suspend fun getUserProfile(userId: String): Result<User> =
        firestoreService.getUserProfile(userId)
}
