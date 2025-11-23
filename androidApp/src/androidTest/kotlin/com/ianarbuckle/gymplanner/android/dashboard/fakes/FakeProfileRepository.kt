package com.ianarbuckle.gymplanner.android.dashboard.fakes

import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.profile.domain.Profile

class FakeProfileRepository : ProfileRepository {

    var shouldReturnError = false

    override suspend fun fetchProfile(userId: String): Result<Profile> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            mockProfileSuccess()
        }
    }

    private fun mockProfileSuccess(): Result<Profile> {
        return Result.success(Profile("123", "ianarbuckle", "Ian", "Arbuckle", "ian@mail.com"))
    }
}
