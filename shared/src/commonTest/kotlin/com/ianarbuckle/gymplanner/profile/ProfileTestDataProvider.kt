package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.profile.dto.ProfileDto

/**
 * Provides test data for ProfileRepository tests
 */
object ProfileTestDataProvider {

    // ========== User IDs ==========

    object UserIds {
        const val user1 = "user-001"
        const val user2 = "user-002"
        const val user3 = "user-003"
        const val user4 = "user-004"
        const val admin = "admin-001"
        const val nonExistent = "user-999"
        const val emptyId = ""
    }

    // ========== Usernames ==========

    object Usernames {
        const val johnDoe = "johndoe"
        const val janeSmith = "janesmith"
        const val mikeBrown = "mikebrown"
        const val sarahWilson = "sarahwilson"
        const val adminUser = "admin"
    }

    // ========== Emails ==========

    object Emails {
        const val john = "john.doe@example.com"
        const val jane = "jane.smith@example.com"
        const val mike = "mike.brown@example.com"
        const val sarah = "sarah.wilson@example.com"
        const val admin = "admin@gymplanner.com"
    }

    // ========== Profile DTOs ==========

    object ProfileDtos {
        val john = ProfileDto(
            userId = UserIds.user1,
            username = Usernames.johnDoe,
            firstName = "John",
            surname = "Doe",
            email = Emails.john
        )

        val jane = ProfileDto(
            userId = UserIds.user2,
            username = Usernames.janeSmith,
            firstName = "Jane",
            surname = "Smith",
            email = Emails.jane
        )

        val mike = ProfileDto(
            userId = UserIds.user3,
            username = Usernames.mikeBrown,
            firstName = "Mike",
            surname = "Brown",
            email = Emails.mike
        )

        val sarah = ProfileDto(
            userId = UserIds.user4,
            username = Usernames.sarahWilson,
            firstName = "Sarah",
            surname = "Wilson",
            email = Emails.sarah
        )

        val admin = ProfileDto(
            userId = UserIds.admin,
            username = Usernames.adminUser,
            firstName = "Admin",
            surname = "User",
            email = Emails.admin
        )
    }

    // ========== Profiles (Domain) ==========

    object Profiles {
        val john = Profile(
            userId = UserIds.user1,
            username = Usernames.johnDoe,
            firstName = "John",
            surname = "Doe",
            email = Emails.john
        )

        val jane = Profile(
            userId = UserIds.user2,
            username = Usernames.janeSmith,
            firstName = "Jane",
            surname = "Smith",
            email = Emails.jane
        )

        val mike = Profile(
            userId = UserIds.user3,
            username = Usernames.mikeBrown,
            firstName = "Mike",
            surname = "Brown",
            email = Emails.mike
        )

        val sarah = Profile(
            userId = UserIds.user4,
            username = Usernames.sarahWilson,
            firstName = "Sarah",
            surname = "Wilson",
            email = Emails.sarah
        )

        val admin = Profile(
            userId = UserIds.admin,
            username = Usernames.adminUser,
            firstName = "Admin",
            surname = "User",
            email = Emails.admin
        )
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("User profile not found")
        val timeout = Exception("Request timeout")
        val invalidUserId = IllegalArgumentException("Invalid user ID format")
    }
}

