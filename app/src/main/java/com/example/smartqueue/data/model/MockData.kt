package com.example.smartqueue.data.model

/**
 * Sample data for UI previews only.
 * Replace entirely with real repository data in Phase 2.
 * All values here are illustrative, not real queue measurements.
 */
object MockData {

    val services = listOf(
        Service(
            serviceId = "s1",
            serviceName = "General Enquiry",
            description = "General information and guidance",
            averageDuration = 5.0,
            status = "open",
            peopleWaiting = 3,
            estimatedWait = 15
        ),
        Service(
            serviceId = "s2",
            serviceName = "Fee Payment",
            description = "Payments and receipts",
            averageDuration = 7.0,
            status = "open",
            peopleWaiting = 6,
            estimatedWait = 42
        ),
        Service(
            serviceId = "s3",
            serviceName = "Document Verification",
            description = "Verification of academic documents",
            averageDuration = 10.0,
            status = "open",
            peopleWaiting = 4,
            estimatedWait = 40
        ),
        Service(
            serviceId = "s4",
            serviceName = "Registration",
            description = "Student and course registration",
            averageDuration = 8.0,
            status = "closed",
            peopleWaiting = 0,
            estimatedWait = 0
        )
    )

    val activeToken = Token(
        tokenId = "t1",
        userId = "u1",
        queueId = "q1",
        serviceId = "s3",
        serviceName = "Document Verification",
        tokenNumber = "A-018",
        status = "waiting",
        positionAhead = 4,
        estimatedWait = 20,
        nowServing = "A-014"
    )

    val adminTokens = listOf(
        Token(tokenId = "t2", tokenNumber = "A-015", status = "waiting", serviceName = "Fee Payment"),
        Token(tokenId = "t3", tokenNumber = "A-016", status = "waiting", serviceName = "Fee Payment"),
        Token(tokenId = "t4", tokenNumber = "A-017", status = "waiting", serviceName = "Fee Payment"),
        Token(tokenId = "t1", tokenNumber = "A-018", status = "waiting", serviceName = "Fee Payment")
    )
}
